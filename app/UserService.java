package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }

    public void themNguoiDung(String hoTen, String sdt, String email, String matKhau, String diaChi) {
        String sql = "INSERT INTO User (ho_ten, sdt, email, mat_khau, dia_chi) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, hoTen);
            preparedStatement.setString(2, sdt);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, matKhau);
            preparedStatement.setString(5, diaChi);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Them nguoi dung thanh cong!");
            } else {
                System.out.println("Them nguoi dung khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ShowUser() {
        String sql = "SELECT * FROM user";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            // In tiêu đề bảng
            System.out.format("%-5s | %-25s | %-15s | %-20s | %-20s\n",
                    "ID", "Ten Tai Khoan", "SDT", "Email", "Dia Chi");
            System.out.println(
                    "----------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int idUser = resultSet.getInt("user_id");
                String tenUser = resultSet.getString("ho_ten");
                String sdt = resultSet.getString("sdt");
                String email = resultSet.getString("email");
                String diachi = resultSet.getString("dia_chi");

                // In thông tin sản phẩm
                System.out.format("%-5d | %-25s | %-15s | %-20s | %-20s\n",
                        idUser, tenUser, sdt, email, diachi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditUser(int idNguoiDung, String hoTen, String sdt, String email, String diachi) {
        String sql = "UPDATE User SET ho_ten = ?, sdt = ?, email = ?, dia_chi =? WHERE user_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, hoTen);
            preparedStatement.setString(2, sdt);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, diachi);
            preparedStatement.setInt(5, idNguoiDung);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sua nguoi dung thanh cong!");
            } else {
                System.out.println("Sua nguoi dung khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int idNguoiDung) {
        String sql = "DELETE FROM User WHERE user_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, idNguoiDung);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xoa nguoi dung thanh cong!");
            } else {
                System.out.println("Xoa nguoi dung khong thanh cong!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Signup(String hoTen, String sdt, String email, String matKhau, String diaChi) {
        String sql = "INSERT INTO User (ho_ten, sdt, email, mat_khau, dia_chi) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, hoTen);
            preparedStatement.setString(2, sdt);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, matKhau);
            preparedStatement.setString(5, diaChi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean Login(String email, String matKhau) {
        int loggedInUserId = Connect.getLoggedInUserId();
        String sql = "SELECT * FROM User WHERE email = ? AND mat_khau = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, matKhau);

            ResultSet resultSet = preparedStatement.executeQuery();
            Connect.setLoggedInUserId(loggedInUserId);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean CheckEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int GetUserbyId(String email) {
        int userId = -1; // Default value if not found

        String sql = "SELECT user_id FROM User WHERE email = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }
}
