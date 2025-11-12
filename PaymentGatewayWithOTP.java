package javaWorkshop2025;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PaymentGatewayWithOTP extends JFrame implements ActionListener {
    // UI components
    private JTextField amountField, cardField, upiField, otpField;
    private JRadioButton cardOption, upiOption;
    private JButton generateOtpBtn, verifyBtn, clearBtn;
    private JLabel statusLabel, otpLabel;
    private ButtonGroup paymentGroup;

    // OTP storage
    private String generatedOtp = null;

    public PaymentGatewayWithOTP() {
        setTitle("Payment Gateway Simulation");
        setSize(520, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 245, 255));

        // Title
        JLabel titleLabel = new JLabel("Payment Gateway Simulation", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 51, 102));
        titleLabel.setBounds(60, 20, 400, 30);
        add(titleLabel);

        // Amount
        JLabel amountLabel = new JLabel("Enter Amount (₹):");
        amountLabel.setBounds(60, 80, 150, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(230, 80, 220, 25);
        add(amountField);

        // Mode
        JLabel modeLabel = new JLabel("Select Payment Mode:");
        modeLabel.setBounds(60, 130, 170, 25);
        add(modeLabel);

        cardOption = new JRadioButton("Card");
        upiOption = new JRadioButton("UPI");
        cardOption.setBackground(new Color(230,245,255));
        upiOption.setBackground(new Color(230,245,255));
        cardOption.setBounds(230, 130, 80, 25);
        upiOption.setBounds(320, 130, 100, 25);
        add(cardOption);
        add(upiOption);

        paymentGroup = new ButtonGroup();
        paymentGroup.add(cardOption);
        paymentGroup.add(upiOption);

        // Card fields
        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setBounds(60, 180, 150, 25);
        add(cardLabel);

        cardField = new JTextField();
        cardField.setBounds(230, 180, 220, 25);
        add(cardField);

        // UPI field
        JLabel upiLabel = new JLabel("UPI ID:");
        upiLabel.setBounds(60, 220, 150, 25);
        add(upiLabel);

        upiField = new JTextField();
        upiField.setBounds(230, 220, 220, 25);
        add(upiField);

        // Generate OTP button
        generateOtpBtn = new JButton("Generate OTP");
        generateOtpBtn.setBounds(150, 270, 180, 36);
        generateOtpBtn.setBackground(new Color(0, 153, 204));
        generateOtpBtn.setForeground(Color.WHITE);
        generateOtpBtn.setFocusPainted(false);
        generateOtpBtn.addActionListener(this);
        add(generateOtpBtn);

        // OTP label & field (hidden until OTP generated)
        otpLabel = new JLabel("Enter OTP:");
        otpLabel.setBounds(60, 330, 150, 25);
        otpLabel.setVisible(false);
        add(otpLabel);

        otpField = new JTextField();
        otpField.setBounds(230, 330, 220, 25);
        otpField.setVisible(false);
        add(otpField);

        // Verify button (hidden until OTP generated)
        verifyBtn = new JButton("Verify & Pay");
        verifyBtn.setBounds(150, 380, 180, 36);
        verifyBtn.setBackground(new Color(0, 153, 76));
        verifyBtn.setForeground(Color.WHITE);
        verifyBtn.setFocusPainted(false);
        verifyBtn.addActionListener(this);
        verifyBtn.setVisible(false);
        add(verifyBtn);

        // Clear button
        clearBtn = new JButton("Clear");
        clearBtn.setBounds(350, 380, 100, 36);
        clearBtn.setBackground(new Color(200, 200, 200));
        clearBtn.addActionListener(this);
        add(clearBtn);

        // Status label
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setBounds(60, 430, 400, 25);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(statusLabel);

        setVisible(true);
    }

    // Validate inputs before OTP is generated
    private boolean validateBeforeOtp() {
        String amount = amountField.getText().trim();
        if (amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter amount first", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!cardOption.isSelected() && !upiOption.isSelected()) {
            JOptionPane.showMessageDialog(this, "Select payment mode (Card or UPI)", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cardOption.isSelected()) {
            String c = cardField.getText().trim();
            if (!c.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this, "Card number must be 16 digits", "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else { // upi selected
            String u = upiField.getText().trim();
            if (!u.contains("@") || u.startsWith("@") || u.endsWith("@")) {
                JOptionPane.showMessageDialog(this, "Enter a valid UPI ID (example: name@bank)", "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private String createOtp() {
        Random r = new Random();
        int otp = 1000 + r.nextInt(9000); // 4-digit
        return String.valueOf(otp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == generateOtpBtn) {
            // Validate amount and payment mode first
            if (!validateBeforeOtp()) {
                return;
            }

            // Generate OTP and show
            generatedOtp = createOtp();
            // For demo we display OTP in dialog; in real app you'd send via SMS/email
            JOptionPane.showMessageDialog(this, "OTP (for demo): " + generatedOtp, "OTP Generated", JOptionPane.INFORMATION_MESSAGE);

            // Show OTP input and verify button
            otpLabel.setVisible(true);
            otpField.setVisible(true);
            verifyBtn.setVisible(true);

            statusLabel.setText("OTP generated. Enter OTP and click Verify & Pay.");
            statusLabel.setForeground(new Color(0, 120, 0));
        }
        else if (src == verifyBtn) {
            String entered = otpField.getText().trim();
            if (entered.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the OTP.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Compare safely (handle null)
            if (generatedOtp != null && entered.equals(generatedOtp)) {
                JOptionPane.showMessageDialog(this, "Payment successful! Transaction ID: TXN" + new Random().nextInt(900000 + 100000), "Success", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("✅ Payment Successful");
                statusLabel.setForeground(new Color(0, 153, 51));
                // After success, reset OTP to prevent reuse
                generatedOtp = null;
                otpField.setText("");
                otpLabel.setVisible(false);
                otpField.setVisible(false);
                verifyBtn.setVisible(false);
                paymentGroupClear();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid OTP. Try again.", "Failure", JOptionPane.ERROR_MESSAGE);
                statusLabel.setText("❌ Invalid OTP");
                statusLabel.setForeground(Color.RED);
            }
        }
        else if (src == clearBtn) {
            amountField.setText("");
            cardField.setText("");
            upiField.setText("");
            otpField.setText("");
            otpLabel.setVisible(false);
            otpField.setVisible(false);
            verifyBtn.setVisible(false);
            statusLabel.setText("");
            paymentGroupClear();
            generatedOtp = null;
        }
    }

    private void paymentGroupClear() {
        if (paymentGroup != null) paymentGroup.clearSelection();
        // ensure we also re-add group if it was null (shouldn't be)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentGatewayWithOTP());
    }
}