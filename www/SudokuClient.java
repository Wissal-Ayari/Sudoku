import java.rmi.*;
import java.rmi.registry.*;
import java.util.Scanner;
public class SudokuClient {
	public SudokuClient (String[] args) {

	     try{

				 if(System.getSecurityManager() == null)
		 			System.setSecurityManager(new RMISecurityManager());
	    Registry reg = LocateRegistry.getRegistry("localhost", 1099);

	    FabSudokuInterface fabrique = (FabSudokuInterface)reg.lookup("Fabrique");



	    SudokuInt sudokuObj;
	    sudokuObj = (SudokuInt) fabrique.newSudoku();
		sudokuObj.fillGrid();


	     }
	     catch (Exception e) {
		System.out.println ("Erreur d'acces a l'objet distant.");
		System.out.println (e.toString());
	     }
     }
}
