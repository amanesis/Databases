package comp302_db;

/* Manesis Athanasios
AM: 2014030061
COMP302: Databases
*/

public class launcher {

 public static void main(String[] args) {

     database db = new database();
     int linkEstablished = db.askConnection();

     if (linkEstablished==1)
         db.mainMenu();

     System.out.println("Exit.");
     
 }

}