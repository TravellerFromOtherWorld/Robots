package model.state;

public class Target {
    private Position targetPosition;

    public Target(int x, int y){
        targetPosition = new Position(x, y);
    }

    public void setX(int x){
        targetPosition.setX(x);
    }
    public void setY(int y){
        targetPosition.setY(y);
    }
    public double x(){return targetPosition.x();}
    public double y(){return targetPosition.y();}
}
