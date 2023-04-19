package gui.saveAndRestore;


import gui.GameWindow;
import gui.LogWindow;
import language.LanguageAdapter;
import log.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SaveAndRestore {
    private final Path userFile = Path.of(".").toAbsolutePath().getParent().resolve(System.getProperty("user.name") + "\\config.txt");

    public boolean userFileDontExist() {
        return !Files.exists(userFile);
    }

    private boolean createUserDirectory() {
        try {
            Files.createDirectory(userFile.getParent());
            Files.createFile(userFile);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean saveUserState(JDesktopPane desktopPane) {
        if (userFileDontExist())
            if (!createUserDirectory())
                return false;

        try (BufferedWriter outFile = new BufferedWriter(new FileWriter(userFile.toFile()))) {
            JInternalFrame[] frames = desktopPane.getAllFrames();
            for (JInternalFrame currentFrame : frames) {
                String name = currentFrame.getTitle();
                Rectangle size = currentFrame.getBounds();
                outFile.write(name + "," + size.height + "," + size.width + "," + size.x + "," + size.y + "\n");
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public JInternalFrame[] restoreUserState(LanguageAdapter adapter) throws Exception {
        List<String> frames = Files.readAllLines(userFile);
        JInternalFrame[] userFrames = new JInternalFrame[frames.size()];
        for (int i = 0; i < frames.size(); i++) {
            String frame = frames.get(i);
            String[] frameInfo = frame.split(",");
            if (frameInfo[0].equals(adapter.translate("game_window"))) {
                userFrames[i] = new GameWindow(adapter);
            } else if (frameInfo[0].equals(adapter.translate("log_window"))) {
                userFrames[i] = new LogWindow(Logger.getDefaultLogSource(), adapter);
                Logger.debug(adapter.translate("protocol_is_working"));
            }
            userFrames[i].setBounds(Integer.parseInt(frameInfo[3]), Integer.parseInt(frameInfo[4]),
                    Integer.parseInt(frameInfo[2]), Integer.parseInt(frameInfo[1]));
        }
        return userFrames;
    }
}
