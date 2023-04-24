package gui.saveAndRestore;

import language.LanguageAdapter;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;


public interface UserState {
    boolean save(JDesktopPane desktopPane);
    JInternalFrame[] load(LanguageAdapter adapter) throws Exception;
}
