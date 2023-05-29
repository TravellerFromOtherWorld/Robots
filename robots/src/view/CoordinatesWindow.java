package view;

import model.state.GameState;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.TextArea;
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends JInternalFrame implements Observer {
    private TextArea coordinates;
    private int cordX = 0;
    private int cordY = 0;
    private final GameState gameModel;

    public CoordinatesWindow(GameState model) {
        super("Окно координат", true, true, true, true);
        gameModel = model;
        gameModel.addObserver(this);
        coordinates = new TextArea("");
        coordinates.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(coordinates, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    private boolean areEqual(Object o1, Object o2) {
        if (o1 == null)
            return o2 == null;
        return o1.equals(o2);
    }

    @Override
    public void update(Observable o, Object key) {
        if (areEqual(gameModel, o)) {
            if (areEqual(GameState.KEY_MODEL_UPDATE, key))
                onModelUpdateEvent();
        }
    }

    private void onModelUpdateEvent() {
        if (cordX != round(gameModel.robotX()) && cordY != round(gameModel.robotY())) {
            cordX = round(gameModel.robotX());
            cordY = round(gameModel.robotY());
            coordinates.append("Координаты по x: " + cordX + ", координаты по y: " + cordY + "\n");
        }
    }

    public static int round(double value) {
        return (int) (value + 0.5);
    }
}
