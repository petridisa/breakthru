public class Move {
    String prePosition;
    String nextPosition;
    int reward;
    State before;
    State after;
    int si,sj,di,dj;
    public Move(State state, Cell src, Cell dst){
        before = state;
        try {
            after = (State) state.clone();
            after.update(src.i,src.j,dst.i, dst.j);
            after.printArray();
        }catch (Exception e){

        }



    }

    public Move(int si,int sj,int di,int dj){
        this.si = si;
        this.sj = sj;
        this.di = di;
        this.dj = dj;
    }
}
