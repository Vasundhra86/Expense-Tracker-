package Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class Expense{
   private int id;
   private double amount;
   private String date;
   private String category;
   private String note;
	
	public void setId(int Id) {
		this.id = Id;
	}
	public int getId() {
		return id;
	}
	public void setAmount(double Amount) {
		this.amount = Amount;
	}
	public double getAmount() {
		return amount;
	}
	public void setDate(String Date) {
		this.date = Date;
	}
	public String getDate() {
		return date;
	}
	public void setCategory(String Category) {
		this.category = Category;
	}
	public String getCategory() {
		return category;
	}
	public void setNote(String Note) {
		this.note = Note;
	}
	public String getNote() {
		return note;
	}
	public void DisplayExpense(){
	}
	public String getMessage() {
		return null;
	}
}
public class ExpenseTracker {
	private static List<Expense> expenses = new ArrayList<>();
	private static int id = 1;
	
	//1.Add Expenses
    public static void AddExpense(Scanner sc) {
		try {
           System.out.println("Enter Amount: ");
           double amount = sc.nextDouble();
           sc.nextLine();
           
           if (!isValidAmount(amount)) return;
           
           if(amount <=0) {
        	   System.out.println("Amount must be positive.");
        	   return;
           }
           System.out.print("Enter date (yyy-MM-dd): ");
           String date = sc.next();
           if (!isValidDateFormat(date)) return;
           sc.nextLine();
           
           System.out.println("Available Categories:");
           System.out.println("1. Food");
           System.out.println("2. Travel");
           System.out.println("3. Bills");
           System.out.println("4. Shopping");
           System.out.println("5. Entertainment");
           System.out.println("6. Health");
           System.out.println("7. Education");
           System.out.println("8. Others");
           System.out.print("Choose category number: ");
           int categoryChoice = sc.nextInt();
           sc.nextLine(); 

           String category = getCategoryFromChoice(categoryChoice);
           if (category == null) {
               System.out.println("❌ Invalid category choice!");
               return;
           }
           
           System.out.print("Enter Category: ");
           String Category = sc.nextLine();
           if (!isValidCategory(category)) return;
           
           System.out.println("Enter note: ");
           String note = sc.nextLine();
           if (!isValidNote(note)) return;
           
           Expense e = new Expense();
           e.setId(id++);
           e.setAmount(amount);
           e.setDate(date);
           e.setCategory(category);
           e.setNote(note);
                 
           saveExpensesToDB(e);
           expenses.add(e);
           System.out.println("Expense added Successfully!");
		}
		catch (InputMismatchException e) {
	        handleInputException(e, "Amount/Category");
	        sc.nextLine();
		}
		catch (Exception e) {
	        handleUnknownError(e, "adding expense");
	    }
	}
		
