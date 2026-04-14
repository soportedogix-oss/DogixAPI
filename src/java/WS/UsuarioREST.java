package WS;

import DAO.UsuarioDAO;
import Model.Usuario;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.List;

@Path("/usuarios")
public class UsuarioREST {

    UsuarioDAO dao = new UsuarioDAO();

    // 🔥 TOKEN 6 caracteres
    private String generarToken() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length());
            token.append(caracteres.charAt(index));
        }

        return token.toString();
    }

    // 🔥 LISTAR
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarUsuarios() {
        List<Usuario> lista = dao.listarUsuarios();
        return Response.ok(lista.toString(), MediaType.APPLICATION_JSON).build();
    }

    // 🔥 REGISTER
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuario(Usuario u) {

        if (u.getRol() == null || u.getRol().isEmpty()) {
            u.setRol("cliente");
        }

        boolean ok = dao.registrarUsuario(u);

        String json = ok ? "{\"estado\":\"ok\"}" : "{\"estado\":\"error\"}";

        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    // 🔥 LOGIN
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(Usuario request) {

        Usuario u = dao.login(request.getEmail(), request.getPassword());

        if (u != null) {
            return Response.ok("{\"estado\":\"ok\"}", MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(401)
                    .entity("{\"estado\":\"error\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // 🔥 FORGOT PASSWORD
    @POST
    @Path("/forgot-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgotPassword(Usuario request) {

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return Response.status(400)
                    .entity("{\"estado\":\"email requerido\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        String email = request.getEmail().trim();

        String token = generarToken();

        System.out.println("TOKEN: " + token);

        boolean ok = dao.guardarToken(email, token);

        if (ok) {
            String json = "{\"estado\":\"ok\",\"token\":\"" + token + "\"}";
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }

        return Response.status(400)
                .entity("{\"estado\":\"email no encontrado\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    // 🔥 RESET PASSWORD
    @POST
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(Usuario request) {

        if (request.getToken() == null || request.getPassword() == null) {
            return Response.status(400)
                    .entity("{\"estado\":\"datos incompletos\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        boolean ok = dao.resetPassword(request.getToken(), request.getPassword());

        String json = ok
                ? "{\"estado\":\"ok\"}"
                : "{\"estado\":\"token inválido o expirado\"}";

        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }
}