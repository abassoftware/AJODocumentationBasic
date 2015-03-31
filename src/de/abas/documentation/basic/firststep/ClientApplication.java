package de.abas.documentation.basic.firststep;

import de.abas.erp.db.DbContext;
import de.abas.erp.db.util.ContextHelper;

public class ClientApplication {

	public static void main(String[] args) {

		DbContext ctx =
				ContextHelper.createClientContext("server", 6550, "client",
						"password", "ClientApplication");
		ctx.out().println("ClientApplication");
		ctx.out().println(" running...");

		ctx.close();
	}

}
