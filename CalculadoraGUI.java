import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculadoraGUI extends JFrame {
    private final JTextField display = new JTextField();
    private double current = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    public CalculadoraGUI() {
        super("Calculadora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5,5));

        display.setFont(new Font("Arial", Font.PLAIN, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setText("0");
        add(display, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(5, 4, 5, 5));
        String[] keys = {
            "C", "←", "%", "÷",
            "7","8","9","×",
            "4","5","6","-",
            "1","2","3","+",
            "0",".","±","="
        };

        for (String key : keys) {
            JButton btn = new JButton(key);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(e -> handleKey(key));
            buttons.add(btn);
        }

        add(buttons, BorderLayout.CENTER);
    }

    private void handleKey(String key) {
        switch (key) {
            case "C":
                current = 0;
                operator = "";
                display.setText("0");
                startNewNumber = true;
                break;
            case "←":
                String t = display.getText();
                if (t.length() > 1) display.setText(t.substring(0, t.length()-1));
                else display.setText("0");
                break;
            case "+":
            case "-":
            case "×":
            case "÷":
                applyOperator(key);
                break;
            case "=":
                calculateResult();
                operator = "";
                startNewNumber = true;
                break;
            case ".":
                if (startNewNumber) {
                    display.setText("0.");
                    startNewNumber = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
                break;
            case "±":
                if (!display.getText().equals("0")) {
                    if (display.getText().startsWith("-"))
                        display.setText(display.getText().substring(1));
                    else
                        display.setText("-" + display.getText());
                }
                break;
            case "%":
                try {
                    double val = Double.parseDouble(display.getText());
                    val = val / 100.0;
                    display.setText(trimZeros(val));
                } catch (NumberFormatException ex) { /* ignore */ }
                startNewNumber = true;
                break;
            default: // números
                if (startNewNumber) {
                    display.setText(key);
                    startNewNumber = false;
                } else {
                    display.setText(display.getText().equals("0") ? key : display.getText() + key);
                }
                break;
        }
    }

    private void applyOperator(String op) {
        try {
            double displayed = Double.parseDouble(display.getText());
            if (!operator.isEmpty()) {
                // já havia um operador pendente -> calcular primeiro
                current = compute(current, displayed, operator);
                display.setText(trimZeros(current));
            } else {
                current = displayed;
            }
        } catch (NumberFormatException ex) {
            current = 0;
        }
        operator = op;
        startNewNumber = true;
    }

    private void calculateResult() {
        if (operator.isEmpty()) return;
        try {
            double displayed = Double.parseDouble(display.getText());
            current = compute(current, displayed, operator);
            display.setText(trimZeros(current));
        } catch (NumberFormatException ex) {
            display.setText("Erro");
        }
    }

    private double compute(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "×": return a * b;
            case "÷":
                if (b == 0) { JOptionPane.showMessageDialog(this, "Erro: divisão por zero"); return a; }
                return a / b;
            default: return b;
        }
    }

    private String trimZeros(double v) {
        if (v == (long) v) return String.format("%d", (long) v);
        else return String.valueOf(v);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI calc = new CalculadoraGUI();
            calc.setVisible(true);
        });
    }
}