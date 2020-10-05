import java.security.AlgorithmParameterGenerator;
import java.util.ArrayList;

public class Agent {

    //1 if Gold, -1 if Silver
    int player;
    State state;
    String playerString;

    Agent(int player,State state){
        this.player = player;
        this.state = state;
        if(player ==1) {
            System.out.println("Agent plays gold");
            playerString = "gold";
        }
        else if(player==-1) {
            System.out.println("Agent plays silver");
            playerString = "silver";
        }


    }

    void start(){

    }

    State play(){
        System.out.println("Agent plays");
        //find all moves
        System.out.println(minimax(state,1,true));

        goes.printArray();

        return goes;
        //find best move
//        findBestState();
//        play the best move
//        playState();

    }
    void playState() {
    }

    void findBestState() {
    }

    ArrayList<State> expandStates() {
        System.out.println("Agent expand states");
        ArrayList<State> states = state.expand(player);
        System.out.println("Agent has "+states.size()+" moves");
        return states;



    }
    State goes;
    int minimax(State current, int depth, boolean maxPlayer){

        int evaluate;
         if(depth ==0 || current.endGame() !='o'){
             current.evaluate();
             return current.getGrade();
         }

         if(maxPlayer){
             int maxEval = -Integer.MAX_VALUE;
             ArrayList<State> children = expandStates();
             for (State child: children){
                 evaluate = minimax(child,depth-1,false);
                 maxEval = Math.max(evaluate,maxEval);
                 if(evaluate == maxEval)
                     goes = child;
             }
             return maxEval;
         }else{
             int minEval = Integer.MAX_VALUE;
             ArrayList<State> children = expandStates();
             for (State child:children){
                 evaluate = minimax(child,depth-1,false);
                 minEval = Math.min(evaluate,minEval);
                 if(evaluate == minEval)
                     goes = child;
             }
             return minEval;
         }
    }


    void chooseMove(){

    }

    void setPlayer(char player){
        this.player = player;
    }
}
