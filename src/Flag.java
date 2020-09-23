public class Flag {

    int i;
    int j;
    boolean captured;

    Flag(){
        this(0,0);
    }
    Flag(int i, int j){
        this.i = i;
        this.j = j;
        this.captured = false;
    }

    public void move(int c, int d) {
        i = c;
        j = d;
    }
}
