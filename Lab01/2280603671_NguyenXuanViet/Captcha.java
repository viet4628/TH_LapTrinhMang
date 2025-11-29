import java.util.Random;
import javax.swing.*;

public class Captcha {
    private String captchaCode;
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789"; // Bỏ I, O, 0, 1, i, l dễ nhầm
    private static final int CAPTCHA_LENGTH = 6;
    
    public Captcha() {
        generateCaptcha();
    }
    
    private void generateCaptcha() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        
        captchaCode = code.toString();
    }
    
    public String getCaptchaCode() {
        return captchaCode;
    }
    
    public String getQuestion() {
        return captchaCode;
    }
    
    public boolean verify(String answer) {
        if (answer == null) {
            return false;
        }
        return captchaCode.equals(answer.trim());
    }
    
    /**
     * Hiển thị dialog CAPTCHA và trả về true nếu đúng
     */
    public static boolean showCaptchaDialog(java.awt.Component parent, String title) {
        Captcha captcha = new Captcha();
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel lblInstruction = new JLabel("Nhập mã CAPTCHA bên dưới để xác nhận:");
        lblInstruction.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JLabel lblQuestion = new JLabel(captcha.getQuestion());
        lblQuestion.setFont(new java.awt.Font("Courier New", java.awt.Font.BOLD, 24));
        lblQuestion.setForeground(new java.awt.Color(220, 50, 50));
        lblQuestion.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblQuestion.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        lblQuestion.setOpaque(true);
        lblQuestion.setBackground(new java.awt.Color(245, 245, 245));
        
        JTextField txtAnswer = new JTextField(10);
        txtAnswer.setMaximumSize(new java.awt.Dimension(150, 30));
        txtAnswer.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        txtAnswer.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        
        panel.add(lblInstruction);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblQuestion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtAnswer);
        
        int result = JOptionPane.showConfirmDialog(
            parent,
            panel,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            if (captcha.verify(txtAnswer.getText())) {
                return true;
            } else {
                JOptionPane.showMessageDialog(
                    parent,
                    "Sai kết quả! Vui lòng thử lại.",
                    "Lỗi CAPTCHA",
                    JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
        
        return false;
    }
}
