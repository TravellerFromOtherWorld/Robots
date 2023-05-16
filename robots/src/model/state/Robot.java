package model.state;

public class Robot {
    private Position robotPosition;
    private volatile double robotDirection = 0;
    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.0025;

    public Robot(double x, double y){
        robotPosition = new Position(x, y);
    }

    public double direction(){return robotDirection;}
    public double x(){return robotPosition.x();}
    public double y(){return robotPosition.y();}
    public void setX(double x){robotPosition.setX(x);}
    public void setY(double y){robotPosition.setY(y);}
    public void setDirection(double direction){robotDirection = direction;}
}
