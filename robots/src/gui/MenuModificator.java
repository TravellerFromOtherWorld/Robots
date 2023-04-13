package gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

public class MenuModificator{
    public JMenu addMenu(String menuName, String description, int key){
        JMenu newMenu = new JMenu(menuName);
        newMenu.setMnemonic(key);
        newMenu.getAccessibleContext().setAccessibleDescription(description);
        return newMenu;
    }

    public JMenuItem addMenuButton(String buttonName, int key, ActionListener action){
        JMenuItem newButton = new JMenuItem(buttonName, key);
        newButton.addActionListener(action);
        return newButton;
    }
}
