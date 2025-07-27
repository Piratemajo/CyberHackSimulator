package es.javierdev.Save;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameSave implements Serializable {
    // Atributos
    private static final String SAVE_PATH = "saves/savegame" +".dat";
    private final String playerName;
    private final int level;
    private final int score;
    private final int accessLevel;
    private final transient boolean isNewGame;

    // Constructor para nueva partida
    public GameSave(String playerName) {
        this.playerName = playerName;
        this.level = 1;
        this.score = 0;
        this.accessLevel = 1;
        this.isNewGame = true;
    }
    // Cargar la partida
    public static GameSave loadGame() {
        if (!Files.exists(Paths.get(SAVE_PATH))) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVE_PATH))) {

            return (GameSave) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error cargando partida: " + e.getMessage());
            return null;
        }
    }
    // Guardar la partida
    public static void saveGame(GameSave save) {
        try {
            // Asegurar que el directorio exista
            Files.createDirectories(Paths.get("saves"));

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(SAVE_PATH))) {

                oos.writeObject(save);
                System.out.println("Partida guardada en: " + SAVE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
            try {
                new FileOutputStream(SAVE_PATH).close();
            } catch (IOException ex) {
                System.err.println("Error crítico al crear archivo: " + ex.getMessage());
            }
        }
    }

    public boolean isNewGame() { return isNewGame; }
    public String getPlayerName() { return playerName; }
    public int getLevel() { return level; }
    public int getScore() { return score; }
    public int getAccessLevel() { return accessLevel; }
}
