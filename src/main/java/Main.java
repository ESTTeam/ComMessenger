import link.DataLinkLayer;
import link.NoSuchUserException;
import user.OnMessageReceiveListener;

import static java.lang.Thread.sleep;

public class Main {
    static OnMessageReceiveListener onMessageReceiveListener = new OnMessageReceiveListener() {
        @Override
        public void onMessageReceive(String data) {}

        @Override
        public void onUserAdd(String userName) {}

        @Override
        public void onUserDelete(String userName) {

        }
    };

    public static void main(String[] args) {

//        DataLinkLayer ws = new DataLinkLayer(onMessageReceiveListener, "User 1", "COM11", "COM12");
//        DataLinkLayer ws = new DataLinkLayer(onMessageReceiveListener, "User 2", "COM21", "COM22");
//        DataLinkLayer ws = new DataLinkLayer(onMessageReceiveListener, "User 3", "COM31", "COM32");
//        DataLinkLayer ws = new DataLinkLayer(onMessageReceiveListener, "User 4", "COM41", "COM42");
        DataLinkLayer ws = new DataLinkLayer(onMessageReceiveListener, "User 5", "COM51", "COM52");

        try {
            sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ws.sendDataTo("User 3", "Data for 3 ws");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }

        try {
            ws.sendDataTo("User 2", "Data for 2 ws");
        } catch (NoSuchUserException e) {
            System.out.println("No such user");
        }
    }
}
