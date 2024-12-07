package gestorproyectos.modelo.pojo;

import java.sql.Date;

public class InscripcionProyecto {
    private int idInscripcionProyecto;
    private Date fechaInscripcion;
    private String periodo;
    private String estadoInscripcion;
    private int idAlumno;
    private int idEE;

    public InscripcionProyecto(int idInscripcionProyecto, Date fechaInscripcion, String periodo, String estadoInscripcion, int idAlumno, int idEE) {
        this.idInscripcionProyecto = idInscripcionProyecto;
        this.fechaInscripcion = fechaInscripcion;
        this.periodo = periodo;
        this.estadoInscripcion = estadoInscripcion;
        this.idAlumno = idAlumno;
        this.idEE = idEE;
    }

    public InscripcionProyecto() {
    }

    public int getIdInscripcionProyecto() {
        return idInscripcionProyecto;
    }

    public void setIdInscripcionProyecto(int idInscripcionProyecto) {
        this.idInscripcionProyecto = idInscripcionProyecto;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(String estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdEE() {
        return idEE;
    }

    public void setIdEE(int idEE) {
        this.idEE = idEE;
    }
}
