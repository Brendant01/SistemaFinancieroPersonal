import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ControlFinanciero {

    static ArrayList<String> nombres = new ArrayList<>();
    static ArrayList<Double> montos = new ArrayList<>();
    static DefaultListModel<String> listaModelo = new DefaultListModel<>();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Control Financiero Vic 💼");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // 📥 Campos principales
        JTextField salarioField = new JTextField(10);
        JTextField luzField = new JTextField(10);
        JTextField aguaField = new JTextField(10);

        // 📌 Campos deuda
        JTextField nombreDeudaField = new JTextField(10);
        JTextField montoDeudaField = new JTextField(10);

        // 📋 Lista visual
        JList<String> listaDeudas = new JList<>(listaModelo);

        // 📊 Resultado
        JTextArea resultadoArea = new JTextArea(8, 40);
        resultadoArea.setEditable(false);

        // ➕ Botón agregar deuda
        JButton agregarBtn = new JButton("Agregar Deuda");
        agregarBtn.addActionListener((ActionEvent e) -> {
            try {
                String nombre = nombreDeudaField.getText();
                double monto = Double.parseDouble(montoDeudaField.getText());

                nombres.add(nombre);
                montos.add(monto);

                listaModelo.addElement(nombre + ": " + monto);

                nombreDeudaField.setText("");
                montoDeudaField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error en los datos");
            }
        });

        // 🧮 Botón calcular
        JButton calcularBtn = new JButton("Calcular");
        calcularBtn.addActionListener((ActionEvent e) -> {
            try {
                double salario = Double.parseDouble(salarioField.getText());
                double luz = Double.parseDouble(luzField.getText());
                double agua = Double.parseDouble(aguaField.getText());

                double totalDeudas = 0;
                for (double d : montos) {
                    totalDeudas += d;
                }

                double gastosTotales = luz + agua + totalDeudas;
                double restante = salario - gastosTotales;
                double ahorro = restante * 0.20;
                double libre = restante - ahorro;

                String resultado = "Gastos: " + gastosTotales +
                        "\nRestante: " + restante +
                        "\nAhorro: " + ahorro +
                        "\nLibre: " + libre;

                if (restante < 0) {
                    resultado += "\n⚠️ Déficit";
                } else {
                    resultado += "\n🔥 Buen control";
                }

                resultadoArea.setText(resultado);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error en los datos");
            }
        });

        // 🧱 Layout (orden visual)
        frame.add(new JLabel("Salario:"));
        frame.add(salarioField);

        frame.add(new JLabel("Electricidad:"));
        frame.add(luzField);

        frame.add(new JLabel("Agua:"));
        frame.add(aguaField);

        frame.add(new JLabel("Nombre deuda:"));
        frame.add(nombreDeudaField);

        frame.add(new JLabel("Monto deuda:"));
        frame.add(montoDeudaField);

        frame.add(agregarBtn);

        frame.add(new JLabel("Deudas:"));
        frame.add(new JScrollPane(listaDeudas));

        frame.add(calcularBtn);
        frame.add(new JScrollPane(resultadoArea));

        frame.setVisible(true);
    }
}