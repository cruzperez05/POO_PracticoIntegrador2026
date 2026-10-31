.import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Representa el préstamo de un libro a un socio de la biblioteca.
 * 
 * @author Zarate Arturo Alan.
 * 
 * @version 1.0
 */
public  class Prestamo{
  private Calendar fechaRetiro; 
  private Calendar fechaDevolucion; 
  private Socio socio;
  private Libro libro;

/**
     * Constructor que permite crear un objeto Préstamo que incluye la fecha de devolución, para el caso en el que el libro fue devuelto.
     * 
     * @param Calendar p_fechaRetiro       Fecha en la que pidio el préstamo.
     * @param Calendar p_fechaDevolucion   Fecha en la que se devolvio el libro.
     * @param Socio p_socio                Socio de la biblioteca que realiza el préstamo.
     * @param Libro p_libro                Libro que fue prestado al socio.
*/
Prestamo(Calendar p_fechaRetiro,Calendar p_fechaDevolucion, Socio p_socio,Libro p_libro ){
      this.setFechaRetiro(p_fechaRetiro);
      this.registrarFechaDevolucion(p_fechaDevolucion);
      this.setSocio(p_socio);
      this.setLibro(p_libro);
} 
  
  
/**
     * Constructor que permite crear un objeto Préstamo sin una fecha de devolución, para el caso en el que el libro aun no fue devuelto.
     * 
     * @param Calendar p_fechaRetiro       Fecha en la que pidio el préstamo.
     * @param Socio p_socio                Socio de la biblioteca que pide el préstamo.
     * @param Libro p_libro                Libro que fue prestado al socio .
*/  
Prestamo(Calendar p_fechaRetiro,Socio p_socio,Libro p_libro ){
      this.setFechaRetiro(p_fechaRetiro);
      this.setSocio(p_socio);
      this.setLibro(p_libro);
  }
   

/**
  * Asigna p_fechaRetiro a la fecha en la que pidio el préstamo.
  *
  * @param Calendar p_fechaRetiro       Fecha de retiro del libro.
 */
private void setFechaRetiro(Calendar p_fechaRetiro){
      this.fechaRetiro = p_fechaRetiro;
}

  
/**
  * Asigna p_socio al socio de la biblioteca que pidio el préstamo.
  *
  * @param Socio p_socio      Socio que pide el préstamo .
 */  
private void setSocio(Socio p_socio){
      this.socio = p_socio;
}


/**
   * Asigna p_libro al libro que fue prestado.
   *
   * @param Libro p_libro      Libro prestado.
*/    
private void setLibro(Libro p_libro){
      this.libro = p_libro;
}
  
  
/**
    * Retorna la fecha  en la que se pidio el préstamo.
    * 
    * @return  fecha de retiro.
*/ 
public Calendar getFechaRetiro(){
      return this.fechaRetiro;
}
  
/**
     * Retorna la fecha en la que se devolvió el libro.
     * 
     * @return fecha de devolución, o null si aún está pendiente.
*/  
public Calendar getFechaDevolucion(){
      return this.fechaDevolucion;
}


/**
     * Retorna el socio que realizó el préstamo.
     * 
     * @return socio de la biblioteca.
*/  
public Socio getSocio(){
      return this.socio;
}


/**
     * Retorna el libro prestado.
     * 
     * @return libro prestado .
*/
public Libro getLibro(){
      return this.libro;
}
  
/**
     * Registra o actualiza la fecha de devolución del préstamo.
     * 
     * Solo se usa una vez que el libro fue devuelto.
     * 
     * @param Calendar p_fecha     Fecha de devolución.
*/  
public void registrarFechaDevolucion(Calendar p_fecha) {
    this.fechaDevolucion = p_fecha;
}


/**
     * Verifica si el préstamo se encuentra vencido respecto a una fecha pasada como parametro.
     * 
     * La verificacion se realiza sumandole a la fecha de retiro los días de préstamo permitidos del socio y con eso obtenemos
     * la fecha de vencimiento.
     * Luego controlar si la fecha pasada como parametro es mayor que dicha fecha de vencimiento.
     *
     * @param Calendar p_fecha     Fecha con la cual se desea comprobar el vencimiento.
     * 
     * @return true si la fecha pasada es mayor a la fecha de vencimiento; false en caso contrario.
*/
public boolean vencido(Calendar p_fecha) {
    Calendar fechaVencimiento = Calendar.getInstance();
    fechaVencimiento.setTime(this.getFechaRetiro().getTime());
    
    fechaVencimiento.add(Calendar.DAY_OF_MONTH, this.getSocio().getDiasPrestamo());
    
    return p_fecha.after(fechaVencimiento);
}


/**
     * Devuelve una representación en forma de cadena de los detalles del préstamo.
     * 
     * @return String con fecha de retiro, fecha de devolución, título del libro y nombre del socio.
*/
public String toString() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");

        String devolucion = "Pendiente";
        if (this.getFechaDevolucion() != null) {
            devolucion = formato.format(this.getFechaDevolucion().getTime());
        }

        return "Retiro: " + formato.format(this.getFechaRetiro().getTime()) + " - Devolución: " + devolucion + "\n"
             + "Libro: " + this.getLibro().getTitulo() + "\n"
             + "Socio: " + this.getSocio().getNombre();
}

}
  
  
