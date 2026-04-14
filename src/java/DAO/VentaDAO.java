package DAO;

import DB.Conexion;
import Model.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public boolean procesarCompra(int idUsuario) {

        Connection con = null;

        try {
            con = Conexion.conectar();
            con.setAutoCommit(false);

            String sql = "SELECT c.id_producto, c.cantidad, p.precio, p.stock " +
                         "FROM carrito c " +
                         "JOIN productos p ON c.id_producto = p.id_producto " +
                         "WHERE c.id_usuario=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            List<double[]> items = new ArrayList<>();

            while (rs.next()) {

                int idProducto = rs.getInt("id_producto");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                if (stock < cantidad) {
                    con.rollback();
                    return false;
                }

                items.add(new double[]{ idProducto, cantidad, precio });
            }

            if (items.isEmpty()) {
                con.rollback();
                return false;
            }

            for (double[] item : items) {

                int idProducto = (int) item[0];
                int cantidad = (int) item[1];
                double precio = item[2];

                double total = precio * cantidad;

                String insert = "INSERT INTO ventas(id_usuario, id_producto, cantidad, total) VALUES(?,?,?,?)";
                PreparedStatement ps2 = con.prepareStatement(insert);

                ps2.setInt(1, idUsuario);
                ps2.setInt(2, idProducto);
                ps2.setInt(3, cantidad);
                ps2.setDouble(4, total);

                if (ps2.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }

                String update = "UPDATE productos SET stock = stock - ? WHERE id_producto=? AND stock >= ?";
                PreparedStatement ps3 = con.prepareStatement(update);

                ps3.setInt(1, cantidad);
                ps3.setInt(2, idProducto);
                ps3.setInt(3, cantidad);

                if (ps3.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            String limpiar = "DELETE FROM carrito WHERE id_usuario=?";
            PreparedStatement ps4 = con.prepareStatement(limpiar);
            ps4.setInt(1, idUsuario);
            ps4.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        }
    }

    public List<Venta> listarPorUsuario(int idUsuario) {

        List<Venta> lista = new ArrayList<>();

        try (Connection con = Conexion.conectar()) {

            String sql = "SELECT v.*, p.nombre, p.imagen " +
                         "FROM ventas v " +
                         "LEFT JOIN productos p ON v.id_producto = p.id_producto " +
                         "WHERE v.id_usuario=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Venta v = new Venta();

                v.setIdVenta(rs.getInt("id_venta"));
                v.setCantidad(rs.getInt("cantidad"));
                v.setTotal(rs.getDouble("total"));
                v.setFecha(rs.getString("fecha"));

                String nombre = rs.getString("nombre");
                String imagen = rs.getString("imagen");

                v.setNombreProducto(nombre != null ? nombre : "Producto eliminado");
                v.setImagen(imagen != null ? imagen : "");

                lista.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}