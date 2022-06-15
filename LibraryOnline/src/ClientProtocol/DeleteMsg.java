package ClientProtocol;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
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
import ClientLibrary.User;


public class DeleteMsg implements Msg{
	private int msgType = Msg.USER_NEW_MSG;
	private User user;
	private LibraryClient lc;
	private ConnectFrame CF;
	private String temp;
	

	public DeleteMsg(ConnectFrame CF) {
		this.CF = CF;
		this.lc = CF.lc;
	}
	public DeleteMsg() {

	}
	
	   class successframe extends Frame{
	        Button b = new Button("exit");
	        public successframe() {
	            super("删除成功");
	            this.setLayout(new FlowLayout());
	            this.add(new Label("Delete success."));
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
	   
	   class failframe extends Frame{
	        Button b = new Button("exit");
	        public failframe() {
	            super("删除失败");
	            this.setLayout(new FlowLayout());
	            this.add(new Label("Delete fail.	Don't find this book."));
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
	            dos.writeUTF(lc.tx2.getText());

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        byte[] buf = baos.toByteArray();
	        try{
	            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, UDP_Port));
	            ds.send(dp);
	            System.out.println("Delete send.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void parse(DataInputStream dis){//the parse should let the client know the user's name and its type
	        try{

	            String bookname = dis.readUTF();
	            String booklocation = dis.readUTF();
	            boolean iffind = false;
	            for(int i = 0 ; i < lc.books.size() ; i++) {
	            	if(lc.books.get(i).getname()==bookname) {
	            		iffind = true;
	            		lc.books.remove(i);
	            		successframe f = new successframe();
	            	}	
	            	
	            }

	            if(!iffind) {
	            	failframe f = new failframe();
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
