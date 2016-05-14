package link.packing;

public class Packer {

    public static byte[] pack(byte[] msg) {
//        byte[] packet = new byte[msg.length + 1];
//        System.arraycopy(msg, 0, packet, 0, msg.length);
//        packet[msg.length] = "\n".getBytes()[0];
        return msg;
    }

    public static byte[] unpack(byte[] packet) {
//        byte[] msg = new byte[packet.length - 1];
//        System.arraycopy(packet, 0, msg, 0, msg.length);
        return packet;
    }
}
