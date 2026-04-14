package DB;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        try {

            Connection con = Conexion.conectar();

            if (con != null && !con.isClosed()) {

                System.out.println("Conexion exitosa con la base de datos");

                con.close();

            } else {

                System.out.println("Error: la conexion es null");

            }

        } catch (Exception e) {

            System.out.println("Error al conectar");
            e.printStackTrace();

        }

    }
}