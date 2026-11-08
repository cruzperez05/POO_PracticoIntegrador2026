import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

/**
 * Interfaz gráfica (Swing) para la Gestión de Biblioteca.
 * La ventana solo pide y muestra datos: la lógica queda en las clases del diseño
 * y los cambios se sincronizan con la Base de Datos en SQL Server.
 */
public class VentanaBiblioteca extends JFrame implements ActionListener {

    private Biblioteca biblioteca;

    // Campos de texto
    private JTextField tfDni;
    private JTextField tfNombre;
    private JTextField tfExtra;      // área (docente) o carrera (estudiante)
    private JTextField tfTitulo;

    // Zona de resultados
    private JTextArea salida;

    // Botones
    private JButton bPrestar;
    private JButton bDevolver;
    private JButton bQuienTiene;
    private JButton bPuedePedir;
    private JButton bVerSocios;
    private JButton bVerLibros;
    private JButton bNuevoDocente;
    private JButton bNuevoEstudiante;
    private JButton bQuitarSocio;

    public VentanaBiblioteca(Biblioteca p_biblioteca) {
        this.biblioteca = p_biblioteca;

        this.setTitle("Gestión de Biblioteca - " + biblioteca.getNombre() + " (Conectado a SQL Server)");
        this.setSize(750, 500);
        this.addWindowListener(new ManejadorCierre());

        // Un panel principal con BorderLayout, agregado al contenedor de la ventana
        JPanel miPanel = new JPanel();
        miPanel.setLayout(new BorderLayout(8, 8));
        Container miPanelContenedor = this.getContentPane();
        miPanelContenedor.add(miPanel);

        // campos de texto arriba
        miPanel.add(this.crearPanelCampos(), BorderLayout.NORTH);

        // resultados en el centro, con scroll
        salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(salida);
        miPanel.add(scroll, BorderLayout.CENTER);

        // botones abajo
        miPanel.add(this.crearPanelBotones(), BorderLayout.SOUTH);
    }

    // ---------- Armado de la ventana ----------

