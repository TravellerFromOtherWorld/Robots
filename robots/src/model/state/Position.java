package model.state;

public class Position {
    private volatile double positionX;
    private volatile double positionY;

    public Position(double x, double y){
        positionX = x;
        positionY = y;
    }

    public double x(){return positionX;}
    public double y(){return positionY;}
    public void setX(double x){positionX = x;}
    public void setY(double y){positionY = y;}
}
