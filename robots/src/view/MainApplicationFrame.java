package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import language.LanguageAdapter;
import model.log.Logger;
import view.saveAndRestore.SaveAndRestore;
import model.robotState.RobotState;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final LanguageAdapter adapter = new LanguageAdapter("rus");
    private final SaveAndRestore saveAndRestore = new SaveAndRestore();
    private final RobotState robotModel;

    public MainApplicationFrame(RobotState model) {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        robotModel = model;
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

//одна строчка
        restoreUserState();

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handlingCloseEvent();
            }
        });
    }

    protected LogWindow createLogWindow() {
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

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
//разбить на методы
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createAdditionalMenu());
        return menuBar;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = addMenu(adapter.translate("display_mode"),
                adapter.translate("display_mode_descr"), KeyEvent.VK_V);
        lookAndFeelMenu.add(addMenuButton(adapter.translate("system_scheme"), KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }));
        lookAndFeelMenu.add(addMenuButton(adapter.translate("uni_scheme"), KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }));
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = addMenu(adapter.translate("tests"), adapter.translate("tests_command"), KeyEvent.VK_T);
        testMenu.add(addMenuButton(adapter.translate("log_message"), KeyEvent.VK_S, (event) -> {
            Logger.debug(adapter.translate("new_str"));
        }));
        return testMenu;
    }

    private JMenu createAdditionalMenu() {
        JMenu additionalMenu = addMenu(adapter.translate("additional"),
                adapter.translate("additional_descr"), KeyEvent.VK_A);
        additionalMenu.add(addMenuButton(adapter.translate("exit"), KeyEvent.VK_ESCAPE, (event) -> handlingCloseEvent()));
        return additionalMenu;
    }

    private void handlingCloseEvent() {
        String[] options = {adapter.translate("option_yes"), adapter.translate("option_no")};
        int exit = JOptionPane.showOptionDialog(null, adapter.translate("confirm"), adapter.translate("exit"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if (exit == 0) {
            int exitCode = 0;
            if (!saveAndRestore.saveUserState(desktopPane))
                exitCode = 1;
            System.exit(exitCode);
        }
    }

    private void restoreUserState() {
        try {
            JInternalFrame[] frames = saveAndRestore.restoreUserState(adapter, robotModel);
            for (JInternalFrame frame : frames) {
                addWindow(frame);
            }
        } catch (Exception ex) {
            createStandardState();
        }
    }

    private void createStandardState() {
        addWindow(createLogWindow());
        addWindow(new GameWindow(adapter, robotModel), 400, 400);
        addWindow(new RobotCoordinates(robotModel));
    }

    private JMenu addMenu(String menuName, String description, int mnemonic) {
        JMenu newMenu = new JMenu(menuName);
        newMenu.setMnemonic(mnemonic);
        newMenu.getAccessibleContext().setAccessibleDescription(description);
        return newMenu;
    }

    private JMenuItem addMenuButton(String buttonName, int mnemonic, ActionListener action) {
        JMenuItem newButton = new JMenuItem(buttonName, mnemonic);
        newButton.addActionListener(action);
        return newButton;
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
