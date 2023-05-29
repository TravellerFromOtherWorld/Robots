package model.state;

import java.awt.Point;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class GameState extends Observable {
    public static final String KEY_REDRAW = "redraw";
    public static final String KEY_MODEL_UPDATE = "update";

    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private Robot robot;
    private Target target;

    public GameState() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setChanged();
                notifyObservers(KEY_REDRAW);
                clearChanged();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setChanged();
                notifyObservers(KEY_MODEL_UPDATE);
                clearChanged();
            }
        }, 0, 10);
        robot = new Robot(100, 100);
        target = new Target(150, 100);
    }

    public void setTargetPosition(Point p) {
        target.setX(p.x + p.x / 4);
        target.setY(p.y + p.y / 4);
    }

    public double robotX() {
        return robot.x();
    }
    //Robot.Position.X

    public double robotY() {
        return robot.y();
    }

    public double robotDirection() {
        return robot.direction();
    }

    public int targetX() {
        return (int) target.x();
    }

    public int targetY() {
        return (int) target.y();
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, Robot.maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -Robot.maxAngularVelocity, Robot.maxAngularVelocity);
        double newX = robot.x() + velocity / angularVelocity *
                (Math.sin(robot.direction() + angularVelocity * duration) -
                        Math.sin(robot.direction()));
        if (!Double.isFinite(newX)) {
            newX = robot.x() + velocity * duration * Math.cos(robot.direction());
        }
        double newY = robot.y() - velocity / angularVelocity *
                (Math.cos(robot.direction() + angularVelocity * duration) -
                        Math.cos(robot.direction()));
        if (!Double.isFinite(newY)) {
            newY = robot.y() + velocity * duration * Math.sin(robot.direction());
        }
        robot.setX(newX);
        robot.setY(newY);
        double newDirection = asNormalizedRadians(robot.direction() + angularVelocity * duration);
        robot.setDirection(newDirection);
    }

    public static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
