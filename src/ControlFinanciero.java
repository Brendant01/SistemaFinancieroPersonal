import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class ControlFinanciero {

    private static final Logger LOGGER = Logger.getLogger(ControlFinanciero.class.getName());

    static ArrayList<String> nombres = new ArrayList<>();
    static ArrayList<Double> montos = new ArrayList<>();
    static DefaultListModel<String> modeloLista = new DefaultListModel<>();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Sistema Financiero Personal");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 14);

        // CAMPOS
        JTextField salarioField = new JTextField();
        JTextField otrosIngresosField = new JTextField("0");
        JTextField luzField = new JTextField();
        JTextField aguaField = new JTextField();
        JTextField ahorroField = new JTextField("20");

        salarioField.setFont(font);
        otrosIngresosField.setFont(font);
        luzField.setFont(font);
        aguaField.setFont(font);
        ahorroField.setFont(font);

        JTextField nombreDeudaField = new JTextField();
        JTextField montoDeudaField = new JTextField();

        nombreDeudaField.setFont(font);
        montoDeudaField.setFont(font);

        JComboBox<String> modoAhorro = new JComboBox<>(new String[]{
                "Sobre salario",
                "Sobre restante"
        });

        modoAhorro.setFont(font);

        // LISTA
        JList<String> listaDeudas = new JList<>(modeloLista);

        // RESULTADOS
        JTextArea resultadoArea = new JTextArea(10, 30);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(font);
        resultadoArea.setLineWrap(true);

        // BOTÓN AGREGAR
        JButton agregarBtn = crearBotonAgregar(frame, nombreDeudaField, montoDeudaField, font);

        // BOTÓN ELIMINAR
        JButton eliminarBtn = new JButton("Eliminar Seleccionado");
        eliminarBtn.setBackground(Color.RED);
        eliminarBtn.setForeground(Color.WHITE);
        eliminarBtn.setOpaque(true);

        eliminarBtn.addActionListener(e -> {
            int index = listaDeudas.getSelectedIndex();
            if (index != -1) {
                nombres.remove(index);
                montos.remove(index);
                modeloLista.remove(index);
            }
        });

        // BOTÓN CALCULAR
        JButton calcularBtn = new JButton("Calcular");
        calcularBtn.setBackground(Color.CYAN);
        calcularBtn.setOpaque(true);

        calcularBtn.addActionListener(e -> {
            try {
                double salario = Double.parseDouble(salarioField.getText());
                double otrosIngresos = Double.parseDouble(otrosIngresosField.getText());
                double luz = Double.parseDouble(luzField.getText());
                double agua = Double.parseDouble(aguaField.getText());
                double porcentaje = Double.parseDouble(ahorroField.getText());

                double totalDeudas = 0;
                for (double d : montos) totalDeudas += d;

                double gastos = luz + agua + totalDeudas;
                double ingresosTotales = salario + otrosIngresos;
                double restanteReal = ingresosTotales - gastos;
                boolean deficit = restanteReal < 0;
                double restante = Math.max(restanteReal, 0);

                double ahorro;
                String modoSeleccionado = (String) modoAhorro.getSelectedItem();

                if ("Sobre salario".equals(modoSeleccionado)) {
                    ahorro = salario * (porcentaje / 100);
                } else {
                    ahorro = restante * (porcentaje / 100);
                }

                double libre = Math.max(restante - ahorro, 0);

                if (deficit) {
                    ahorro = 0;
                    libre = 0;
                }

                String resultado = String.format(
                        "Ingresos Totales: Q%.2f\n" +
                                "Gastos Totales: Q%.2f\n" +
                                "Restante: Q%.2f\n" +
                                "Modo de ahorro: %s\n" +
                                "Ahorro (%.2f%%): Q%.2f\n" +
                                "Saldo Libre: Q%.2f\n\n",
                        ingresosTotales, gastos, restante,
                        modoSeleccionado, porcentaje, ahorro, libre
                );

                resultado += deficit ? "⚠ Déficit Financiero" : "✔ Buen Control Financiero";
                resultadoArea.setText(resultado);

                guardarDatosPDF(salario, otrosIngresos, gastos, ahorro, libre,
                        modoSeleccionado, nombres, montos);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: ingresa solo números en los campos de monto.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error inesperado al calcular.");
                LOGGER.log(Level.SEVERE, "Error al calcular", ex);
            }
        });

        // PANEL DATOS
        JPanel panelDatos = new JPanel(new GridLayout(6, 2, 10, 10));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDatos.add(new JLabel("Salario:")); panelDatos.add(salarioField);
        panelDatos.add(new JLabel("Otros ingresos:")); panelDatos.add(otrosIngresosField);
        panelDatos.add(new JLabel("Electricidad:")); panelDatos.add(luzField);
        panelDatos.add(new JLabel("Agua:")); panelDatos.add(aguaField);
        panelDatos.add(new JLabel("% Ahorro:")); panelDatos.add(ahorroField);
        panelDatos.add(new JLabel("Modo de ahorro:")); panelDatos.add(modoAhorro);

        // PANEL GASTOS
        JPanel panelDeudas = new JPanel(new BorderLayout());
        panelDeudas.setBorder(BorderFactory.createTitledBorder("Gastos"));

        JPanel inputDeuda = new JPanel(new GridLayout(2, 2, 5, 5));
        inputDeuda.add(new JLabel("Nombre del gasto:")); inputDeuda.add(nombreDeudaField);
        inputDeuda.add(new JLabel("Monto del gasto:")); inputDeuda.add(montoDeudaField);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(agregarBtn);
        botones.add(eliminarBtn);

        panelDeudas.add(inputDeuda, BorderLayout.NORTH);
        panelDeudas.add(new JScrollPane(listaDeudas), BorderLayout.CENTER);
        panelDeudas.add(botones, BorderLayout.SOUTH);

        // PANEL RESULTADOS
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        panelResultados.add(calcularBtn, BorderLayout.NORTH);
        panelResultados.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);

        frame.add(panelDatos, BorderLayout.NORTH);
        frame.add(panelDeudas, BorderLayout.CENTER);
        frame.add(panelResultados, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Método extraído para crear el botón Agregar (sugerencia de IntelliJ)
    private static JButton crearBotonAgregar(JFrame frame, JTextField nombreDeudaField,
                                             JTextField montoDeudaField, Font font) {
        JButton agregarBtn = new JButton("Agregar Gasto");
        agregarBtn.setBackground(Color.GREEN);
        agregarBtn.setOpaque(true);

        agregarBtn.addActionListener(e -> {
            try {
                String nombre = nombreDeudaField.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Ingresa un nombre para el gasto.");
                    return;
                }
                double monto = Double.parseDouble(montoDeudaField.getText());
                nombres.add(nombre);
                montos.add(monto);
                modeloLista.addElement(nombre + ": Q" + String.format("%.2f", monto));
                nombreDeudaField.setText("");
                montoDeudaField.setText("");
                nombreDeudaField.requestFocusInWindow();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "El monto debe ser un número válido.");
            }
        });

        return agregarBtn;
    }

    // PDF
    public static void guardarDatosPDF(
            double salario, double otrosIngresos, double gastos,
            double ahorro, double libre, String modoAhorro,
            ArrayList<String> nombres, ArrayList<Double> montos) {

        try {
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            PdfWriter writer = new PdfWriter("Finanzas.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // TÍTULO
            document.add(
                    new Paragraph("Control Financiero")
                            .setFont(fontBold)
                            .setFontSize(20)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            document.add(
                    new Paragraph("Fecha: " + LocalDate.now())
                            .setFont(fontNormal)
                            .setFontSize(12)
            );

            document.add(new Paragraph(" "));

            // TABLA
            Table tabla = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                    .useAllAvailableWidth();

            tabla.addHeaderCell(
                    new Cell().add(new Paragraph("Gasto").setFont(fontBold))
                            .setTextAlignment(TextAlignment.CENTER)
            );
            tabla.addHeaderCell(
                    new Cell().add(new Paragraph("Monto").setFont(fontBold))
                            .setTextAlignment(TextAlignment.CENTER)
            );

            for (int i = 0; i < nombres.size(); i++) {
                tabla.addCell(new Cell().add(new Paragraph(nombres.get(i)).setFont(fontNormal)));
                tabla.addCell(
                        new Cell().add(
                                new Paragraph("Q" + String.format("%.2f", montos.get(i))).setFont(fontNormal)
                        ).setTextAlignment(TextAlignment.CENTER)
                );
            }

            document.add(tabla);
            document.add(new Paragraph(" "));

            // RESUMEN
            document.add(
                    new Paragraph("Resumen Financiero")
                            .setFont(fontBold)
                            .setFontSize(16)
            );

            document.add(new Paragraph("Salario: Q" + String.format("%.2f", salario)).setFont(fontNormal));
            document.add(new Paragraph("Otros ingresos: Q" + String.format("%.2f", otrosIngresos)).setFont(fontNormal));
            document.add(new Paragraph("Gastos Totales: Q" + String.format("%.2f", gastos)).setFont(fontNormal));
            document.add(new Paragraph("Modo de ahorro: " + modoAhorro).setFont(fontNormal));
            document.add(new Paragraph("Ahorro: Q" + String.format("%.2f", ahorro)).setFont(fontNormal));
            document.add(new Paragraph("Saldo Libre: Q" + String.format("%.2f", libre)).setFont(fontNormal));

            document.close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al generar PDF", e);
        }
    }
}