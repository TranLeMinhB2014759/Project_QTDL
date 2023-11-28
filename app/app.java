package app;

import java.sql.Connection;
import java.util.Scanner;

public class app {
    public static void main(String[] args) {
        showMainLoginMenu();
    }

    private static void logout() {
        Connect.setLoggedInUserId(-1);
        showMainLoginMenu();
    }

    private static void showMainLoginMenu() {
        Scanner sc = new Scanner(System.in);
        Connection conn = Connect.getConnect();
        UserService UserService = new UserService(conn);

        // Display menu
        while (true) {
            System.out.println("\n");
            System.out.println("1. Dang Nhap");
            System.out.println("2. Dang Ky");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Đăng Nhập
                    System.out.println("\n");
                    System.out.print("Nhap Email: ");
                    String loginEmail = scanner.next();
                    System.out.print("Nhap Mat Khau: ");
                    String loginPassword = scanner.next();
                    if (UserService.Login(loginEmail, loginPassword)) {
                        Connect.setLoggedInUserId(UserService.GetUserbyId(loginEmail));
                        if (loginEmail.equals("admin@gmail.com")) {
                            showMenuAdmin();
                        } else {
                            showUser();
                        }
                    } else {
                        System.out.println("Dang nhap khong thong cong. Kiem tra lai thong tin!!!");
                    }
                    break;

                case 2:
                    // Đăng Ký
                    System.out.println("\n");
                    scanner.nextLine();
                    System.out.print("Nhap Ho Ten: ");
                    String hoTen = scanner.nextLine();
                    System.out.print("Nhap So Đien Thoai: ");
                    String sdt = scanner.nextLine();
                    System.out.print("Nhap Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Nhap Mat Khau: ");
                    String matKhau = scanner.nextLine();
                    System.out.print("Nhap Dia Chi: ");
                    String diaChi = scanner.nextLine();

                    if (UserService.CheckEmail(email)) {
                        System.out.println("Email da duoc dang ky. Vui long su dung email khac!!!");
                    } else {
                        UserService.Signup(hoTen, sdt, email, matKhau, diaChi);
                        System.out.println("Dang ky thanh cong!");
                    }
                    break;

                case 0:
                    // Thoát
                    System.out.println("\n");
                    System.out.println("Chuong trinh ket thuc!!!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Chon khong hop le. Vui long chon lai!!!");
            }
        }
    }

    private static void showUser() {
        Scanner sc = new Scanner(System.in);
        Connection conn = Connect.getConnect();
        ProductService ProductService = new ProductService(conn);
        OrderService OrderService = new OrderService(conn);
        System.out.println("Welcome to HouseWares1 Store!!!");

        while (true) {
            System.out.println("\n");
            System.out.println("--------------------USER--------------------");
            System.out.println("1. Xem danh sach san pham");
            System.out.println("2. Dat Hang");
            System.out.println("3. Xem cac don dat hang");
            System.out.println("4. Sua don dat hang");
            System.out.println("5. Xoa  don dat hang");
            System.out.println("6. Dang xuat");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n");
                    ProductService.ShowProduct();
                    break;

                case 2:
                    System.out.println("\n");
                    System.out.print("Nhap ID san pham: ");
                    int idSanPham = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nhap so luong san pham: ");
                    int soLuong = sc.nextInt();
                    sc.nextLine();
                    if (ProductService.CheckPD_OLD(idSanPham)) {
                        int idNguoiDung = Connect.getLoggedInUserId();
                        OrderService.AddOrder(soLuong, idNguoiDung, idSanPham);
                        System.out.println("Dat hang thanh cong!");
                    } else {
                        System.out.println("San pham khong ton tai!");
                    }
                    break;

                case 3:
                    System.out.println("\n");
                    int idNguoiDung = Connect.getLoggedInUserId();
                    OrderService.ShowCart(idNguoiDung);
                    break;
                case 4:
                    // Sua san pham trong dat hang
                    System.out.println("\n");
                    System.out.print("Nhap ID don dat hang: ");
                    int idSanPhamSua = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nhap so luong san pham moi: ");
                    int soLuongMoi = sc.nextInt();
                    sc.nextLine();

                    int idNguoiDungSua = Connect.getLoggedInUserId();
                    OrderService.EditCart(idNguoiDungSua, idSanPhamSua, soLuongMoi);
                    System.out.println("Sua dat hang thanh cong!");
                    break;

                case 5:
                    // Xoa san pham trong dat hang
                    System.out.println("\n");
                    System.out.print("Nhap ID san pham muon xoa trong dat hang: ");
                    int idSanPhamXoa = sc.nextInt();
                    sc.nextLine();

                    int idNguoiDungXoa = Connect.getLoggedInUserId();
                    OrderService.deletCart(idNguoiDungXoa, idSanPhamXoa);
                    System.out.println("Xoa san pham trong dat hang thanh cong!");
                    break;
                case 6:
                    System.out.println("\n");
                    System.out.println("Dang xuat tai khoan!");
                    logout();
                case 0:
                    System.out.println("\n");
                    Connect.disconnect();
                    System.out.println("Thoat chuong trinh.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }

    private static void showMenuAdmin() {
        Scanner sc = new Scanner(System.in);
        Connection conn = Connect.getConnect();
        UserService UserService = new UserService(conn);
        ProductService ProductService = new ProductService(conn);
        OrderService OrderService = new OrderService(conn);
        System.out.println("Chao mung admin vao he thong ban hang!!!");
        while (true) {
            System.out.println("\n");
            System.out.println("--------------------ADMIN--------------------");
            System.out.println("1. Them nguoi dung");
            System.out.println("2. Sua nguoi dung");
            System.out.println("3. Xoa nguoi dung");
            System.out.println("4. Hien thi danh sach san pham");
            System.out.println("5. Them san pham");
            System.out.println("6. Sua san pham");
            System.out.println("7. Xoa san pham");
            System.out.println("8. Hien thi thong tin dat hang");
            System.out.println("9. Dang xuat");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n");
                    System.out.print("Nhap ho va ten: ");
                    String hoTen = sc.nextLine();
                    System.out.print("Nhap so dien thoai: ");
                    String sdt = sc.nextLine();
                    System.out.print("Nhap email: ");
                    String email = sc.nextLine();
                    System.out.print("Nhap mat khau: ");
                    String matKhau = sc.nextLine();
                    System.out.print("Nhap đia chi: ");
                    String diaChi = sc.nextLine();

                    UserService.themNguoiDung(hoTen, sdt, email, matKhau, diaChi);
                    break;

                case 2:
                    System.out.println("\n");
                    System.out.print("Nhap ID nguoi dung can sua: ");
                    int idSua = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nhap ho ten moi: ");
                    String hoTenMoi = sc.nextLine();

                    UserService.EditUser(idSua, hoTenMoi);
                    break;

                case 3:
                    System.out.println("\n");
                    System.out.print("Nhap ID nguoi dung can xoa: ");
                    int idXoa = sc.nextInt();
                    UserService.deleteUser(idXoa);
                    break;

                case 4:
                    System.out.println("\n");
                    ProductService.ShowProduct();
                    break;

                case 5:
                    System.out.println("\n");
                    System.out.print("Nhap ten san pham: ");
                    String tenSP = sc.nextLine();
                    System.out.print("Nhap gia: ");
                    int gia = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nhap mo ta san pham: ");
                    String mota = sc.nextLine();

                    ProductService.AddPD(tenSP, gia, mota);
                    break;
                case 6:
                    System.out.println("\n");
                    System.out.print("Nhap ID san pham can sua: ");
                    int idSuaSP = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nhap ten san pham moi: ");
                    String tenSuaSP = sc.nextLine();
                    System.out.print("Nhap gia san pham moi: ");
                    int giaSP = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nhap mo ta san pham moi: ");
                    String MotaSP = sc.nextLine();

                    ProductService.EditPD(idSuaSP, tenSuaSP, giaSP, MotaSP);
                    break;
                case 7:
                    System.out.println("\n");
                    System.out.print("Nhap ID san pham can xoa: ");
                    int idSPXoa = sc.nextInt();
                    ProductService.deletePD(idSPXoa);
                    break;
                case 8:
                    System.out.println("\n");
                    int idNguoiDung = Connect.getLoggedInUserId();
                    OrderService.ShowCart(idNguoiDung);
                    break;
                case 9:
                    System.out.println("\n");
                    System.out.println("Dang xuat tai khoan!");
                    logout();
                case 0:
                    System.out.println("\n");
                    Connect.disconnect();
                    System.out.println("Thoat chuong trinh.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
            }
        }
    }
}
