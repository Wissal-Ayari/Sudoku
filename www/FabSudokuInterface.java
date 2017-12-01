import java.rmi.*;
import java.rmi.server.*;
public interface FabSudokuInterface extends Remote
{
	public SudokuInt newSudoku() throws RemoteException;

}
