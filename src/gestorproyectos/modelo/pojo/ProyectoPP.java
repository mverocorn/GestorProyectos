package gestorproyectos.modelo.pojo;

import java.sql.Date;

public class ProyectoPP {
    private int idProyectoPP;
    private Date fechaProyecto;
    private String periodoProyecto;
    private String nombreProyecto;
    private String objetivoProyecto;
    private String descripcionProyecto;
    private int cupoProyecto;
    private int idEmpresa;
    private int idResponsable;

    public ProyectoPP(int idProyectoPP, Date fechaProyecto, String periodoProyecto, String nombreProyecto, String objetivoProyecto, String descripcionProyecto, int cupoProyecto, int idEmpresa, int idResponsable) {
        this.idProyectoPP = idProyectoPP;
        this.fechaProyecto = fechaProyecto;
        this.periodoProyecto = periodoProyecto;
        this.nombreProyecto = nombreProyecto;
        this.objetivoProyecto = objetivoProyecto;
        this.descripcionProyecto = descripcionProyecto;
        this.cupoProyecto = cupoProyecto;
        this.idEmpresa = idEmpresa;
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

    public Date getFechaProyecto() {
        return fechaProyecto;
    }

    public void setFechaProyecto(Date fechaProyecto) {
        this.fechaProyecto = fechaProyecto;
    }

    public String getPeriodoProyecto() {
        return periodoProyecto;
    }

    public void setPeriodoProyecto(String periodoProyecto) {
        this.periodoProyecto = periodoProyecto;
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

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }
}
