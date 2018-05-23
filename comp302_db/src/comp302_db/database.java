package comp302_db;

/* Manesis Athanasios
AM: 2014030061
COMP302: Databases
*/
import java.sql.*;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;


public class database {
 Connection conn;

 public database() {
     conn = null;
 }


 public void dbConnect(String ip, int port, String database, String username, String password) {
     try {
         Class.forName("org.postgresql.Driver");

         conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + database, username, password);
         System.out.println("Connection Established!");

         conn.setAutoCommit(false);
         System.out.println("Autocommit turned-off!");

     } catch (Exception e) {
         e.printStackTrace();
     }
 }

 public void db_commit() {
     try {
         // commit all changes
         conn.commit();
         System.out.println("Changes committed!");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }

 public void db_abort() {
     try {
         // abort
         conn.rollback();
         System.out.println("Uncommitted changes cancelled!");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }

 public void waitForEnter() {
     Scanner scn = new Scanner(System.in);
     System.out.println("Press ENTER to continue...");
     scn.nextLine();
 }

/*************************************************************************/

 
 public void findDetailedGrades(){
	 	Scanner input = new Scanner(System.in);
	 	PreparedStatement statement;
	 try {
	 	
	 	String query = " SELECT * FROM \"Register\" r WHERE r.amka =( SELECT amka FROM \"Student\" s WHERE s.am = ?) ";
	 	statement = conn.prepareStatement(query);
	 	
	 	System.out.println("Enter AM of student:");
	 	String st_am = input.nextLine();
	 	
	 	statement.setString(1, st_am);
	 	
	 	 ResultSet rs = statement.executeQuery();
	 	
		System.out.println("Amka | Serial Number | Course Code | Exam Grade | Final Grade | Lab Grade | Status");
		while (rs.next()) {
			System.out.println(rs.getString(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+rs.getString(4)+"|"+rs.getString(5)+"|"+rs.getString(6)+"|"+rs.getString(7));
		}
		
	}
	 catch (SQLException e1) {
	     e1.printStackTrace();
	 } finally {
	     waitForEnter();
	 }
 }

 public void changeGrade() {
     Scanner input = new Scanner(System.in);
     PreparedStatement statement;
     try {
         String query = "SELECT	exam_grade, lab_grade, final_grade, register_status "
                 + "FROM	\"Register\""
                 + "WHERE	amka = (SELECT	amka FROM \"Student\" WHERE am = ?) "
                 + "AND serial_number = "
                 + "(SELECT max(serial_number) FROM \"Register\" "
                 + "WHERE amka = (SELECT amka FROM \"Student\" WHERE am = ?) AND course_code = ?)";

         statement = conn.prepareStatement(query);


         System.out.println("Enter AM of student:");
         String student_am = input.nextLine();

         System.out.println("Enter course code:");
         String course_code = input.nextLine();

         statement.setString(1, student_am);
         statement.setString(2, student_am);
         statement.setString(3, course_code);
         ResultSet result = statement.executeQuery();
         if (result.next()){
             System.out.println("Exam grade: " + result.getDouble(1) + " Lab grade: " + result.getDouble(2) + " Final grade: " + result.getDouble(3));
             System.out.println("Registration status: " + result.getString(4));
         }
         else
             System.out.println("Student " + student_am + " has not been registered to course " + course_code + " or student/course doesn't exist!");
     } catch (SQLException e1) {
         e1.printStackTrace();
     } finally {
         waitForEnter();
     }
 }
//create back up;


 /************************************************************************/

 public int askConnection() {
     Scanner menuScanner = new Scanner(System.in);
     int choice = -1;
     System.out.println("Would you like to connect to database?");
     System.out.println("Type: 1/0 (Y/n)");
     while (choice != 1 && choice != 0) {
         try {
             choice = menuScanner.nextInt();
             menuScanner.nextLine();
             if (choice == 1) {
                 dbConnect("localhost", 5432, "comp302_db", "postgres", "Stftdt>ln2"); 
             }
         } catch (InputMismatchException e) {
             System.out.println("Invalid input! Please type '1' for yes or '0' for no.");
             menuScanner.nextLine();
         }
     }
     return choice;
 }

     public void printMainMenu() {
    	 System.out.println("");
    	 System.out.println("+-------------------------------------------------------+");
         System.out.println("[1] Print grade list of a student");
         System.out.println("[2] Update course grade of a student");
         System.out.println("[3] Commit transaction");
         System.out.println("[4] Abort transaction");
         System.out.println("[5] Create back up");
         System.out.println("[0] Exit (any uncommitted transactions will be aborted!)");
         System.out.println("+-------------------------------------------------------+");
         System.out.println("");
         System.out.println("Select: ");
     }
     /*
      *
      */

     public void closeConnection () {
         try {
             System.out.println("Closing connection...");
             db_abort();
             conn.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

     /*
      *
      */

     public void mainMenu () {
         Scanner menuScanner = new Scanner(System.in);
         int choice = -1;

         while (choice != 0) {
             try {
                 printMainMenu();
                 choice = menuScanner.nextInt();
                 menuScanner.nextLine();
                 switch (choice) {
                     case 1: findDetailedGrades();
                         break;
                     case 2: changeGrade();
                         break;
                     case 3:
                         db_commit();
                         break;
                     case 4:
                         db_abort();
                         break;
                     case 5: //create back up;
                         break;
                     case 0:
                         closeConnection();
                         break;
                     default:
                         System.out.println("ERROR");
                 }
             } catch (InputMismatchException e) {
                 System.out.println("Invalid input!");
                 menuScanner.nextLine();
             }
         }
     }


 }

