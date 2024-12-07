/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Vero
 */
public class UsuarioDAO {
	public static String identificarTipoUsuario(String correo) {
        String sql = "SELECT 'alumno' AS tipoUsuario FROM Alumno WHERE correo = ? " +
                     "UNION " +
                     "SELECT 'profesor' AS tipoUsuario FROM Profesor WHERE correo = ?";
        String tipoUsuario = null;

        try (Connection conn = ConexionBD.abrirConexion(); // Reemplaza con tu clase de conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, correo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tipoUsuario = rs.getString("tipoUsuario");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tipoUsuario;
    }
}
