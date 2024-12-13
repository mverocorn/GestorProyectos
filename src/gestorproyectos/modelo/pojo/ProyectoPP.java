package gestorproyectos.modelo.pojo;

import java.sql.Date;

public class ProyectoPP {
    private int idProyectoPP;
    private String fechaProyecto;
    private String nombreProyecto;
    private String objetivoProyecto;
    private String descripcionProyecto;
    private int cupoProyecto;
    private int idResponsable;
    private int prioridad;

    public ProyectoPP(int idProyectoPP, String fechaProyecto, String nombreProyecto, 
        String objetivoProyecto, String descripcionProyecto, int cupoProyecto, int idResponsable) {
        this.idProyectoPP = idProyectoPP;
        this.fechaProyecto = fechaProyecto;
        this.nombreProyecto = nombreProyecto;
        this.objetivoProyecto = objetivoProyecto;
        this.descripcionProyecto = descripcionProyecto;
        this.cupoProyecto = cupoProyecto;
        this.idResponsable = idResponsable;
    }

    public ProyectoPP() {
    }
    
    public int getIdProyectoPP() {
        return idProyectoPP;
    }

    public void setIdProyectoPP(int idProyectoPP) {
        this.idProyectoPP = idProyectoPP;
    }

    public String getFechaProyecto() {
        return fechaProyecto;
    }

    public void setFechaProyecto(String fechaProyecto) {
        this.fechaProyecto = fechaProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getObjetivoProyecto() {
        return objetivoProyecto;
    }

    public void setObjetivoProyecto(String objetivoProyecto) {
        this.objetivoProyecto = objetivoProyecto;
    }

    public String getDescripcionProyecto() {
        return descripcionProyecto;
    }

    public void setDescripcionProyecto(String descripcionProyecto) {
        this.descripcionProyecto = descripcionProyecto;
    }

    public int getCupoProyecto() {
        return cupoProyecto;
    }

    public void setCupoProyecto(int cupoProyecto) {
        this.cupoProyecto = cupoProyecto;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
 
}
