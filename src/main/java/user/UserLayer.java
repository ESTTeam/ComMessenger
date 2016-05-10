package user;
import link.DataLinkLayer;

public class UserLayer implements OnMessageReceiveListener {
    @Override
    public void onMessageReceive(String data) {
        // TODO
    }

    @Override
    public void onUserAdd(String userName) {

    }

    @Override
    public void onUserDelete(String userName) {

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 UserFormSettings userFormSettings = new UserFormSettings();
            }
        });

    }
}