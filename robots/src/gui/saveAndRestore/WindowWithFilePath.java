package gui.saveAndRestore;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class WindowWithFilePath extends WindowWithState {
    private final Path userFile;

    public WindowWithFilePath(String fileName, String name, boolean iconfible, boolean maxsizeble, boolean b2, boolean b3) {
        super(name, iconfible, maxsizeble, b2, b3);
        userFile = Path.of(".").toAbsolutePath().getParent().resolve(System.getProperty("user.name") + "\\" + fileName);
    }

    public boolean userFileDontExist() {
        return !Files.exists(userFile);
    }

    public boolean userDirectoryDontExist() {
        return !Files.exists(userFile.getParent());
    }

    private boolean createUserDirectory() {
        try {
            Files.createDirectory(userFile.getParent());
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean createUserFile() {
        try {
            Files.createFile(userFile);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public void save() {
        if (userFileDontExist())
            if (userDirectoryDontExist()) {
                if (!(createUserDirectory() && createUserFile())) {
                    return;
                }
            } else {
                if (!createUserFile()) {
                    return;
                }
            }


        try (BufferedWriter outFile = new BufferedWriter(new FileWriter(userFile.toFile()))) {
            outFile.write(this.getTitle() + "," + this.getBounds().height + "," + this.getBounds().width + ","
                    + this.getBounds().x + "," + this.getBounds().y + "\n");
            outFile.flush();
        } catch (IOException e) {
        }
    }

    public void load() {
        try {
            String frame = Files.readString(userFile);
            String[] frameInfo = frame.split(",");
            this.setTitle(frameInfo[0]);
            this.setBounds(Integer.parseInt(frameInfo[3]), Integer.parseInt(frameInfo[4].strip()),
                    Integer.parseInt(frameInfo[2]), Integer.parseInt(frameInfo[1]));
        } catch (IOException e) {
        }
    }
}
