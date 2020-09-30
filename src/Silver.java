public class Silver {


    int i;
    int j;

    Silver(){
        this(0,0);
    }
    Silver(int i, int j){
        this.i = i;
        this.j = j;
    }

    public void move(int c, int d) {
        i = c;
        j = d;
    }
}
