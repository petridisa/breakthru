import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.ByteOrder;

import static java.lang.Math.*;


public class SquareLabels extends JButton {

    public int i;
    int j;
    public SquareLabels(ActionListener l,int i,int j){
//        super(""+i+""+j);
        super(""+(int)(max(abs(5-j),abs(5-i))+(min(abs(5-j),abs(5-i))/2)));
        addActionListener(l);
        this.i = i;
        this.j = j;

    }
    void set()
    {
        setOpaque(true);
        setBackground(Color.WHITE);
        setSize(1,1);
////        setText(""+i+","+j);
//        if(j ==0){
//            setText(""+(11-i));
//        }
//        else if(i==11){
//            setText(""+(char)(j+64));
//        }else
        if(i>=3 && i<=7 && j>=3 && j<=7){
            setBackground(Color.white);
            setBorder(BorderFactory.createLineBorder(Color.black,2));
        }
        else{
            setBackground(Color.white);
            setBorder(BorderFactory.createLineBorder(Color.black,1));
        }
        setHorizontalAlignment(CENTER);
    }




}