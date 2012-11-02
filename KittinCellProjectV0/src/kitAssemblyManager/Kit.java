//package kitAssemblyManager;


public class Kit{
    int id;
    double x;
    double y;

    public Kit(int i){
        id = i;
        x = 80;
        y = -110;
    }

    public synchronized double getX(){
        return x;
    }

    public synchronized double getY(){
        return y;
    }

    public synchronized void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public synchronized int getID(){
        return id;
    }
}
