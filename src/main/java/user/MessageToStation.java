package user;

/**
 * Created by Igor on 11.05.16.
 */
public class MessageToStation implements OnMessageReceiveListener {
    public UserChatWindow userChatWindow;
    public MessageToStation (UserChatWindow userChatWindow) {
        this.userChatWindow = userChatWindow;
    }
    @Override
    public void onMessageReceive(String userName, String data) {
        userChatWindow.chatWindow.append(data +"\n");
    }

    @Override
    public void onUserAdd(String userName) {
        userChatWindow.model.addElement(userName);
    }

    @Override
    public void onUserDelete(String userName) {
        int indexRemovingUser = userChatWindow.model.indexOf(userName);
        userChatWindow.model.remove(indexRemovingUser);
    }

}
