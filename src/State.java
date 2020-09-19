public class State {
    int goldShips;
    int silverShips;
    boolean flag;


    int[][] shipsArray;
    //0 is empty
    //-1 is silver
    //1 is gold
    //2 is flag
    int[][] initialState = new int[][]{{0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,-1,-1,-1,-1,-1,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0},
            {0,-1,0,0,1,1,1,0,0,-1,0},
            {0,-1,0,1,0,0,0,1,0,-1,0},
            {0,-1,0,1,0,2,0,1,0,-1,0},
            {0,-1,0,1,0,0,0,1,0,-1,0},
            {0,-1,0,0,1,1,1,0,0,-1,0},
            {0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,-1,-1,-1,-1,-1,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0}

    };
    public State(){
        shipsArray = initialState;
        goldShips = 12;
        silverShips = 20;
        flag = true;}
    public State(State pre, Move move){

    }
    public boolean flagCaptured(){
        return !flag;
    }
    void checkValid(){

    }

}
