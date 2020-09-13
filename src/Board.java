
//import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Board extends JFrame {
    Container contentPane;
    GridLayout gridLayout;
    JPanel panel, panel2;
    
    private SquareLabels[][] labels = new SquareLabels[12][12];


    public Board(){
        setTitle("Breakthru");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gridLayout = new GridLayout(12,12);
//        setSize(400,400);
        panel = new JPanel(gridLayout);
        panel.setBounds(40,80,600,600);
        panel.setVisible(true);
        add(panel);
//
//        turn = new JLabel("Gold Player Plays!!");
//        turn.setBounds(700,80,200,80);
//        add(turn);

//        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setLocationRelativeTo(null);

        putLabels();

        JLabel moves = new JLabel();
        moves.setPreferredSize(new Dimension(200,1000));
        moves.setVerticalAlignment(1);
        moves.setText("A5->A6");
        moves.setBackground(Color.white);
        moves.setVisible(true);

        JLabel movesTitle = new JLabel("MOVES", SwingConstants.CENTER);
        movesTitle.setBounds(700,80,300,40);
        movesTitle.setVisible(true);
        add(movesTitle);

        JScrollPane scroll = new JScrollPane(moves);
        scroll.setBounds(700,120,300,560);
        scroll.setBackground(Color.white);
        add(scroll);

        long now = System.currentTimeMillis();
        Counter counter =new Counter(0,10);
        JLabel timer = new JLabel(counter.toString(),SwingConstants.CENTER);
        timer.setBounds(1100,80,100,40);


        add(timer);
        //GOLD PLAYER
        new Timer(1000,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println(counter.toString());
                if(!counter.isZero()){
                    counter.decrease();
                    timer.setText(counter.toString());
                }else{
                    JOptionPane.showMessageDialog(null, "Time is up");

                }
            }
        }).start();





        setVisible(true);
    }
    public void putLabels(){
        int row = -1,i=0;
        for(i=0;i<12;i++){
            for (int j=0;j<12;j++){
            labels[i][j] = new SquareLabels();
            if(i % 12 == 0) row ++; // increment row number
             labels[i][j].set(i,j);
             panel.add(labels[i][j]);



            }}
        }
    }


