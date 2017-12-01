import java.rmi.*;
public interface IServer extends Remote {

	public SudokuImpl callMeBack() throws RemoteException;
}
