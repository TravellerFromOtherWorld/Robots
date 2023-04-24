package view;

import language.LanguageAdapter;
import model.robotState.RobotState;

import java.awt.EventQueue;
import java.awt.BorderLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements Observer {
    private final RobotState robotModel;

    public GameWindow(LanguageAdapter adapter, RobotState model) {
        super(adapter.translate("game_window"), true, true, true, true);
        robotModel = model;
        robotModel.addObserver(this);
        RobotPainter robotPainter = new RobotPainter(robotModel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(robotPainter, BorderLayout.CENTER);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                robotModel.setTargetPosition(e.getPoint());
            }
        });
        getContentPane().add(panel);
        pack();
    }
    private boolean areEqual(Object o1, Object o2)
    {
        if (o1 == null)
            return o2 == null;
        return o1.equals(o2);
    }

    @Override
    public void update(Observable o, Object key) {
        if (areEqual(robotModel, o)){
            if (areEqual(RobotState.KEY_REDRAW, key))
                onRedrawEvent();
            else if (areEqual(RobotState.KEY_MODEL_UPDATE, key))
                onModelUpdateEvent();
        }
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return RobotState.asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent()
    {
        double distance = distance(robotModel.getM_targetPositionX(), robotModel.getM_targetPositionY(),
                robotModel.getM_robotPositionX(), robotModel.getM_robotPositionY());
        if (distance < 0.5)
        {
            return;
        }
        double velocity = RobotState.maxVelocity;
        double angleToTarget = angleTo(robotModel.getM_robotPositionX(), robotModel.getM_robotPositionY(),
                robotModel.getM_targetPositionX(), robotModel.getM_targetPositionY());
        double angularVelocity = 0;

        double angle = RobotState.asNormalizedRadians(angleToTarget - robotModel.getM_robotDirection());

        if (angle < Math.PI / 2) {
            angularVelocity = RobotState.maxAngularVelocity;
        } else if (angle > Math.PI / 2) {
            angularVelocity = -RobotState.maxAngularVelocity;
        }
        /*
        if (angleToTarget > robotModel.getM_robotDirection())
        {
            angularVelocity = RobotState.maxAngularVelocity;
        }
        if (angleToTarget < robotModel.getM_robotDirection())
        {
            angularVelocity = -RobotState.maxAngularVelocity;
        }*/

        robotModel.moveRobot(velocity, angularVelocity, 10);
    }
}
