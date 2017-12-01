import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuInt extends Remote {

 	void setCheckButton() throws RemoteException;
  void setRestartButton() throws RemoteException;
  void setQuitButton() throws RemoteException;
	void fillGrid() throws RemoteException;
	boolean checkSudokuStatus(int[][] grid) throws RemoteException;
	boolean validate(int[] check) throws RemoteException;


}
