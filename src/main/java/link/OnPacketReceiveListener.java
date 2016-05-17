package link;

public interface OnPacketReceiveListener {
    void onPacketReceive(byte[] bytes);

    void onDSRLost();
}
