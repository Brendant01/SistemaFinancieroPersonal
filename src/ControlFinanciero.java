import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ControlFinanciero {

    static ArrayList<String> nombres = new ArrayList<>();
    static ArrayList<Double> montos = new ArrayList<>();
    static DefaultListModel<String> modeloLista = new DefaultListModel<>();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Control Financiero");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        // 📥 Campos
        JTextField salarioField = new JTextField();
        JTextField luzField = new JTextField();
        JTextField aguaField = new JTextField();
        JTextField ahorroField = new JTextField("20");

        JTextField nombreDeudaField = new JTextField();
        JTextField montoDeudaField = new JTextField();

        JList<String> listaDeudas = new JList<>(modeloLista);

        JTextArea resultadoArea = new JTextArea(8, 30);
        resultadoArea.setEditable(false);

        // ➕ Agregar deuda
        JButton agregarBtn = new JButton("Agregar Deuda");
        agregarBtn.addActionListener((ActionEvent e) -> {
            try {
                String nombre = nombreDeudaField.getText();
                double monto = Double.parseDouble(montoDeudaField.getText());

                nombres.add(nombre);
                montos.add(monto);
                modeloLista.addElement(nombre + ": " + monto);

                nombreDeudaField.setText("");
                montoDeudaField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al agregar deuda");
            }
        });

        // ❌ Eliminar deuda
        JButton eliminarBtn = new JButton("Eliminar Seleccionada");
        eliminarBtn.addActionListener(e -> {
            int index = listaDeudas.getSelectedIndex();
            if (index != -1) {
                nombres.remove(index);
                montos.remove(index);
                modeloLista.remove(index);
            }
        });

        // 🧮 Calcular
        JButton calcularBtn = new JButton("Calcular");
        calcularBtn.addActionListener(e -> {
            try {
                double salario = Double.parseDouble(salarioField.getText());
                double luz = Double.parseDouble(luzField.getText());
                double agua = Double.parseDouble(aguaField.getText());
                double porcentaje = Double.parseDouble(ahorroField.getText());

                double totalDeudas = 0;
                for (double d : montos) totalDeudas += d;

                double gastos = luz + agua + totalDeudas;
                double restante = salario - gastos;
                double ahorro = restante * (porcentaje / 100);
                double libre = restante - ahorro;

                String resultado = "Gastos: " + gastos +
                        "\nRestante: " + restante +
                        "\nAhorro (" + porcentaje + "%): " + ahorro +
                        "\nLibre: " + libre;

                if (restante < 0) resultado += "\n⚠️ Déficit";
                else resultado += "\n🔥 Buen control";

                resultadoArea.setText(resultado);

                // 💾 Guardar automáticamente
                guardarDatos(salario, gastos, ahorro, libre);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error en datos");
            }
        });

        // 🧱 Layout organizado
        panel.add(new JLabel("Salario:"));
        panel.add(salarioField);

        panel.add(new JLabel("Electricidad:"));
        panel.add(luzField);

        panel.add(new JLabel("Agua:"));
        panel.add(aguaField);

        panel.add(new JLabel("% Ahorro:"));
        panel.add(ahorroField);

        panel.add(new JLabel("Nombre deuda:"));
        panel.add(nombreDeudaField);

        panel.add(new JLabel("Monto deuda:"));
        panel.add(montoDeudaField);

        panel.add(agregarBtn);
        panel.add(eliminarBtn);

        panel.add(new JLabel("Deudas:"));
        panel.add(new JScrollPane(listaDeudas));

        panel.add(calcularBtn);
        panel.add(new JScrollPane(resultadoArea));

        frame.add(panel);
        frame.setVisible(true);
    }

    // 💾 Método guardar
    public static void guardarDatos(double salario, double gastos, double ahorro, double libre) {
        try {
            FileWriter writer = new FileWriter("finanzas.txt", true);

            writer.write("Salario: " + salario + "\n");
            writer.write("Gastos: " + gastos + "\n");
            writer.write("Ahorro: " + ahorro + "\n");
            writer.write("Libre: " + libre + "\n");
            writer.write("----------------------\n");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}