import java.rmi.*;
 public class Servant extends Thread {
   private volatile SudokuImpl su;

	 public Servant() {

	 }

	 public void run() {

		 try {

			 Thread.sleep(10000);
		 } catch(InterruptedException e) { }

		 try {

			 su = new SudokuImpl();
		 } catch(RemoteException e) {

			 System.err.println("Echech appel en retour : " + e);
		 }

		System.gc();
	 }

   public SudokuImpl getValue() {

     return su;
   }
 }
