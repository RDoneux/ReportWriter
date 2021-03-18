package frontend;

import javax.swing.*;
import java.awt.*;

public class Signature extends JPanel {

    public Signature(){
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        JLabel signature = new JLabel("created by Rob Doneux");
        signature.setFont(new Font("Lucida Console", Font.PLAIN, 8));
        signature.setForeground(Color.LIGHT_GRAY);
        add(signature);
    }

}
