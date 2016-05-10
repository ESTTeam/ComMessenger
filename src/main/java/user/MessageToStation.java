package user;

/**
 * Created by Igor on 11.05.16.
 */
public class MessageToStation implements OnMessageReceiveListener {
    UserChatWindow userChatWindow;
    public MessageToStation () {
        userChatWindow = new UserChatWindow();
    }
    @Override
    public void onMessageReceive(String data) {
        userChatWindow.chatWindow.append(data +"\n");
    }

    @Override
    public void onUserAdd(String userName) {
        userChatWindow.usersWindow.append(userName + "\n");
    }

    @Override
    public void onUserDelete(String userName) {
        //TODO
    }

}
