public class Gold {

    int i;
    int j;

    Gold(){
        this(0,0);
    }
    Gold(int i, int j){
        this.i = i;
        this.j = j;
    }

    public void move(int c, int d) {
        i = c;
        j = d;
    }
}
