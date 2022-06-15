package ClientProtocol;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {
    public static final int USER_NEW_MSG = 1;
    public static final int BOOK_FIND_MSG= 2;
    public static final int BOOK_NEW_MSG = 3;
    public static final int BOOK_DELETE_MSG = 4;
    public void send(DatagramSocket ds, String IP, int UDP_Port);
    public void parse(DataInputStream dis);
}
