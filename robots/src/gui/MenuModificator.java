package gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

public class MenuModificator{
    public JMenu addMenu(String menuName, String description, int mnemonic){
        JMenu newMenu = new JMenu(menuName);
        newMenu.setMnemonic(mnemonic);
        newMenu.getAccessibleContext().setAccessibleDescription(description);
        return newMenu;
    }

    public JMenuItem addMenuButton(String buttonName, int mnemonic, ActionListener action){
        JMenuItem newButton = new JMenuItem(buttonName, mnemonic);
        newButton.addActionListener(action);
        return newButton;
    }
}
