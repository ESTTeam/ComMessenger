package user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Igor on 09.05.16.
 */
public class UserChatWindow extends JFrame{
    public UserChatWindow() {
        super("Чат");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 440));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
