
//import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Board extends JFrame implements ActionListener {
    Container contentPane;
    GridLayout gridLayout;
    JPanel panel, panel2;
    int delay,player=-1;
    Timer timerGold, timerSilver;
    Color gold = new Color(212,175,55);
    String movesString;
    boolean picked;
    JTextArea moves;

    boolean goldPlays, startGame;
    
    private SquareLabels[][] labels = new SquareLabels[11][11];


    public Board(){
        setTitle("Breakthru");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gridLayout = new GridLayout(11,11);
        setSize(1000,600);
        panel = new JPanel(gridLayout);
        panel.setBounds(30,30,400,400);
        panel.setVisible(true);
        add(panel);


//        setSize(Toolkit.getDefaultToolkit().getScreenSize());
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setLocationRelativeTo(null);

        putLabels();

        JMenuBar mb = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JMenuItem m1 = new JMenuItem("New Game");
        JMenuItem m2 = new JMenuItem("Exit");
        menu1.add(m1);menu1.add(m2);
        mb.add(menu1);
        setJMenuBar(mb);

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



        long now = System.currentTimeMillis();
        delay = 1000;
        int n = -1;






        //GOLD PLAYER
        {
            Counter counterGold = new Counter(10);
            JLabel goldTLabel = new JLabel(counterGold.toString(), SwingConstants.CENTER);
            goldTLabel.setBounds(40, 740, 150, 40);
            add(goldTLabel);
            JLabel goldTitle = new JLabel("GOLD'S TIMER", SwingConstants.CENTER);
            goldTitle.setBounds(40,700,150,40);
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
                        }
                    }
                }
            });
            timerGold.start();
        }

        //SILVER PLAYER
        {
            Counter counterSilver = new Counter(10);
            JLabel silverTLabel = new JLabel(counterSilver.toString(), SwingConstants.CENTER);
            silverTLabel.setBounds(40, 830, 150, 40);
            add(silverTLabel);
            JLabel silverTitle = new JLabel("SILVER'S TIMER", SwingConstants.CENTER);
            silverTitle.setBounds(40,790,150,40);
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
                        }
                    }
                }
            });
            timerSilver.start();
        }

        Component frame = this;

        JLabel chooseTitle = new JLabel("Choose Player");
        JLabel firstTitle = new JLabel("Which player plays first");

        chooseTitle.setBounds(200,700,200,40);
        firstTitle.setBounds(200,860,200,40);
        JRadioButton r1 = new JRadioButton("A) Gold");
        JRadioButton r2 = new JRadioButton("B) Silver");
        JRadioButton r3 = new JRadioButton("A) Gold");
        JRadioButton r4 = new JRadioButton("B) Silver");
        r1.setBounds(200,740,200,40);
        r2.setBounds(200,780,200,40);
        r3.setBounds(200,900,200,40);
        r4.setBounds(200,940,200,40);

        ButtonGroup bgPLayer = new ButtonGroup();
        ButtonGroup bgFirst = new ButtonGroup();

        bgPLayer.add(r1);
        bgPLayer.add(r2);
        bgFirst.add(r3);
        bgFirst.add(r4);

        add(r1);add(r2);add(r3);add(r4);
        add(chooseTitle);add(firstTitle);

        JButton startButton = new JButton("START GAME");
        startButton.setBounds(400,700,200,100);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame = true;
//                player = JOptionPane.showOptionDialog(frame,"Choose which player plays first",
//                        "First Player",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Gold Player","Silver Player"},
//                        null);
                startButton.setEnabled(false);
            }
        });
        add(startButton);


        loadState(new State());
        if(checkEndgame(new State())) System.out.println("endgame");

        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setVisible(true);

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
                else if(s.shipsArray[i][j] ==2)
                    labels[i][j].setBackground(gold);
            }
        }

    }

    public boolean checkEndgame(State s){
        for(int i=0;i<11;i++){
            if(s.shipsArray[0][i] == 2 || s.shipsArray[i][0] ==2){
                return true;
            }
            if(s.flagCaptured())
                return true;
        }
        return false;
    }

    SquareLabels btn,btn2;
    Color pre;
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(!picked) {
            //check if it is the right turn
            btn = (SquareLabels) ae.getSource();
            if(btn.getBackground()==Color.white)
                JOptionPane.showMessageDialog(this,"You can't move this piece");
            else{
                pre = btn.getBackground();
                btn.setBackground(Color.blue);
                picked = true;

            }
        }
        else {
            //check if move is valid
            btn2 = (SquareLabels) ae.getSource();
            btn.setBackground(pre);
            move(btn.i,btn.j,btn2.i,btn2.j);
            picked = false;


        }
    }


    public boolean move(int si, int sj, int di, int dj){

        //show the move on GUI
        labels[di][dj].setBackground(labels[si][sj].getBackground());
        btn.setBackground(Color.white);

        //display the move on scroll
        String newMove = chessNotation(si,sj) +" -> "+ chessNotation(di,dj);
        newMove = moves.getText() +System.lineSeparator() + newMove ;
        moves.setText(newMove);
        //stop the timer
        //update state
        //turn move
        return true;
    }

    public String chessNotation(int i,int j){
        String k = "";
        k = k+(char)(j+65)+"";
        k = k+(11-i)+"";

        return k;
    }



}
