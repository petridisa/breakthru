public class Ship {


    int i;
    int j;

    Ship(){
        this(0,0);
    }
    Ship(int i, int j){
        this.i = i;
        this.j = j;
    }

    public void move(int c, int d) {
        i = c;
        j = d;
    }

    @Override
    public String toString() {
        return "Ship{" + this.getClass() +
                "i=" + i +
                ", j=" + j +
                '}';
    }
}
