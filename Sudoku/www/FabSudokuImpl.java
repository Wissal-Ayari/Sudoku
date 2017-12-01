import java.rmi.*;
import java.rmi.server.*;
public class FabSudokuImpl extends UnicastRemoteObject implements FabSudokuInterface
{
       public FabSudokuImpl() throws RemoteException {};
       public SudokuInt newSudoku() throws RemoteException {

    	   return new SudokuImpl();
       }
}
