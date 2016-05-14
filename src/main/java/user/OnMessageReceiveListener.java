package user;

public interface OnMessageReceiveListener {
    void onMessageReceive(String userName, String data);
    void onUserAdd(String userName);
    void onUserDelete(String userName);
    void onDisconnect();
}
