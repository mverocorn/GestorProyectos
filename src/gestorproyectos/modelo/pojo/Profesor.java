package gestorproyectos.modelo.pojo;

public class Profesor {
    private int idProfesor;
    private String nombreProfesor;
    private String apellidoProfesor;
    private String clave;
    private String correoProfesor;
    private String contraseniaProfesor;

    public Profesor(int idProfesor, String nombreProfesor, String apellidoProfesor, String clave, String correoProfesor, String contraseniaProfesor) {
        this.idProfesor = idProfesor;
        this.nombreProfesor = nombreProfesor;
        this.apellidoProfesor = apellidoProfesor;
        this.clave = clave;
        this.correoProfesor = correoProfesor;
        this.contraseniaProfesor = contraseniaProfesor;
    }

    public Profesor() {
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getApellidoProfesor() {
        return apellidoProfesor;
    }

    public void setApellidoProfesor(String apellidoProfesor) {
        this.apellidoProfesor = apellidoProfesor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreoProfesor() {
        return correoProfesor;
    }

    public void setCorreoProfesor(String correoProfesor) {
        this.correoProfesor = correoProfesor;
    }

    public String getContraseniaProfesor() {
        return contraseniaProfesor;
    }

    public void setContraseniaProfesor(String contraseniaProfesor) {
        this.contraseniaProfesor = contraseniaProfesor;
    }
    
}
