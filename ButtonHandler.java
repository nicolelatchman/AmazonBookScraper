import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ButtonHandler extends JFrame implements ActionListener{
	private DisplayWindow window;
	private static Hashtable<String, Book> bookCollection;  
	private static JTable table = new JTable(); 
	private static DefaultTableModel tableModel;
	private static File myFile;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; Touch; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; Tablet PC 2.0; rv:11.0) like Gecko";

	public ButtonHandler (DisplayWindow w, Hashtable<String, Book> p, JTable a, DefaultTableModel tm, File fn) {
		setWindow(w);
		bookCollection = p;  
		table = a;
		tableModel = tm;
		myFile = fn;
	}
	
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		String buttonName = event.getActionCommand();
		if (buttonName.equals("Insert Book")){
			//text.setEditable(false);
			String isbn = JOptionPane.showInputDialog("Please input book's ISBN: "); 
			//Checking if IDNum is a valid ID
			if (bookCollection.containsKey(isbn)){
				JOptionPane.showMessageDialog(window, "This ISBN is already in system. Quantity updated.");
				Book x = bookCollection.get(isbn);
				Date date = new Date();
				x.setModifiedDate(date);
				x.setQuantity(x.getQuantity()+1);
				return;
			} else
				try {
					getInfo(isbn);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		JOptionPane.showMessageDialog(window, "Item added!");
			
		}
		if (buttonName.equals("Delete Book")){
			String ISBN = JOptionPane.showInputDialog("Please input book's ISBN: "); 
			Book x = bookCollection.remove(ISBN);
			int rowNumber = -1;
			for (int i = 0; i < table.getRowCount(); i++){
				if (ISBN.equals(table.getValueAt(i, 0))){
					rowNumber = i;
				}
			}
			System.out.println(rowNumber);
			if (rowNumber != -1){
				System.out.println(rowNumber);
				tableModel.removeRow(rowNumber);
			}
			if (x != null){
				JOptionPane.showMessageDialog(window, "Book deleted!");
				int newQuantity = x.getQuantity() - 1;
				if (newQuantity > 0){
					x.setQuantity(newQuantity);
					bookCollection.put(ISBN, x);
				}

			}
			else {
				JOptionPane.showMessageDialog(window, "This book does not exist. Please recheck ISBN.");
			}
		}
		if (buttonName.equals("Save File")){ //Save option
			saveFile();
		}
		if (buttonName.equals("Exit")) //Quit option
			System.exit(0);
	}
	
	public void getInfo(String ISBN) throws Exception{
		String url = "https://www.amazon.com/dp/"+ISBN;
		String fileName = "webpagereader.txt";
		PrintWriter writer = new PrintWriter(fileName);
		BufferedReader reader = read(url);
		String line = reader.readLine();
		writer.println("Contents of the following URL: "+url+"\n");
		while (line != null) {
			writer.println(line);
			line = reader.readLine();
		}
		writer.close();
		File file = new File(fileName);
		extractInfo(file, ISBN);
		
	}
	
	public void extractInfo(File file, String ISBN) throws IOException{
		Pattern patternPrice = Pattern.compile("Your cost could be");
		Pattern patternSecondPrice = Pattern.compile("<span class=\"a-size-medium a-color-price offer-price a-text-normal\">");
		Pattern patternTitle = Pattern.compile("<meta name=\"title\"");
		Pattern patternAuthor = Pattern.compile("<meta name=\"title\"");
		Pattern patternPublisher = Pattern.compile("<li><b>Publisher:</b>");
		BufferedReader br = new BufferedReader(new FileReader(file));
		BufferedReader br2 = new BufferedReader(new FileReader(file));
		BufferedReader br3 = new BufferedReader(new FileReader(file));
		BufferedReader br4 = new BufferedReader(new FileReader(file));
		String newLine = br.readLine();
		String newLine2 = br2.readLine();
		String newLine3 = br3.readLine();
		String newLine4 = br4.readLine();
		String priceLine = null;
		String titleLine = null;
		String authorLine = null;
		String publisherAndYearLine = null;
		String publisherLine = null; 
		String yearLine = null;
		boolean foundPrice = false;
		boolean foundTitle = false;
		boolean foundAuthor = false;
		boolean foundPublisherAndYear = false;
		while (newLine != null & foundPrice == false){
			Matcher matcherPrice = patternPrice.matcher(newLine);
			while (matcherPrice.find()) {
                priceLine = newLine;
                foundPrice = true;
            }
			newLine = br.readLine();
		}
		
		if (priceLine == null){
			while (newLine != null & foundPrice == false){
				Matcher matcherSecondPrice = patternSecondPrice.matcher(newLine);
				while (matcherSecondPrice.find()) {
	                priceLine = newLine;
	                foundPrice = true;
	            }
				newLine = br.readLine();
			}
		}
		
		while (newLine2 != null & foundTitle == false){
			Matcher matcherTitle = patternTitle.matcher(newLine2);
			while (matcherTitle.find()) {
                titleLine = newLine2;
                foundTitle = true;
            }
			newLine2 = br2.readLine();
		}
		
		while (newLine3 != null & foundPublisherAndYear == false){
			Matcher matcherPublisher = patternPublisher.matcher(newLine3);
			while (matcherPublisher.find()) {
                publisherAndYearLine = newLine3;
                foundPublisherAndYear = true;
            }
			newLine3 = br3.readLine();
		}
		
		while (newLine4 != null & foundAuthor == false){
			Matcher matcherAuthor = patternAuthor.matcher(newLine4);
			while (matcherAuthor.find()){
				authorLine = newLine4; 
				foundAuthor = true; 
			}
			newLine4 = br4.readLine();
		}
		
		br.close();
		br2.close();
		br3.close();
		br4.close();
	
		Pattern onlyPrice = Pattern.compile("of \\$\\d+.\\d\\d<\\/b>");
		Pattern onlyAuthor = Pattern.compile(": \\D+: \\d");
		Pattern onlyTitle = Pattern.compile("content=\".+: \\d");
		Pattern onlyYear = Pattern.compile("[0-9][0-9][0-9][0-9]");
		Pattern onlyPublisher = Pattern.compile("\\/b>.+\\(");
		
		Matcher matcher = onlyPrice.matcher(priceLine);
		while (matcher.find()) {
            priceLine = matcher.group();
        }
		
		Matcher matcher2 = onlyAuthor.matcher(authorLine);
		while (matcher2.find()) {
             authorLine = matcher2.group();
        }
		
		Matcher matcher3 = onlyTitle.matcher(titleLine);
		while (matcher3.find()) {
             titleLine = matcher3.group();
        }
		
		Matcher matcher4 = onlyYear.matcher(publisherAndYearLine);
		while (matcher4.find()) {
             yearLine = matcher4.group();
        }
		
		Matcher matcher5 = onlyPublisher.matcher(publisherAndYearLine);
		while (matcher5.find()) {
             publisherLine = matcher5.group();
        }
		
		authorLine = authorLine.substring(2, authorLine.length()-3);
		priceLine = priceLine.substring(4, priceLine.length()-4);
		titleLine = titleLine.substring(9, titleLine.length()-2);
		titleLine = titleLine.split(":")[0];
		publisherLine = publisherLine.substring(4, publisherLine.length()-2);
		System.out.println(authorLine);
		System.out.println(priceLine);
		Double price = Double.parseDouble(priceLine);
		System.out.println(titleLine);
		System.out.println(yearLine);
		System.out.println(publisherLine);
		
		Date enteredDate = new Date();
		Date modifiedDate = new Date();
		
		Book x = new Book(ISBN, titleLine, authorLine, yearLine, publisherLine, price, 1 );
		x.setEnteredDate(enteredDate);
		x.setModifiedDate(modifiedDate);
		bookCollection.put(ISBN, x);
		
		Object rowData[] = new Object[]{ISBN, titleLine, authorLine, yearLine, publisherLine, price, 1};
		tableModel.addRow(rowData);
		for (int i = 0; i <= table.getRowCount(); i++){
			table.setRowHeight(i, 30);
		}
	}

	public static InputStream getURLInputStream(String sURL) throws Exception {
        URLConnection oConnection = (new URL(sURL)).openConnection();
        oConnection.setRequestProperty("User-Agent", USER_AGENT);
        return oConnection.getInputStream();
    }

    //WebpageReaderWithAgent.java code given in class
    public static BufferedReader read(String url) throws Exception {
        InputStream content = (InputStream)getURLInputStream(url);
        return new BufferedReader (new InputStreamReader(content));
    }
    
    public void saveFile(){
		try {
			fileWriter(myFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Writes to file
		JOptionPane.showMessageDialog(window, "Done!");
	}
    
    public static void fileWriter (File fileName) throws IOException{
		PrintWriter outStream;
		outStream = new PrintWriter(new FileOutputStream(fileName));
		String newline = System.getProperty("line.separator");
		
		Enumeration<String> isbn; 
		String book;
		isbn = bookCollection.keys();
		
		while(isbn.hasMoreElements()) {
	         book = (String) isbn.nextElement();
	         String x = bookCollection.get(book).toPrint()+newline+"---------------------------------"+"\n";
	         outStream.println(x);
	    }  
		outStream.close();
	}
    
    public DisplayWindow getWindow() {
		return window;
	}

	public void setWindow(DisplayWindow window) {
		this.window = window;
	}

}
