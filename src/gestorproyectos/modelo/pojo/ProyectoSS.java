package gestorproyectos.modelo.pojo;

import java.sql.Date;

public class ProyectoSS {
    private int idProyectoSS;
    private Date fechaProyecto;
    private String nombreProyecto;
    private String objetivoProyecto;
    private String descripcionProyecto;
    private int cupoProyecto;
    private int idResponsable;

    public ProyectoSS(int idProyectoSS, Date fechaProyecto, String nombreProyecto, String objetivoProyecto, String descripcionProyecto, int cupoProyecto, int idResponsable) {
        this.idProyectoSS = idProyectoSS;
        this.fechaProyecto = fechaProyecto;
        this.nombreProyecto = nombreProyecto;
        this.objetivoProyecto = objetivoProyecto;
        this.descripcionProyecto = descripcionProyecto;
        this.cupoProyecto = cupoProyecto;
        this.idResponsable = idResponsable;
    }

    public ProyectoSS() {
    }

    public int getIdProyectoSS() {
        return idProyectoSS;
    }

    public void setIdProyectoSS(int idProyectoSS) {
        this.idProyectoSS = idProyectoSS;
    }

    public Date getFechaProyecto() {
        return fechaProyecto;
    }

    public void setFechaProyecto(Date fechaProyecto) {
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
}
