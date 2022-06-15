package ClientLibrary;

import java.util.ArrayList;

public class BookShelf {

	private String BookShelfName;
	private ArrayList<Book> BookList = new ArrayList<Book>();
	
	public BookShelf(String name) {
		this.BookShelfName = name;
	}
	
	public void addBook(Book b) {
		BookList.add(b);
	}
	
	public String GetShelfName() {
		return BookShelfName;
	}
	
	
}
