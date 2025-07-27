package es.javierdev.Frames;

import es.javierdev.Games.AIBypassGame;
import es.javierdev.Games.BruteForceGame;
import es.javierdev.Games.QuickScriptGame;
import es.javierdev.Save.GameSave;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class HackerTerminalFrame extends JFrame   {

    private JTextArea terminalText;
    private JTextField inputField;
    private final GameSave currentSave;

    public HackerTerminalFrame(GameSave save) {
        this.currentSave = Objects.requireNonNull(save, "El GameSave no puede ser null");
        setupUI();
    }

    private void setupUI() {
        setTitle("Terminal de Hacking - Nivel " + currentSave.getLevel());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con efecto de scanlines
        JPanel terminalPanel = new ScanLinesPanel();
        terminalPanel.setLayout(new BorderLayout());
        terminalPanel.setBackground(new Color(0, 20, 0));

        // Área de texto con estilo terminal
        terminalText = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Efecto de texto difuminado
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g2d);
            }
        };

        terminalText.setFont(new Font("Consolas", Font.PLAIN, 16));
        terminalText.setForeground(new Color(0, 255, 0));
        terminalText.setBackground(new Color(0, 10, 0));
        terminalText.setEditable(false);
        terminalText.setCaret(new BlockCaret()); // Estilo de la terminal (Caret)

        // ScrollPane con bordes personalizados
        JScrollPane scrollPane = new JScrollPane(terminalText) {
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0, 150, 0));
                g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
                g2.drawRect(1, 1, getWidth()-3, getHeight()-3);
            }
        };
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Campo de entrada con estilo
        inputField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g2d);
            }
        };

        inputField.setFont(new Font("Consolas", Font.PLAIN, 16));
        inputField.setForeground(new Color(0, 255, 150));
        inputField.setBackground(new Color(0, 30, 0));
        inputField.setCaretColor(new Color(0, 255, 0));
        inputField.setBorder(BorderFactory.createCompoundBorder());

        // Botón de enviar
        JButton sendButton = createCyberButton("EJECUTAR");
        sendButton.addActionListener(this::processCommand);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Ensamblado
        terminalPanel.add(scrollPane, BorderLayout.CENTER);
        terminalPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Eventos
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processCommand(null);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {}
            }
        });

        add(terminalPanel);
        printWelcomeMessage();
    }

    private JButton createCyberButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(0, 200, 100));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 255, 150), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 255, 150));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 200, 100));
            }
        });

        return button;
    }

    private void printWelcomeMessage() {
        appendText("> Inicializando terminal segura...\n");
        appendText("> Conectado a servidor: CYBERHACK By Piratemajo\n");
        appendText("> Encriptando datos... \n");
        appendText("> Iniciando sistema de misiones...\n");
        appendText("> Iniciando VPN para conectarse a la red...\n");
        appendText("> Version del Programa: " + MainMenuFrame.VERSION + "\n");
        appendText("> Usuario: " + currentSave.getPlayerName() + "\n");
        appendText("> Contraseña de acceso: *******\n");
        appendText("> Progreso de avance: " + currentSave.getScore() + " puntos\n");
        appendText("> Nivel de acceso: " + currentSave.getAccessLevel() + "\n\n");
        appendText("COMANDOS DISPONIBLES:\n");
        appendText("- fuerza_bruta: Inicia descifrado de contraseña\n");
        appendText("- script_rapido: Minijuego de tecleo rápido\n");
        appendText("- aibypass: Analizar peticiones de una IA de seguridad\n");
        appendText("- guardar: Guarda tu progreso\n");
        appendText("- salir: Cierra la terminal\n\n");
        appendText("> ");
    }

    public void appendText(String text) {
        SwingUtilities.invokeLater(() -> {
            terminalText.append(text);
            terminalText.setCaretPosition(terminalText.getDocument().getLength());
        });
    }

    private void processCommand(ActionEvent e) {
        String command = inputField.getText().trim();
        inputField.setText("");

        appendText(command + "\n");

        switch(command.toLowerCase()) {
            case "fuerza_bruta":
                appendText("> Iniciando módulo de fuerza bruta...\n");
                new BruteForceGame(this).setVisible(true);
                break;

            case "script_rapido":
                appendText("> Ejecutando minijuego de scripting...\n");
                int difficultad = currentSave.getLevel();
                new QuickScriptGame(this, difficultad).setVisible(true);
                break;

            case "aibypass":
                appendText("> Analizando peticiones de una IA de seguridad...\n");
                new AIBypassGame(this).setVisible(true);
                break;

            case "guardar":
                GameSave.saveGame(currentSave);
                appendText("> Progreso guardado correctamente.\n");
                break;

            case "salir":
                dispose();
                new MainMenuFrame().setVisible(true);
                break;

            case "ayuda":
                appendText("COMANDOS DISPONIBLES:\n");
                appendText("- fuerza_bruta: Inicia descifrado de contraseña\n");
                appendText("- aibypass: Analizar peticiones de una IA de seguridad\n");
                appendText("- script_rapido: Minijuego de tecleo rápido\n");
                appendText("- guardar: Guarda tu progreso\n");
                appendText("- salir: Cierra la terminal\n\n");
                break;

            default:
                appendText("> Comando no reconocido. Escribe 'ayuda' para ayuda.\n");
        }

        appendText("> ");
    }

    private void recallPreviousCommand() {

        // Historial de comandos
    }

    // Clase para el efecto scanlines
    private static class ScanLinesPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Efecto de scanlines
            g2d.setColor(new Color(0, 50, 0, 30));
            for (int y = 0; y < getHeight(); y += 3) {
                g2d.fillRect(0, y, getWidth(), 1);
            }
        }
    }

    // Caret estilo terminal
    private static class BlockCaret extends DefaultCaret {
        public BlockCaret() {
            setBlinkRate(500); // Velocidad de parpadeo
        }

        @Override
        protected synchronized void damage(Rectangle r) {
            if (r == null) return;
            x = r.x;
            y = r.y;
            width = 10;
            height = r.height;
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            if (!isVisible()) return;
            JTextComponent comp = getComponent();
            g.setColor(comp.getCaretColor());
            g.fillRect(x, y, width, height);
        }
    }

    // Pruebas de los Frames (Solo ejecutar cuando se hacen las pruebas)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HackerTerminalFrame(new GameSave("Developer")).setVisible(true);
        });
    }

}