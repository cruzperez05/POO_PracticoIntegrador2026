import java.util.*; 

public class Docente extends Socio{
    
    private String area;
    
    public Docente(String p_area, int p_dniSocio, String p_nombre, int p_diasPrestamos){
       super(p_dniSocio, p_nombre, 5)
       this.setArea(p_area);
    }
    
    private void setArea(String p_area){
        this.area = p_area;
    }
    
    public String getArea(){
        return this.area;
    }
    
    public boolean esResponsable(){
        
    }
    
    public void cambiarDiasDePrestamo(int p_dias){
        
    }
    
    public boolean puedePedir(){
        
    }
    
    public String soyDeLaClase(){
        
    }
}
