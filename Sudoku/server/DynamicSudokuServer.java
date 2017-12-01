import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.rmi.server.RMIClassLoader;
import java.util.Properties;
public class DynamicSudokuServer {
	public static void main(String[] args) {
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
	       try {
	    Registry registry = LocateRegistry.createRegistry(1099);
	   	System.out.println( "Serveur : Construction de l'implementation ");
		System.out.println("Objet Fabrique lie dans le RMIregistry");
		
		Properties p = System.getProperties();
		String url = p.getProperty("java.rmi.server.codebase");
		Class ClasseServeur = RMIClassLoader.loadClass(url, "FabSudokuImpl");
		Class ClasseServeur1 = RMIClassLoader.loadClass(url, "IServerImp");
		
		registry.rebind("Fabrique", (Remote)ClasseServeur.newInstance());
		registry.rebind("Serveur", (Remote)ClasseServeur.newInstance());
		System.out.println("Serveur prêt");
		System.out.println("Attente des invocations des clients ...");
	       }
	       catch (Exception e) {
		System.out.println("Erreur de liaison de l'objet Compteur");
		System.out.println(e.toString());
	       }
	}
}
