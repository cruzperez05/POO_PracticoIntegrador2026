import java.util.ArrayList;

/**
 * Representa un libro de la biblioteca y administra sus préstamos.
 * 
 * @author Morales, Francisco 
 * @version 1.0
 */
public class Libro
{
    //Atributos
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos;

    //Constructores
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio)
    {
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        
        this.prestamos = new ArrayList<Prestamo>();
    }
    
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos)
    {
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(p_prestamos);
    }

    //Metodos
    private void setTitulo(String p_titulo){
        this.titulo = p_titulo;
    }
    
    private void setEdicion(int p_edicion){
        this.edicion = p_edicion;
    }
    
    private void setEditorial(String p_editorial){
        this.editorial = p_editorial;
    }
    
    private void setAnio(int p_anio){
        this.anio = p_anio;
    }
    
    private void setPrestamos(ArrayList<Prestamo> p_prestamos){
        this.prestamos = p_prestamos;
    }
    
    public String getTitulo(){
        return this.titulo;
    }
    
    public int getEdicion(){
        return this.edicion;
    }
    
    public String getEditorial(){
        return this.editorial;
    }
    
    public int getAnio(){
        return this.anio;
    }
    
    public ArrayList<Prestamo> getPrestamos(){
        return this.prestamos;
    }

    public boolean agregarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().add(p_prestamo);
    }

    public boolean quitarPrestamo(Prestamo p_prestamo){
        return this.getPrestamos().remove(p_prestamo);
    }

    /**
    * Verifica si el libro se encuentra prestado.
    * @return true si el libro está prestado, false en caso contrario.
    */
    public boolean prestado(){
        if(ultimoPrestamo() != null){
            if(ultimoPrestamo().getFechaDevolucion() == null){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /**
    * Retorna el último préstamo registrado del libro.
    * @return el último préstamo o null si no existen préstamos.
    */
    public Prestamo ultimoPrestamo(){
        if(!this.getPrestamos().isEmpty()){
            return this.getPrestamos().get(this.getPrestamos().size() - 1);
        }else{
            return null;
        }
    }
    
    /**
    * Retorna el título del libro en forma de cadena.
    * @return String con el título del libro.
    */
    public String toString(){
        return "Titulo: " + this.getTitulo(); 
    }
}

