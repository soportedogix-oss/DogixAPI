package WS;

import DAO.CarritoDAO;
import Model.Carrito;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/carrito")
public class CarritoREST {

    CarritoDAO dao = new CarritoDAO();

    // 🔥 LISTAR SIN JSON AUTOMÁTICO
    @GET
    @Path("/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar(@PathParam("idUsuario") int idUsuario) {

        List<Carrito> lista = dao.listar(idUsuario);

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < lista.size(); i++) {

            Carrito c = lista.get(i);

            json.append("{")
                .append("\"idCarrito\":").append(c.getIdCarrito()).append(",")
                .append("\"idProducto\":").append(c.getIdProducto()).append(",")
                .append("\"cantidad\":").append(c.getCantidad()).append(",")
                .append("\"nombreProducto\":\"").append(c.getNombreProducto()).append("\",")
                .append("\"imagen\":\"").append(c.getImagen()).append("\",")
                .append("\"precio\":").append(c.getPrecio()).append(",")
                .append("\"stock\":").append(c.getStock())
                .append("}");

            if (i < lista.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return Response.ok(json.toString()).build();
    }

    // 🔥 AGREGAR (igual que antes)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregar(Carrito c) {

        boolean ok = dao.agregarOActualizar(
                c.getIdUsuario(),
                c.getIdProducto(),
                c.getCantidad() > 0 ? c.getCantidad() : 1
        );

        return ok
                ? Response.ok("{\"estado\":\"ok\"}").build()
                : Response.status(500).entity("{\"estado\":\"error\"}").build();
    }
}