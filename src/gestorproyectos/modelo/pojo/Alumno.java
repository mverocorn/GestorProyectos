package gestorproyectos.modelo.pojo;

public class Alumno {
    private Integer idAlumno;
    private String nombreAlumno;
    private String apellidoAlumno;
    private String matricula;
    private String telefonoAlumno;
    private String correoAlumno;
    private Float promedio;
    private String estadoAlumno;

    public Alumno() {
    }

    public Alumno(Integer idAlumno, String nombreAlumno, String apellidoAlumno, String matricula, String telefonoAlumno, String correoAlumno, Float promedio, String estadoAlumno) {
        this.idAlumno = idAlumno;
        this.nombreAlumno = nombreAlumno;
        this.apellidoAlumno = apellidoAlumno;
        this.matricula = matricula;
        this.telefonoAlumno = telefonoAlumno;
        this.correoAlumno = correoAlumno;
        this.promedio = promedio;
        this.estadoAlumno = estadoAlumno;
    }

    public Integer getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidoAlumno() {
        return apellidoAlumno;
    }

    public void setApellidoAlumno(String apellidoAlumno) {
        this.apellidoAlumno = apellidoAlumno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTelefonoAlumno() {
        return telefonoAlumno;
    }

    public void setTelefonoAlumno(String telefonoAlumno) {
        this.telefonoAlumno = telefonoAlumno;
    }

    public String getCorreoAlumno() {
        return correoAlumno;
    }

    public void setCorreoAlumno(String correoAlumno) {
        this.correoAlumno = correoAlumno;
    }

    public Float getPromedio() {
        return promedio;
    }

    public void setPromedio(Float promedio) {
        this.promedio = promedio;
    }

    public String getEstadoAlumno() {
        return estadoAlumno;
    }

    public void setEstadoAlumno(String estadoAlumno) {
        this.estadoAlumno = estadoAlumno;
    }
}

