import java.security.AlgorithmParameterGenerator;

public class Agent {

    //0 if Gold, 1 if Silver
    int player;
    Agent(int player){
        this.player = player;

    }

    void start(){

    }

    void play(){
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
    }


    void chooseMove(){

    }

    void setPlayer(char player){
        this.player = player;
    }
}
