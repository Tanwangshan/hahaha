package Server;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import Client.ConnectFrame;
import Server.IPGetter;
import Server.LibraryServer;
import ClientProtocol.*;



public class LibraryServer extends Frame {
	
	public static String HostIP;
    public static int ID = 100;
    public static final int TCP_PORT = 60000;
    public static final int UDP_PORT = 60001;
    public static final int TANK_DEAD_UDP_PORT = 60002;
    private List<client> clients = new ArrayList<>();
    private Image offScreenImage = null;
    private static final int SERVER_HEIGHT = 500;
    private static final int SERVER_WIDTH = 300;

    private ConnectFrame CF;
    
    public static void main(String[] args) {
    	LibraryServer ls = new LibraryServer();
        ls.launchFrame();
        ls.start();
    }

    public void start(){
        new Thread(new UDPThread()).start();
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(TCP_PORT);
            System.out.println("Server has started...");
            HostIP = IPGetter.getLocalIp();

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            Socket s = null;
            try {
                s = ss.accept();
                System.out.println("A client has connected...");
                DataInputStream dis = new DataInputStream(s.getInputStream());
                int UDP_PORT = dis.readInt();
                client client = new client(s.getInetAddress().getHostAddress(), UDP_PORT, ID);
                clients.add(client);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(ID++);
                dos.writeInt(LibraryServer.UDP_PORT);

            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(s != null) s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UDPThread implements Runnable{

        byte[] buf = new byte[1024];

        @Override
        public void run() {
            DatagramSocket ds = null;
            try{
                ds = new DatagramSocket(UDP_PORT);
            }catch (SocketException e) {
                e.printStackTrace();
            }

            while (null != ds){
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                try {
                    ds.receive(dp);
                   // parse(dp);
                    for (client c : clients){
                        dp.setSocketAddress(new InetSocketAddress(c.IP, c.UDP_PORT));
                        ds.send(dp);
                }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        
//        private void parse(DatagramPacket dp) {
//            ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
//            DataInputStream dis = new DataInputStream(bais);//这个DataInputStream的意思就是转换成Java的底层输入流。后面的readInt方法就是从输入流中读出整数。
//            int msgType = 0;
//            try {
//                msgType = dis.readInt();//获得消息类型
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Msg msg = null;
//            switch (msgType){//根据消息的类型调用对应消息的解析方法，坦克那里的多态用的很妙，每个消息类型都实现了如何解析。
//                case Msg.USER_NEW_MSG :
//                    msg = new UserNewMsg();
//                    msg.parse(dis);
//                    break;
//                case  Msg.BOOK_FIND_MSG :
//                    msg = new FindMsg();
//                    msg.parse(dis);
//                    break;
//                case Msg.BOOK_NEW_MSG:
//                    msg = new AddMsg();
//                    msg.parse(dis);
//                    break;
//                case Msg.BOOK_DELETE_MSG:
//                    msg = new DeleteMsg();
//                    msg.parse(dis);
//                    break;
//            }
//        }
    }



    
    

     class client{
        String IP;
        int UDP_PORT;
        int id;

        public client(String ipAddr, int UDP_PORT, int id) {
            this.IP = ipAddr;
            this.UDP_PORT = UDP_PORT;
            this.id = id;
        }
    }


    @Override
    public void paint(Graphics g) {
        g.drawString("LibraryClient :", 30, 50);
        int y = 80;
        g.drawString(HostIP,20,y);
        for(int i = 0; i < clients.size(); i++){
            client c = clients.get(i);
            y += 30;
            g.drawString("id : " + c.id + " - IP : " + c.IP, 30, y);
            
        }
    }

    @Override
    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(SERVER_WIDTH, SERVER_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.white);
        gOffScreen.fillRect(0, 0, SERVER_WIDTH, SERVER_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void launchFrame() {
        this.setLocation(200, 100);
        this.setSize(SERVER_WIDTH, SERVER_HEIGHT);
        this.setTitle("LibraryServer");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.yellow);
        this.setVisible(true);
        new Thread(new PaintThread()).start();

    }


    class PaintThread implements Runnable {
        public void run() {
            while(true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
}
