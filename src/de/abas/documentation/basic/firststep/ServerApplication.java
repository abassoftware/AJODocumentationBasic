package de.abas.documentation.basic.firststep;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.ContextRunnable;
import de.abas.eks.jfop.remote.FOPSessionContext;
import de.abas.erp.db.DbContext;

public class ServerApplication implements ContextRunnable {
	@Override
	public int runFop(FOPSessionContext ctx, String[] args) throws FOPException {
		DbContext dbContext = ctx.getDbContext();
		dbContext.out().println("ServerApplication");
		dbContext.out().println(" running...");
		return 0;
	}
}