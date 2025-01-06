package gestorproyectos.utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.text.ParseException;

public class Validador {

	String mensaje;

	public Validador(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public static void validarMatricula(String matricula) {
		if (matricula == null || !matricula.matches("^zS\\d{8}$")) {
			throw new IllegalArgumentException("La matrícula del estudiante es inválida. Debe comenzar con 'zS' seguido de 8 dígitos numéricos.");
		}
	}

	public static void validarTexto(String texto, String campo, int maxLength) {
		if (texto == null || texto.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo " + campo
					+ " no puede estar vacío.");
		}
		if (texto.length() > maxLength) {
			throw new IllegalArgumentException("El campo " + campo
					+ " no puede exceder los " + maxLength + " caracteres.");
		}
		String patronTexto = "^[\\p{L} .,'-]+$";
		if (!Pattern.matches(patronTexto, texto)) {
			throw new IllegalArgumentException("El campo " + campo
					+ " contiene caracteres no permitidos.");
		}
		if (texto.contains("  ")) {
			throw new IllegalArgumentException("El campo " + campo
					+ " no puede contener múltiples espacios en blanco consecutivos.");
		}
		if (texto.chars().allMatch(c -> c == texto.charAt(0))) {
			throw new IllegalArgumentException("El campo " + campo
					+ " no puede contener caracteres repetidos o ser uno solo.");
		}
		if (Pattern.compile("[.,]{2,}").matcher(texto).find()
				|| Pattern.compile("[.,]\\s+[.,]").matcher(texto).find()) {
			throw new IllegalArgumentException("El campo " + campo
					+ " no puede contener múltiples caracteres especiales consecutivos o separados por múltiples espacios.");
		}
	}

	public static void validarCorreo(String correo) {
		if (correo == null || correo.trim().isEmpty()) {
			throw new IllegalArgumentException("El correo no puede estar vacío.");
		}
		String patronCorreo = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
		if (!Pattern.matches(patronCorreo, correo)) {
			throw new IllegalArgumentException("El formato del correo electrónico es incorrecto.");
		}
	}

	public static void validarTelefono(String telefono) {
		String patronTelefono = "^[0-9]{10}$";
		if (!Pattern.matches(patronTelefono, telefono)) {
			throw new IllegalArgumentException("El formato del teléfono es incorrecto. Debe contener exactamente 10 dígitos numéricos.");
		}
	}

	public static void validarContrasenia(String contrasenia) {
		if (contrasenia == null || contrasenia.trim().isEmpty()) {
			throw new IllegalArgumentException("La contraseña no puede estar vacía.");
		}
		if (contrasenia.length() < 8) {
			throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
		}
		boolean tieneMayuscula = false;
		boolean tieneMinuscula = false;
		boolean tieneNumero = false;
		boolean tieneCaracterEspecial = false;
		String caracteresEspeciales = "!@#$%^&*()-_=+[]{}|;:,.<>?";
		for (char c : contrasenia.toCharArray()) {
			if (Character.isUpperCase(c)) {
				tieneMayuscula = true;
			} else if (Character.isLowerCase(c)) {
				tieneMinuscula = true;
			} else if (Character.isDigit(c)) {
				tieneNumero = true;
			} else if (caracteresEspeciales.contains(String.valueOf(c))) {
				tieneCaracterEspecial = true;
			}
		}
		if (!tieneMayuscula) {
			throw new IllegalArgumentException("La contraseña debe tener al menos una letra mayúscula.");
		}
		if (!tieneMinuscula) {
			throw new IllegalArgumentException("La contraseña debe tener al menos una letra minúscula.");
		}
		if (!tieneNumero) {
			throw new IllegalArgumentException("La contraseña debe tener al menos un número.");
		}
		if (!tieneCaracterEspecial) {
			throw new IllegalArgumentException("La contraseña debe tener al menos un carácter especial.");
		}
	}

	public static void validarCupo(int cupo) {
		if (cupo <= 1 || cupo > 30) {
			throw new IllegalArgumentException("El cupo debe ser un valor numérico mayor a 1 y menor o igual a 30.");
		}
	}

