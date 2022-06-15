package ClientLibrary;

public class Book {

	private String name;
	private String location;
	
	public Book(String name,String location) {
		this.name = name;
		this.location = location;
	}
	
	public String getname() {
		return name;
	}
	
	public String getlocation() {
		return location;
	}
	
}
