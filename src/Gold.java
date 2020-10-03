public class Gold extends Ship{

    int i;
    int j;
    Gold(int i, int j){
        this.i = i;
        this.j = j;
    }
    public void move(int c, int d) {
        i = c;
        j = d;
    }

}
