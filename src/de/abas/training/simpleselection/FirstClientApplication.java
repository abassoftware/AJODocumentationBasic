package de.abas.training.simpleselection;

import de.abas.training.common.AbstractAjoAccess;

public class FirstClientApplication extends AbstractAjoAccess {

	public static void main(String[] args) {
		new FirstClientApplication().runClientProgram(args);
	}
	
	@Override
	public void run(String[] args) {
		// Erzeugt eine Client-Verbindung zum Mandant
		getDbContext();
		getDbContext().out().println("Client-Application running...");
		
		
		// Aufrufparameter ausgeben
		for (int i = 0; i < args.length; i++) {
			getDbContext().out().println(i + " -> " + args[i]);
		}
	}
}
