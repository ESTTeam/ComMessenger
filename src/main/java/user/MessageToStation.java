package user;

import javax.swing.*;

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
        userChatWindow.userTextAreaMap.get(userName).append(data + "\n");
    }

    @Override
    public void onUserAdd(String userName) {
        userChatWindow.userTextAreaMap.put(userName, (JTextArea) userChatWindow.stackTextArea.pop());
        userChatWindow.model.addElement(userName);
    }

    @Override
    public void onUserDelete(String userName) {
        int indexRemovingUser = userChatWindow.model.indexOf(userName);
        userChatWindow.usersWindow.clearSelection();
        userChatWindow.chatWindowPane.setViewportView(userChatWindow.chatWindowEmpty);
        userChatWindow.model.remove(indexRemovingUser);
        userChatWindow.stackTextArea.add(userChatWindow.userTextAreaMap.get(userName));
        userChatWindow.userTextAreaMap.remove(userName);
    }

    @Override
    public void onDisconnect() {
        userChatWindow.dispose();
    }

}
