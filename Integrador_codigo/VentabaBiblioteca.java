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

    public VentanaBiblioteca(Biblioteca p_biblioteca) {
        this.biblioteca = p_biblioteca;

        this.setTitle("Gestión de Biblioteca - " + biblioteca.getNombre());
        this.setSize(700, 450);
        this.addWindowListener(new ManejadorCierre());

        // Un panel principal con BorderLayout, agregado al contenedor de la ventana
        JPanel miPanel = new JPanel();
        miPanel.setLayout(new BorderLayout(8, 8));
        Container miPanelContenedor = this.getContentPane();
        miPanelContenedor.add(miPanel);

       //campos de texto
        miPanel.add(this.crearPanelCampos(), BorderLayout.NORTH);

       //resultados, con scroll
        salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(salida);
        miPanel.add(scroll, BorderLayout.CENTER);

        // botones
        miPanel.add(this.crearPanelBotones(), BorderLayout.SOUTH);
    }

    // ---------- Armado de la ventana ----------

    private JPanel crearPanelCampos() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 5, 5));

        tfDni = new JTextField(15);
        tfTitulo = new JTextField(15);

        panel.add(new JLabel("DNI del socio:"));
        panel.add(tfDni);
        panel.add(new JLabel("Título del libro:"));
        panel.add(tfTitulo);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 5, 5));

        bPrestar = this.crearBoton("Prestar libro");
        bDevolver = this.crearBoton("Devolver libro");
        bQuienTiene = this.crearBoton("¿Quién tiene el libro?");
        bPuedePedir = this.crearBoton("¿Puede pedir el socio?");
        bVerSocios = this.crearBoton("Ver socios");
        bVerLibros = this.crearBoton("Ver libros");

        panel.add(bPrestar);
        panel.add(bDevolver);
        panel.add(bQuienTiene);
        panel.add(bPuedePedir);
        panel.add(bVerSocios);
        panel.add(bVerLibros);

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
