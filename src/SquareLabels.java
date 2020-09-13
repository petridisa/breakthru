import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.nio.ByteOrder;

public class SquareLabels extends JLabel {

    public SquareLabels(){
        super();

    }
    void set(int i,int j)
    {
        setOpaque(true);
        setBackground(Color.WHITE);
        setSize(1,1);
//        setText(""+i+","+j);
        if(j ==0){
            setText(""+(11-i));
        }
        else if(i==11){
            setText(""+(char)(j+64));
        }
        else if(i>=3 && i<=7 && j>=4 && j<=8){
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