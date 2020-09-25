import static java.lang.Math.*;

public class State {
    int goldShips;
    int silverShips;
    Flag flag;
    int grade;


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
        flag = new Flag(5,5);
        grade = 0;}

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

    char endGame(){
        //Silver wins
        if(flag.captured)
            return 's';
        //Gold wins
        if(flag.i==0 || flag.i==10)
            return 'g';
        if(flag.j==0 || flag.j ==10)
            return 'g';

        if(draw())
            return 'd';
        return 'o';

    }

    private boolean draw() {
        //TODO
        //check if player has no moves
        return false;
    }

    public boolean checkValid(int a, int b, int c, int d) {
       int min,max;
       if(a==c){
           min=Math.min(b,d);
           max=Math.max(b,d);
           for(int j=min;j<=max;j++){
               if(shipsArray[a][j]!=0 && j!=b)
                   return false;
           }
       }
       if(b==d){
           min=Math.min(a,c);
           max=Math.max(a,c);
           for(int i=min;i<=max;i++){
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

    public boolean isFlag(int i, int j){
        return shipsArray[i][j] == 2;
    }

    public void evaluate(){
        char end = endGame();
        if(end == 'g'){
            grade = 100;
            return;
        }
        if(end == 's'){
            grade = -100;
            return;
        }
        if(end == 'd'){
            grade = 0;
            return;
        }
        else{
            grade =0;
            grade+=max(abs(flag.i-5),abs(flag.j-5))*20;
            grade+= goldShips*5;
            grade-= silverShips*3;
            if(shipsArray[flag.i-1][flag.j+1]==1)
                grade+=5;
            if(shipsArray[flag.i-1][flag.j-1]==1)
                grade+=5;
            if(shipsArray[flag.i+1][flag.j+1]==1)
                grade+=5;
            if(shipsArray[flag.i+1][flag.j-1]==1)
                grade+=5;
            if(shipsArray[flag.i+1][flag.j+1]==-1||shipsArray[flag.i-1][flag.j+1]==-1||shipsArray[flag.i-1][flag.j-1]==-1||shipsArray[flag.i+1][flag.j-1]==-1)
                grade-=80;


        }

    }
    int getGrade(){
        return grade;
    }
}
