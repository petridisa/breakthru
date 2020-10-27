
//import javax.swing.*;
import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Board extends JFrame implements ActionListener {
    Container contentPane;
    GridLayout gridLayout;
    JPanel panel, panelH, panelV;
    int delay,player=-1,agentPlayer=-1;
    Timer timerGold, timerSilver;
    Color gold = new Color(212,175,55);
    String movesString;
    boolean picked;
    JTextArea moves,moveAI;
    State s;
    Component frame;
    String winner;
    Agent agent;

    boolean goldPlays, startGame;
    
    static SquareLabels[][] labels = new SquareLabels[11][11];


    public Board(){
        setTitle("Breakthru");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gridLayout = new GridLayout(11,11);
        GridLayout gridH = new GridLayout(1,11), gridV = new GridLayout(11,1);
        setSize(1000,700);
        panel = new JPanel(gridLayout);
        panel.setBounds(30,30,400,400);
        panel.setVisible(true);
        add(panel);
        frame = this;
        winner = "";

        setLayout(null);
        setLocationRelativeTo(null);

        panelH = new JPanel(gridH);
        panelV = new JPanel(gridV);
        panelV.setBounds(10,30,20,400);
        panelH.setBounds(42,430,400,20);
        panelH.setVisible(true);
        panelV.setVisible(true);
        add(panelH);
        add(panelV);
        addChessNotation();
        setLayout(null);
        setLocationRelativeTo(null);

        putLabels();
        setMenu();

        moveAI = new JTextArea();
        moveAI.setEditable(false);
        moveAI.setBackground(Color.white);
        moveAI.setVisible(true);
        JLabel moveAITitle = new JLabel("AGENT'S MOVE", SwingConstants.CENTER);
        moveAITitle.setBounds(720,30,250,20);
        moveAITitle.setVisible(true);
        add(moveAITitle);
        moveAI.setBounds(720,50,250,250);
        add(moveAI);

        moves = new JTextArea("Players Moves:");
        moves.setPreferredSize(new Dimension(200,1000));
        moves.setEditable(false);
        moves.setBackground(Color.white);
        moves.setVisible(true);

        JLabel movesTitle = new JLabel("MOVES", SwingConstants.CENTER);
        movesTitle.setBounds(450,30,250,20);
        movesTitle.setVisible(true);
        add(movesTitle);

        JScrollPane scroll = new JScrollPane(moves);
        scroll.setBounds(450,50,250,380);
        scroll.setBackground(Color.white);
        add(scroll);

        addTimers();
        addRadioButtons();

        s = new State();
        s.evaluate();
        System.out.println(s.grade);
        loadState(s);
//        if(checkEndgame(new State())) System.out.println("endgame");

        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);


    }

    private void setMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JMenuItem m1 = new JMenuItem("New Game");
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO new game
            }
        });
        JMenuItem m2 = new JMenuItem("Exit");
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent((Window) frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        menu1.add(m1);menu1.add(m2);
        mb.add(menu1);
        setJMenuBar(mb);
    }

    private void addRadioButtons() {

        JLabel chooseTitle = new JLabel("Choose Player");
        JLabel firstTitle = new JLabel("Which player plays first");

        chooseTitle.setBounds(150,480,200,20);
        firstTitle.setBounds(150,570,200,20);
        JRadioButton r1 = new JRadioButton("A) Gold");
        JRadioButton r2 = new JRadioButton("B) Silver");
        JRadioButton r3 = new JRadioButton("A) Gold");
        JRadioButton r4 = new JRadioButton("B) Silver");
        r1.setBounds(150,500,200,20);
        r2.setBounds(150,520,200,20);
        r3.setBounds(150,590,200,20);
        r4.setBounds(150,610,200,20);

        ButtonGroup bgPLayer = new ButtonGroup();
        ButtonGroup bgFirst = new ButtonGroup();

        bgPLayer.add(r1);
        bgPLayer.add(r2);
        bgFirst.add(r3);
        bgFirst.add(r4);

        add(r1);add(r2);add(r3);add(r4);
        add(chooseTitle);add(firstTitle);

        JButton startButton = new JButton("START GAME");
        startButton.setBounds(350,470,150,80);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Check if radio buttons are selected
                startGame = true;
//                player = JOptionPane.showOptionDialog(frame,"Choose which player plays first",
//                        "First Player",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Gold Player","Silver Player"},
//                        null);
                if(r1.isSelected()) {
                    s.setPlayer(true);
                    agentPlayer = 1;
                }
                else if(r2.isSelected()) {
                    s.setPlayer(false);
                    agentPlayer = -1;
                }
                player = r3.isSelected()?0:1;
                startButton.setEnabled(false);
                createAgent();
                agentPlay();


            }
        });
        add(startButton);
    }

    private void agentPlay() {
        if(player == 0 && agentPlayer ==1 || player == 1 && agentPlayer ==-1) {
            now = System.currentTimeMillis();
            s.printArray();
            State agentsMove = agent.play(s);
            agentsMove.printArray();
//            loadState(s);
            moveAI.setText(agentsMove.arrayToString());
            long timeLong = (System.currentTimeMillis()-now);
            int timePassed = (int)(timeLong/1000);
            System.out.println(timePassed);
            if(player==0 && !counterGold.isZero()){
                counterGold.decrease(timePassed);
                goldTLabel.setText(counterGold.toString());
            }
            if(player==1 && !counterSilver.isZero()){
                counterSilver.decrease(timePassed);
                silverTLabel.setText(counterSilver.toString());
            }
//            player = 1 - player;
        }

    }


    private void createAgent() {
        agent = new Agent(agentPlayer,s);
    }
    long now;
    Counter counterGold;
    Counter counterSilver;
    JLabel goldTLabel;
    JLabel silverTLabel;

    private void exportFile(String s,String winner){
        String agent = "";
        if(agentPlayer == 1){
            agent = "Gold";
        }else{
            agent = "Silver";
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        try{
            PrintWriter printWriter = new PrintWriter("Game's moves at "+ dtf.format(now) +
                    " with AI as "+ agent);
             printWriter.println(s);
            printWriter.close();
        }
        catch (FileNotFoundException e){

        }
    }

    private void addTimers() {
        long now = System.currentTimeMillis();
        delay = 1000;
        int n = -1;

        //GOLD PLAYER
        {
            counterGold = new Counter(10);
            goldTLabel = new JLabel(counterGold.toString(), SwingConstants.CENTER);
            goldTLabel.setBounds(30, 520, 110, 20);
            add(goldTLabel);
            JLabel goldTitle = new JLabel("GOLD'S TIMER", SwingConstants.CENTER);
            goldTitle.setBounds(30,500,110,20);
            add(goldTitle);
            timerGold = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startGame && player == 0) {
                        if (!counterGold.isZero()) {
                            counterGold.decrease();
                            goldTLabel.setText(counterGold.toString());
                        } else {
                            JOptionPane.showMessageDialog(null, "Game over for Gold Player");
                            timerGold.stop();
                            String s = moves.getText();
                            exportFile(s,"silver");
                        }
                    }
                }
            });
            timerGold.start();
        }

        //SILVER PLAYER
        {
            counterSilver = new Counter(10);
            silverTLabel = new JLabel(counterSilver.toString(), SwingConstants.CENTER);
            silverTLabel.setBounds(30, 590, 110, 20);
            add(silverTLabel);
            JLabel silverTitle = new JLabel("SILVER'S TIMER", SwingConstants.CENTER);
            silverTitle.setBounds(30,570,110,20);
            add(silverTitle);
            timerSilver = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startGame && player ==1) {
                        if (!counterSilver.isZero()) {
                            counterSilver.decrease();
                            silverTLabel.setText(counterSilver.toString());
                        } else {
                            JOptionPane.showMessageDialog(null, "Game over for Silver Player");
                            timerSilver.stop();
                            String s = moves.getText();
                            exportFile(s, "gold");
                        }
                    }
                }
            });
            timerSilver.start();
        }
    }

    //Put the labels in the board
    public void putLabels(){
        int row = -1,i=0;
        for(i=0;i<11;i++){
            for (int j=0;j<11;j++){
            labels[i][j] = new SquareLabels(this,i,j);
            if(i % 12 == 0) row ++; // increment row number
             labels[i][j].set();
             panel.add(labels[i][j]);



            }}
    }

    public void loadState(State s){
        for(int i=0;i<11;i++){
            for(int j =0;j<11;j++){
                if(s.shipsArray[i][j] == -1)
                    labels[i][j].setBackground(Color.lightGray);
                else if(s.shipsArray[i][j] == 1)
                    labels[i][j].setBackground(Color.YELLOW);
                else if(s.shipsArray[i][j] == 2)
                    labels[i][j].setBackground(gold);
                else if(s.shipsArray[i][j] == 0)
                    labels[i][j].setBackground(Color.white);
            }
        }

    }

    public boolean checkEndgame(State s){
        switch (s.endGame()){
            case 'g':
                winner = "Gold";
                break;
            case 's':
                winner = "Silver";
                break;
            case'd':
                winner = "Draw";
                break;
            default:
                return false;

        }
        return true;

    }


    SquareLabels btn,btn2;
    Color pre;
    int movesRemaining = 2;
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(!picked) {

            btn = (SquareLabels) ae.getSource();
            if(btn.getBackground()==Color.white) {
                JOptionPane.showMessageDialog(this, "You can't move this piece");
                return;
            }else if(notHisTurn(btn.getBackground())) {
                JOptionPane.showMessageDialog(this, "Not his turn");
                return;
            }else if(s.isFlag(btn.i,btn.j)&& movesRemaining!=2){
                JOptionPane.showMessageDialog(this, "Can't move the flag");
                return;
            }
            else{
                pre = btn.getBackground();
                btn.setBackground(Color.blue);
                picked = true;
                return;

            }
        }
        else {

            btn2 = (SquareLabels) ae.getSource();

            if(btn.equals(btn2)){
                btn.setBackground(pre);
                picked = false;
                return;
            }
            if(moveValid(btn,btn2)){
                btn.setBackground(pre);
                move(btn.i, btn.j, btn2.i, btn2.j);
                picked = false;

            }
            //Capture
            else if(s.checkCapture(btn.i, btn.j, btn2.i, btn2.j) && movesRemaining==2){
                btn.setBackground(pre);
                movesRemaining--;
                move(btn.i, btn.j, btn2.i, btn2.j);

                picked = false;
                if(s.flagCaptured()){
                    JOptionPane.showMessageDialog(this, "Game Over. Flag Captured. Silver Won!");
                    String s = moves.getText();
                    exportFile(s, "silver");
                    System.exit(0);
                    return;

                }



            }

        }
        if(checkEndgame(s)){
            JOptionPane.showMessageDialog(this,"The game ended "+winner+" won!");
            String s = moves.getText();
            exportFile(s,winner);
        }
    }

    private boolean notHisTurn(Color background) {
        //Not silver's turn, silver's piece
        if(player !=1 && background.equals(Color.lightGray))
            return true;
        //Not Gold' turn, gold's piece
        if(player !=0 && background.equals(Color.YELLOW) || player !=0 && background.equals(gold))
            return true;
        return false;

    }

    boolean moveValid(SquareLabels btn, SquareLabels btn2){

        if(btn.i!=btn2.i && btn.j != btn2.j )
            return false;
        if(!s.checkValid(btn.i,btn.j,btn2.i,btn2.j))
            return false;
        return true;
    }


    public boolean move(int si, int sj, int di, int dj){

        //Move or Capture
        String mOc ="Move: ";
        if(Math.abs(sj-dj)==1 && Math.abs(si-di)==1){
            mOc = "Capture: ";
        }
        //show the move on GUI
        labels[di][dj].setBackground(labels[si][sj].getBackground());
        btn.setBackground(Color.white);

        //Gold or Silver
        String piece = "Gold";
        if(s.shipsArray[si][sj] == -1)
            piece = "Silver";

        //display the move on scroll
        String newMove = piece +" "+mOc + chessNotation(si,sj) +" -> "+ chessNotation(di,dj);
        newMove = moves.getText() +System.lineSeparator() + newMove ;
        moves.setText(newMove);

        //stop the timer, The timer stops on lines 146,172

        //update state
        s.update(si,sj,di,dj);
        //checkEndgames

        //turn move
        movesRemaining--;
        if(labels[di][dj].getBackground()==gold){
            movesRemaining = 0;
        }
        if(movesRemaining==0){
            player = 1-player;
            movesRemaining=2;
            s.evaluate();
            System.out.println(s.getGrade());
            agentPlay();
        }

        return true;
    }


    public String chessNotation(int i,int j){
        String k = "";
        k = k+(char)(j+65)+"";
        k = k+(11-i)+"";

        return k;
    }

    Label notationH[], notationV[];
    public void addChessNotation() {
        notationH = new Label[11];
        notationV = new Label[11];
        for (int i = 0; i < 11; i++) {
            notationH[i] = new Label();
            notationH[i].setText((char) (i + 65) + "");
            panelH.add(notationH[i]);
            notationV[i] = new Label();
            notationV[i].setText((11 - i) + "");
            panelV.add(notationV[i]);

        }
    }





}
