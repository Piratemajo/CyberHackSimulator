package es.javierdev;

import es.javierdev.Frames.MainMenuFrame;

import javax.swing.*;
// Archivo Main de ejecucion 
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenuFrame().setVisible(true);
            System.out.println("CyberHack Simulator by SrJavier");
            System.out.println("Version 1.0-Release\n\n");
        });
    }
}