		private static String getCategoryFromChoice(int choice) {
		    switch (choice) {
		        case 1: return "Food";
		        case 2: return "Travel";
		        case 3: return "Bills";
		        case 4: return "Shopping";
		        case 5: return "Entertainment";
		        case 6: return "Health";
		        case 7: return "Education";
		        case 8: return "Others";
		        default: return null;
		    }
		}
    private static boolean isValidateDate(String date) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    sdf.setLenient(false);
	    try {
	    	sdf.parse(date);
	    	return true;
	    }
	    catch (ParseException e) {
	    	return false;	
	    }
	}
    
	//2. View Expenses
    public static void ViewExpense(Scanner sc) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("| ID  | Amount   |   Date     |    Category    |        Note        |");
        System.out.println("---------------------------------------------------------------------");

        for (Expense e : expenses) {
            System.out.printf("| %3d | %-8.2f | %-10s | %-12s   | %-20s%n",
                    e.getId(), e.getAmount(), e.getDate(), e.getCategory(), e.getNote());
        }
    }
    private static void showTotalSpent() {
    	double total = 0;
		for(Expense e : expenses) {
			total = total + e.getAmount();
		}
		System.out.println("----------------------");
		System.out.println("Total Spent: "+total);
		System.out.println("----------------------");
	}
    
	//3. Update Expenses
	public static void UpdateExpense(Scanner sc) {
		Expense expenseToUpdate = null;
		
		for(Expense e : expenses) {
			if(e.getId() == id) {
				expenseToUpdate = e;
				break;
			}
		}
		
		if(expenseToUpdate == null) {
			System.out.println("Expense not found to update!");
			return;
		}
		
		System.out.println("Current Expense:");
		System.out.println("ID: " + expenseToUpdate.getId() + 
				" | Amount: " + expenseToUpdate.getAmount() + 
				" | Date: " + expenseToUpdate.getDate() +
				" | Category: " + expenseToUpdate.getCategory() +
				" | Note: "+ expenseToUpdate.getNote());
		
		System.out.println("What do you want to update?");
		System.out.println("1. Amount");
		System.out.println("2. Date");
		System.out.println("3. Category");
		System.out.println("4. Note");
		System.out.println("Enter choice: ");
		int choice = sc.nextInt();
		
		switch(choice) {
		case 1: System.out.println("Enter new Amount: ");
		        double newAmount = sc.nextDouble();
		        if(newAmount > 0) {
		        	expenseToUpdate.setAmount(newAmount);
		        	System.out.println("Amount updated successfully!");
		        }
		        else {
		        	System.out.println("Invalid amount!");
		        }
		        break;
		case 2: System.out.println("Enter new date(yyyy-MM-dd): ");
		        String newDate = sc.nextLine();
		        expenseToUpdate.setDate(newDate);
		        System.out.println("Date updated successfully! ");
		        break;
		case 3: System.out.println("Enter new category: ");
                String newCategory = sc.nextLine();
                expenseToUpdate.setCategory(newCategory);
                System.out.println("Category updated successfully! ");
                break;
		case 4: System.out.println("Enter new note: ");
		        String newNote = sc.nextLine();
		        if(!newNote.isEmpty()) {
		        	expenseToUpdate.setNote(newNote);
		        	System.out.println("Note updated successfully! ");
		        }
		        else {
		        	System.out.println("Note Cannot be empty!");
		        }
		        break;
		default: System.out.println("Invalid choice! ");
		}
		Expense e = new Expense();
		updateExpenseInDB(e);
	}
	
	//4. Delete Expenses
	public static void DeleteExpense(Scanner sc) {
		System.out.println("Enter Expense ID to delete: ");
		int id = sc.nextInt();
		Expense e = findExpenseById(id);
		if(e == null) {
			System.out.println("Expense not found to delete!");
		}
		deleteExpenseFromDB(id);
		expenses.remove(e);
	}
	
	private static Expense findExpenseById(int id) {
		for(Expense e : expenses) {
			if(e.getId() == id) return e;
		}
		return null;
	}
	
	//5.Reports 
	public static void Reports(Scanner sc) {
		System.out.println("Show Reports!");
		System.out.println("1. Show Total Spent");
		System.out.println("2. Show Filter Category");
		System.out.println("3. Show Filter Date");
	    System.out.println("Enter choice: ");
		int choice = sc.nextInt();
	    switch(choice) {
	    case 1:showTotalSpent();	
	           break;
	    case 2:String category = null;
	    	showByFilterCategory(category);
	           break;
	    case 3:String date = null;
			showByFilterDate(date );
	           break;
	    default: 
	    	System.out.println("Invalid Reports Choice!");
	    }
	}
	private static void showByFilterDate(String date) {
		System.out.println("Expenses on Date: "+date);
		for(Expense e : expenses) {
			if(e.getDate().equals(date)) {
				System.out.printf("ID: %d | Amount: %.2f | Category: %s | Note: %s%n",
						e.getId(),e.getAmount(),e.getCategory(),e.getNote());
			}
		}
	}
	private static void showByFilterCategory(String category) {
	    Scanner sc = new Scanner(System.in);
	    System.out.println("Enter Category to filter (Food, Travel, Bills, Shopping, etc.): ");
	    category = sc.nextLine();

	    boolean found = false;
	    System.out.println("Expenses in Category: " + category);
	    for (Expense e : expenses) {
	        if (e.getCategory().equalsIgnoreCase(category)) {
	            System.out.printf("ID: %d | Amount: %.2f | Date: %s | Note: %s%n",
	                    e.getId(), e.getAmount(), e.getDate(), e.getNote());
	            found = true;
	        }
	    }
	    if (!found) System.out.println("No expenses found for this category.");
	}

	
	//6.Save Data Expenses to DataBase
		public static void saveExpensesToDB(Expense e) {
			String sql = "INSERT INTO expenses (amount, date, category, note) VALUES (?,?,?,?)";
			try(Connection conn = DBConnection.getConnection();
					PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
						pst.setDouble(1, e.getAmount());
						pst.setDate(2, java.sql.Date.valueOf(e.getDate()));
						pst.setString(3, e.getCategory());
						pst.setString(4, e.getNote());
						pst.executeUpdate();
						
						ResultSet rs = pst.getGeneratedKeys();
						if(rs.next()) e.setId(rs.getInt(1));
						System.out.println("Expense saved to database!");
					}
			catch (SQLException ex) {
			    handleSQLException(ex, "saving expense");
			}
		}
	
	//7.Exit
	public static void Exit(Scanner sc) throws Exception {
		sc.close();
	}
	
	//Basic Validations & error handling
	private static boolean isValidAmount(double amount) {
	    if (amount <= 0) {
	        System.out.println("❌ Error: Amount must be greater than 0.");
	        return false;
	    }
	    return true;
	}
    
	private static boolean isValidDateFormat(String date) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    sdf.setLenient(false);
	    try {
	        sdf.parse(date);
	        return true;
	    } catch (ParseException e) {
	        System.out.println("❌ Error: Invalid date format. Please use yyyy-MM-dd.");
	        return false;
	    }
	}
	
	private static boolean isValidNote(String note) {
	    if (note == null || note.trim().isEmpty()) {
	        System.out.println("❌ Error: Note cannot be empty.");
	        return false;
	    }
	    return true;
	}
	
	private static boolean isValidCategory(String category) {
	    if (category == null || category.trim().isEmpty()) {
	        System.out.println("❌ Error: Category cannot be empty.");
	        return false;
	    }
	    return true;
	}
	
	private static void handleSQLException(SQLException e, String message) {
	    System.out.println("! SQL Error (" + message + "): " + e.getMessage());
	}

	private static void handleInputException(InputMismatchException e, String fieldName) {
	    System.out.println("! Invalid input for " + fieldName + ". Please enter the correct value type.");
	}

	private static void handleUnknownError(Exception e, String message) {
	    System.out.println("! Unexpected error during " + message + ": " + e.getMessage());
	}
	
	//Load ExpensesFromDB
	public static void loadExpensesFromDB() {
	    String sql = "SELECT * FROM expenses";
	    try (Connection conn = DBConnection.getConnection();
	         Statement st = conn.createStatement();
	         ResultSet rs = st.executeQuery(sql)) {

	        while (rs.next()) {
	            Expense e = new Expense();
	            e.setId(rs.getInt("id"));
	            e.setAmount(rs.getDouble("amount"));
	            e.setDate(rs.getDate("date").toString());
	            e.setCategory(rs.getString("category"));
	            e.setNote(rs.getString("note"));
	            expenses.add(e);
	        }
	    } catch (SQLException ex) {
	        handleSQLException(ex, "loading expenses");
	    }
	}
	
	//Update Expense
	public static void updateExpenseInDB(Expense e) {
		String sql = "UPDATE expenses SET amount=?, date=?,category=?, note=? WHERE id =?";
		try(Connection conn = DBConnection.getConnection();
				PreparedStatement pst = conn.prepareStatement(sql)){
					pst.setDouble(1, e.getAmount());
					pst.setDate(2, java.sql.Date.valueOf(e.getDate()));
					pst.setString(3, e.getCategory());
					pst.setString(4, e.getNote());
					pst.setDouble(5, e.getId());
					pst.executeUpdate();
					System.out.println("Expense updated in database!");
				}
		catch (SQLException ex) {
		    handleSQLException(ex, "updating expense");
		}
	}
	//DeleteExpenseFromDB
	public static void deleteExpenseFromDB(int id) {
		String sql = "DELETE FROM expenses WHERE id=?";
		try(Connection conn = DBConnection.getConnection();
				PreparedStatement pst = conn.prepareStatement(sql)){
					pst.setInt(1, id);
					pst.executeUpdate();
					System.out.println("Expense deleted from database!");
				}
		catch (SQLException ex) {
		    handleSQLException(ex, "deleting expense");
		}
	}
	
	//Main Menu
	public static void main(String[] args) throws Exception {
		loadExpensesFromDB();
		Scanner sc = new Scanner(System.in);		
		int choice;
		do {
			System.out.println("--------------------------");
			System.out.println("|S.No|  Expense Tracker  | ");
			System.out.println("--------------------------");
			System.out.println("| 1. | Add Expense       | ");
			System.out.println("| 2. | View Expense      | ");
			System.out.println("| 3. | Update Expense    | ");
			System.out.println("| 4. | Delete Expense    | ");
            System.out.println("| 5. | Reports (optional)| ");
            System.out.println("| 6. | Exit              | ");
			System.out.println("--------------------------");
			System.out.println("Enter your Choice: ");
		    while(!sc.hasNextInt()) {
			   System.out.println("Invalid Input! Please enter a number 1-5.");
		       sc.next();
		    }
		       choice = sc.nextInt();
		       
			    switch(choice) {
			        case 1: AddExpense(sc);
			    	     break;
			        case 2: ViewExpense(sc);
			        	 break;
			        case 3: UpdateExpense(sc);
			        	 break;
			        case 4: DeleteExpense(sc);
			        	 break;
			        case 5: Reports(sc);
			             break;
			        case 6:
			            System.out.println("Exiting... Data saved!");
			            break;
			        case 7: Exit(sc);
			        	 break;
			        default: 
			        	System.out.println("Invalid Choice. Try again.");
			    }
			    System.out.println("Press Any Key to Continue.");
			}
		while(choice != 7);
		sc.close();
	}	
}

