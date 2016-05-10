package user;
import link.DataLinkLayer;

public class UserLayer implements OnMessageReceiveListener {
    @Override
    public void onMessageReceive(String data) {
        // TODO
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 UserFormSettings userFormSettings = new UserFormSettings();
                // UserChatWindow userChatWindow = new UserChatWindow();
            }
        });

    }
}