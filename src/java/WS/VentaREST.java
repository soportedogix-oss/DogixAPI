package WS;

import DAO.VentaDAO;
import Model.Venta;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ventas")
public class VentaREST {

    VentaDAO dao = new VentaDAO();

    // 🔥 CHECKOUT
    @POST
    @Path("/checkout/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkout(@PathParam("idUsuario") int idUsuario) {

        boolean ok = dao.procesarCompra(idUsuario);

        return ok
                ? Response.ok("{\"estado\":\"ok\"}").build()
                : Response.status(400).entity("{\"estado\":\"error\"}").build();
    }

    // 🔥 LISTAR JSON MANUAL
    @GET
    @Path("/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar(@PathParam("idUsuario") int idUsuario) {

        List<Venta> lista = dao.listarPorUsuario(idUsuario);

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < lista.size(); i++) {

            Venta v = lista.get(i);

            json.append("{")
                .append("\"idVenta\":").append(v.getIdVenta()).append(",")
                .append("\"cantidad\":").append(v.getCantidad()).append(",")
                .append("\"total\":").append(v.getTotal()).append(",")
                .append("\"fecha\":\"").append(v.getFecha()).append("\",")
                .append("\"nombreProducto\":\"").append(v.getNombreProducto()).append("\",")
                .append("\"imagen\":\"").append(v.getImagen()).append("\"")
                .append("}");

            if (i < lista.size() - 1) json.append(",");
        }

        json.append("]");

        return Response.ok(json.toString()).build();
    }
}