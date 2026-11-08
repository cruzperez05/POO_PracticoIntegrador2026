import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

/**
 * Interfaz gráfica (Swing) para la Gestión de Biblioteca.
 * La ventana solo pide y muestra datos: la lógica queda en las clases del diseño.
 */
public class VentanaBiblioteca extends JFrame implements ActionListener {

    private Biblioteca biblioteca;

    // Campos de texto
    private JTextField tfDni;
    private JTextField tfNombre;
    private JTextField tfExtra;      // area (docente) o carrera (estudiante)
    private JTextField tfTitulo;
    private JTextField tfEdicion;
    private JTextField tfEditorial;
    private JTextField tfAnio;

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
    private JButton bNuevoLibro;

    public VentanaBiblioteca(Biblioteca p_biblioteca) {
        this.setBiblioteca(p_biblioteca);

        this.setTitle("Gestión de Biblioteca - " + biblioteca.getNombre());
        this.setSize(850, 480);
        this.addWindowListener(new ManejadorCierre());

        // Un panel principal con BorderLayout, agregado al contenedor de la ventana
        JPanel miPanel = new JPanel();
        miPanel.setLayout(new BorderLayout(8, 8));
        Container miPanelContenedor = this.getContentPane();
        miPanelContenedor.add(miPanel);

        // a la izquierda campos de texto
        miPanel.add(this.crearPanelCampos(), BorderLayout.WEST);

        // Centro: resultados, con scroll
        salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(salida);
        miPanel.add(scroll, BorderLayout.CENTER);

        // abajo los botones
        miPanel.add(this.crearPanelBotones(), BorderLayout.SOUTH);
    }
    
    private void setBiblioteca(Biblioteca p_biblioteca){
        this.biblioteca = p_biblioteca;
    }
    
    public Biblioteca getBiblioteca(){
        return this.biblioteca;
    }

    // ---------- Armado de la ventana ----------

    private JPanel crearPanelCampos() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 5, 5));

        tfDni = new JTextField(15);
        tfNombre = new JTextField(15);
        tfExtra = new JTextField(15);
        tfTitulo = new JTextField(15);
        tfEdicion = new JTextField(15);
        tfEditorial = new JTextField(15);
        tfAnio = new JTextField(15);

        panel.add(new JLabel("DNI del socio:"));
        panel.add(tfDni);
        panel.add(new JLabel("Nombre y apellido:"));
        panel.add(tfNombre);
        panel.add(new JLabel("Área / Carrera:"));
        panel.add(tfExtra);
        panel.add(new JLabel("Título del libro:"));
        panel.add(tfTitulo);
        panel.add(new JLabel("Edición:"));
        panel.add(tfEdicion);
        panel.add(new JLabel("Editorial:"));
        panel.add(tfEditorial);
        panel.add(new JLabel("Año:"));
        panel.add(tfAnio);

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
        bNuevoLibro = this.crearBoton("Agregar libro");

        panel.add(bPrestar);
        panel.add(bDevolver);
        panel.add(bQuienTiene);
        panel.add(bPuedePedir);
        panel.add(bVerSocios);
        panel.add(bVerLibros);
        panel.add(bNuevoDocente);
        panel.add(bNuevoEstudiante);
        panel.add(bNuevoLibro);

        return panel;
    }

    private JButton crearBoton(String p_texto) {
        JButton boton = new JButton(p_texto);
        boton.addActionListener(this);   
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
        if (fuente == bNuevoLibro) {
            this.agregarLibro();
        }
    }

    // ---------- Acciones de préstamo y consulta ----------

    private void prestar() {
        Socio socio = this.buscarSocioIngresado();
        Libro libro = this.buscarLibroIngresado(false);
        if (socio == null || libro == null) {
            return;
        }
        boolean seConcreto = biblioteca.prestarLibro(Calendar.getInstance(), socio, libro);
        if (seConcreto) {
            salida.setText("Préstamo realizado:\n" + libro.getTitulo() + " -> " + socio.getNombre());
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
            salida.setText("Libro devuelto: " + libro.getTitulo());
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

    // ---------- Altas ----------

    private void agregarDocente() {
        try {
            int dni = Integer.parseInt(tfDni.getText().trim());
            String nombre = tfNombre.getText().trim();
            String area = tfExtra.getText().trim();
            if (nombre.equals("") || area.equals("")) {
                salida.setText("Complete DNI, nombre y apellido, y el área del docente.");
                return;
            }
            if (biblioteca.buscarSocio(dni) != null) {
                salida.setText("Ya existe un socio con el DNI " + dni);
                return;
            }
            biblioteca.nuevoSocioDocente(dni, nombre, area);
            salida.setText("Docente agregado:\n" + biblioteca.buscarSocio(dni));
        } catch (NumberFormatException ex) {
            salida.setText("El DNI debe ser un número.");
        }
    }

    private void agregarEstudiante() {
        try {
            int dni = Integer.parseInt(tfDni.getText().trim());
            String nombre = tfNombre.getText().trim();
            String carrera = tfExtra.getText().trim();
            if (nombre.equals("") || carrera.equals("")) {
                salida.setText("Complete DNI, nombre y apellido, y la carrera del estudiante.");
                return;
            }
            if (biblioteca.buscarSocio(dni) != null) {
                salida.setText("Ya existe un socio con el DNI " + dni);
                return;
            }
            biblioteca.nuevoSocioEstudiante(dni, nombre, carrera);
            salida.setText("Estudiante agregado:\n" + biblioteca.buscarSocio(dni));
        } catch (NumberFormatException ex) {
            salida.setText("El DNI debe ser un número.");
        }
    }

    private void agregarLibro() {
        try {
            String titulo = tfTitulo.getText().trim();
            int edicion = Integer.parseInt(tfEdicion.getText().trim());
            String editorial = tfEditorial.getText().trim();
            int anio = Integer.parseInt(tfAnio.getText().trim());
            if (titulo.equals("") || editorial.equals("")) {
                salida.setText("Complete título, edición, editorial y año.");
                return;
            }
            biblioteca.nuevoLibro(titulo, edicion, editorial, anio);
            salida.setText("Libro agregado: " + titulo);
        } catch (NumberFormatException ex) {
            salida.setText("La edición y el año deben ser números.");
        }
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
            System.exit(0);
        }
    }
}
