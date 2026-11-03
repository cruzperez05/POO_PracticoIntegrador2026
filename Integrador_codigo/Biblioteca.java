import java.util.*;

public class Biblioteca{
  private String nombre; 
  private ArrayList<Socio>socios;
  private ArrayList<Libro>libros;
  
Biblioteca(String p_nombre) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
}


private void setNombre(String p_nombre){
      this.nombre = p_nombre;
  }
  
  
  private void setSocios(ArrayList<Socio> p_socios){
      this.socios = p_socios;
  }
  
  private void setLibros(ArrayList<Libro> p_libros){
      this.libros = p_libros;
  }
  
  public String getNombre(){
      return this.nombre;
}


public ArrayList<Socio> getSocios(){
      return this.socios;
}


public ArrayList<Libro> getLibros(){
      return this.libros;
}


public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
    Libro nuevoLibro = new Libro(p_titulo, p_edicion, p_editorial, p_anio);
    this.getLibros().add(nuevoLibro);
}


public void  nuevoSocioEstudiante(int p_dniSocio, String p_nombre,String p_carrera){
     Estudiante nuevoEstudiante = new Estudiante(p_dniSocio, p_nombre, p_carrera);
     this.getSocios().add(nuevoEstudiante);
}

public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area){
    Docente nuevoDocente = new Docente(p_dniSocio,p_nombre,p_area);
    this.getSocios().add(nuevoDocente);
}

public boolean prestarLibro(Calendar p_fechaRetiro,Socio p_socio, Libro p_libro){
   
     if(p_socio.puedePedir() && !p_libro.prestado()){
         Prestamo prestamo = new Prestamo(p_fechaRetiro, p_socio, p_libro);
         p_socio.añadirPrestamo(prestamo);
         p_libro.anadirPrestamo(prestamo);   
         return true;
     } else{
         return false;
     }
}
  
}
  
