package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import language.LanguageAdapter;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame(LanguageAdapter adapter) {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

//одна строчка
        addWindow(createLogWindow(adapter));
        addWindow(new GameWindow(adapter), 400, 400);

        setJMenuBar(generateMenuBar(adapter));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handlingCloseEvent(adapter);
            }
        });
    }

    protected LogWindow createLogWindow(LanguageAdapter adapter) {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), adapter);
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(adapter.translate("protocol_is_working"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected void addWindow(JInternalFrame frame, int width, int height) {
        frame.setSize(width, height);
        addWindow(frame);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private JMenuBar generateMenuBar(LanguageAdapter adapter) {
        JMenuBar menuBar = new JMenuBar();
        MenuModificator modificator = new MenuModificator();
//разбить на методы
        menuBar.add(createLookAndFeelMenu(modificator, adapter));
        menuBar.add(createTestMenu(modificator, adapter));
        menuBar.add(createAdditionalMenu(modificator, adapter));
        return menuBar;
    }

    private JMenu createLookAndFeelMenu(MenuModificator modificator, LanguageAdapter adapter) {
        JMenu lookAndFeelMenu = modificator.addMenu(adapter.translate("display_mode"),
                adapter.translate("display_mode_descr"), KeyEvent.VK_V);
        lookAndFeelMenu.add(modificator.addMenuButton(adapter.translate("system_scheme"), KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }));
        lookAndFeelMenu.add(modificator.addMenuButton(adapter.translate("uni_scheme"), KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }));
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu(MenuModificator modificator, LanguageAdapter adapter) {
        JMenu testMenu = modificator.addMenu(adapter.translate("tests"), adapter.translate("tests_command"), KeyEvent.VK_T);
        testMenu.add(modificator.addMenuButton(adapter.translate("log_message"), KeyEvent.VK_S, (event) -> {
            Logger.debug(adapter.translate("new_str"));
        }));
        return testMenu;
    }

    private JMenu createAdditionalMenu(MenuModificator modificator, LanguageAdapter adapter) {
        JMenu additionalMenu = modificator.addMenu(adapter.translate("additional"),
                adapter.translate("additional_descr"), KeyEvent.VK_A);
        additionalMenu.add(modificator.addMenuButton(adapter.translate("exit"), KeyEvent.VK_ESCAPE, (event) -> handlingCloseEvent(adapter)));
        return additionalMenu;
    }

    private void handlingCloseEvent(LanguageAdapter adapter) {
        String[] options = {adapter.translate("option_yes"), adapter.translate("option_no")};
        int exit = JOptionPane.showOptionDialog(null, adapter.translate("confirm"), adapter.translate("exit"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if (exit == 0)
            System.exit(0);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
