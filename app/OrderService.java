package app;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            String sql = "SELECT u.ho_ten, dh.order_id, sp.ten_sp, sp.gia, dh.so_luong, dh.tong_chi_phi, dh.day " +
                    "FROM ds_dat_hang dh " +
                    "JOIN Product sp ON dh.product_id = sp.product_id " +
                    "JOIN User u ON dh.user_id = u.user_id " +
                    "ORDER BY dh.order_id DESC";

            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                // In tiêu đề bảng
                System.out.printf("%-20s | %-15s | %-20s | %-10s | %-10s | %-15s | %-10s\n", "Khach Hang",
                        "ID Don Hang",
                        "Ten San Pham",
                        "Gia",
                        "So Luong",
                        "Tong Chi Phi",
                        "Ngay dat hang");
                System.out.println(
                        "------------------------------------------------------------------------------------------------------------------------");

                while (resultSet.next()) {
                    String tenUser = resultSet.getString("ho_ten");
                    int idSanPham = resultSet.getInt("order_id");
                    String tenSP = resultSet.getString("ten_sp");
                    int gia = resultSet.getInt("gia");
                    int soLuong = resultSet.getInt("so_luong");
                    int tongChiPhi = resultSet.getInt("tong_chi_phi");
                    Date ngayDat = resultSet.getDate("day");

                    // In thông tin sản phẩm trong giỏ hàng
                    System.out.printf("%-20s | %-15d | %-20s | %-10d | %-10d | %-15d | %-10s\n", tenUser, idSanPham,
                            tenSP, gia,
                            soLuong,
                            tongChiPhi, ngayDat);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String sql = "SELECT dh.order_id, sp.ten_sp, sp.gia, dh.so_luong, dh.tong_chi_phi, dh.day " +
                    "FROM ds_dat_hang dh " +
                    "JOIN Product sp ON dh.product_id = sp.product_id " +
                    "WHERE dh.user_id = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, idNguoiDung);
                ResultSet resultSet = preparedStatement.executeQuery();

                // In tiêu đề bảng
                System.out.printf("%-5s | %-20s | %-10s | %-10s | %-15s | %-10s\n", "ID", "Ten San Pham", "Gia",
                        "So Luong",
                        "Tong Chi Phi", "Ngay Dat Hang");
                System.out.println(
                        "-----------------------------------------------------------------------------------------------------");

                while (resultSet.next()) {
                    int idSanPham = resultSet.getInt("order_id");
                    String tenSP = resultSet.getString("ten_sp");
                    int gia = resultSet.getInt("gia");
                    int soLuong = resultSet.getInt("so_luong");
                    int tongChiPhi = resultSet.getInt("tong_chi_phi");
                    Date ngayDat = resultSet.getDate("day");

                    // In thông tin sản phẩm trong giỏ hàng
                    System.out.printf("%-5d | %-20s | %-10d | %-10d | %-15d | %-10s\n", idSanPham, tenSP, gia, soLuong,
                            tongChiPhi, ngayDat);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void EditCart(int idNguoiDung, int idDatHang, int soLuongMoi) {
        String selectOrder = "SELECT order_id, product_id FROM ds_dat_hang WHERE order_id = ?";
        String selectProduct = "SELECT gia FROM Product WHERE product_id = ?";
        String updateOrder = "UPDATE ds_dat_hang SET so_luong=?, tong_chi_phi=? WHERE user_id=? AND order_id= ?";

        try (PreparedStatement selectOrderStatement = conn.prepareStatement(selectOrder)) {
            selectOrderStatement.setInt(1, idDatHang);
            ResultSet resultSet = selectOrderStatement.executeQuery();

            if (resultSet.next()) {
                int idSanPham = resultSet.getInt("product_id");

                try (PreparedStatement selectStatement = conn.prepareStatement(selectProduct)) {
                    selectStatement.setInt(1, idSanPham);
                    ResultSet productResultSet = selectStatement.executeQuery();

                    if (productResultSet.next()) {
                        int gia = productResultSet.getInt("gia");
                        int tongChiPhiMoi = soLuongMoi * gia;

                        try (PreparedStatement updateStatement = conn.prepareStatement(updateOrder)) {
                            updateStatement.setInt(1, soLuongMoi);
                            updateStatement.setInt(2, tongChiPhiMoi);
                            updateStatement.setInt(3, idNguoiDung);
                            updateStatement.setInt(4, idDatHang);

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
                }
            } else {
                System.out.println("Khong tim thay don hang trong CSDL!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletCart(int idNguoiDung, int idDatHang) {
        String sql = "DELETE FROM ds_dat_hang WHERE user_id=? AND order_id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, idNguoiDung);
            preparedStatement.setInt(2, idDatHang);

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
}
