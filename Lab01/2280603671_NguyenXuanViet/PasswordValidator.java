import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class PasswordValidator {
    
    // Yêu cầu mật khẩu: ít nhất 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt
    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*[0-9].*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    
    /**
     * Kiểm tra mật khẩu có đủ mạnh không
     */
    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Mật khẩu không được để trống!";
        }
        
        if (password.length() < MIN_LENGTH) {
            return "Mật khẩu phải có ít nhất " + MIN_LENGTH + " ký tự!";
        }
        
        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            return "Mật khẩu phải có ít nhất 1 chữ hoa (A-Z)!";
        }
        
        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            return "Mật khẩu phải có ít nhất 1 chữ thường (a-z)!";
        }
        
        if (!DIGIT_PATTERN.matcher(password).matches()) {
            return "Mật khẩu phải có ít nhất 1 chữ số (0-9)!";
        }
        
        if (!SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
            return "Mật khẩu phải có ít nhất 1 ký tự đặc biệt (!@#$%^&*...)!";
        }
        
        return null; // Hợp lệ
    }
    
    /**
     * Mã hóa mật khẩu bằng SHA-256
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            
            // Chuyển byte array sang hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi mã hóa mật khẩu", e);
        }
    }
    
    /**
     * Kiểm tra username hợp lệ (có cả chữ và số)
     */
    public static String validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return "Username không được để trống!";
        }
        
        if (username.length() < 3) {
            return "Username phải có ít nhất 3 ký tự!";
        }
        
        boolean hasLetter = false;
        boolean hasDigit = false;
        
        for (char c : username.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        
        if (!hasLetter) {
            return "Username phải có ít nhất 1 chữ cái!";
        }
        
        if (!hasDigit) {
            return "Username phải có ít nhất 1 chữ số!";
        }
        
        return null; // Hợp lệ
    }
    
    /**
     * Hiển thị yêu cầu mật khẩu
     */
    public static String getPasswordRequirements() {
        return "<html>Mật khẩu phải có:<br/>" +
               "• Ít nhất " + MIN_LENGTH + " ký tự<br/>" +
               "• Ít nhất 1 chữ HOA (A-Z)<br/>" +
               "• Ít nhất 1 chữ thường (a-z)<br/>" +
               "• Ít nhất 1 chữ số (0-9)<br/>" +
               "• Ít nhất 1 ký tự đặc biệt (!@#$%...)</html>";
    }
    
    /**
     * Hiển thị yêu cầu username
     */
    public static String getUsernameRequirements() {
        return "<html>Username phải có:<br/>" +
               "• Ít nhất 3 ký tự<br/>" +
               "• Có ít nhất 1 chữ cái<br/>" +
               "• Có ít nhất 1 chữ số</html>";
    }
}
