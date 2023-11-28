package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Types;

public class ProductService {

    private Connection conn;

    public ProductService(Connection conn) {
        this.conn = conn;
    }

    public void AddPD(String tenSanPham, int gia, String mota) {
        try {
            CallableStatement cStmt = conn.prepareCall("{call createProduct(?, ?, ?, ?)}");
            cStmt.setString(1, tenSanPham);
            cStmt.setInt(2, gia);
            cStmt.setString(3, mota);
            cStmt.registerOutParameter(4, Types.INTEGER);
            cStmt.executeUpdate();
            int kq = cStmt.getInt(4);
            if (kq == 1) {
                System.out.println("Them san pham thanh cong!");
            } else {
                System.out.println("Them san pham khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditPD(int idSuaSP, String tenSuaSP, int giaSP, String MotaSP) {
        try {
            CallableStatement cStmt = conn.prepareCall("{call editProduct(?, ?, ?, ?, ?)}");
            cStmt.setString(1, tenSuaSP);
            cStmt.setInt(2, giaSP);
            cStmt.setString(3, MotaSP);
            cStmt.setInt(4, idSuaSP);
            cStmt.registerOutParameter(5, Types.INTEGER);
            cStmt.executeUpdate();
            int kq = cStmt.getInt(5);
            if (kq == 1) {
                System.out.println("Sua san pham thanh cong!");
            } else {
                System.out.println("San pham khong ton tai");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePD(int idSanPham) {
        String sql = "DELETE FROM Product WHERE product_id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, idSanPham);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xoa san pham thanh cong!");
            } else {
                System.out.println("Xoa san pham khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ShowProduct() {
        String sql = "SELECT * FROM Product";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            // In tiêu đề bảng
            System.out.format("%-5s | %-20s | %-10s | %-20s\n",
                    "ID", "Ten San Pham", "Gia", "Mo Ta");
            System.out.println(
                    "--------------------------------------------------------------------------");

            while (resultSet.next()) {
                int idSanPham = resultSet.getInt("product_id");
                String tenSP = resultSet.getString("ten_sp");
                int gia = resultSet.getInt("gia");
                String mota = resultSet.getString("mota");

                // In thông tin sản phẩm
                System.out.format("%-5d | %-20s | %-10s | %-20s\n",
                        idSanPham, tenSP, gia, mota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean CheckPD_OLD(int idSanPham) {
        String sql = "SELECT COUNT(*) AS count FROM Product WHERE product_id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, idSanPham);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
