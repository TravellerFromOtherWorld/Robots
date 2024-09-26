package gui;

import gui.saveAndRestore.WindowWithFilePath;
import language.LanguageAdapter;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class GameWindow extends WindowWithFilePath {
    private final GameVisualizer m_visualizer;

    public GameWindow(LanguageAdapter adapter) {
        super("Game.txt", adapter.translate("game_window"), true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
