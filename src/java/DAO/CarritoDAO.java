package DAO;

import DB.Conexion;
import Model.Carrito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarritoDAO {

    // 🔥 AGREGAR O ACTUALIZAR
    public boolean agregarOActualizar(int idUsuario, int idProducto, int cantidad) {

        String check = "SELECT id_carrito, cantidad FROM carrito WHERE id_usuario=? AND id_producto=?";
        String update = "UPDATE carrito SET cantidad=? WHERE id_carrito=?";
        String insert = "INSERT INTO carrito(id_usuario, id_producto, cantidad) VALUES(?,?,?)";

        try (Connection con = Conexion.conectar()) {

            // 🔍 VALIDAR PRODUCTO EXISTE
            String stockSql = "SELECT stock FROM productos WHERE id_producto=?";
            PreparedStatement psStock = con.prepareStatement(stockSql);
            psStock.setInt(1, idProducto);

            ResultSet rsStock = psStock.executeQuery();

            if (!rsStock.next()) {
                System.out.println("❌ Producto no existe");
                return false;
            }

            int stock = rsStock.getInt("stock");

            // 🔍 EXISTE EN CARRITO
            try (PreparedStatement ps1 = con.prepareStatement(check)) {

                ps1.setInt(1, idUsuario);
                ps1.setInt(2, idProducto);

                ResultSet rs = ps1.executeQuery();

                if (rs.next()) {

                    int idCarrito = rs.getInt("id_carrito");
                    int actual = rs.getInt("cantidad");

                    int nuevaCantidad = actual + cantidad;

                    if (nuevaCantidad > stock) {
                        System.out.println("❌ Excede stock");
                        return false;
                    }

                    try (PreparedStatement ps2 = con.prepareStatement(update)) {

                        ps2.setInt(1, nuevaCantidad);
                        ps2.setInt(2, idCarrito);

                        return ps2.executeUpdate() > 0;
                    }

                } else {

                    if (cantidad > stock) {
                        System.out.println("❌ Excede stock");
                        return false;
                    }

                    try (PreparedStatement ps3 = con.prepareStatement(insert)) {

                        ps3.setInt(1, idUsuario);
                        ps3.setInt(2, idProducto);
                        ps3.setInt(3, cantidad);

                        return ps3.executeUpdate() > 0;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error agregar carrito: " + e.getMessage());
            return false;
        }
    }

    // 🔥 LISTAR (CORREGIDO TOTAL 🔥)
    public List<Carrito> listar(int idUsuario) {

        List<Carrito> lista = new ArrayList<>();

        String sql =
                "SELECT c.id_carrito, c.id_usuario, c.id_producto, c.cantidad, " +
                "p.nombre AS nombreProducto, p.imagen, p.precio, p.stock " +
                "FROM carrito c " +
                "LEFT JOIN productos p ON c.id_producto = p.id_producto " +
                "WHERE c.id_usuario=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Carrito c = new Carrito();

                c.setIdCarrito(rs.getInt("id_carrito"));
                c.setIdUsuario(rs.getInt("id_usuario"));
                c.setIdProducto(rs.getInt("id_producto"));
                c.setCantidad(rs.getInt("cantidad"));

                // 🔥 PROTECCIÓN NULL (CLAVE)
                String nombre = rs.getString("nombreProducto");
                String imagen = rs.getString("imagen");

                c.setNombreProducto(nombre != null ? nombre : "Producto eliminado");
                c.setImagen(imagen != null ? imagen : "");
                c.setPrecio(rs.getDouble("precio"));
                c.setStock(rs.getInt("stock"));

                lista.add(c);
            }

            System.out.println("🟢 Carrito size: " + lista.size());

        } catch (Exception e) {
            System.out.println("❌ Error listar carrito: " + e.getMessage());
        }

        return lista;
    }

    // 🔥 ELIMINAR
    public boolean eliminar(int idCarrito) {

        String sql = "DELETE FROM carrito WHERE id_carrito=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCarrito);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Error eliminar: " + e.getMessage());
            return false;
        }
    }

    // 🔥 LIMPIAR
    public boolean limpiarCarrito(int idUsuario) {

        String sql = "DELETE FROM carrito WHERE id_usuario=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() >= 0;

        } catch (Exception e) {
            System.out.println("❌ Error limpiar carrito: " + e.getMessage());
            return false;
        }
    }
}