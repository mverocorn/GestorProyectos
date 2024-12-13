package gestorproyectos.modelo.pojo;

public class Profesor implements IUsuario{
    private int idProfesor;
    private String nombreProfesor;
    private String apellidoProfesor;
    private String clave;
    private String correo;
    private String contrasenia;

    public Profesor(int idProfesor, String nombreProfesor, String apellidoProfesor, 
        String clave, String correo, String contrasenia) {
        this.idProfesor = idProfesor;
        this.nombreProfesor = nombreProfesor;
        this.apellidoProfesor = apellidoProfesor;
        this.clave = clave;
        this.correo = correo;
        this.contrasenia = contrasenia;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

	@Override
	public String toString() {
		return nombreProfesor + " " + apellidoProfesor;
	}
	
	
    
}
