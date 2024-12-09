package gestorproyectos.modelo.pojo;

public class Alumno implements IUsuario{
    private Integer idAlumno;
    private String nombreAlumno;
    private String apellidoAlumno;
    private String matricula;
    private String telefonoAlumno;
    private String correo;
    private Float promedio;
    private String estadoAlumno;
    private String contrasenia;

    public Alumno() {
    }

    public Alumno(Integer idAlumno, String nombreAlumno, String apellidoAlumno, String matricula, String telefonoAlumno, String correo, Float promedio, String estadoAlumno, String contrasenia) {
        this.idAlumno = idAlumno;
        this.nombreAlumno = nombreAlumno;
        this.apellidoAlumno = apellidoAlumno;
        this.matricula = matricula;
        this.telefonoAlumno = telefonoAlumno;
        this.correo = correo;
        this.promedio = promedio;
        this.estadoAlumno = estadoAlumno;
        this.contrasenia = contrasenia;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}

