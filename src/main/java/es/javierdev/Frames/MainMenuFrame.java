package es.javierdev.Frames;

import es.javierdev.Save.GameSave;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    public final static String VERSION = "v1.0";
    public MainMenuFrame() {
        setTitle("CyberHack Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con imagen de fondo
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Fondo degradado negro-azul oscuro
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 0, 20), 0, getHeight(), Color.BLACK);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Título del juego (estilo neón)
        JLabel titleLabel = new JLabel("CYBERHACK SIMULATOR \n v1.0");
        titleLabel.setFont(new Font("OCR A Extended", Font.BOLD, 48));
        titleLabel.setForeground(new Color(0, 255, 255)); // Cian neón
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        buttonPanel.setOpaque(false);

        String[] buttonTexts = {"NUEVA PARTIDA", "CARGAR PARTIDA", "TUTORIAL", "SALIR"};
        for (String text : buttonTexts) {
            JButton button = createMenuButton(text);
            buttonPanel.add(button);

            // Acciones de los botones
            if (text.equals("NUEVA PARTIDA")) {
                button.addActionListener(e -> startNewGame());
            } else if (text.equals("CARGAR PARTIDA")) {
                button.addActionListener(e -> startGame());
            } else if (text.equals("TUTORIAL")) {
                button.addActionListener(e -> new TutorialFrame().setVisible(true));
            } else if (text.equals("SALIR")) {
                button.addActionListener(e -> System.exit(0));
            }
        }



        // Posicionamiento
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);


        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Consolas", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 70));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 2));
        button.setPreferredSize(new Dimension(300, 50));

        // Efecto hover (cambio de color)
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

    // Metodo startNewGame():
    private void startNewGame() {
        // Intentar cargar partida existente
        GameSave save = GameSave.loadGame();

        // Si no existe, crear nueva partida
        if (save == null) {
            String playerName = JOptionPane.showInputDialog(this,
                    "Ingresa tu nombre de hacker:",
                    "Nueva Partida",
                    JOptionPane.PLAIN_MESSAGE);

            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Anonimo";
            }

            save = new GameSave(playerName);
        }

        dispose();
        new HackerTerminalFrame(save).setVisible(true);
    }

    private void startGame() {
        GameSave save = GameSave.loadGame();

        if (save == null) {
            // Crear nueva partida si no hay guardado
            String playerName = showPlayerNameDialog();
            if (playerName == null) return; // Usuario canceló

            save = new GameSave(playerName);
            GameSave.saveGame(save); // Guardar inmediatamente
        }

        // Mostrar mensaje de bienvenida adecuado
        String welcomeMsg = save.isNewGame()
                ? ">> Nueva partida iniciada para " + save.getPlayerName()
                : ">> Partida cargada para " + save.getPlayerName() + " (Nivel " + save.getLevel() + ")";

        dispose();
        HackerTerminalFrame terminal = new HackerTerminalFrame(save);
        terminal.appendText(welcomeMsg + "\n");
        terminal.setVisible(true);
    }

    private String showPlayerNameDialog() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Ingresa tu alias de hacker:"), BorderLayout.NORTH);

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Consolas", Font.PLAIN, 14));
        panel.add(nameField, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Nueva Partida",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        return (result == JOptionPane.OK_OPTION) ? nameField.getText().trim() : null;
    }

    // Pruebas de los Frames (Solo ejecutar cuando se hacen las pruebas)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenuFrame().setVisible(true);
        });
    }


}