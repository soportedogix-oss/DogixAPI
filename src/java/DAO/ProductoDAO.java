package DAO;

import DB.Conexion;
import Model.Producto;

import java.sql.*;
import java.util.*;

public class ProductoDAO {

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();

                p.setId(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getString("imagen"));
                p.setStock(rs.getInt("stock"));

                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return lista;
    }

    public Producto obtenerPorId(int id) {

        String sql = "SELECT * FROM productos WHERE id_producto=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Producto p = new Producto();

                p.setId(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getString("imagen"));
                p.setStock(rs.getInt("stock"));

                return p;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean insertar(String nombre, String descripcion, double precio, String imagen, int stock) {

        String sql = "INSERT INTO productos(nombre, descripcion, precio, imagen, stock) VALUES(?,?,?,?,?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setString(4, imagen);
            ps.setInt(5, stock);

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean actualizar(int id, String nombre, String descripcion, double precio, String imagen, int stock) {

        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, imagen=?, stock=? WHERE id_producto=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setString(4, imagen);
            ps.setInt(5, stock);
            ps.setInt(6, id);

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {

        String sql = "DELETE FROM productos WHERE id_producto=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}