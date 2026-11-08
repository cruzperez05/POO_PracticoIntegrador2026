/**
 * Write a description of class Biblioteca here.
 * 
 * @author Pindo Angel, Maidana Juan, Romero Enzo
 * @version (1.0)
 */
import java.util.Calendar;
import java.util.*;

public class Biblioteca {
    private String nombre;
    private ArrayList<Libro> libros;
    private HashMap<Integer, Socio> socios;

    /**
     * Constructores
     */
    public Biblioteca(String p_nombre) {
        this.setNombre(p_nombre);
        this.setLibros(new ArrayList<Libro>());
        this.setSocios(new HashMap<Integer, Socio>());
    }

    public Biblioteca(String p_nombre, Libro p_libro, int p_dni, Socio p_socio) {
        this.setNombre(p_nombre);
        this.setLibros(new ArrayList<Libro>());
        this.getLibros().add(p_libro);
        this.setSocios(new HashMap<Integer, Socio>());
        this.getSocios().put(p_dni, p_socio);
    }

    public Biblioteca(String p_nombre, ArrayList<Libro> p_libros, HashMap<Integer, Socio> p_socios) {
        this.setNombre(p_nombre);
        this.setLibros(p_libros);
        this.setSocios(p_socios);
    }

    /**
     * Accessors
     */
    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }

    private void setLibros(ArrayList<Libro> p_libro) {
        this.libros = p_libro;
    }

    private void setSocios(HashMap<Integer, Socio> p_socio) {
        this.socios = p_socio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<Libro> getLibros() {
        return this.libros;
    }

    public HashMap<Integer, Socio> getSocios() {
        return this.socios;
    }

    /**
     * Instanciamos Libros
     */
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        Libro nuevoLibro = new Libro(p_titulo, p_edicion, p_editorial, p_anio);
        this.getLibros().add(nuevoLibro);
    }

    /**
     * Instanciamos Socios de tipo Estudiantes
     */
    public void nuevoSocioEstudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        Estudiante nuevoEstudiante = new Estudiante(p_dniSocio, p_nombre, p_carrera);
        this.getSocios().put(p_dniSocio, nuevoEstudiante);
    }

    /**
     * Instanciamos Socios de tipo Docentes
     */
    public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area) {
        Docente nuevoDocente = new Docente(p_dniSocio, p_nombre, p_area);
        this.getSocios().put(p_dniSocio, nuevoDocente);
    }

    /**
     * Remueve un socio de la biblioteca por su DNI.
     */
    public Socio quitarSocio(int p_dniSocio) {
        return this.getSocios().remove(p_dniSocio);
    }

    /**
     * Verificamos, si el solicitante no cuenta con una devolucion pendiente y si el libro se encuentra disponible
     * el prestamo se puede efectuar.
     * 
     */
    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {

        if (p_socio.puedePedir() && !p_libro.prestado()) {
            Prestamo prestamo = new Prestamo(p_fechaRetiro, p_socio, p_libro);
            p_socio.anadirPrestamo(prestamo);
            p_libro.agregarPrestamo(prestamo);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Devolvemos el libro prestado, si no se encuentra prestado entonces lanza una excepcion
     * @param // p_libro
     */
    public void devolverLibro(Libro p_libro) throws LibroNoPrestadoException{
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro " + p_libro.getTitulo() + " no se puede devolver ya que se encuentra en la biblioteca");
        }
        Prestamo p = p_libro.ultimoPrestamo();
        p.registrarFechaDevolucion(Calendar.getInstance());
    }

    /**
     * Contamos los Socios por su Tipo
     */
    public int cantidadDeSociosPorTipo(String p_objeto) {
        int contSocios = 0;

        for (Socio s : this.getSocios().values()) {
            if (s.soyDeLaClase().equals(p_objeto)) {
                contSocios += 1;
            }
        }

        return contSocios;
    }

    /**
     * Verifica: si el libro se encuentra prestado y todavia no ha sido devuelto,
     * guarda en la coleccion el libro.
     * @return ArrayList de libros con prestamos vencidos
     */
    public ArrayList<Libro> prestamosVencidos() {
        ArrayList<Libro> vencido = new ArrayList<Libro>();
        Calendar fechaActual = Calendar.getInstance();

        for (Libro l : this.getLibros()) {
            Prestamo p = l.ultimoPrestamo();
            if (l.prestado() && p != null && p.vencido(fechaActual)) {
                vencido.add(l);
            }
        }

        return vencido;
    }

    /**
     * Verifica: si el Socio es de tipo Docente y cumple con las condiciones de esResponsable(),
     * los acumula.
     * @return ArrayList de Docentes Responsables
     */
    public ArrayList<Socio> docentesResponsable() {
        ArrayList<Socio> docente = new ArrayList<Socio>();
        for (Socio s : this.socios.values()) {
            if (s.soyDeLaClase().equals("Docente") && ((Docente) s).esResponsable()) {
                docente.add(s);
            }
        }

        return docente;
    }

    public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
         if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
        }
        return p_libro.ultimoPrestamo().getSocio().getNombre();
    }

    public String listaDeSocios(){
        String socio = "";
        int i = 1;

        System.out.println("Lista de Socios");
        for(Socio s: this.getSocios().values()){
             socio += i + ") " + s.toString() + "\n";
             i++;
        }

        return socio;
    }

     /**
     * Busca el Socio por medio del dni, si lo encuentra retorna el socio
     * sino devuelve null
     * @param p_dni
     * @return Socio o Null
     */     
    public Socio buscarSocio(int p_dni){
        return this.getSocios().get(p_dni);
    }

    public String listaDeTitulos(){
        String titulo = "";

        for(Libro l: this.getLibros()){
            titulo += l.getTitulo() + "\n";
        }

        return titulo;
    }

    public String listaDeLibros(){
        String lista = "";

        for(Libro l: this.getLibros()){
            lista += "Titulo: " + l.getTitulo() + " || Prestado: " + (l.prestado() ? "Sí" : "No") + "\n";
        }
        return lista;
    }

    public String listaDeDocentesResponsables(){
        String responsable = "";

        for(Socio s: this.docentesResponsable()){
            responsable += s.toString() + "\n";
        }

        return responsable;
    }
}
