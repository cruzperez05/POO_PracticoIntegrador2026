import java.util.*;

/**
 * Socio docente. No tiene límite de libros, pero no puede pedir
 * si tiene alguna devolución vencida.
 * Arranca con 5 días de préstamo; si es responsable, se le pueden
 * agregar días como premio.
 */
public class Docente extends Socio {

    private String area;

    public Docente(int p_dniSocio, String p_nombre, String p_area) {
        super(p_dniSocio, p_nombre, 5);   // 5 días iniciales de préstamo
        this.setArea(p_area);
    }
    
    private void setArea(String p_area){
        this.area = p_area;
    }

    public String getArea() {
        return this.area;
    }

    /**
     * Es responsable si NUNCA tuvo ni tiene un préstamo vencido.
     * Se revisan todos los préstamos: los devueltos se comparan contra
     * su fecha de devolución y los que siguen en su poder, contra hoy.
     */
    public boolean esResponsable() {
        for (Prestamo p : this.getPrestamos()) {
            Calendar fechaReferencia;
            if (p.getFechaDevolucion() != null) {
                fechaReferencia = p.getFechaDevolucion();
            } else {
                fechaReferencia = Calendar.getInstance();
            }
            if (p.vencido(fechaReferencia)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Suma días de préstamo a los que ya tiene (premio a la responsabilidad).
     */
    public void cambiarDiasDePrestamo(int p_dias) {
        this.setDiasPrestamo(this.getDiasPrestamo() + p_dias);
    }

    /**
     * Puede pedir si no tiene ningún préstamo vencido (sin límite de libros).
     * Solo se miran los préstamos que todavía están en su poder.
     */
    public boolean puedePedir() {
        Calendar hoy = Calendar.getInstance();
        for (Prestamo p : this.getPrestamos()) {
            if (p.getFechaDevolucion() == null && p.vencido(hoy)) {
                return false;
            }
        }
        return true;
    }

    public String soyDeLaClase() {
        return "Docente";
    }
}
