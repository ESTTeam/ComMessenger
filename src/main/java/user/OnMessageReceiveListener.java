package user;

public interface OnMessageReceiveListener {
    void onMessageReceive(String data);
    void onUserAdd(String userName);
}
