package WS;

import DAO.ProductoDAO;
import Model.Producto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/productos")
public class ProductoREST {

    ProductoDAO dao = new ProductoDAO();

    // 🔥 LISTAR
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {

        List<Producto> lista = dao.listar();

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < lista.size(); i++) {
            Producto p = lista.get(i);

            json.append("{")
                .append("\"id\":").append(p.getId()).append(",")
                .append("\"nombre\":\"").append(esc(p.getNombre())).append("\",")
                .append("\"precio\":").append(p.getPrecio()).append(",")
                .append("\"descripcion\":\"").append(esc(p.getDescripcion())).append("\",")
                .append("\"imagen\":\"").append(esc(p.getImagen())).append("\",")
                .append("\"stock\":").append(p.getStock())
                .append("}");

            if (i < lista.size() - 1) json.append(",");
        }

        json.append("]");

        return Response.ok(json.toString()).build();
    }

    // 🔥 OBTENER POR ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtener(@PathParam("id") int id) {

        List<Producto> lista = dao.listar();

        for (Producto p : lista) {
            if (p.getId() == id) {

                String json = "{"
                        + "\"id\":" + p.getId() + ","
                        + "\"nombre\":\"" + esc(p.getNombre()) + "\","
                        + "\"precio\":" + p.getPrecio() + ","
                        + "\"descripcion\":\"" + esc(p.getDescripcion()) + "\","
                        + "\"imagen\":\"" + esc(p.getImagen()) + "\","
                        + "\"stock\":" + p.getStock()
                        + "}";

                return Response.ok(json).build();
            }
        }

        return Response.status(404)
                .entity("{\"estado\":\"error\",\"mensaje\":\"No encontrado\"}")
                .build();
    }

    // 🔥 INSERTAR
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(String body) {

        String nombre = get(body, "nombre");
        String descripcion = get(body, "descripcion");
        String imagen = get(body, "imagen");
        double precio = toDouble(get(body, "precio"));
        int stock = toInt(get(body, "stock"));

        boolean ok = dao.insertar(nombre, descripcion, precio, imagen, stock);

        return Response.ok("{\"estado\":\"ok\"}").build();
    }

    // 🔥 ACTUALIZAR
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizar(String body) {

        int id = toInt(get(body, "id"));
        String nombre = get(body, "nombre");
        String descripcion = get(body, "descripcion");
        String imagen = get(body, "imagen");
        double precio = toDouble(get(body, "precio"));
        int stock = toInt(get(body, "stock"));

        boolean ok = dao.actualizar(id, nombre, descripcion, precio, imagen, stock);

        if (ok) {
            return Response.ok("{\"estado\":\"ok\"}").build();
        } else {
            return Response.status(404)
                    .entity("{\"estado\":\"error\"}")
                    .build();
        }
    }

    // 🔥 ELIMINAR
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("id") int id) {

        boolean ok = dao.eliminar(id);

        return ok
                ? Response.ok("{\"estado\":\"ok\"}").build()
                : Response.status(404).entity("{\"estado\":\"error\"}").build();
    }

    // ======================
    // HELPERS
    // ======================

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }

    private String get(String json, String key) {
        try {
            int i = json.indexOf("\"" + key + "\"");
            if (i == -1) return "";

            i = json.indexOf(":", i) + 1;

            if (json.charAt(i) == '"') {
                i++;
                int j = json.indexOf("\"", i);
                return json.substring(i, j);
            } else {
                int j = json.indexOf(",", i);
                if (j == -1) j = json.indexOf("}", i);
                return json.substring(i, j);
            }
        } catch (Exception e) {
            return "";
        }
    }

    private int toInt(String s) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return 0; }
    }

    private double toDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0; }
    }
}