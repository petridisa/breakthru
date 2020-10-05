public class Silver{


    int i;
    int j;

    Silver(int i, int j){
        this.i = i;
        this.j = j;
    }
    public void move(int c, int d) {
        i = c;
        j = d;
    }
    @Override
    public String toString() {
        return "Silver: " +
                "i=" + i +
                ", j=" + j +
                '}';
    }




}
