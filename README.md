# CyberHack Simulator 🚀

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Swing](https://img.shields.io/badge/GUI-Swing-orange)
![License](https://img.shields.io/badge/License-MIT-green)

Un simulador de hacking en ambiente cyberpunk con múltiples minijuegos.

## Diagrama de Clases UML

```mermaid
classDiagram
    %% Jerarquía de Herencia
    class JFrame {
        <<Biblioteca Swing>>
        +setTitle(String)
        +setSize(int, int)
        +setVisible(boolean)
    }

    class JPanel {
        <<Biblioteca Swing>>
        +add(Component)
        +setLayout(LayoutManager)
    }

    class KeyAdapter {
        <<Biblioteca AWT>>
        +keyPressed(KeyEvent)
    }

    %% Nuestras Clases que Heredan
    class MainMenuFrame {
        +startNewGame()
    }

    class TutorialFrame {
        +createTutorialPanel()
    }

    class HackerTerminalFrame {
        +processCommand(String)
    }

    class BruteForceGame {
        +checkPassword(String)
    }


    class QuickScriptGame {
        +checkInput(String)
    }

    class AIBypassGame {
        +processDialogue(int)
    }

    %% Relaciones de Herencia
    JFrame <|-- MainMenuFrame
    JFrame <|-- TutorialFrame
    JFrame <|-- HackerTerminalFrame
    JFrame <|-- BruteForceGame
    JFrame <|-- QuickScriptGame
    JFrame <|-- AIBypassGame

    JPanel <|-- ScanLinesPanel

    %% Clases Internas Personalizadas
    class ScanLinesPanel {
        -paintComponent(Graphics)
    }

    class GameSave {
        +save()
        +load()
    }

```

## Características Principales

- 🖥️ **Terminal de hacking** estilo cyberpunk con efectos visuales
- 🎮 4 minijuegos de hacking diferentes:
  - Fuerza bruta de contraseñas
  - Scripting rápido bajo presión
  - Bypass de IA conversacional
- 💾 Guardado automático de partidas

## Requisitos

- Java 17 o superior
- Maven (para construcción)



## Licencia

MIT License - Ver [LICENSE](LICENSE) para más detalles.

---

⌨️ Desarrollado con ❤️ por Piratemajo para cyberhack
