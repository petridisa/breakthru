import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.*;

public class State {
    int goldShips;
    int silverShips;
    Flag flag;
    int grade;
    ArrayList<Gold> goldPieces;
    ArrayList<Silver> silverPieces;
    static boolean gold;

    int[][] lastArray = new int[11][11];
    int[][] shipsArray;
    //0 is empty
    //-1 is silver
    //1 is gold
    //2 is flag
    int[][] initialState = new int[][]{
            {0,0,0,0,0,0,0,0,0,0,0},
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
    public State(State pre, int a, int b, int c, int d){
        shipsArray = new int[11][11];
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                this.shipsArray[i][j] = pre.shipsArray[i][j];
            }
        }
        this.goldShips = pre.goldShips;
        this.silverShips = pre.silverShips;
        this.goldPieces = new ArrayList<>();
        this.silverPieces = new ArrayList<>();
        for(Gold g: pre.goldPieces){
            this.goldPieces.add(new Gold(g.i,g.j));
        }
        for(Silver s:pre.silverPieces){
            this.silverPieces.add(new Silver(s.i,s.j));
        }
        flag = new Flag(pre.flag.i,pre.flag.j);
        this.update(a,b,c,d);
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
        for(int i=0;i<11;i++){
            System.arraycopy(shipsArray[i], 0, lastArray[i], 0, 11);
        }
        if(shipsArray[a][b]==2){
            flag.move(c,d);
        }
        else if(shipsArray[a][b] == 1){
            goldPieces.get(getIndex(a,b,1)).move(c,d);
        }
        else if(shipsArray[a][b] == -1){
            silverPieces.get(getIndex(a,b,-1)).move(c,d);
        }
        if(shipsArray[c][d] == 1) {
            goldPieces.remove(getIndex(c,d,1));
            goldShips--;
        }
        else if(shipsArray[c][d] == -1){
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
        if(silverShips==0)
            return 'g';

        if(draw())
            return 'd';
        return 'o';

    }

