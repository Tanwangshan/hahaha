package Client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;

import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ClientLibrary.*;
import ClientProtocol.AddMsg;
import ClientProtocol.DeleteMsg;
import ClientProtocol.FindMsg;

public class LibraryClient extends Frame{
    public static final int CLIENT_WIDTH = 300;
    public static final int CLIENT_HEIGHT = 170;

  //  public boolean isPublic = true;
    private ConnectFrame CF;
    public boolean showDialog = false;
    
    public List<Book> books = new ArrayList<>();

    private JButton b = new JButton("Execute");
    
    private ButtonGroup bg = new ButtonGroup();
    private JRadioButton jrb1 = new JRadioButton("Find");
    private JRadioButton jrb2 = new JRadioButton("Add");
    private JRadioButton jrb3 = new JRadioButton("Delete");
    
    public TextField tx1 = new TextField("请输入书名",15);
    public TextField tx2 = new TextField("",15);
    
    protected NetClient nc ;
 
    public LibraryClient(ConnectFrame CF) {
    	super("Library Management System");
    	this.CF = CF;
    	nc = CF.nc;
    }
    
    public void launchAdminFrame() {//Client Frame
        this.setLocation(0, 0);
    this.setSize(CLIENT_WIDTH, CLIENT_HEIGHT);
        this.setTitle("LibraryManagement");
        this.setLayout(new FlowLayout());
        this.add(new Label("Function:"));
        this.add(jrb1);
        this.add(jrb2);
        this.add(jrb3);
        bg.add(jrb1);
        bg.add(jrb2);
        bg.add(jrb3);
        this.add(new Label("Book Name:"));
        this.add(tx1);
        this.add(new Label("Location:"));
        this.add(tx2);
        this.add(b);
        
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(jrb1.isSelected()) {//start search
                    nc.send(new FindMsg(CF));
            	}
            	if(jrb2.isSelected()) {//add
                    nc.send(new AddMsg(CF));
            	}
            	if(jrb3.isSelected()) {//delete
                    nc.send(new DeleteMsg(CF));
            	}

            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nc.sendClientDisconnectMsg();//send a disconnet msg to the server before close the frame
                System.exit(0);
            }
        });
        this.setResizable(true);
       // this.setBackground(Color.LIGHT_GRAY);


        this.setVisible(true);
        
    }
    
    public void launchUserFrame() {//Client Frame
        this.setLocation(0, 0);
        this.setSize(CLIENT_WIDTH, CLIENT_HEIGHT);
        this.setTitle("LibraryClient");
        this.setLayout(new FlowLayout());
        this.add(new Label("Function:"));
        this.add(jrb1);
        this.add(new Label("Book Name:"));
        this.add(tx1);
        this.add(new Label("Location:"));
        this.add(tx2);
        this.add(b);
        
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(jrb1.isSelected()) {//start search
                    nc.send(new FindMsg(CF));
            	}
            	

            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nc.sendClientDisconnectMsg();//send a disconnet msg to the server before close the frame
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.LIGHT_GRAY);


        this.setVisible(true);
        
    }

	
}
