import java.util.*;

public class GestionBiblioteca {
    public static void main(String[] args) {
        
        
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
        
        biblioteca.nuevoLibro("JAVA. Como Programar", 1, "Pearson", 2012);
        biblioteca.nuevoLibro("Longman. Diccionario Pocket", 3, "Longman", 2010);
        biblioteca.nuevoLibro("Vivir para contarla", 1, "Sudamericana", 2002);
        biblioteca.nuevoLibro("Programando con JAVA", 2, "Alfaomega", 2015); 


        biblioteca.nuevoSocioDocente(14524782, "Juan Perez", "Programacion");
        biblioteca.nuevoSocioDocente(17982110, "Juan Fernandez", "Matematica");
        biblioteca.nuevoSocioDocente(10912002, "Maria Alegre", "Literatura");
        biblioteca.nuevoSocioEstudiante(28987498, "Francisco Paenza", "Sistemas");
        biblioteca.nuevoSocioEstudiante(31987123, "Cesar Milstein", "Biología");

        
        Socio juanPerez = biblioteca.buscarSocio(14524782);
        Socio juanFernandez = biblioteca.buscarSocio(17982110);
        Socio mariaAlegre = biblioteca.buscarSocio(10912002);
        Socio franciscoPaenza = biblioteca.buscarSocio(28987498);
        Socio cesarMilstein = biblioteca.buscarSocio(31987123);
        
        Libro JavaComoProgramar = biblioteca.getLibros().get(0);
        Libro Logman = biblioteca.getLibros().get(1);
        Libro Vivir = biblioteca.getLibros().get(2);
        Libro ProgramandoConJava = biblioteca.getLibros().get(3);
        
        
        Calendar fechaPrestasmo = Calendar.getInstance();  
        biblioteca.prestarLibro(fechaPrestasmo, juanPerez, ProgramandoConJava);

        Calendar fechaHaceTresDias = Calendar.getInstance();  
        fechaHaceTresDias.add(Calendar.DAY_OF_MONTH, -3);
        biblioteca.prestarLibro(fechaHaceTresDias,mariaAlegre, Logman);

         // Uno de los socios devuelve un libro dentro del plazo establecido.
        try {
             biblioteca.devolverLibro(Logman);
             System.out.println("El socio "+Logman.ultimoPrestamo().getSocio().getNombre()+" devuelve el libro "+Logman.getTitulo());
            } catch (LibroNoPrestadoException e) {
                         System.out.println(e.getMessage());
              }
        
        // Cuantos Socios de tipo Estudiante Hay?
        System.out.println("Cantidad de Socios de tipo Estudiantes: " +biblioteca.cantidadDeSociosPorTipo("Estudiante"));
        
        // ¿Cuál es la lista de docentes que nunca han adeudado ni adeudan libros? 
        System.out.println("Lista de Docentes Responsables: \n" + biblioteca.listaDeDocentesResponsables());
        
        //¿Cuál es la lista de libros? ¿Y la de socios? 
        System.out.println("Lista de Libros: \n" + biblioteca.listaDeLibros());
        System.out.println(biblioteca.listaDeSocios());
        
        // ¿Qué socio tiene prestado el libro “Programando con JAVA”?  
        try{
            System.out.println("El socio que tiene el libro *Programando con Java* es:  " + biblioteca.quienTieneElLibro(ProgramandoConJava));}
        catch(LibroNoPrestadoException e){
            System.out.println(e.getMessage());
        }
        
                // ===== DOCENTES Y ESTUDIANTES =====
        System.out.println("\n===== Docentes y Estudiantes =====");

        // Docente con préstamo vencido (5 días de plazo, retiró hace 10)
        Calendar hace10Dias = Calendar.getInstance();
        hace10Dias.add(Calendar.DAY_OF_MONTH, -10);
        biblioteca.prestarLibro(hace10Dias, juanFernandez, JavaComoProgramar);

        // Estudiante con préstamo vencido (20 días de plazo, retiró hace 25)
        Calendar hace25Dias = Calendar.getInstance();
        hace25Dias.add(Calendar.DAY_OF_MONTH, -25);
        biblioteca.prestarLibro(hace25Dias, franciscoPaenza, Logman);

        // Premio a una docente responsable
        Docente alegre = (Docente) mariaAlegre;
        alegre.cambiarDiasDePrestamo(2);

        // Polimorfismo: el mismo mensaje, cada tipo de socio responde distinto
        Socio[] socios = { juanFernandez, mariaAlegre, franciscoPaenza, cesarMilstein };
        for (Socio s : socios) {
            System.out.println(s.getNombre() + " (" + s.soyDeLaClase() + ") puede pedir: " + s.puedePedir());
        }

        // Docentes que nunca adeudaron no debería aparecer Juan Fernández
        System.out.println(biblioteca.listaDeDocentesResponsables());
        
        // Prestamos vencidos
            System.out.println("\n   Prestamos Vencidos:  ");
           for (Libro unLibro : biblioteca.prestamosVencidos()) {
           System.out.println("Libro: " + unLibro.getTitulo() + " - Socio: " + unLibro.ultimoPrestamo().getSocio().getNombre());
           }
        
    }
}
