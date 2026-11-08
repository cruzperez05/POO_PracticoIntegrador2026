import java.util.*;
/**
 * Clase abstracta que representa a un socio de la biblioteca.
 *
 * @author Zarate Arturo Alan.
 * 
 * @version 1.0
 */
public abstract class Socio{
  private int dniSocio; 
  private String nombre;
  private int diasPrestamo;
  private ArrayList<Prestamo> prestamos;

  
/**
     * Constructor que permite crear un objeto Socio sin una lista previa de prestamos.
     * 
     * @param int p_dniSocio        DNI del socio.
     * @param String p_nombre       Nombre y apellido del socio.
     * @param int p_diasPrestamo    Cantidad de días de prestamo permitidos.
*/
Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo){
      this.setDniSocio(p_dniSocio);
      this.setNombre(p_nombre);
      this.setDiasPrestamo(p_diasPrestamo);
      this.setPrestamos(new ArrayList<Prestamo>());
  }

  
/**
     * Constructor que permite crear un objeto Socio con una lista previa de prestamos.
     * 
     * @param int p_dniSocio            DNI del socio.
     * @param String p_nombre           Nombre y apellido del socio.
     * @param int p_diasPrestamo        Cantidad de días de prestamo permitidos.
     * @param ArrayList p_prestamos     Coleccion inicial de prestamos del socio.
*/
Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo,ArrayList<Prestamo> p_prestamos){
      this.setDniSocio(p_dniSocio);
      this.setNombre(p_nombre);
      this.setDiasPrestamo(p_diasPrestamo);
      this.setPrestamos(p_prestamos);
  }

  
/**
  * Asigna p_dniSocio al numero de dni del socio.
  *
  * @param int p_dniSocio.
 */
private void setDniSocio(int p_dniSocio){
      this.dniSocio = p_dniSocio;
  }

  
/**
     * Asigna p_nombre al nombre del socio.
     * 
     * @param String p_nombre.
*/
private void setNombre(String p_nombre){
      this.nombre = p_nombre;
  }

  
/**
     * Asigna p_diasPrestamo a los días de prestamo permitidos.
     * 
     * @param int p_diasPrestamo        Cantidad de días.
*/
protected void setDiasPrestamo(int p_diasPrestamo){
      this.diasPrestamo = p_diasPrestamo;
  }

  
/**
     * Asigna p_prestamos a la lista de prestamos del socio.
     * 
     * @param ArrayList p_prestamos      Coleccion de prestamos.
*/
private void setPrestamos(ArrayList<Prestamo> p_prestamos){
      this.prestamos = p_prestamos;
  }
  

/**
     * Retorna el DNI del socio.
     * 
     * @return  DNI del socio.
*/
public int getDniSocio(){
      return this.dniSocio;
}
 

/**
     * Retorna el nombre del socio.
     * 
     * @return  nombre del socio.
*/ 
public String getNombre(){
      return this.nombre;
}


/**
     * Retorna los dias de un prestamo permitido para el socio.
     * 
     * @return días de prestamo.
*/  
public int getDiasPrestamo(){
      return this.diasPrestamo;
}


/**
     * Retorna la coleccion completa de prestamos asociados al socio.
     * 
     * @return un ArrayList de objetos Prestamo.
*/  
public ArrayList<Prestamo> getPrestamos(){
      return this.prestamos;
}
  
 /**
     * Agrega un nuevo prestamo a la lista de prestamos del socio.
     * 
     * @param Prestamo p_prestamo     Prestamo a añadir.
*/ 
public boolean anadirPrestamo(Prestamo p_prestamo) {
            return this.getPrestamos().add(p_prestamo);
    }

    
/**
     * Remueve un prestamo de la lista de prestamos del socio.
     * 
     * @param Prestamo p_prestamo    Prestamo a quitar.
*/
public boolean quitarPrestamo(Prestamo p_prestamo) {
            return this.getPrestamos().remove(p_prestamo);
}
    

/**
     * Devuelve la cantidad de libros en poder del socio.
     * 
     * @return cantidad de libros en poder del socio.
*/    
public int cantLibrosPrestados(){
    int contador = 0;
    for (Prestamo unPrestamo : this.getPrestamos()) {
        if(unPrestamo.getFechaDevolucion() == null){
            contador++;
        }
    }
      return contador;
    
}

/**
     * Devuelve una representacion en formato de cadena de los datos del socio.
     * 
     * @return String conformado con los datos del socio y cantidad de libros prestados.
*/ 
public String toString(){
      return "D.N.I: "+this.getDniSocio()+" || "+this.getNombre()+"( "+this.soyDeLaClase()+") || Libros Prestados: "
      +this.cantLibrosPrestados();
  }
  

/**
     * Evalua si el socio puede solicitar un nuevo prestamo. 
     * 
     * Devuelve true si el socio no tiene ningun prestamo activo y vencido.
     * 
     * @return true si no posee deudas de tiempo activas, false en caso contrario.
*/
public boolean puedePedir() {
        boolean puede = true;
        for (Prestamo unPrestamo : this.getPrestamos()) {
            if (unPrestamo.getFechaDevolucion() == null && unPrestamo.vencido(Calendar.getInstance())) {
                puede = false;
                break;
            }
        }
        return puede;
    }

    
/**
     * Metodo abstracto que retorna el tipo específico de socio.
     * 
     * @return un String indicando el tipo de socio.
 */
public abstract String soyDeLaClase();
  

}
 
