import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;

public class Agent {

    //1 if Gold, -1 if Silver
    int player;
    String playerString;
    boolean maxPlayer;
    int INF = 1000000;
    int depth = 1;
    int shipsArray[][] = new int[11][11];
    long start;
    static final int timeoutMiliseconds = 7000;
    boolean timeout;
    boolean useID;
    Agent(int player,State state){
        this.player = player;
        if(player == 1) {
            System.out.println("Agent plays gold");
            playerString = "gold";
            maxPlayer = true;
        }
        else if(player == -1) {
            System.out.println("Agent plays silver");
            playerString = "silver";
            maxPlayer = false;
        }


    }

    State play(State s){
        System.out.println("Agent plays");
        useID = false;
        if(maxPlayer)
            maximizer(s,depth,-300,300);
        else
            minimizer(s,depth,-300,300);

        if(useID){
            iterativeDeepening(s, depth);
        }
        return new State(shipsArray);
    }

    private void iterativeDeepening(State s,int depth) {
        timeout = false;
        start = System.currentTimeMillis();
        for(int d=0;;d++){
            if(d>0){
                System.out.println("ID Search completed with depth "+d);
            }
            if(maxPlayer)
                maximizer(s,d,-300,300);
            else
                minimizer(s,d,-300,300);
            if(timeout){
               return;
            }
        }
    }

    ArrayList<State> expandStates(State current, Boolean maxPlayer) {
        if(maxPlayer) {
            player = 1;
        }else {
            player = -1;
        }
        return current.expand(player);



    }
    State goes;
    int minimax(State current, int depth, boolean maxPlayer){

        int evaluate;
         if(depth ==0 || current.endGame() !='o'){
             current.evaluate();
             return current.getGrade();
         }

         if(maxPlayer){
             int maxEval = -INF;
             ArrayList<State> children = expandStates(current,true);
             for (State child: children){
                 evaluate = minimax(child,depth-1,false);
                 maxEval = Math.max(evaluate,maxEval);
                 if(evaluate == maxEval)
                     goes = child;
             }
             return maxEval;
         }else{
             int minEval = INF;
             ArrayList<State> children = expandStates(current,false);
             for (State child:children){
                 evaluate = minimax(child,depth-1,true);
                 minEval = Math.min(evaluate,minEval);
                 if(evaluate == minEval)
                     goes = child;
             }
             return minEval;
         }
    }

    int maximizer(State current,int depth, int alpha, int beta) {
        if(useID){
            if (System.currentTimeMillis() - start > timeoutMiliseconds) {
                timeout = true;
                return alpha;
            }
        }
        if (depth == 0 || current.endGame() != 'o') {
            current.evaluate();
            return current.getGrade();
        }
        ArrayList<State> children = expandStates(current, true);
        for (State child : children) {
            int evaluate = minimizer(child, depth - 1, alpha, beta);

            if (evaluate > alpha) {
                alpha = evaluate;
                if(maxPlayer){
                    setShipsArray(child.shipsArray);

//                    printArray();
//                    System.out.println(evaluate);
//                    child.printFeatures();
                }

            }
//            child.invert(current);
            if (alpha >= beta) {
                return alpha;
            }

        }
        return alpha;
    }
    int minimizer(State current, int depth, int alpha, int beta){
        if(useID){
            if (System.currentTimeMillis() - start > timeoutMiliseconds) {
                timeout = true;
                return beta;
            }
        }
        if(depth ==0 || current.endGame() !='o'){
            current.evaluate();
            return current.getGrade();
        }
        ArrayList<State> children = expandStates(current, false);
        for(State child : children){
            int evaluate = maximizer(child,depth-1,alpha,beta);
//            child.invert(current);
//            if(player == 1) {
//                child.invert(current);
//            }
            if(evaluate < beta){
                beta = evaluate;
                if(!maxPlayer) {
                   setShipsArray(child.shipsArray);
//                    printArray();
//                    System.out.println(evaluate);
//                    child.printFeatures();
                }
//                child.printArray();
//                System.out.println(evaluate);

            }
//            child.invert(current);
//            if(player ==  -1) {
//                child.invert(current);
//            }
            if(alpha >= beta){
                return beta;
            }

        }
        return beta;
    }


    void chooseMove(){

    }

    void setPlayer(char player){
        this.player = player;
    }

    void setShipsArray(int [][] shipsArray){
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                this.shipsArray[i][j] = shipsArray[i][j];
            }
        }
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
}
