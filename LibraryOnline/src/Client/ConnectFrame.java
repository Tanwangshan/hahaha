package Client;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import ClientLibrary.User;

public class ConnectFrame extends JFrame {
    public static final int ConnectFrame_WIDTH = 200;
    public static final int CoonectFrame_HEIGHT = 100;

    Button b = new Button("Connect");
    ButtonGroup jrbGroup = new ButtonGroup();
    JRadioButton jrbA = new JRadioButton("Admin",true);
    JRadioButton jrbU = new JRadioButton("User",false);

    
    
    TextField tfIP = new TextField("192.168.110.1", 15);
    TextField tfUserName = new TextField("myuser", 8);
    
    
    private User myuser;
    
    public  NetClient nc = new NetClient(this);
    public  LibraryClient lc = new LibraryClient(this);
    private List<User> users = new ArrayList<>();
    
    public static void main(String[] args) {
    	ConnectFrame CF = new ConnectFrame("Connecting Frame");
    	CF.launch();

    }
    
    public void launch() {}


	public ConnectFrame(String string) {
		super(string);
		this.setSize(ConnectFrame_WIDTH, CoonectFrame_HEIGHT);
		this.setLayout(new FlowLayout());
		this.add(new Label("Server IP:"));
		this.add(tfIP);
		this.add(jrbA);
		this.add(jrbU);
		jrbGroup.add(jrbA);
		jrbGroup.add(jrbU);
		this.add(new Label("User name:"));
		this.add(tfUserName);
		this.add(b);
        this.setLocation(500, 400);
        this.pack();
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        
        
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(jrbA.isSelected()) {//Admin connect
                    String IP = tfIP.getText().trim();
                    String userName = tfUserName.getText().trim();
                    myuser = new User(userName);
                    nc.connect(IP);
                    setVisible(false);
                    
                    lc.setVisible(true);
                    lc.launchAdminFrame();
                    
            	}
            	else if(jrbU.isSelected()) {//user connect
                    String IP = tfIP.getText().trim();
                    String userName = tfUserName.getText().trim();
                    myuser = new User(userName);
                    nc.connect(IP);
                    setVisible(false);
                    
                    lc.setVisible(true);
                    lc.launchUserFrame();
                    
            	}
            	else {//tell that you must choose one
            		
            	}

            }
        });
    }
	

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getMyUser() {
        return myuser;
    }

    public void setMyUser(User myuank) {
        this.myuser = myuser;
    }

    public NetClient getNc() {
        return nc;
    }

    public void setNc(NetClient nc) {
        this.nc = nc;
    }
}
