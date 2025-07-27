package es.javierdev.Games;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class QuickScriptGame extends JFrame  {
    private static final String[][] COMMAND_SETS = {
            {"sudo rm -rf /virus.exe", "Eliminar virus del sistema"},
            {"bypass.security --level=3 --target=mainframe", "Saltar seguridad del mainframe"},
            {"inject.payload --type=ransomware --target=192.168.1.1", "Inyectar payload malicioso"},
            {"decrypt --algorithm=aes256 --keyfile=secret.key", "Descifrar archivos protegidos"},
            {"ddos --target=corp-server --threads=1000", "Iniciar ataque DDoS"},
            {"sql.injection --target=db-server --query='SELECT * FROM users'", "Inyección SQL en base de datos"},
            {"npm run start", "Iniciado de paquete de nodejs"}
    };

    private static final String[] TARGETS = {
            "Arasaka DB Server",
            "Militech Firewall",
            "Night City PD",
            "Kang Tao Mainframe"
    };

    private String target;
    private String fullCommand;
    private String commandDescription;
    private String currentInput = "";
    private int timeLeft;
    private int score = 0;
    private final int difficultyLevel;
    private int correctChars = 0;
    private int errors = 0;
    private boolean gameActive = true;

    // Componentes UI
    private JTextPane consolePane;
    private JLabel timerLabel;
    private  JLabel targetLabel;
    private JLabel scoreLabel;
    private JLabel descriptionLabel;
    private JProgressBar typingProgress;
    private Timer gameTimer;
    private JTextField inputField;

    public QuickScriptGame(JFrame parent, int difficultyLevel) {
        this.difficultyLevel = Math.max(1, Math.min(5, difficultyLevel)); // Asegurar 1-5
        setTitle("CyberTerminal - Quick Script Challenge");
        setSize(850, 650);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initGame();
        setupUI();
        startGame();
    }

    private void initGame() {
        Random rand = new Random();
        this.target = TARGETS[rand.nextInt(TARGETS.length)];

        int commandIndex = rand.nextInt(COMMAND_SETS.length);
        this.fullCommand = COMMAND_SETS[commandIndex][0];
        this.commandDescription = COMMAND_SETS[commandIndex][1];

        this.timeLeft = 45 - (difficultyLevel * 5);
        this.currentInput = "";
        this.correctChars = 0;
        this.errors = 0;
        this.gameActive = true;
    }

    private void setupUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error del programa: " + e.getMessage());
        }

        // Panel principal con efecto de terminal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 15, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de información superior
        JPanel infoPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        infoPanel.setOpaque(false);

        targetLabel = createStyledLabel("Objetivo: " + target, new Color(0, 255, 255));
        timerLabel = createStyledLabel("Tiempo: " + timeLeft + "s", Color.YELLOW);
        scoreLabel = createStyledLabel("Puntuación: " + score, new Color(0, 255, 100));
        descriptionLabel = createStyledLabel("Acción: " + commandDescription, new Color(200, 200, 255));

        infoPanel.add(targetLabel);
        infoPanel.add(timerLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(descriptionLabel);
        infoPanel.add(new JLabel()); // Espacio vacío
        infoPanel.add(new JLabel()); // Espacio vacío

        // Área de consola (usando JTextPane para formato HTML)
        consolePane = new JTextPane();
        consolePane.setContentType("text/html");
        consolePane.setEditable(false);
        consolePane.setFont(new Font("Consolas", Font.PLAIN, 16));
        consolePane.setBackground(new Color(0, 30, 40));
        consolePane.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0), 1));

        updateConsoleDisplay();

        // Barra de progreso de tecleo
        typingProgress = new JProgressBar(0, fullCommand.length());
        typingProgress.setStringPainted(true);
        typingProgress.setFont(new Font("Consolas", Font.PLAIN, 12));
        typingProgress.setForeground(new Color(0, 255, 0));
        typingProgress.setBackground(new Color(0, 50, 70));
        typingProgress.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 1));

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setOpaque(false);

        JLabel inputLabel = createStyledLabel("Escribe el comando:", new Color(0, 200, 200));

        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 16));
        inputField.setBackground(new Color(0, 40, 60));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.GREEN);
        inputField.setBorder(BorderFactory.createCompoundBorder(
        ));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!gameActive) return;

                currentInput = inputField.getText();
                checkTypingProgress();
            }
        });

        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);

        // Ensamblado
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(consolePane), BorderLayout.CENTER);
        mainPanel.add(typingProgress, BorderLayout.SOUTH);
        mainPanel.add(inputPanel, BorderLayout.PAGE_END);

        add(mainPanel);
    }
    // Estilos
    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Consolas", Font.BOLD, 14));
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }
    // Iniciar el juego de escritura rapida
    private void startGame() {
        gameTimer = new Timer(1000, e -> updateGameTimer());
        gameTimer.start();
    }
    // Temporizador con efectos
    private void updateGameTimer() {
        if (!gameActive) return;

        timeLeft--;
        timerLabel.setText("Tiempo: " + timeLeft + "s");

        // Efecto de advertencia
        if (timeLeft <= 10) {
            timerLabel.setForeground(Color.RED);
            if (timeLeft % 2 == 0) {
                consolePane.setBackground(new Color(30, 0, 0));
            } else {
                consolePane.setBackground(new Color(0, 30, 40));
            }
        }

        if (timeLeft <= 0) {
            endGame(false);
        }
    }
    // Comprobar el progreso
    private void checkTypingProgress() {
        correctChars = 0;
        errors = 0;

        for (int i = 0; i < currentInput.length() && i < fullCommand.length(); i++) {
            if (currentInput.charAt(i) == fullCommand.charAt(i)) {
                correctChars++;
            } else {
                errors++;
            }
        }

        updateProgressBar();
        updateConsoleDisplay();

        if (correctChars == fullCommand.length() && currentInput.length() == fullCommand.length()) {
            endGame(true);
        }
    }
    // Actualizar la Barra de progreso
    private void updateProgressBar() {
        int progress = correctChars;
        typingProgress.setValue(progress);
        typingProgress.setString(String.format("%d/%d (%.0f%%)",
                progress, fullCommand.length(),
                (double) progress / fullCommand.length() * 100));

        // Cambiar color según progreso
        if (progress > fullCommand.length() * 0.75) {
            typingProgress.setForeground(new Color(0, 255, 0));
        } else if (progress > fullCommand.length() * 0.5) {
            typingProgress.setForeground(new Color(255, 255, 0));
        } else {
            typingProgress.setForeground(new Color(255, 100, 100));
        }
    }

    private void updateConsoleDisplay() {
        try {
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html><body style='margin: 5px; font-family: Consolas; color: #AAAAAA;'>");
            htmlBuilder.append("<div style='color: #00FF00;'>CyberTerminal v3.1</div>");
            htmlBuilder.append("<div style='margin-bottom: 10px;'>Objetivo: <span style='color: #00FFFF;'>").append(target).append("</span></div>");
            htmlBuilder.append("<div>").append(commandDescription).append("</div><br>");
            htmlBuilder.append("<div style='margin-bottom: 10px;'>Comando requerido:</div>");

            // Mostrar comando con formato
            htmlBuilder.append("<div style='font-size: 18px; background: #001010; padding: 5px; border: 1px solid #005050;'>");
            for (int i = 0; i < fullCommand.length(); i++) {
                if (i < currentInput.length()) {
                    if (currentInput.charAt(i) == fullCommand.charAt(i)) {
                        htmlBuilder.append("<span style='color: #00FF00;'>").append(fullCommand.charAt(i)).append("</span>");
                    } else {
                        htmlBuilder.append("<span style='color: #FF0000;'>").append(fullCommand.charAt(i)).append("</span>");
                    }
                } else {
                    htmlBuilder.append("<span style='color: #666666;'>").append(fullCommand.charAt(i)).append("</span>");
                }
            }
            htmlBuilder.append("</div><br>");

            // Mostrar entrada actual con cursor
            htmlBuilder.append("<div style='font-size: 18px;'>> ").append(currentInput)
                    .append("<span style='color: #00FF00;'>|</span></div>");

            // Mostrar estadísticas
            htmlBuilder.append("<div style='margin-top: 10px; font-size: 14px;'>")
                    .append("Precisión: ").append(calculateAccuracy()).append("% | ")
                    .append("Errores: ").append(errors).append(" | ")
                    .append("Tiempo: ").append(timeLeft).append("s")
                    .append("</div>");

            htmlBuilder.append("</body></html>");

            consolePane.setText(htmlBuilder.toString());
            consolePane.setCaretPosition(consolePane.getDocument().getLength());
        } catch (Exception e) {
            System.err.println("Error updating console: " + e.getMessage());
        }
    }

    private int calculateAccuracy() {
        if (correctChars + errors == 0) return 100;
        return (int) ((double) correctChars / (correctChars + errors) * 100);
    }
    // Metodo de fin del juego
    private void endGame(boolean success) {
        gameActive = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Calcular puntuación
        this.score = calculateScore(success);

        // Mostrar resultados
        String message;
        if (success) {
            message = String.format(
                    "<html><div style='font-family: Consolas; color: #00FF00;'>" +
                            "¡COMANDO EJECUTADO CON ÉXITO!<br><br>" +
                            "Precisión: %d%%<br>" +
                            "Tiempo restante: %ds<br>" +
                            "Puntuación: %d" +
                            "</div></html>",
                    calculateAccuracy(), timeLeft, score
            );

            JOptionPane.showMessageDialog(this, message, "Éxito",
                    JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon());
        } else {
            message = String.format(
                    "<html><div style='font-family: Consolas; color: #FF0000;'>" +
                            "¡FALLO EN LA EJECUCIÓN!<br><br>" +
                            "Comando requerido: %s<br>" +
                            "Progreso: %d/%d caracteres<br>" +
                            "Precisión: %d%%" +
                            "</div></html>",
                    fullCommand, correctChars, fullCommand.length(), calculateAccuracy()
            );

            JOptionPane.showMessageDialog(this, message, "Fallo",
                    JOptionPane.ERROR_MESSAGE);
        }

        dispose();
    }

    private int calculateScore(boolean success) {
        if (!success) return 0;

        int baseScore = fullCommand.length() * 10;
        int timeBonus = timeLeft * 3;
        int accuracyBonus = calculateAccuracy() * 2;
        int difficultyMultiplier = difficultyLevel * 10;

        return (baseScore + timeBonus + accuracyBonus) * difficultyMultiplier / 10;
    }
    // Pruebas de los Frames (Solo ejecutar cuando se hacen las pruebas)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new QuickScriptGame(null, 2).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar el juego: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}