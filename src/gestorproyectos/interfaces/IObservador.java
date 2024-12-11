package gestorproyectos.interfaces;

import gestorproyectos.modelo.pojo.Alumno;
/**
 *
 * @author Vero
 */
public interface IObservador {
	
	public void notificarAlumnoGuardado(String nombreAlumno, String operacion);
	
}
