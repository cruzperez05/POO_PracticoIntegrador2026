import java.util.*;

public class GestionBiblioteca {
    public static void main(String[] args) {
        
        
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
        
        biblioteca.nuevoLibro("JAVA. Como Programar", 1, "Pearson", 2012);
        biblioteca.nuevoLibro("Programando con JAVA", 2, "Alfaomega", 2015);
        biblioteca.nuevoLibro("Longman. Diccionario Pocket", 3, "Longman", 2010);
        biblioteca.nuevoLibro("Vivir para contarla", 1, "Sudamericana", 2002);


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
        
 
        
        
        
        
        
        
        
        
        
        
        
    
    }
    }
