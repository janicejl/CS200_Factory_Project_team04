public class Kit{
    int id;
    double x;
    double y;

    public Kit(int i){
        id = i;
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
        return id;
    }
}
