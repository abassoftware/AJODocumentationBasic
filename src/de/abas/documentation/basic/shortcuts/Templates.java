package de.abas.documentation.basic.shortcuts;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;

public class Templates extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.out().println("Test");
		return 0;
	}

}
