import java.util.ArrayList;

import static java.lang.Math.*;

public class State {
    int goldShips;
    int silverShips;
    Flag flag;
    int grade;
    ArrayList<Gold> goldPieces;
    ArrayList<Silver> silverPieces;


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

    public State(int[][] shipsArray){
        this();
        this.shipsArray = shipsArray;
    }

    public State(){
        shipsArray = initialState;
        goldShips = 12;
        silverShips = 20;
        grade = 0;
        goldPieces = new ArrayList<>(goldShips);
        silverPieces = new ArrayList<>(silverShips);
        initialize();
    }

    void initialize(){
        flag = new Flag(5,5);
        for(int i=0; i<11; i++){
            for(int j=0; j<11;j++){
                if(shipsArray[i][j] == 1) {
                    goldPieces.add(new Gold(i, j));
                }
                else if(shipsArray[i][j] == -1){
                    silverPieces.add(new Silver(i,j));
                }
            }
        }
    }

    public void update(int a, int b, int c, int d){

        System.out.println("i"+a+" j"+ b+" i"+ c+" j"+ d);
        if(shipsArray[a][b]==2){
            flag.move(c,d);
        }
        else if(shipsArray[a][b] == 1){
            goldPieces.get(getIndex(a,b,1)).move(c,d);
        }
        else if(shipsArray[a][b] == -1){
            silverPieces.get(0).move(c,d);
        }
        if(shipsArray[c][d] == 1) {
            goldPieces.remove(getIndex(c,d,1));
            goldShips--;
        }
        else if(shipsArray[c][d] == -1){
            System.out.println("a = " + a + ", b = " + b + ", c = " + c + ", d = " + d);
            silverPieces.remove(getIndex(c,d,-1));
            silverShips--;
        }
        else if(shipsArray[c][d] == 2){
            flag.captured = true;
        }
        shipsArray[c][d] = shipsArray[a][b];
        shipsArray[a][b] = 0;

    }

    private int getIndex(int a, int b, int i) {
        if(i==1){
            for(Gold g:goldPieces){
                if(g.i == a && g.j == b){
                    return goldPieces.indexOf(g);
                }
            }
        }
        if(i==-1){
            for(Silver s:silverPieces){
                System.out.println(s);
                if(s.i == a && s.j == b){
                    return silverPieces.indexOf(s);
                }
            }
        }
        return -1;
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
            grade = 200;
            return;
        }
        if(end == 's'){
            grade = -200;
            return;
        }
        grade = 0;
        if(end == 'd'){
            return;
        }
        else{
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
            if(flagHasPath()){
                System.out.println("Flag has path!");
                grade+=80;
            }




        }

    }

    int getGrade(){
        return grade;
    }

    public ArrayList<Move> expand(int player){

        ArrayList<Move> moves = new ArrayList<>();
        System.out.println(moves.size());
        if(player == 1){
            //Expand flag moves
            moves.addAll(pieceMoves(flag.i,flag.j));
            for(Gold g: goldPieces){
                moves.addAll(pieceMoves(g.i,g.j));
            }
        }
        else if(player == -1){
            for(Silver s: silverPieces){
                moves.addAll(pieceMoves(s.i,s.j));
            }
        }

        return moves;
    }
    ArrayList<Move> pieceMoves(int pi, int pj){
        ArrayList<Move> moves = new ArrayList<>();

//        Can move on up
        for(int i=pi-1;i>=0;i--){
            if(shipsArray[i][pj] == 0) {
                moves.add(new Move(this,new Cell(pi,pj),new Cell(i,pj)));

            }else break;

        }

        //Can move on down
        for(int i=pi+1;i<11;i++){
            if(shipsArray[i][pj] ==0) {
                moves.add(new Move(this,new Cell(pi,pj),new Cell(i,pj)));
            }else break;
        }
        //Can move left
        for(int j=pj-1;j>=0;j--){
            if(shipsArray[pi][j] ==0) {
                moves.add(new Move(this,new Cell(pi,pj),new Cell(pi,j)));
            }else break;

        }
        //Can move right
        for(int j=pj+1;j<11;j++){
            if(shipsArray[pi][j] ==0) {
                moves.add(new Move(this,new Cell(pi,pj),new Cell(pi,j)));
            }else break;
        }


        return moves;
    }

    public boolean flagHasPath(){
        boolean has = false;
        //Flag has up path
        for(int i=flag.i-1;i>=0;i--){
            if(shipsArray[i][flag.j] != 0) {
                break;
            }
            if(i==0){
                has = true;
            }
        }

        //Flag has down path
        for(int i=flag.i+1;i<11;i++){
            if(shipsArray[i][flag.j] != 0) {
                break;
            }
            if(i==10){
                has = true;
            }
        }
        //Flag has left path
        for(int j=flag.j-1;j>=0;j--){
            if(shipsArray[flag.i][j] !=0) {
                break;
            }
            if(j==0){
                has = true;
            }

        }
        //Flag has right path
        for(int j=flag.j+1;j<11;j++){
            if(shipsArray[flag.i][j] !=0) {
                break;
            }
            if(j==10){
                has = true;
            }
        }

        return has;
    }

    public void printArray(){
        for(int[] i: shipsArray){
            for(int j: i){
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

}


