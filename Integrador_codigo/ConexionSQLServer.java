import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Conector hacia la Base de Datos Relacional en SQL Server (BibliotecaPOO).
 * Permite cargar y guardar Socios, Libros y Préstamos desde VentanaBiblioteca.
 */
public class ConexionSQLServer {

    private static final String SERVIDOR = "localhost";
    private static final String BASE_DATOS = "BibliotecaPOO";
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Carga todos los Socios, Libros y Préstamos guardados en SQL Server
     * dentro del objeto Biblioteca.
     */
    public static boolean cargarDesdeSQLServer(Biblioteca p_biblioteca) {
        try {
            // 1. Consultar Socios
            List<String[]> filasSocios = ejecutarConsulta(
                    "SELECT dni, nombre, tipo, carrera_o_area, dias_prestamo FROM Socios ORDER BY dni"
            );
            // 2. Consultar Libros
            List<String[]> filasLibros = ejecutarConsulta(
                    "SELECT id_libro, titulo, edicion, editorial, anio FROM Libros ORDER BY id_libro"
            );

            if (filasSocios.isEmpty() && filasLibros.isEmpty()) {
                return false;
            }

            p_biblioteca.getSocios().clear();
            p_biblioteca.getLibros().clear();

            for (String[] fila : filasSocios) {
                if (fila.length < 5) continue;
                int dni = Integer.parseInt(fila[0].trim());
                String nombre = fila[1].trim();
                String tipo = fila[2].trim();
                String extra = fila[3].trim();
                int dias = Integer.parseInt(fila[4].trim());

                if (tipo.equalsIgnoreCase("Docente")) {
                    Docente d = new Docente(dni, nombre, extra);
                    if (dias > 5) {
                        d.cambiarDiasDePrestamo(dias - 5);
                    }
                    p_biblioteca.getSocios().put(dni, d);
                } else {
                    Estudiante e = new Estudiante(dni, nombre, extra);
                    p_biblioteca.getSocios().put(dni, e);
                }
            }

            HashMap<Integer, Libro> mapaLibrosPorId = new HashMap<Integer, Libro>();
            for (String[] fila : filasLibros) {
                if (fila.length < 5) continue;
                int idLibro = Integer.parseInt(fila[0].trim());
                String titulo = fila[1].trim();
                int edicion = Integer.parseInt(fila[2].trim());
                String editorial = fila[3].trim();
                int anio = Integer.parseInt(fila[4].trim());

                Libro l = new Libro(titulo, edicion, editorial, anio);
                p_biblioteca.getLibros().add(l);
                mapaLibrosPorId.put(idLibro, l);
            }

            // 3. Consultar Préstamos
            List<String[]> filasPrestamos = ejecutarConsulta(
                    "SELECT id_libro, dni_socio, CONVERT(VARCHAR(10), fecha_retiro, 23), " +
                    "ISNULL(CONVERT(VARCHAR(10), fecha_devolucion, 23), 'NULL') FROM Prestamos ORDER BY id_prestamo"
            );

            for (String[] fila : filasPrestamos) {
                if (fila.length < 4) continue;
                int idLibro = Integer.parseInt(fila[0].trim());
                int dniSocio = Integer.parseInt(fila[1].trim());
                String strRetiro = fila[2].trim();
                String strDevolucion = fila[3].trim();

                Libro libro = mapaLibrosPorId.get(idLibro);
                Socio socio = p_biblioteca.buscarSocio(dniSocio);

                if (libro != null && socio != null) {
                    Calendar calRetiro = Calendar.getInstance();
                    calRetiro.setTime(FORMATO_FECHA.parse(strRetiro));

                    Prestamo p = new Prestamo(calRetiro, socio, libro);
                    if (!strDevolucion.equalsIgnoreCase("NULL") && !strDevolucion.isEmpty()) {
                        Calendar calDev = Calendar.getInstance();
                        calDev.setTime(FORMATO_FECHA.parse(strDevolucion));
                        p.registrarFechaDevolucion(calDev);
                    }
                    socio.anadirPrestamo(p);
                    libro.agregarPrestamo(p);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Aviso: No se pudo leer desde SQL Server (" + e.getMessage() + ")");
            return false;
        }
    }

    /**
     * Sincroniza y guarda los Socios, Libros y Préstamos actuales de la Biblioteca
     * en las tablas relacionales de SQL Server (BibliotecaPOO).
     */
    public static void guardarEnSQLServer(Biblioteca p_biblioteca) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("USE ").append(BASE_DATOS).append(";\n");
            sql.append("DELETE FROM Prestamos;\n");
            sql.append("DELETE FROM Libros;\n");
            sql.append("DELETE FROM Socios;\n");
            sql.append("DBCC CHECKIDENT ('Libros', RESEED, 0);\n");
            sql.append("DBCC CHECKIDENT ('Prestamos', RESEED, 0);\n");

            // 1. Insertar Socios
            for (Socio s : p_biblioteca.getSocios().values()) {
                String extra = "";
                if (s instanceof Docente) {
                    extra = ((Docente) s).getArea();
                } else if (s instanceof Estudiante) {
                    extra = ((Estudiante) s).getCarrera();
                }
                sql.append(String.format(
                        "INSERT INTO Socios (dni, nombre, tipo, carrera_o_area, dias_prestamo) VALUES (%d, N'%s', N'%s', N'%s', %d);\n",
                        s.getDniSocio(),
                        escaparSQL(s.getNombre()),
                        escaparSQL(s.soyDeLaClase()),
                        escaparSQL(extra),
                        s.getDiasPrestamo()
                ));
            }

            // 2. Insertar Libros y sus Préstamos
            int idLibroActual = 1;
            for (Libro l : p_biblioteca.getLibros()) {
                sql.append(String.format(
                        "INSERT INTO Libros (titulo, edicion, editorial, anio) VALUES (N'%s', %d, N'%s', %d);\n",
                        escaparSQL(l.getTitulo()),
                        l.getEdicion(),
                        escaparSQL(l.getEditorial()),
                        l.getAnio()
                ));

                for (Prestamo p : l.getPrestamos()) {
                    String fRetiro = "'" + FORMATO_FECHA.format(p.getFechaRetiro().getTime()) + "'";
                    String fDev = (p.getFechaDevolucion() != null)
                            ? "'" + FORMATO_FECHA.format(p.getFechaDevolucion().getTime()) + "'"
                            : "NULL";

                    sql.append(String.format(
                            "INSERT INTO Prestamos (id_libro, dni_socio, fecha_retiro, fecha_devolucion) VALUES (%d, %d, %s, %s);\n",
                            idLibroActual,
                            p.getSocio().getDniSocio(),
                            fRetiro,
                            fDev
                    ));
                }
                idLibroActual++;
            }

            ejecutarComandoSQL(sql.toString());
        } catch (Exception e) {
            System.out.println("Error al guardar en SQL Server: " + e.getMessage());
        }
    }