    private JPanel crearPanelCampos() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));

        tfDni = new JTextField(15);
        tfNombre = new JTextField(15);
        tfExtra = new JTextField(15);
        tfTitulo = new JTextField(15);

        panel.add(new JLabel("DNI del socio:"));
        panel.add(tfDni);
        panel.add(new JLabel("Nombre y apellido (para alta):"));
        panel.add(tfNombre);
        panel.add(new JLabel("Área / Carrera (para alta):"));
        panel.add(tfExtra);
        panel.add(new JLabel("Título del libro:"));
        panel.add(tfTitulo);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 5, 5));

        bPrestar = this.crearBoton("Prestar libro");
        bDevolver = this.crearBoton("Devolver libro");
        bQuienTiene = this.crearBoton("¿Quién tiene el libro?");
        bPuedePedir = this.crearBoton("¿Puede pedir el socio?");
        bVerSocios = this.crearBoton("Ver socios");
        bVerLibros = this.crearBoton("Ver libros");
        bNuevoDocente = this.crearBoton("Agregar socio docente");
        bNuevoEstudiante = this.crearBoton("Agregar socio estudiante");
        bQuitarSocio = this.crearBoton("Quitar socio");

        panel.add(bPrestar);
        panel.add(bDevolver);
        panel.add(bQuienTiene);
        panel.add(bPuedePedir);
        panel.add(bVerSocios);
        panel.add(bVerLibros);
        panel.add(bNuevoDocente);
        panel.add(bNuevoEstudiante);
        panel.add(bQuitarSocio);

        return panel;
    }

    private JButton crearBoton(String p_texto) {
        JButton boton = new JButton(p_texto);
        boton.addActionListener(this);   // la propia ventana escucha los eventos
        boton.setPreferredSize(new Dimension(150, 35));
        return boton;
    }

    // ---------- Manejo de eventos ----------

    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        if (fuente == bPrestar) {
            this.prestar();
        }
        if (fuente == bDevolver) {
            this.devolver();
        }
        if (fuente == bQuienTiene) {
            this.quienTiene();
        }
        if (fuente == bPuedePedir) {
            this.puedePedir();
        }
        if (fuente == bVerSocios) {
            salida.setText(biblioteca.listaDeSocios());
        }
        if (fuente == bVerLibros) {
            salida.setText(biblioteca.listaDeLibros());
        }
        if (fuente == bNuevoDocente) {
            this.agregarDocente();
        }
        if (fuente == bNuevoEstudiante) {
            this.agregarEstudiante();
        }
        if (fuente == bQuitarSocio) {
            this.quitarSocio();
        }
    }

    // ---------- Acciones ----------

    private void prestar() {
        Socio socio = this.buscarSocioIngresado();
        Libro libro = this.buscarLibroIngresado(false);
        if (socio == null || libro == null) {
            return;
        }
        boolean seConcreto = biblioteca.prestarLibro(Calendar.getInstance(), socio, libro);
        if (seConcreto) {
            ConexionSQLServer.guardarEnSQLServer(biblioteca);
            salida.setText("Préstamo realizado y guardado en SQL Server:\n" + libro.getTitulo() + " -> " + socio.getNombre());
        } else {
            salida.setText("No se pudo prestar. El socio no puede pedir "
                    + "(préstamos vencidos o límite de libros) o el libro ya está prestado.");
        }
    }

    private void devolver() {
        Libro libro = this.buscarLibroIngresado(true);
        if (libro == null) {
            return;
        }
        try {
            biblioteca.devolverLibro(libro);
            ConexionSQLServer.guardarEnSQLServer(biblioteca);
            salida.setText("Libro devuelto y actualizado en SQL Server: " + libro.getTitulo());
        } catch (LibroNoPrestadoException ex) {
            salida.setText(ex.getMessage());
        }
    }

    private void quienTiene() {
        Libro libro = this.buscarLibroIngresado(true);
        if (libro == null) {
            return;
        }
        try {
            salida.setText("El libro " + libro.getTitulo() + " lo tiene: "
                    + biblioteca.quienTieneElLibro(libro));
        } catch (LibroNoPrestadoException ex) {
            salida.setText(ex.getMessage());
        }
    }

    private void puedePedir() {
        Socio socio = this.buscarSocioIngresado();
        if (socio == null) {
            return;
        }
        String respuesta;
        if (socio.puedePedir()) {
            respuesta = "SÍ";
        } else {
            respuesta = "NO";
        }
        salida.setText(socio.getNombre() + " (" + socio.soyDeLaClase() + ") puede pedir: " + respuesta);
    }

    // ---------- Alta y Baja de Socios ----------

    private void agregarDocente() {
        try {
            int dni = Integer.parseInt(tfDni.getText().trim());
            String nombre = tfNombre.getText().trim();
            String area = tfExtra.getText().trim();
            if (nombre.isEmpty() || area.isEmpty()) {
                salida.setText("Complete DNI, Nombre y apellido, y el Área del docente.");
                return;
            }
            if (biblioteca.buscarSocio(dni) != null) {
                salida.setText("Ya existe un socio con el DNI " + dni);
                return;
            }
            biblioteca.nuevoSocioDocente(dni, nombre, area);
            ConexionSQLServer.guardarEnSQLServer(biblioteca);
            salida.setText("Docente agregado y guardado en SQL Server:\n" + biblioteca.buscarSocio(dni));
        } catch (NumberFormatException ex) {
            salida.setText("El DNI debe ser un número.");
        }
    }

    private void agregarEstudiante() {
        try {
            int dni = Integer.parseInt(tfDni.getText().trim());
            String nombre = tfNombre.getText().trim();
            String carrera = tfExtra.getText().trim();
            if (nombre.isEmpty() || carrera.isEmpty()) {
                salida.setText("Complete DNI, Nombre y apellido, y la Carrera del estudiante.");
                return;
            }
            if (biblioteca.buscarSocio(dni) != null) {
                salida.setText("Ya existe un socio con el DNI " + dni);
                return;
            }
            biblioteca.nuevoSocioEstudiante(dni, nombre, carrera);
            ConexionSQLServer.guardarEnSQLServer(biblioteca);
            salida.setText("Estudiante agregado y guardado en SQL Server:\n" + biblioteca.buscarSocio(dni));
        } catch (NumberFormatException ex) {
            salida.setText("El DNI debe ser un número.");
        }
    }

    private void quitarSocio() {
        Socio socio = this.buscarSocioIngresado();
        if (socio == null) {
            return;
        }
        if (socio.cantLibrosPrestados() > 0) {
            salida.setText("No se puede quitar al socio " + socio.getNombre()
                    + " porque todavía tiene " + socio.cantLibrosPrestados() + " libro(s) sin devolver.");
            return;
        }
        // Quitar préstamos históricos de los libros asociados a este socio para mantener consistencia en la BD
        for (Libro l : biblioteca.getLibros()) {
            for (int i = l.getPrestamos().size() - 1; i >= 0; i--) {
                if (l.getPrestamos().get(i).getSocio().getDniSocio() == socio.getDniSocio()) {
                    l.quitarPrestamo(l.getPrestamos().get(i));
                }
            }
        }
        biblioteca.quitarSocio(socio.getDniSocio());
        ConexionSQLServer.guardarEnSQLServer(biblioteca);
        salida.setText("Socio eliminado correctamente (también en SQL Server):\n" + socio.getNombre() + " (DNI: " + socio.getDniSocio() + ")");
    }

    // ---------- Búsquedas auxiliares ----------

    /**
     * Busca el socio con el DNI escrito en la ventana.
     * Si no lo encuentra, avisa en pantalla y devuelve null.
     */
    private Socio buscarSocioIngresado() {
        try {
            int dni = Integer.parseInt(tfDni.getText().trim());
            Socio socio = biblioteca.buscarSocio(dni);
            if (socio == null) {
                salida.setText("No existe un socio con el DNI " + dni);
            }
            return socio;
        } catch (NumberFormatException ex) {
            salida.setText("El DNI debe ser un número.");
            return null;
        }
    }

    /**
     * Busca un libro por el título escrito en la ventana.
     * Si hay varios ejemplares, prefiere uno cuyo estado de préstamo coincida
     * con p_prestado (para prestar: uno disponible; para devolver: uno prestado).
     * Si no lo encuentra, avisa en pantalla y devuelve null.
     */
    private Libro buscarLibroIngresado(boolean p_prestado) {
        String titulo = tfTitulo.getText().trim();
        Libro primero = null;
        for (Libro l : biblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                if (l.prestado() == p_prestado) {
                    return l;
                }
                if (primero == null) {
                    primero = l;
                }
            }
        }
        if (primero == null) {
            salida.setText("No existe un libro con el título \"" + titulo + "\"");
        }
        return primero;
    }

    // ---------- Manejador de cierre de la ventana ----------

    private class ManejadorCierre extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            ConexionSQLServer.guardarEnSQLServer(biblioteca);
            System.exit(0);
        }
    }
}
