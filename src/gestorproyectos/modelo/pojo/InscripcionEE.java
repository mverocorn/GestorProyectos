package gestorproyectos.modelo.pojo;

import java.sql.Date;

public class InscripcionEE {

	private int idInscripcionEE;
	private Date fechaInscripcion;
	private String estadoInscripcion;
	private int idAlumno;
	private int idEE;
	private int idProyectoPP;
	private int idProyectoSS;
	private String nombreAlumno; // Nombre del alumno
	private float promedio; // Promedio del alumno
	private String nombreEE; // Nombre de la EE
	private String seccion; // Sección de la EE
        private String nombreProyecto; // Nombre del Proyecto (PP o SS)

	public InscripcionEE(int idInscripcionEE, Date fechaInscripcion, String estadoInscripcion,
			int idAlumno, int idEE, int idProyectoPP, int idProyectoSS) {
		this.idInscripcionEE = idInscripcionEE;
		this.fechaInscripcion = fechaInscripcion;
		this.estadoInscripcion = estadoInscripcion;
		this.idAlumno = idAlumno;
		this.idEE = idEE;
		this.idProyectoPP = idProyectoPP;
		this.idProyectoSS = idProyectoSS;
	}

	public InscripcionEE() {
	}

	public int getIdInscripcionEE() {
		return idInscripcionEE;
	}

	public void setIdInscripcionEE(int idInscripcionEE) {
		this.idInscripcionEE = idInscripcionEE;
	}

	public Date getFechaInscripcion() {
		return fechaInscripcion;
	}

	public void setFechaInscripcion(Date fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
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

	public int getIdProyectoPP() {
		return idProyectoPP;
	}

	public void setIdProyectoPP(int idProyectoPP) {
		this.idProyectoPP = idProyectoPP;
	}

	public int getIdProyectoSS() {
		return idProyectoSS;
	}

	public void setIdProyectoSS(int idProyectoSS) {
		this.idProyectoSS = idProyectoSS;
	}
	
	public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }

    public String getNombreEE() {
        return nombreEE;
    }

    public void setNombreEE(String nombreEE) {
        this.nombreEE = nombreEE;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
}
