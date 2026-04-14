package DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    public static Connection conectar() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            System.out.println("===== DEBUG BD =====");
            System.out.println("URL: " + url);
            System.out.println("USER: " + user);
            System.out.println("PASS: " + (password != null ? "*****" : "NULL"));

            if (url == null || user == null || password == null) {
                throw new RuntimeException("❌ Faltan variables de entorno");
            }

            con = DriverManager.getConnection(url, user, password);

            if (con != null && !con.isClosed()) {
                System.out.println("✅ CONECTADO A BD CORRECTAMENTE");
                System.out.println("AutoCommit: " + con.getAutoCommit());
            } else {
                System.out.println("❌ CONEXION NULL");
            }

        } catch (Exception e) {

            System.out.println("❌ ERROR EN CONEXION");
            e.printStackTrace();

        }

        return con;
    }
}