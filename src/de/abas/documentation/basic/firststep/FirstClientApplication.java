package de.abas.documentation.basic.firststep;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;

public class FirstClientApplication extends AbstractAjoAccess {

	public static void main(String[] args) {
		new FirstClientApplication().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext ctx = getDbContext();
		ctx.out().println("Client application running...");
		// displays calling parameters
		for (int i = 0; i < args.length; i++) {
			ctx.out().println(i + " - " + args[i]);
		}
		return 0;
	}

}
