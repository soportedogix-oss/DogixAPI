package DAO;

import DB.Conexion;
import Model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> listarUsuarios() {

        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario u = new Usuario();

                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setTelefono(rs.getString("telefono"));
                u.setFechaRegistro(rs.getString("fecha_registro"));
                u.setRol(rs.getString("rol"));

                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean registrarUsuario(Usuario u) {

        String sql = "INSERT INTO usuarios(nombre,email,password,telefono,rol) VALUES (?,?,?,?,?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getRol());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Usuario login(String email, String password) {

        Usuario u = null;

        String sql = "SELECT * FROM usuarios WHERE email=? AND password=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                u = new Usuario();

                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setTelefono(rs.getString("telefono"));
                u.setFechaRegistro(rs.getString("fecha_registro"));
                u.setRol(rs.getString("rol"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

    public boolean guardarToken(String email, String token) {

        String sql = "UPDATE usuarios SET token=?, token_expira=DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE email=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, token);
            ps.setString(2, email);

            int filas = ps.executeUpdate();

            System.out.println("FILAS TOKEN: " + filas);

            return filas > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean resetPassword(String token, String nuevaPassword) {

        String sql = "UPDATE usuarios SET password=?, token=NULL, token_expira=NULL " +
                     "WHERE token=? AND token_expira > NOW()";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaPassword);
            ps.setString(2, token);

            int filas = ps.executeUpdate();

            System.out.println("RESET FILAS: " + filas);

            return filas > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}