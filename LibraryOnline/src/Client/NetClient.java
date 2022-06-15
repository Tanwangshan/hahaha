package Client;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

import ClientLibrary.Book;
import ClientLibrary.User;
import ClientProtocol.*;
import ClientProtocol.AddMsg.frame;
import Server.LibraryServer;


public class NetClient {
    private LibraryClient lc;
    private ConnectFrame CF;
    
    private Book book;
    Button b = new Button("exit");
    
    public UDPThread ut ;
    
    private int UDP_PORT;
    private String serverIP;
    private int serverUDPPort;
    private int TANK_DEAD_UDP_PORT;
    private DatagramSocket ds = null;

    public void setUDP_PORT(int UDP_PORT) {
        this.UDP_PORT = UDP_PORT;
    }

    public NetClient(ConnectFrame CF){
        this.CF = CF;
        this.lc = this.CF.lc;

        try {
            this.UDP_PORT = getRandomUDPPort();
        }catch (Exception e){
            System.exit(0);
        }
    }


    public void connect(String ip){
        serverIP = ip;
        Socket s = null;

        try {
            ds = new DatagramSocket(UDP_PORT);
            
            try {
                s = new Socket(ip, LibraryServer.TCP_PORT);
            }catch (Exception e1){            }
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(UDP_PORT);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            this.serverUDPPort = dis.readInt();//the udp port of the server
            System.out.println("connect to server successfully...");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ut = new UDPThread();
        new Thread(ut).start();
        System.out.println("UDP Thread start");

    }

    /**
     * The Client gets the UDP port randomly.
     * @return
     */
    private int getRandomUDPPort(){
        return 55558 + (int)(Math.random() * 9000);
    }

    public void send(Msg msg){
        msg.send(ds, serverIP, serverUDPPort);
    }

    public class UDPThread implements Runnable{

        byte[] buf = new byte[1024];

        @Override
        public void run() {
       	 System.out.println("parse11");
           // while(null != ds){
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                try{
                    ds.receive(dp);
                    parse(dp);
                   System.out.println("parse22");
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("parse33");
				}
           // }
        }

        public void parse(DatagramPacket dp) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
            DataInputStream dis = new DataInputStream(bais);
            int msgType = 0;
            try {
                msgType = dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Msg msg = null;
            String bookname;
            String booklocation;
            String userName;
            try {switch (msgType){//explain the msg
                case Msg.USER_NEW_MSG :
                	userName = dis.readUTF();
    	            User newUser = new User(userName);
    	            CF.getUsers().add(newUser);//add new user
    	            System.out.println("New success.");
                    break;
                case  Msg.BOOK_NEW_MSG :
                	bookname = dis.readUTF();
     	            booklocation = dis.readUTF();
     	            book = new Book(bookname,booklocation);
     	            lc.books.add(book);
     	           System.out.println("Add success.");
                    break;
                case Msg.BOOK_FIND_MSG:
    	            bookname = dis.readUTF();
    	            booklocation = dis.readUTF();
    	            boolean iffind = false;
    	            for(int i = 0 ; i < lc.books.size() ; i++) {
    	            	if(lc.books.get(i).getname()==bookname) {
    	            		iffind = true;
    	            		booklocation = lc.books.get(i).getlocation();
    	            		lc.tx2 = new TextField(booklocation);
    	            		System.out.println("Find success.");
    	            	}	
    	            	
    	            }

    	            if(!iffind) {
    	            	System.out.println("Find failed.");
    	            }
                    break;
                case Msg.BOOK_DELETE_MSG:
    	            bookname = dis.readUTF();
    	            booklocation = dis.readUTF();
    	            iffind = false;
    	            for(int i = 0 ; i < lc.books.size() ; i++) {
    	            	if(lc.books.get(i).getname()==bookname) {
    	            		iffind = true;
    	            		lc.books.remove(i);
    	      	           System.out.println("Delete success.");
    	            	}	
    	            	
    	            }

    	            if(!iffind) {
    	     	           System.out.println("Find failed.Delete failed.");
    	            }
                    break;       
            }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendClientDisconnectMsg(){//鑷繁鍏冲鎴风鎴栬�呭潶鍏嬫浜℃椂璋冪敤銆�
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(UDP_PORT);//鍙戦�佸鎴风鐨刄DP绔彛鍙�, 浠庢湇鍔″櫒Client闆嗗悎涓敞閿�
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != dos){
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != baos){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, TANK_DEAD_UDP_PORT));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
