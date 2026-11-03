import java.util.*;
/**
 * Socio estudiante. Puede pedir si no tiene ningún préstamo vencido
 * y si no tiene más de 3 libros en su poder.
 * Sus préstamos vencen a los 20 días de la fecha de retiro.
 */
public class Estudiante extends Socio {

    private String carrera;

    public Estudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        super(p_dniSocio, p_nombre, 20);  // 20 días de préstamo
        this.setCarrera(p_carrera);
    }
    
    private void setCarrera(String p_carrera){
        this.carrera = p_carrera;
    }

    public String getCarrera() {
        return this.carrera;
    }

    /**
     * Puede pedir si no tiene préstamos vencidos y no tiene más de 3 libros.
     */
    public boolean puedePedir() {
        Calendar hoy = Calendar.getInstance();
        for (Prestamo p : this.getPrestamos()) {
            if (p.getFechaDevolucion() == null && p.vencido(hoy)) {
                return false;
            }
        }
        return this.cantLibrosPrestados() <= 3;
    }

    public String soyDeLaClase() {
        return "Estudiante";
    }
}
