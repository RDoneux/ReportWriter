package frontend;

import javax.swing.*;
import java.awt.*;

public class Signature extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Signature() {
        setBackground(new Color(240, 240, 240));
        setLayout(new GridBagLayout());
        JLabel signature = new JLabel("created by Rob Doneux");
        signature.setFont(new Font("Lucida Console", Font.PLAIN, 8));
        signature.setForeground(Color.LIGHT_GRAY);
        add(signature);
    }

}
