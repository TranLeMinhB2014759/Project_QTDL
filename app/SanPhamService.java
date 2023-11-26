package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SanPhamService {

    private Connection conn;

    public SanPhamService(Connection conn) {
        this.conn = conn;
    }

    public void themSanPham(String tenSanPham, int gia, String mota) {
        String sql = "INSERT INTO Product (ten_sp, gia, mota) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, tenSanPham);
            preparedStatement.setInt(2, gia);
            preparedStatement.setString(3, mota);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Them san pham thanh cong!");
            } else {
                System.out.println("Them san pham khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void suaSanPham(int idSuaSP, String tenSuaSP, int giaSP, String MotaSP) {
        String sql = "UPDATE Product SET ten_sp=?, gia=?, mota=? WHERE product_id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, tenSuaSP);
            preparedStatement.setInt(2, giaSP);
            preparedStatement.setString(3, MotaSP);
            preparedStatement.setInt(4, idSuaSP);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sua san pham thanh cong!");
            } else {
                System.out.println("Sua san pham khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void xoaSanPham(int idSanPham) {
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

    public void hienThiDanhSachSanPham() {
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

    public boolean kiemTraSanPhamTonTai(int idSanPham) {
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
