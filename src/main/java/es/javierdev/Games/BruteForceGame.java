package es.javierdev.Games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class BruteForceGame extends JFrame {
    private static final String[] TECH_COMPANIES = {"NETFLIX", "X", "GOOGLE", "MICROSOFT"};
    private static final String[] PASSWORDS = {
            "Contra123", "1234567", "JamonCocido", "Manuel1995",
            "Lukajais!", "LuisFer12", "1562", "JorgedelCampo@"
    };

    private String targetCompany;
    private String realPassword;
    private char[] currentGuess;
    private int attemptsLeft;
    private int timeLeft;
    private int score;

    private JLabel companyLabel;
    private JLabel timerLabel;
    private JLabel attemptsLabel;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JButton[] keyboardButtons;
    private JButton submitButton;
    // Frame del juego de fuerza bruta
    public BruteForceGame(JFrame parent) {
        setTitle("Fuerza Bruta - CyberHack Terminal");
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initGame();
        setupUI();
        startTimer();
    }

    private void initGame() {
        Random rand = new Random();
        this.targetCompany = TECH_COMPANIES[rand.nextInt(TECH_COMPANIES.length)];
        this.realPassword = PASSWORDS[rand.nextInt(PASSWORDS.length)];
        this.currentGuess = new char[realPassword.length()];
        Arrays.fill(currentGuess, '_');
        this.attemptsLeft = 6;
        this.timeLeft = 120; // 2 minutos
        this.score = 0;
    }

    private void setupUI() {
        // Panel principal con efecto de scanlines
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawScanLines(g);
            }
        };
        mainPanel.setBackground(new Color(0, 20, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de información superior
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.setOpaque(false);

        companyLabel = new JLabel("Objetivo: " + targetCompany, SwingConstants.CENTER);
        companyLabel.setFont(new Font("OCR A Extended", Font.BOLD, 16));
        companyLabel.setForeground(new Color(0, 255, 255));

        timerLabel = new JLabel("Tiempo: " + timeLeft + "s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        timerLabel.setForeground(Color.YELLOW);

        attemptsLabel = new JLabel("Intentos: " + attemptsLeft, SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        attemptsLabel.setForeground(new Color(255, 100, 100));

        infoPanel.add(companyLabel);
        infoPanel.add(timerLabel);
        infoPanel.add(attemptsLabel);

        // Barra de progreso
        progressBar = new JProgressBar(0, realPassword.length());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Consolas", Font.PLAIN, 12));
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(new Color(30, 30, 70));
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 1));

        // Área de log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        logArea.setBackground(new Color(0, 30, 20));
        logArea.setForeground(new Color(0, 255, 100));
        logArea.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 0), 1));
        logMessage("Iniciando ataque de fuerza bruta contra " + targetCompany + "...\n");
        logMessage("Longitud de contraseña detectada: " + realPassword.length() + " caracteres\n");

        // Teclado virtual
        JPanel keyboardPanel = createKeyboardPanel();

        // Botón de enviar
        submitButton = new JButton("Ejecutar Ataque (ENTER)");
        submitButton.setFont(new Font("Consolas", Font.BOLD, 14));
        submitButton.addActionListener(e -> checkPassword());
        styleButton(submitButton, new Color(0, 150, 0), new Color(0, 200, 0));

        // Ensamblado
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(keyboardPanel, BorderLayout.CENTER);
        southPanel.add(submitButton, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createKeyboardPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 10));
        panel.setOpaque(false);

        String[] keys = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                "A", "S", "D", "F", "G", "H", "J", "K", "L", "Ñ",
                "Z", "X", "C", "V", "B", "N", "M", "_", "#", "@"
        };

        keyboardButtons = new JButton[keys.length];

        for (int i = 0; i < keys.length; i++) {
            keyboardButtons[i] = new JButton(keys[i]);
            keyboardButtons[i].setFont(new Font("Consolas", Font.BOLD, 14));
            styleButton(keyboardButtons[i], new Color(30, 30, 70), new Color(0, 100, 200));

            final String key = keys[i];
            keyboardButtons[i].addActionListener(e -> addToPassword(key));

            panel.add(keyboardButtons[i]);
        }

        return panel;
    }

    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 1));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void addToPassword(String character) {
        for (int i = 0; i < currentGuess.length; i++) {
            if (currentGuess[i] == '_') {
                currentGuess[i] = character.charAt(0);
                break;
            }
        }
        updatePasswordDisplay();
    }

    private void updatePasswordDisplay() {
        StringBuilder sb = new StringBuilder("Intento actual: ");
        for (char c : currentGuess) {
            sb.append(c).append(" ");
        }
        logArea.setText(logArea.getText() + sb.toString() + "\n");
        progressBar.setValue(countFilledPositions());
    }

    private int countFilledPositions() {
        int count = 0;
        for (char c : currentGuess) {
            if (c != '_') count++;
        }
        return count;
    }

    private void checkPassword() {
        attemptsLeft--;
        attemptsLabel.setText("Intentos: " + attemptsLeft);

        String attempt = new String(currentGuess).replace("_", "");
        logMessage("Probando: " + attempt + "...");

        if (attempt.equals(realPassword)) {
            winGame();
            return;
        }

        // Dar pistas
        String hint = generateHint(attempt);
        logMessage(hint);

        if (attemptsLeft <= 0) {
            loseGame();
        } else {
            Arrays.fill(currentGuess, '_');
            updatePasswordDisplay();
        }
    }

    private String generateHint(String attempt) {
        StringBuilder hint = new StringBuilder("Pista: ");

        // Caracteres correctos en posición correcta
        for (int i = 0; i < Math.min(attempt.length(), realPassword.length()); i++) {
            if (attempt.charAt(i) == realPassword.charAt(i)) {
                hint.append("✓");
            } else if (realPassword.contains(String.valueOf(attempt.charAt(i)))) {
                hint.append("~");
            } else {
                hint.append("✗");
            }
        }

        // Calcula porcentaje de similitud
        double similarity = calculateSimilarity(attempt, realPassword);
        hint.append(String.format(" (%.0f%% similar)", similarity * 100));

        return hint.toString();
    }

    private double calculateSimilarity(String s1, String s2) {
        String longer = s1.length() > s2.length() ? s1 : s2;
        String shorter = s1.length() > s2.length() ? s2 : s1;

        int matches = 0;
        for (int i = 0; i < shorter.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i)) matches++;
        }

        return (double) matches / longer.length();
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeLeft <= 0) {
                    timer.cancel();
                    SwingUtilities.invokeLater(() -> {
                        logMessage("\n¡Tiempo agotado! El sistema ha bloqueado el acceso.");
                        loseGame();
                    });
                    return;
                }

                timeLeft--;
                SwingUtilities.invokeLater(() -> {
                    timerLabel.setText("Tiempo: " + timeLeft + "s");
                    if (timeLeft < 30) {
                        timerLabel.setForeground(Color.RED);
                        if (timeLeft % 2 == 0) {
                            timerLabel.setForeground(Color.YELLOW);
                        }
                    }
                });
            }
        }, 1000, 1000);
    }

    private void winGame() {
        this.score = calculateScore();
        logMessage("\n¡ACCESO CONCEDIDO!\nContraseña descifrada: " + realPassword);
        logMessage("Puntuación: " + score + " puntos");

        JOptionPane.showMessageDialog(this,
                "¡Hackeo exitoso!\nHas obtenido " + score + " puntos. Contraseña "+ realPassword +".",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
   // Metodo de cuando pierdes el juego
    private void loseGame() {
        // Salta a un mensaje de el acceso denegado y muestra la contraseña
        logMessage("\n¡ACCESO DENEGADO!\nLa contraseña era: " + realPassword);

        JOptionPane.showMessageDialog(this,
                "¡Sistema bloqueado!\nMejora tus habilidades e inténtalo de nuevo." + realPassword,
                "Fallo", JOptionPane.ERROR_MESSAGE);

        dispose();
    }

    private int calculateScore() {
        int timeBonus = timeLeft * 2;
        int attemptBonus = attemptsLeft * 10;
        int lengthBonus = realPassword.length() * 5;
        return timeBonus + attemptBonus + lengthBonus;
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void drawScanLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 80, 0, 30));
        for (int y = 0; y < getHeight(); y += 3) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }
    // Pruebas de los Frames (Solo ejecutar cuando se hacen las pruebas)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BruteForceGame(null).setVisible(true);
        });
    }
}