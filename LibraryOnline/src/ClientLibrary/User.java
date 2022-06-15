package ClientLibrary;

import Client.*;
import ClientProtocol.*;

public class User {

	private String name;
//	private int password;
	
	private LibraryClient lc;
	
//	public User(String name, int password) {
//		this.name = name;
//		this.password = password;
//	}
	
	public User(String name ) {
		this.name = name ;
	}
	
	public String find(Book b) {
		return b.getlocation();
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
}
