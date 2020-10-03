import java.security.AlgorithmParameterGenerator;
import java.util.ArrayList;

public class Agent {

    //1 if Gold, -1 if Silver
    int player;
    State state;

    Agent(int player,State state){
        this.player = player;
        this.state = state;
        if(player ==1)
            System.out.println("Agent plays gold");
        else if(player==-1)
            System.out.println("Agent plays silver");

    }

    void start(){

    }

    void play(){
        System.out.println("Agent" + player + "plays");
        //find all moves
        expandStates();
        //find best move
        findBestState();
        //play the best move
        playState();

    }

    private void playState() {
    }

    private void findBestState() {
    }

    private void expandStates() {
        System.out.println("Agent expand states");
//        ArrayList<Move> moves = state.expand(player);
//        System.out.println("Agent has "+moves.size()+" moves");


    }


    void chooseMove(){

    }

    void setPlayer(char player){
        this.player = player;
    }
}
