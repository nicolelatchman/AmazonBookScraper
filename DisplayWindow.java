import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class DisplayWindow extends JFrame{
	
	protected static Hashtable<String, Book> bookCollection = new Hashtable<String, Book>(); 
	private JFrame jframe = new JFrame();
	JTable table = new JTable();
	DefaultTableModel tm;
	JScrollPane scrollPane = new JScrollPane();
	static File myFile;
	static BufferedReader br = null;
	private JButton open;
    private JButton save;
    private JButton insert;
    private JButton delete;
    private JButton exit;
	
	// Setting the features of the Display Window
	DisplayWindow(){
		super("Inventory"); //Title of GUI
		JPanel buttonPanel = new JPanel();
		
		open = new JButton("Open File");
        save = new JButton("Save File");
        insert = new JButton("Insert Book");
        delete = new JButton("Delete Book");
        exit = new JButton("Exit");

        buttonPanel.add(open);
        buttonPanel.add(save);
        buttonPanel.add(delete);
        buttonPanel.add(insert);
        buttonPanel.add(exit);

        add(buttonPanel, BorderLayout.NORTH);
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, "North");
		
        fileChooser();
        ButtonHandler buttonHandler = new ButtonHandler(this, bookCollection, table, tm, myFile); 
        open.addActionListener(buttonHandler);
        save.addActionListener(buttonHandler);
        insert.addActionListener(buttonHandler);
        delete.addActionListener(buttonHandler);
        exit.addActionListener(buttonHandler);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void fileChooser(){
		JFileChooser fd = new JFileChooser(); //Opens up document explorer
		fd.showOpenDialog(null);
		myFile = fd.getSelectedFile(); //Selected file becomes file in program
		try {
			fileReader(myFile);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		if (myFile != null){
			setTable();
		}
	}
	
	public static void fileReader (File fileName) throws IOException, ParseException{ 
		br = new BufferedReader(new FileReader(fileName)); 
		String newLine = br.readLine();
		while (newLine!= null){
			DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
			@SuppressWarnings("unused")
			String status; //Garbage token collector
			@SuppressWarnings("unused")
			String line;
			
			StringTokenizer myTokens = new StringTokenizer(newLine, ",,");
			String isbn = myTokens.nextToken();
			System.out.println(isbn);
			String title = myTokens.nextToken();
			String author = myTokens.nextToken();
			newLine = br.readLine();
			myTokens = new StringTokenizer(newLine, ",,");
			String yearPublished = myTokens.nextToken();
			String publisher = myTokens.nextToken();
			double price = Double.parseDouble(myTokens.nextToken());
			int quantity = Integer.parseInt(myTokens.nextToken());
			Book book = new Book(isbn, title, author, yearPublished, publisher, price, quantity);
			
			newLine = br.readLine();
			StringTokenizer myTokens2 = new StringTokenizer(newLine, ">>>>");
			status = myTokens2.nextToken();
			String dateE = myTokens2.nextToken();
		   	Date dateEntered = (Date)format.parse(dateE);
		   	book.setEnteredDate(dateEntered);
		  
		   	newLine = br.readLine();
		   	myTokens2 = new StringTokenizer(newLine, ">>>>");
			status = myTokens2.nextToken();
			String dateM = myTokens2.nextToken();
		   	Date dateModified = (Date)format.parse(dateM);
		   	book.setModifiedDate(dateModified);
	
		   	bookCollection.put(isbn, book);
		   	
		   	line = br.readLine();
		   	newLine = br.readLine();
	    }
		return;
	}
	
	public void setTable(){
		
		String[] columnNames = {"ISBN","Title","Author","Year Published","Publisher","Price","Quantity"};
		
		Enumeration<String> isbn; 
		String ISBN;
		isbn = bookCollection.keys();
		
		String[] ISBNs = new String[bookCollection.size()];
		Book[] books = new Book[bookCollection.size()];
		
		while(isbn.hasMoreElements()) {
			for (int i = 0; i < bookCollection.size(); i++){
				ISBN = (String) isbn.nextElement();//Get key
				ISBNs[i] = ISBN;
				Book x = bookCollection.get(ISBN);//Get book
				books[i] = x;
			}
		}
		
		System.out.println(bookCollection.size());
		tm = new DefaultTableModel(columnNames,0);
		
		for (int i = 0; i < bookCollection.size(); i++){
			
				String iSBN = books[i].getISBN();
				String title = books[i].getTitle();
				String author = books[i].getAuthor();
				String yearP = books[i].getYearPublished();
				String publisher = books[i].getPublisher();
				Double price = books[i].getPrice();
				int quantity = books[i].getQuantity();
				
				Object rowData[] = new Object[]{iSBN, title, author, yearP, publisher, price, quantity};
				tm.addRow(rowData);
		}
		
		table.setModel(tm);
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
		table.setOpaque(true);
		for (int i = 0; i <= table.getRowCount(); i++){
			table.setRowHeight(i, 30);
		}
		Font font = new Font("Serif", Font.PLAIN, 20);
		table.setFont(font);
	}
	
	public JFrame getJframe() {
		return jframe;
	}

	public void setJframe(JFrame jframe) {
		this.jframe = jframe;
	}
}
