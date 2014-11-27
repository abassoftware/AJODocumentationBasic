package de.abas.documentation.basic.simpleselection;

import de.abas.documentation.basic.common.AbstractAjoAccess;

public class FirstClientApplication extends AbstractAjoAccess {

	public static void main(String[] args) {
		new FirstClientApplication().runClientProgram(args);
	}
	
	@Override
	public void run(String[] args) {
		// creates a client connection
		getDbContext();
		getDbContext().out().println("Client-Application running...");
		
		// prints this method's arguments
		for (int i = 0; i < args.length; i++) {
			getDbContext().out().println(i + " -> " + args[i]);
		}
	}
}
