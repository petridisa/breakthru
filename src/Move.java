public class Move {
    String prePosition;
    String nextPosition;
    int reward;
    State before;
    static State after;

    public Move(State state, Cell src, Cell dst){
        before = state;
        try {
            after = (State) state.clone();
            after.update(src.i,src.j,dst.i, dst.j);
        }catch (Exception e){

        }



    }
}
