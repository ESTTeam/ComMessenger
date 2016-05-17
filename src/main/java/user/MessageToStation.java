package user;

import javax.swing.*;

import static javax.swing.JOptionPane.showMessageDialog;

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
        userChatWindow.userTextAreaMap.get(userName).setText("");
        userChatWindow.userTextAreaMap.remove(userName);
    }

    @Override
    public void onDisconnect() {
        userChatWindow.dispose();
    }

    @Override
    public void onPortParametersChanged(int baudRate, int dataBits, int stopBits, int parity) {
        String parityString = null;
        switch (parity) {
            case 0:
                parityString = "None";
                break;
            case 1:
                parityString = "Odd";
                break;
            case 2:
                parityString = "Even";
                break;
        }
        showMessageDialog(null, "Параметры COM- порта были изменены:" +
                "" + "\n" + "скорость = " + baudRate + "\n" + "биты данных = " + dataBits
                +"\n" + "стоп биты = " + stopBits + "\n" + "биты четности " + parityString);

    }

    @Override
    public void onDSRLost() {}

}