    private boolean draw() {
        if(silverShips ==0 && goldShips ==0){
            return true;
        }
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
        //TODOs
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
            int g1 = 10, g2 = 15, g3 = 20, g4 = 20, g5 = 15, g6 = 5, g7 = 5, g8 = 10, g9 = 0, g10 = 0;
            int s1 = 10, s2 = 20, s3 = 20, s4 = 25, s5 = 5, s6 = 5, s7 = 5, s8 = 5, s9 = 0, s10 = 0;

            int f1 = 0, f2 = 0, f3 = 0, f4 = 0 , f5 = 0, f6 = 0, f7 = 0, f8 = 0, f9 = 0, f10 =0;



            f1 = max(abs(flag.i-5),abs(flag.j-5))*20;
            f2 = flagProtected();
            f3 = flagThreatened();
            if(flagHasPath()){
                f4 = 100;
            }
            f5 = (int)(goldShips*8.3) - silverShips*(5);
            f6 = -piecesInPath();
            f7 = goldDistance();
            f8 = -silverEverywhere();
            f9 = -silverBlock();



            if(gold){
                if(f2>0)f1=0;
                grade = g1*f1 + g2*f2 + g3*f3 + g4*f4 + g5*f5 + g6*f6 + g7*f7 +  g8*f8 + g9*f9;
                if(f2!=0)grade-=g1*f1;
            }
            else
                grade = s1*f1 + s2*f2 + s3*f3 + s4*f4 + s5*f5 + s6*f6 + s7*f7 +s8*f8 + s9*f9;
        }
        grade = grade /100;
    }

    private int silverBlock() {
        int sB = 0;
        if(flag.i<10 && shipsArray[flag.i+1][flag.j]==-1)
            sB+=25;
        if(flag.j>0  && shipsArray[flag.i][flag.j-1]==-1)
            sB+=25;
        if(flag.j<10 && shipsArray[flag.i][flag.j+1]==-1)
            sB+=25;
        if(flag.i>0&& shipsArray[flag.i-1][flag.j]==-1)
            sB+=25;
        return sB;
    }

    private int goldDistance() {
        int gR = 0;
        for(Gold g: goldPieces){
            if(abs(g.i-flag.i)+abs(g.j-flag.j)<=3){
                gR += 9;
            }
        }
        return gR;
    }

    public int flagProtected(){
        int fP=0;
        if(flag.i>0  && flag.j<10 &&shipsArray[flag.i-1][flag.j+1]==1)
            fP+=25;
        if(flag.i>0  && flag.j>0 && shipsArray[flag.i-1][flag.j-1]==1)
            fP+=25;
        if(flag.i<10 && flag.j<10 &&shipsArray[flag.i+1][flag.j+1]==1)
            fP+=25;
        if(flag.i<10 && flag.j>0  && shipsArray[flag.i+1][flag.j-1]==1)
            fP+=25;
        return fP;
    }
    public int flagThreatened(){
        int fT = 0;
        int hD = hasDiagonial(flag.i, flag.j, -1);
        if(hD>0){
            fT=-50;
            if (hasDiagonial(flag.i + 1, flag.j + 1, 1)>0 && hD ==1) {
                fT = -100;
            }
            if (hasDiagonial(flag.i + 1, flag.j - 1, 1)>0 && hD ==2) {
                fT = -100;
            }
            if (hasDiagonial(flag.i - 1, flag.j + 1, 1)>0 && hD ==3) {
                fT = -100;
            }
            if (hasDiagonial(flag.i - 1, flag.j - 1, 1)>0 && hD ==4) {
                fT = -100;
            }
        }
        return fT;
    }

    public int hasDiagonial(int i, int j, int player){
       if(i<10 && j<10 && shipsArray[i+1][j+1]==player)return 1;
       if(i<10 && j>0  && shipsArray[i+1][j-1]==player)return 2;
       if(i>0  && j<10 && shipsArray[i-1][j+1]==player)return 3;
       if(i>0  && j>0  && shipsArray[i-1][j-1]==player)return 4;
       return 0;
    }

    int getGrade(){
        return grade;
    }

    public ArrayList<State> expand(int player){

        ArrayList<State> states = new ArrayList<>();
        if(player == 1){
            //Expand flag moves
            states.addAll(flagMoves(flag.i,flag.j));
            states.addAll(pieceCaptures(flag.i,flag.j));
            for(Gold g: goldPieces){
                states.addAll(goldMoves(g.i,g.j,0));
                states.addAll(pieceCaptures(g.i,g.j));

            }
        }
        else if(player == -1){
            for(Silver s: silverPieces){
                states.addAll(silverMoves(s.i,s.j,0));
                states.addAll(pieceCaptures(s.i,s.j));
            }
        }

        return states;
    }

    ArrayList<State> pieceCaptures(int pi, int pj){
        ArrayList<State> states = new ArrayList<>();
        if(pi<10 && pj<10&&checkCapture(pi,pj,pi+1,pj+1)){
            State temp = new State(this,pi,pj,pi+1,pj+1);
            states.add(temp);
        }
        if(pi<10 && pj>0&&checkCapture(pi,pj,pi+1,pj-1)){
            State temp = new State(this,pi,pj,pi+1,pj-1);
            states.add(temp);
        }
        if(pi>0 && pj<10&&checkCapture(pi,pj,pi-1,pj+1)){
            State temp = new State(this,pi,pj,pi-1,pj+1);
            states.add(temp);
        }
        if(pi>0 && pj>0&&checkCapture(pi,pj,pi-1,pj-1)){
            State temp = new State(this,pi,pj,pi-1,pj-1);
            states.add(temp);
        }
        return states;
    }

    ArrayList<State> flagMoves(int pi, int pj) {
        ArrayList<State> states = new ArrayList<>();

//        Can move up
        for(int i=pi-1;i>=0;i--){
            if(shipsArray[i][pj] == 0){

                State temp = new State(this, pi, pj, i, pj);
                states.add(temp);


//                System.out.println(pi+", "+pj+"can go UP");

            }else break;
        }
        //Can move down
        for(int i=pi+1;i<11;i++){
            if(shipsArray[i][pj] ==0) {
                State temp = new State(this,pi,pj,i,pj);
                states.add(temp);
//                System.out.println(pi+", "+pj+"can go DOWN");
            }else break;
        }
        //Can move left
        for(int j=pj-1;j>=0;j--){
            if(shipsArray[pi][j] ==0) {
                State temp = new State(this,pi,pj,pi,j);
                states.add(temp);
//                System.out.println(pi+", "+pj+"can go LEFT");
            }else break;
        }
        //Can move right
        for(int j=pj+1;j<11;j++){
            if(shipsArray[pi][j] ==0) {
                State temp = new State(this,pi,pj,pi,j);
                states.add(temp);
//                System.out.println(pi+", "+pj+"can go RIGHT");
            }else break;
        }

        return states;
    }

    ArrayList<State> goldMoves(int pi, int pj,int count) {
        ArrayList<State> states = new ArrayList<>();

//        Can move up
        for(int i=pi-1;i>=0;i--){
            if(shipsArray[i][pj] == 0){
                State temp = new State(this, pi, pj, i, pj);
                if(count ==0){
                for(Gold g1: temp.goldPieces){
                    if(!(g1.i == i && g1.j == pj)){
                        states.addAll(temp.goldMoves(g1.i,g1.j,1));
                    }
                }}else {
                    states.add(temp);
                }


//                System.out.println(pi+", "+pj+"can go UP");

            }else break;
        }
        //Can move down
        for(int i=pi+1;i<11;i++){
            if(shipsArray[i][pj] ==0) {
                State temp = new State(this,pi,pj,i,pj);
                if(count ==0){
                    for(Gold g1: temp.goldPieces){
                        if(!(g1.i == i && g1.j == pj)){
                            states.addAll(temp.goldMoves(g1.i,g1.j,1));
                        }
                    }}else {
                    states.add(temp);
                }
//                System.out.println(pi+", "+pj+"can go DOWN");
            }else break;
        }
        //Can move left
        for(int j=pj-1;j>=0;j--){
            if(shipsArray[pi][j] ==0) {
                State temp = new State(this,pi,pj,pi,j);
                if(count ==0){
                    for(Gold g1: temp.goldPieces){
                        if(!(g1.i == pi && g1.j == j)){
                            states.addAll(temp.goldMoves(g1.i,g1.j,1));
                        }
                    }}else {
                    states.add(temp);
                }
//                System.out.println(pi+", "+pj+"can go LEFT");
            }else break;
        }
        //Can move right
        for(int j=pj+1;j<11;j++){
            if(shipsArray[pi][j] ==0) {
                State temp = new State(this,pi,pj,pi,j);
                if(count ==0){
                    for(Gold g1: temp.goldPieces){
                        if(!(g1.i == pi && g1.j == j)){
                            states.addAll(temp.goldMoves(g1.i,g1.j,1));
                        }
                    }}else {
                    states.add(temp);
                }
//                System.out.println(pi+", "+pj+"can go RIGHT");
            }else break;
        }

        return states;
    }

    ArrayList<State> silverMoves(int pi, int pj,int count) {
        ArrayList<State> states = new ArrayList<>();

//        Can move up
        for(int i=pi-1;i>=0;i--){
            if(shipsArray[i][pj] == 0){
                State temp = new State(this, pi, pj, i, pj);
                if(count ==0){
                    for(Silver s1: temp.silverPieces){
                        if(!(s1.i == i && s1.j == pj)){
                            states.addAll(temp.silverMoves(s1.i,s1.j,1));
                        }
                    }}else {
                    states.add(temp);
                }


//                System.out.println(pi+", "+pj+"can go UP");

            }else break;
        }
        //Can move down
        for(int i=pi+1;i<11;i++){
            if(shipsArray[i][pj] ==0) {
                State temp = new State(this,pi,pj,i,pj);
                if(count ==0){
                    for(Silver s1: temp.silverPieces){
                        if(!(s1.i == i && s1.j == pj)){
                            states.addAll(temp.silverMoves(s1.i,s1.j,1));
                        }
                    }}else {
                    states.add(temp);
                }
//                System.out.println(pi+", "+pj+"can go DOWN");
            }else break;
        }
        //Can move left
        for(int j=pj-1;j>=0;j--){
            if(shipsArray[pi][j] ==0) {
                State temp = new State(this,pi,pj,pi,j);
                if(count ==0){
                    for(Silver s1: temp.silverPieces){
                        if(!(s1.i == pi && s1.j == j)){
                            states.addAll(temp.silverMoves(s1.i,s1.j,1));
                        }
                    }}else {
                    states.add(temp);
                }
//                System.out.println(pi+", "+pj+"can go LEFT");
            }else break;
        }
        //Can move right
        for(int j=pj+1;j<11;j++){
            if(shipsArray[pi][j] ==0) {
                State temp = new State(this,pi,pj,pi,j);
                if(count ==0){
                    for(Silver s1: temp.silverPieces){
                        if(!(s1.i == pi && s1.j == j)){
                            states.addAll(temp.silverMoves(s1.i,s1.j,1));
                        }
                    }}else {
                    states.add(temp);
                }
//                System.out.println(pi+", "+pj+"can go RIGHT");
            }else break;
        }

        return states;
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

    private int silverEverywhere() {
        int smth = 0;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (shipsArray[i][j] == -1) {
                    smth+=5;
                    j = 11;
                }
            }
        }
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (shipsArray[j][i] == -1) {
                    smth+=5;
                    j = 11;
                }
            }
        }
        return smth;
    }

    public int piecesInPath(){
        int pieces = 0;
        //Flag has up path
        for(int i=flag.i-1;i>=0;i--){
            if(shipsArray[i][flag.j] != 0) {
                pieces+=5;
            }
        }

        //Flag has down path
        for(int i=flag.i+1;i<11;i++){
            if(shipsArray[i][flag.j] != 0) {
                pieces+=5;
            }

        }
        //Flag has left path
        for(int j=flag.j-1;j>=0;j--){
            if(shipsArray[flag.i][j] !=0) {
                pieces+=5;
            }

        }
        //Flag has right path
        for(int j=flag.j+1;j<11;j++){
            if(shipsArray[flag.i][j] !=0) {
                pieces+=5;
            }
        }

        return pieces;
    }

    public String printArray(){
        String s = "";
        for(int[] i: shipsArray){
            for(int j: i){
                if(j==0) {
                    System.out.print("   ");
                    s+="     ";
                }
                else if(j==2) {
                    System.out.print(" F ");
                    s+="  F  ";
                }
                else if(j==1) {
                    System.out.print(" G ");
                    s+="  G  ";
                }
                else if(j==-1) {
                    System.out.print(" S ");
                    s+="  S  ";
                }

            }
            System.out.println();
            s+="\n";
        }
        return s;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public void printFs() {
        int f1 = 0, f2 = 0, f3 = 0, f4 = 0 , f5 = 0, f6 = 0, f7 = 0, f8 = 0, f9 = 0, f10 =0;
        f4 = max(abs(flag.i-5),abs(flag.j-5))*10;
        f3 = goldShips*5 - silverShips*3;
        f3 = f3*2;
        f2 = flagThreatened();
        if(flagHasPath()){
            f1 = 100;
        }
        f5 = flagProtected();
        System.out.println("f1 = " + f1);
        System.out.println("f2 = " + f2);
        System.out.println("f3 = " + f3);
        System.out.println("f4 = " + f4);
        System.out.println("f5 = " + f5);
    }

    public String arrayToString(){
        String s = "";
        for(int[] i: shipsArray){
            for(int j: i){
                if(j==0) {
                    s+="  _  ";
                }
                else if(j==2) {
                    s+="  F  ";
                }
                else if(j==1) {
                    s+="  G  ";
                }
                else if(j==-1) {
                    s+="  S  ";
                }

            }
            s+="\n";
        }
        return s;
    }

    public void setPlayer(boolean gold){
        this.gold = gold;
    }

    public void invert(State s){
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                shipsArray[i][j] = s.shipsArray[i][j];
            }
        }
        goldShips = s.goldShips;
        silverShips = s.silverShips;

    }
}


