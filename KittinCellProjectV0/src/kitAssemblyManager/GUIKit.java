//package kitAssemblyManager;


public class GUIKit{
    Kit kit;
    double x;
    double y;

    public GUIKit(Kit k){
        kit = k;
        x = 150;
        y = -50;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public int getID(){
        return kit.getID();
    }
}
