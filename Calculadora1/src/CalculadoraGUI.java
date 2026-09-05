/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author josep
 */
public class CalculadoraGUI extends JFrame {
    private JTextField txtNum1, txtNum2, txtResultado;
    private JComboBox<String> cmbOperaciones;
    private JButton btnCalcular;

    public CalculadoraGUI() {
        // Configuración básica de la ventana
        setTitle("Calculadora Básica");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Inicialización de componentes
        JLabel lblNum1 = new JLabel("Número 1:");
        txtNum1 = new JTextField();
        
        JLabel lblNum2 = new JLabel("Número 2:");
        txtNum2 = new JTextField();
        
        JLabel lblOperacion = new JLabel("Operación:");
        String[] operaciones = {"Sumar", "Restar", "Multiplicar", "Dividir", "Raíz Cuadrada"};
        cmbOperaciones = new JComboBox<>(operaciones);
        
        JLabel lblResultado = new JLabel("Resultado:");
        txtResultado = new JTextField();
        txtResultado.setEditable(false); // El usuario no debe editar el resultado
        txtResultado.setBackground(Color.LIGHT_GRAY);

        btnCalcular = new JButton("Calcular");

        // Aplicar la validación de entrada a los campos de texto
        txtNum1.addKeyListener(crearFiltroNumerico());
        txtNum2.addKeyListener(crearFiltroNumerico());

        // Deshabilitar el segundo número si se selecciona Raíz Cuadrada
        cmbOperaciones.addActionListener(e -> {
            if (cmbOperaciones.getSelectedItem().equals("Raíz Cuadrada")) {
                txtNum2.setText("");
                txtNum2.setEnabled(false);
            } else {
                txtNum2.setEnabled(true);
            }
        });

        // Lógica de cálculo
        btnCalcular.addActionListener(e -> calcularResultado());

        // Agregar componentes a la ventana
        add(lblNum1); add(txtNum1);
        add(lblNum2); add(txtNum2);
        add(lblOperacion); add(cmbOperaciones);
        add(lblResultado); add(txtResultado);
        add(new JLabel("")); // Espacio en blanco
        add(btnCalcular);
    }

    // Método para validar que solo se ingresen números, un punto y un signo menos
    private KeyListener crearFiltroNumerico() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                JTextField campo = (JTextField) e.getSource();
                String textoActual = campo.getText();

                // Permitir borrar
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                // Permitir signo negativo solo al inicio
                if (c == '-') {
                    if (!textoActual.isEmpty()) {
                        e.consume(); // Bloquear si ya hay texto
                    }
                    return;
                }

                // Permitir solo un punto decimal
                if (c == '.') {
                    if (textoActual.contains(".")) {
                        e.consume(); // Bloquear si ya hay un punto
                    }
                    return;
                }

                // Bloquear cualquier cosa que no sea un dígito
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        };
    }

    private void calcularResultado() {
        try {
            // Validación de campos vacíos
            if (txtNum1.getText().isEmpty() || 
               (!txtNum2.isEnabled() == false && txtNum2.getText().isEmpty())) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese los valores requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double num1 = Double.parseDouble(txtNum1.getText());
            double resultado = 0;
            String operacion = (String) cmbOperaciones.getSelectedItem();

            if (operacion.equals("Raíz Cuadrada")) {
                if (num1 < 0) {
                    JOptionPane.showMessageDialog(this, "No se puede calcular la raíz de un número negativo.", "Error Matemático", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                resultado = Math.sqrt(num1);
            } else {
                double num2 = Double.parseDouble(txtNum2.getText());
                switch (operacion) {
                    case "Sumar":
                        resultado = num1 + num2;
                        break;
                    case "Restar":
                        resultado = num1 - num2;
                        break;
                    case "Multiplicar":
                        resultado = num1 * num2;
                        break;
                    case "Dividir":
                        if (num2 == 0) {
                            JOptionPane.showMessageDialog(this, "No se puede dividir por cero.", "Error Matemático", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        resultado = num1 / num2;
                        break;
                }
            }
            
            // Mostrar resultado limitando a 4 decimales si es necesario
            txtResultado.setText(String.format("%.4f", resultado).replace(".0000", ""));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida. Verifique los números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalculadoraGUI().setVisible(true);
        });
    }
}
