public class State {
    int goldShips;
    int silverShips;
    Flag flag;


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
        flag = new Flag(5,5);}

    public State(State pre, Move move){

    }


    public void update(int a, int b, int c, int d){
        if(shipsArray[a][b]==2){
            flag.move(c,d);
        }
        if(shipsArray[c][d] == 1)
            goldShips--;
        else if(shipsArray[c][d] == -1){
            silverShips--;
        }
        else if(shipsArray[c][d] == 2){
            flag.captured = true;
        }
        shipsArray[c][d] = shipsArray[a][b];
        shipsArray[a][b] = 0;

    }

    boolean endGame(){
        //Silver wins
        if(flag.captured)
            return true;
        //Gold wins
        if(flag.i==0)
            return true;
        if(flag.j==0)
            return true;

        //Draw
        //if a player has no moves

        return false;

    }

    public boolean checkValid(int a, int b, int c, int d) {
       int min,max;
        System.out.println("a = " + a + ", b = " + b + ", c = " + c + ", d = " + d);
       if(a==c){
           min=Math.min(b,d);
           max=Math.max(b,d);
           for(int j=min;j<=max;j++){
               System.out.println("a,j="+shipsArray[a][j]);
               if(shipsArray[a][j]!=0 && j!=b)
                   return false;
           }
       }
       if(b==d){
           min=Math.min(a,c);
           max=Math.max(a,c);
           for(int i=min;i<=max;i++){
               System.out.println("i,b="+shipsArray[i][b]);
               if(shipsArray[i][b]!=0 && i!=a) {
                   return false;
               }
           }

       }
       return true;

    }

    public boolean checkCapture(int a, int b, int c, int d) {
        if(shipsArray[c][d]==0){
            return false;
        }
        if(Math.abs(a-c) != 1 || Math.abs(b-d) != 1 ){
            return false;
        }
        if(shipsArray[a][b]/shipsArray[c][d]>0)
            return false;
        return true;

    }

    public boolean flagCaptured() {
        return flag.captured;
    }
}
