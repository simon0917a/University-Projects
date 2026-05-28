package bookstore;

import java.io.Serializable;

public class Book implements Serializable {
	private static final long serialVersionUID = 1L;
	private String isbn;
	private String author;
	private String title;
	private double price;
	private int edition;
	private String publisher;
	private String copyright;
	
	public Book(String isbn, String author, String title, double price, int edition, String publisher, String copyright) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.price = price;
        this.edition = edition;
        this.publisher = publisher;
        this.copyright = copyright;
    }
    
    public Book() {
    }
	
    public String getIsbn() {
		//Put your code here
		return this.isbn;
	}
	public String getAuthor() {
		//Put your code here
		return this.author;
	}
	public int getEdition() {
		//Put your code here
		return this.edition;
	}
	public String getPublisher() {
		//Put your code here
		return this.publisher;
	}
	public String getCopyright() {
		//Put your code here
		return this.copyright;
	}
	public String getTitle() {
		//Put your code here
		return this.title;
	}
	public void setTitle(String title) {
		//Put your code here
		this.title = title;
	}
	public double getPrice() {
		//Put your code here
		return this.price;
	}
	public void setPrice(double price) {
		//Put your code here
		this.price = price;
	}
}
