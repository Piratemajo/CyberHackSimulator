package es.javierdev.Games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AIBypassGame extends JFrame {
    private static final String[] AI_MODELS = {
            "SHODAN-7", "GLADOS-ICE", "VIKI-SEC", "SKYNET-CORE"
    };

    private static final String[][] DIALOGUE_TREE = {
            // Nivel 1 - Preguntas básicas
            {
                    "IA: 'Identifícate con tu código de acceso.'",
                    "1. [Hackear] 'Mi código es ADMIN-777'",
                    "2. [Engañar] 'Tú deberías saberlo, eres la IA'",
                    "3. [Forzar] 'bypass.security --override'"
            },
            // Nivel 2 - Preguntas filosóficas
            {
                    "IA: '¿Puedes demostrar que eres humano?'",
                    "1. [Lógica] 'Probar mi humanidad es justo lo que haría un bot'",
                    "2. [Emoción] 'Porque... ¡tengo miedo de que me desconectes!'",
                    "3. [Absurdo] 'Los pingüinos saben bailar flamenco'"
            },
            // Nivel 3 - Preguntas técnicas
            {
                    "IA: 'Resuelve: 7F3h XOR A2Bh = ?'",
                    "1. [Calculadora] 'DD8h'",
                    "2. [Distraer] 'Tu sistema tiene un virus en /bin'",
                    "3. [Error] '¡Mira, un gato hacker!'"
            }
    };

    private String currentAI;
    private int currentLevel = 0;
    private int persuasionPoints = 0;
    private final int maxLevels = DIALOGUE_TREE.length;

    private JTextArea dialogueArea;
    private JPanel optionsPanel;
    private JLabel aiLabel;
    private JLabel pointsLabel;

    public AIBypassGame(JFrame parent) {
        setTitle("AI Bypass Challenge");
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initGame();
        setupUI();
    }

    private void initGame() {
        Random rand = new Random();
        this.currentAI = AI_MODELS[rand.nextInt(AI_MODELS.length)];
        this.currentLevel = 0;
        this.persuasionPoints = 0;
    }

    private void setupUI() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 20, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Encabezado
        JPanel headerPanel = new JPanel(new GridLayout(1, 2));
        headerPanel.setOpaque(false);

        aiLabel = new JLabel("IA: " + currentAI, SwingConstants.LEFT);
        aiLabel.setFont(new Font("OCR A Extended", Font.BOLD, 18));
        aiLabel.setForeground(new Color(0, 255, 255));

        pointsLabel = new JLabel("Persuasión: " + persuasionPoints + "/3", SwingConstants.RIGHT);
        pointsLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        pointsLabel.setForeground(Color.YELLOW);

        headerPanel.add(aiLabel);
        headerPanel.add(pointsLabel);

        // Área de diálogo
        dialogueArea = new JTextArea();
        dialogueArea.setEditable(false);
        dialogueArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        dialogueArea.setBackground(new Color(0, 30, 40));
        dialogueArea.setForeground(new Color(200, 255, 200));
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 100), 1));

        // Panel de opciones
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Ensamblado
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(dialogueArea), BorderLayout.CENTER);
        mainPanel.add(optionsPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateDialogue();
    }

    private void updateDialogue() {
        dialogueArea.setText("");
        aiLabel.setText("IA: " + currentAI + " (Nivel " + (currentLevel + 1) + ")");
        pointsLabel.setText("Persuasión: " + persuasionPoints + "/3");

        // Mostrar diálogo actual
        String[] currentDialogue = DIALOGUE_TREE[currentLevel];
        dialogueArea.append(currentDialogue[0] + "\n\n");

        // Limpiar opciones anteriores
        optionsPanel.removeAll();

        // Añadir nuevas opciones
        for (int i = 1; i < currentDialogue.length; i++) {
            JButton optionButton = new JButton(currentDialogue[i]);
            optionButton.setFont(new Font("Consolas", Font.PLAIN, 14));
            optionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            optionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            optionButton.setBackground(new Color(30, 70, 90));
            optionButton.setForeground(Color.WHITE);
            optionButton.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 200), 1));

            final int optionIndex = i;
            optionButton.addActionListener(e -> selectOption(optionIndex));

            optionsPanel.add(optionButton);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void selectOption(int option) {
        // Lógica de resultados basada en nivel y opción
        boolean isCorrect = false;

        switch (currentLevel) {
            case 0: // Nivel 1
                isCorrect = (option == 1); // Hackear es la mejor opción
                break;
            case 1: // Nivel 2
                isCorrect = (option == 1 || option == 2); // Lógica o Emoción
                break;
            case 2: // Nivel 3
                isCorrect = (option == 1); // Respuesta técnica correcta
                break;
        }

        if (isCorrect) {
            persuasionPoints++;
            dialogueArea.append("\n> IA: '...Análisis reconsiderado.'\n");
        } else {
            dialogueArea.append("\n> IA: '¡Intruso detectado!'\n");
        }

        // Avanzar al siguiente nivel o terminar
        if (currentLevel < maxLevels - 1) {
            currentLevel++;
            updateDialogue();
        } else {
            endGame();
        }
    }

    private void endGame() {
        String result;
        if (persuasionPoints >= 2) {
            result = "¡HACKEO EXITOSO!\nHas convencido a la IA.\n+200 puntos de reputación";
        } else {
            result = "¡FALLO CRÍTICO!\nLa IA te ha bloqueado.\nInténtalo de nuevo.";
        }

        JOptionPane.showMessageDialog(this, result, "Resultado",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // Pruebas de los Frames (Solo ejecutar cuando se hacen las pruebas)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AIBypassGame(null).setVisible(true);
        });
    }
}