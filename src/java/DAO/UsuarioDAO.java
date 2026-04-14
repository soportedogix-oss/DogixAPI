package DAO;

import Config.Conexion;
import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        try {
            Connection cn = Conexion.getConnection();
            String sql = "SELECT * FROM usuarios";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setTelefono(rs.getString("telefono"));
                u.setToken(rs.getString("token"));
                u.setFechaRegistro(rs.getString("fecha_registro"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean registrarUsuario(Usuario u) {
        boolean ok = false;

        try {
            Connection cn = Conexion.getConnection();
            String sql = "INSERT INTO usuarios(nombre, email, password, telefono, token, rol) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getToken());
            ps.setString(6, u.getRol());

            ok = ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok;
    }

    public Usuario login(String email, String password) {
        Usuario u = null;

        try {
            Connection cn = Conexion.getConnection();
            String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
            PreparedStatement ps = cn.prepareStatement(sql);

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
                u.setToken(rs.getString("token"));
                u.setFechaRegistro(rs.getString("fecha_registro"));
                u.setRol(rs.getString("rol"));
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

    public boolean guardarToken(String email, String token) {
        boolean ok = false;

        try {
            Connection cn = Conexion.getConnection();
            String sql = "UPDATE usuarios SET token = ? WHERE email = ?";
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, token);
            ps.setString(2, email);

            ok = ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok;
    }

    public boolean resetPassword(String token, String password) {
        boolean ok = false;

        try {
            Connection cn = Conexion.getConnection();
            String sql = "UPDATE usuarios SET password = ?, token = NULL WHERE token = ?";
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, password);
            ps.setString(2, token);

            ok = ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok;
    }
}