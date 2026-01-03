/*
 * Class NguoiDung - Model cho thông tin người dùng
 */
package bai1;

/**
 * @author Administrator
 */
public class NguoiDung {
    private int id;
    private String tenDangNhap;
    private String matKhau;
    private String duongDanThuMuc;
    private String quyenTruyXuat; // "Đọc", "Viết", "Cả hai"

    public NguoiDung() {
    }

    public NguoiDung(String tenDangNhap, String matKhau, String duongDanThuMuc, String quyenTruyXuat) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.duongDanThuMuc = duongDanThuMuc;
        this.quyenTruyXuat = quyenTruyXuat;
    }

    public NguoiDung(int id, String tenDangNhap, String matKhau, String duongDanThuMuc, String quyenTruyXuat) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.duongDanThuMuc = duongDanThuMuc;
        this.quyenTruyXuat = quyenTruyXuat;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getDuongDanThuMuc() {
        return duongDanThuMuc;
    }

    public void setDuongDanThuMuc(String duongDanThuMuc) {
        this.duongDanThuMuc = duongDanThuMuc;
    }

    public String getQuyenTruyXuat() {
        return quyenTruyXuat;
    }

    public void setQuyenTruyXuat(String quyenTruyXuat) {
        this.quyenTruyXuat = quyenTruyXuat;
    }

    @Override
    public String toString() {
        return "NguoiDung{" + "id=" + id + ", tenDangNhap=" + tenDangNhap + 
               ", duongDanThuMuc=" + duongDanThuMuc + ", quyenTruyXuat=" + quyenTruyXuat + '}';
    }
}
