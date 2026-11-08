/**
 * Clase auxiliar para ejecutar la ventana de la biblioteca.
 * Carga los datos desde SQL Server (o datos de prueba si está vacía) y muestra la interfaz.
 */
public class ProcesoVentana {
 
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
 
        // Intenta cargar los socios, libros y préstamos desde SQL Server (Base de datos BibliotecaPOO)
        boolean cargadoDesdeBD = ConexionSQLServer.cargarDesdeSQLServer(biblioteca);

        // Si la base de datos no tenía datos, carga los de prueba y los guarda en SQL Server
        if (!cargadoDesdeBD) {
            biblioteca.nuevoLibro("Java", 1, "Pearson", 2012);
            biblioteca.nuevoLibro("Diccionario", 3, "Longman", 2010);
            biblioteca.nuevoLibro("Vivir para contarla", 1, "Sudamericana", 2002);
            biblioteca.nuevoLibro("Programando", 2, "Alfaomega", 2015);
 
            biblioteca.nuevoSocioDocente(1, "Juan Perez", "Programacion");
            biblioteca.nuevoSocioDocente(2, "Juan Fernandez", "Matematica");
            biblioteca.nuevoSocioDocente(3, "Maria Alegre", "Literatura");
            biblioteca.nuevoSocioEstudiante(4, "Francisco Paenza", "Sistemas");
            biblioteca.nuevoSocioEstudiante(5, "Cesar Milstein", "Biologia");

            ConexionSQLServer.guardarEnSQLServer(biblioteca);
        }
 
        VentanaBiblioteca miVentana = new VentanaBiblioteca(biblioteca);
        miVentana.setVisible(true);
    }
}
