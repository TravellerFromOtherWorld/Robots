package gui.saveAndRestore;

import javax.swing.*;
import java.awt.Rectangle;

public abstract class WindowWithState extends JInternalFrame implements ObjectState{
    public WindowWithState(String name, boolean iconfible, boolean maxsizeble, boolean b2, boolean b3) {
        super(name,iconfible,maxsizeble, b2, b3);
    }
}
