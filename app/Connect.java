package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connection conn;
    private static int loggedInUserId;

    private Connect() {
    }

    public static Connection getConnect() {
        if (conn == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/quanlybanhang";
                String user = "root";
                String password = "";

                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Ket noi thang cong!");
            } catch (SQLException e) {
                System.out.println("Ket noi khong thanh cong!");
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Ngat ket noi thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }
}
