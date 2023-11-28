package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private Connection conn;

    public OrderService(Connection conn) {
        this.conn = conn;
    }

    public void AddOrder(int soLuong, int idNguoiDung, int idSanPham) {
        String sqlGetProductPrice = "SELECT gia FROM Product WHERE product_id = ?";

        try (PreparedStatement priceStatement = conn.prepareStatement(sqlGetProductPrice)) {
            priceStatement.setInt(1, idSanPham);
            ResultSet resultSet = priceStatement.executeQuery();

            if (resultSet.next()) {
                int gia = resultSet.getInt("gia");
                int tongChiPhi = soLuong * gia;

                String sqlInsertOrder = "INSERT INTO ds_dat_hang (tong_chi_phi, so_luong, user_id, product_id) VALUES (?, ?, ?, ?)";

                try (PreparedStatement insertOrderStatement = conn.prepareStatement(sqlInsertOrder)) {
                    insertOrderStatement.setInt(1, tongChiPhi);
                    insertOrderStatement.setInt(2, soLuong);
                    insertOrderStatement.setInt(3, idNguoiDung);
                    insertOrderStatement.setInt(4, idSanPham);

                    int rowsAffected = insertOrderStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Them dat hang thanh cong!");
                    } else {
                        System.out.println("Them dat hang khong thanh cong!");
                    }
                }
            } else {
                System.out.println("Khong tim thay gia cua san pham!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ShowCart(int idNguoiDung) {
        if (idNguoiDung == 1) {
            String sql = "SELECT u.ho_ten, sp.product_id, sp.ten_sp, sp.gia, dh.so_luong, dh.tong_chi_phi " +
                    "FROM ds_dat_hang dh " +
                    "JOIN Product sp ON dh.product_id = sp.product_id " +
                    "JOIN User u ON dh.user_id = u.user_id";

            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // In tiêu đề bảng
                System.out.printf("%-20s | %-15s | %-20s | %-10s | %-10s | %-15s\n", "Khach Hang", "ID San Pham",
                        "Ten San Pham",
                        "Gia",
                        "So Luong",
                        "Tong Chi Phi");
                System.out.println(
                        "------------------------------------------------------------------------------------------------------------");

                while (resultSet.next()) {
                    String tenUser = resultSet.getString("ho_ten");
                    int idSanPham = resultSet.getInt("product_id");
                    String tenSP = resultSet.getString("ten_sp");
                    int gia = resultSet.getInt("gia");
                    int soLuong = resultSet.getInt("so_luong");
                    int tongChiPhi = resultSet.getInt("tong_chi_phi");

                    // In thông tin sản phẩm trong giỏ hàng
                    System.out.printf("%-20s | %-15d | %-20s | %-10d | %-10d | %-15d\n", tenUser, idSanPham, tenSP, gia,
                            soLuong,
                            tongChiPhi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String sql = "SELECT sp.product_id, sp.ten_sp, sp.gia, dh.so_luong, dh.tong_chi_phi " +
                    "FROM ds_dat_hang dh " +
                    "JOIN Product sp ON dh.product_id = sp.product_id " +
                    "WHERE dh.user_id = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, idNguoiDung);
                ResultSet resultSet = preparedStatement.executeQuery();

                // In tiêu đề bảng
                System.out.printf("%-5s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Ten San Pham", "Gia", "So Luong",
                        "Tong Chi Phi");
                System.out.println("---------------------------------------------------------------------");

                while (resultSet.next()) {
                    int idSanPham = resultSet.getInt("product_id");
                    String tenSP = resultSet.getString("ten_sp");
                    int gia = resultSet.getInt("gia");
                    int soLuong = resultSet.getInt("so_luong");
                    int tongChiPhi = resultSet.getInt("tong_chi_phi");

                    // In thông tin sản phẩm trong giỏ hàng
                    System.out.printf("%-5d | %-20s | %-10d | %-10d | %-15d\n", idSanPham, tenSP, gia, soLuong,
                            tongChiPhi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void EditCart(int idNguoiDung, int idSanPham, int soLuongMoi) {
        String selectSql = "SELECT gia FROM Product WHERE product_id = ?";
        String updateSql = "UPDATE ds_dat_hang SET so_luong=?, tong_chi_phi=? WHERE user_id=? AND product_id=?";

        try (PreparedStatement selectStatement = conn.prepareStatement(selectSql)) {
            // Retrieve the price of the product
            selectStatement.setInt(1, idSanPham);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int gia = resultSet.getInt("gia");
                int tongChiPhiMoi = soLuongMoi * gia;

                try (PreparedStatement updateStatement = conn.prepareStatement(updateSql)) {
                    updateStatement.setInt(1, soLuongMoi);
                    updateStatement.setInt(2, tongChiPhiMoi);
                    updateStatement.setInt(3, idNguoiDung);
                    updateStatement.setInt(4, idSanPham);

                    int rowsAffected = updateStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Sua san pham trong gio hang thanh cong!");
                    } else {
                        System.out.println("Sua san pham trong gio hang khong thanh cong!");
                    }
                }
            } else {
                System.out.println("Khong tim thay san pham trong CSDL!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletCart(int idNguoiDung, int idSanPham) {
        String sql = "DELETE FROM ds_dat_hang WHERE user_id=? AND product_id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, idNguoiDung);
            preparedStatement.setInt(2, idSanPham);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xoa san pham trong gio hang thanh cong!");
            } else {
                System.out.println("Xoa san pham trong gio hang khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> ShowListCart() {
        List<String> ListCart = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM ds_dat_hang");
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String thongTinDatHang = "ID: " + resultSet.getInt("id_dat_hang") +
                        ", Tong chi phi: " + resultSet.getInt("tong_chi_phi") +
                        ", So luong: " + resultSet.getInt("so_luong") +
                        ", Ngay: " + resultSet.getTimestamp("ngay") +
                        ", ID Nguoi dung: " + resultSet.getInt("user_id") +
                        ", ID San pham: " + resultSet.getInt("product_id");

                ListCart.add(thongTinDatHang);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ListCart;
    }
}
