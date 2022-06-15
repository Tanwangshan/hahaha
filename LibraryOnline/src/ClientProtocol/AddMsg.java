package ClientProtocol;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import Client.ConnectFrame;
import Client.LibraryClient;
import ClientLibrary.Book;
import ClientLibrary.User;
import Server.LibraryServer;


public class AddMsg implements Msg {
	private int msgType = Msg.USER_NEW_MSG;
	private User user;
	private Book book;
	private LibraryClient lc;
	private ConnectFrame CF;
	private frame f;
	
	private LibraryServer ls;
	
	public AddMsg(ConnectFrame CF) {
		this.CF = CF;
		this.lc = CF.lc;
	}
	
	public AddMsg() {
	}
	
	 public  class frame extends Frame{
	        Button b = new Button("exit");
	        public frame() {
	            super("添加成功");
	            this.setLayout(new FlowLayout());
	            this.add(new Label("Add success."));
	            this.add(b);
	            this.setLocation(500, 400);
	            this.pack();
	            this.addWindowListener(new WindowAdapter() {
	                @Override
	                public void windowClosing(WindowEvent e) {
	                    System.exit(0);
	                }
	            });
	            b.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    System.exit(0);
	                }
	            });
	            this.setVisible(true);
	        }
	    }
	
	  public void send(DatagramSocket ds, String IP, int UDP_Port){
	        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
	        DataOutputStream dos = new DataOutputStream(baos);
	        try {
	            dos.writeInt(msgType);
	            dos.writeUTF(lc.tx1.getText());
	           // System.out.println(lc.tx1.getText());
	            dos.writeUTF(lc.tx2.getText());

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        byte[] buf = baos.toByteArray();
	        try{
	            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, UDP_Port));
	            ds.send(dp);
	            System.out.println("Add send");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void parse(DataInputStream dis){//the parse should let the client know the user's name and its type
	        try{

	            String bookname = dis.readUTF();
	            String booklocation = dis.readUTF();
	            book = new Book(bookname,booklocation);
	            lc.books.add(book);
	            f = new frame();
	            f.setLayout(new FlowLayout());
	            f.setVisible(true);
	            


	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