    private static List<String[]> ejecutarConsulta(String query) throws Exception {
        List<String[]> resultados = new ArrayList<String[]>();
        ProcessBuilder pb = new ProcessBuilder(
                "sqlcmd", "-S", SERVIDOR, "-d", BASE_DATOS, "-E", "-C",
                "-h", "-1", "-W", "-s", "|", "-Q", "SET NOCOUNT ON; " + query
        );
        pb.redirectErrorStream(true);
        Process proc = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty() && linea.contains("|")) {
                    resultados.add(linea.split("\\|"));
                }
            }
        }
        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            throw new IOException("No se pudo conectar a SQL Server.");
        }
        return resultados;
    }

    private static void ejecutarComandoSQL(String scriptSQL) throws Exception {
        File tempScript = File.createTempFile("sync_biblio_", ".sql");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(tempScript), StandardCharsets.UTF_8)) {
            writer.write("SET NOCOUNT ON;\n" + scriptSQL);
        }
        ProcessBuilder pb = new ProcessBuilder(
                "sqlcmd", "-S", SERVIDOR, "-d", BASE_DATOS, "-E", "-C", "-f", "65001", "-i", tempScript.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        proc.waitFor();
        tempScript.delete();
    }

    private static String escaparSQL(String texto) {
        if (texto == null) return "";
        return texto.replace("'", "''");
    }
}
