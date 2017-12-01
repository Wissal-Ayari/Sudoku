import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class IServerImp  extends UnicastRemoteObject implements IServer {

	public IServerImp() throws RemoteException {

		super();
	}

	public SudokuImpl callMeBack() throws RemoteException {

		Servant servant = new Servant();
		servant.start();
		SudokuImpl su = servant.getValue();
		return su;

	}

}
