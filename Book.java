import java.util.Date;

public class Book {
	
	/* You should store bibliographic information (Author First Name, 
	 * Author Last Name, Title, Year Published, Publisher) and/or also history 
	 * of item (Date Purchased, Date Entered into System, Date Last Modified).
	 * OPTIONS: A complete inventory system will also store the Condition 
	 * of Book and any Collectible Features (is it first edition, 
	 * signed by author etc.)*/
	
	//Book information
	private String ISBN; 
	private String title;
	private String author; 
	private String yearPublished;
	private String publisher;
	private double price;
	private int quantity; 
	
	//History of book
	String status; //??? Do I need?? 
	private Date purchasedDate; 
	private Date enteredDate; 
	private Date modifiedDate;
	
	//Extra (might not do)
	//private String bookType; 
	//private String condition;
	
	public Book(){
		
	}
	
	public Book(String ISBN, String title, String author, String yearPublished, String publisher, double price, int quantity){
		this.ISBN = ISBN; 
		this.title = title; 
		this.author = author; 
		this.yearPublished = yearPublished; 
		this.publisher = publisher; 
		this.price = price; 
		this.quantity = quantity; 
	}
	
	public void setHistory(String status, Date purchasedDate, Date enteredDate, Date modifiedDate){
		this.status = status; 
		this.purchasedDate = purchasedDate;
		this.enteredDate = enteredDate;
		this.modifiedDate = modifiedDate; 
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getYearPublished() {
		return yearPublished;
	}

	public void setYearPublished(String yearPublished) {
		this.yearPublished = yearPublished;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status; 
	}
	
	public void setStatus(String status) {
		this.status = status; 
	}
	
	public Date getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public Date getEnteredDate() {
		return enteredDate;
	}

	public void setEnteredDate(Date enteredDate) {
		this.enteredDate = enteredDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String toString(){
		String x = " ISBN: " + ISBN.toString() + " || " + " Title: " + title + " || " + " Author: " + author + " || " + " Publisher: " + publisher + " (" + yearPublished + ") || "+ " Price: $" + price + " || " + "Quantity: " + quantity +
					"\n" + " Entered Date: " + enteredDate.toString() + " >>>> " + "Modified Date: " + modifiedDate.toString(); 
		return x;
	}
	
	public String toPrint(){
		String newline = System.getProperty("line.separator");
		String x = ISBN+",,"+title+",,"+author+newline+yearPublished+",,"+publisher+",,"+price+",,"+quantity+
				 	newline+"ENTERED>>>>"+enteredDate+newline+"MODIFIED>>>>"+modifiedDate;
		return x; 
	}
	
}