	public static float validarPromedio(float input) throws NumberFormatException {
		if (input < 0.0 || input > 10.0) {
			throw new NumberFormatException("El valor debe estar entre 0.0 y 10.0.");
		}

		String inputString = String.valueOf(input);

		if (!inputString.matches("\\d+(\\.\\d{1,2})?")) {
			throw new NumberFormatException("El valor no puede tener más de dos decimales.");
		}

		return input;
	}

	public static void validarPeriodo(String periodo) {
		if (periodo == null || periodo.trim().isEmpty()) {
			throw new IllegalArgumentException("El periodo no puede estar vacío.");
		}
		String patronPeriodo = "^[A-Z]{3}\\d{4}-[A-Z]{3}\\d{4}$";
		if (!Pattern.matches(patronPeriodo, periodo)) {
			throw new IllegalArgumentException("El formato del periodo es incorrecto. Debe ser como 'MMMYYYY-MMMYYYY'. Ejemplo: 'AGO2024-ENE2025'.");
		}
	}

	public static void validarFechaProyecto(String fechaProyecto) {
		if (fechaProyecto == null) {
			throw new IllegalArgumentException("La fecha de proyecto no puede ser nula.");
		}

		String regex = "^([A-Za-z]{3})(\\d{4})-([A-Za-z]{3})(\\d{4})$";

		if (!fechaProyecto.matches(regex)) {
			throw new IllegalArgumentException("La fecha del proyecto debe tener el formato MMMAAAA-MMMAAAA.");
		}

		String[] fechas = fechaProyecto.split("-");
		String fechaInicio = fechas[0];
		String fechaFin = fechas[1];

		SimpleDateFormat formatoFecha = new SimpleDateFormat("MMMyyyy", Locale.ENGLISH);
		try {
			Date fechaInicioDate = formatoFecha.parse(fechaInicio);
			Date fechaFinDate = formatoFecha.parse(fechaFin);

			if (fechaInicioDate.after(fechaFinDate)) {
				throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
			}

			Date fechaActual = new Date(System.currentTimeMillis());
			if (fechaInicioDate.before(fechaActual)) {
				throw new IllegalArgumentException("La fecha de inicio del proyecto no puede ser anterior a la fecha actual.");
			}

		} catch (ParseException ex) {
			throw new IllegalArgumentException("Error al analizar las fechas. Asegúrese de que el formato sea MMMAAAA-MMMAAAA.");
		}
	}

	public static void validarResponsable(int idResponsable) {
		if (idResponsable <= 0) {
			throw new IllegalArgumentException("El responsable asociado es inválido.");
		}
	}

	public static void validarEmpresa(int idEmpresa) {
		if (idEmpresa <= 0) {
			throw new IllegalArgumentException("La empresa asociada es inválida.");
		}
	}

	public static void validarNRC(int nrc) {
		String nrcStr = String.valueOf(nrc);

		if (nrcStr.length() != 5 || !nrcStr.matches("\\d{5}")) {
			throw new IllegalArgumentException("El NRC debe contener exactamente 5 números.");
		}
	}

	public static void validarSeccion(int seccion) {
		if (seccion < 1 || seccion > 3) {
			throw new IllegalArgumentException("El bloque debe ser 1, 2 o 3.");
		}
	}

	public static void validarProyectosAPriorizar(Map<Integer, Integer> proyectosYPrioridades, int totalProyectos) {
		Set<Integer> prioridades = new HashSet<>();

		for (Integer prioridad : proyectosYPrioridades.values()) {
			if (prioridad == null || prioridad <= 0) {
				throw new IllegalArgumentException("Todas las prioridades deben estar asignadas y ser mayores a 0.");
			}
			if (!prioridades.add(prioridad)) {
				throw new IllegalArgumentException("No pueden haber números de priorización duplicados.");
			}
		}

		if (prioridades.size() != totalProyectos) {
			throw new IllegalArgumentException("Debe haber una prioridad única para cada proyecto.");
		}
	}

	public static void validarHoras(int horas) {
		if (horas < 1) {
			throw new IllegalArgumentException("El numero de horas es invalido");
		}
	}
}
