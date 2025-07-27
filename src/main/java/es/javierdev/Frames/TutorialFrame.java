package es.javierdev.Frames;

import javax.swing.*;
import java.awt.*;

public class TutorialFrame extends JFrame {

    public TutorialFrame() {
        setTitle("Tutoriales - CyberHack Simulator V1.0");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con título estilo ciberpunk
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panel central con lista de tutoriales
        JPanel tutorialPanel = createTutorialPanel();
        add(new JScrollPane(tutorialPanel), BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        applyCyberpunkStyle();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título principal
        JLabel titleLabel = new JLabel("TUTORIALES", SwingConstants.CENTER);
        titleLabel.setFont(new Font("OCR A Extended", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 255, 255)); // Cian neón

        // Versión
        JLabel versionLabel = new JLabel("V1.0", SwingConstants.RIGHT);
        versionLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        versionLabel.setForeground(Color.GRAY);

        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(versionLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTutorialPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Subtítulo
        JLabel subtitle = new JLabel("ÍNDICE - TODOS LOS TUTORIALES");
        subtitle.setFont(new Font("Consolas", Font.BOLD, 16));
        subtitle.setForeground(new Color(0, 200, 200));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Lista de tutoriales
        String[] tutorials = {
                "1. Conceptos Básicos de Hacking",
                "2. Uso de la Terminal",
                "3. Minijuego: Fuerza Bruta",
                "4. Minijuego: Escribir Rapidamente",
                "5. Minijuego: Intentar engañar a una ia",
                "6. Comandos Avanzados"
        };

        for (String tutorial : tutorials) {
            JButton tutorialBtn = createTutorialButton(tutorial);
            tutorialBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(tutorialBtn);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return panel;
    }

    private JButton createTutorialButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Consolas", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 70));
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 1));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        button.addActionListener(e -> showTutorialContent(text));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 30, 70));
            }
        });

        return button;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Volver al Menú Principal");
        backButton.setFont(new Font("Consolas", Font.PLAIN, 12));
        backButton.addActionListener(e -> dispose());

        panel.add(backButton);
        return panel;
    }

    private void showTutorialContent(String tutorialTitle) {
        String content = getTutorialContent(tutorialTitle);

        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                tutorialTitle, JOptionPane.PLAIN_MESSAGE);
    }

    private String getTutorialContent(String title) {
        // Aquí puedes cargar el contenido real de cada tutorial
        // Ejemplo básico:
        switch (title) {
            case "1. Conceptos Básicos de Hacking":
                return "Este tutorial cubre:\n- Uso básico de la terminal\n- Comandos esenciales\n- Navegación del sistema";
            case "2. Uso de la Terminal":
                return "Comandos disponibles:\n- 'misiones': Ver misiones\n- 'hack': Iniciar hacking\n- 'inventario': Ver items";
            case "3. Minijuego: Fuerza Bruta":
                return "Minijuego: Fuerza Bruta\n\n- 'fuerza bruta': Iniciar minijuego\n- 'fuerza bruta <nivel>': Especificar nivel";
            case "4. Minijuego: Escribir Rapidamente":
                return "Minijuego: Escribir Rapidamente\n\n- 'escribir': Iniciar minijuego\n- 'escribir <nivel>': Especificar nivel";
            case "5. Intentar engañar a una IA":
                return "";

            default:
                return "Contenido detallado de " + title + ".\n\nPróximamente: más información...";
        }
    }

    private void applyCyberpunkStyle() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            // Color de fondo general
            UIManager.put("Panel.background", new Color(0, 10, 20));
            UIManager.put("OptionPane.background", new Color(0, 20, 30));
            UIManager.put("TextArea.background", new Color(0, 30, 40));
            UIManager.put("TextArea.foreground", new Color(0, 255, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Pruebas de los Frames (Solo ejecutar cuando se hacen las pruebas)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TutorialFrame frame = new TutorialFrame();
            frame.setVisible(true);
        });
    }
}