package user;

public interface OnMessageReceiveListener {
    void onMessageReceive(String userName, String data);
    void onUserAdd(String userName);
    void onUserDelete(String userName);
    void onDisconnect();
    void onPortParametersChanged(int baudRate, int dataBits, int stopBits, int parity);
    void onDSRLost();
}
