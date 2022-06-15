package ClientProtocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import Client.*;
import ClientLibrary.*;


public class UserNewMsg implements Msg {

	private int msgType = Msg.USER_NEW_MSG;
	private User user;
	private LibraryClient lc;
	private ConnectFrame CF;

	public UserNewMsg(ConnectFrame CF) {
		this.CF = CF;
		this.lc = CF.lc;
	}
	
	public UserNewMsg() {
	}
	
	  public void send(DatagramSocket ds, String IP, int UDP_Port){
	        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
	        DataOutputStream dos = new DataOutputStream(baos);
	        try {
	            dos.writeInt(msgType);
	            dos.writeUTF(user.getName());

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        byte[] buf = baos.toByteArray();
	        try{
	            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, UDP_Port));
	            ds.send(dp);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void parse(DataInputStream dis){//the parse should let the client know the user's name and its type
	        try{

	            String userName = dis.readUTF();
	            User newUser = new User(userName);
	            CF.getUsers().add(newUser);//add new user

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
}
