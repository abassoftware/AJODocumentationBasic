package de.abas.documentation.basic.calling;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.eks.jfop.remote.FOe;
import de.abas.erp.db.DbContext;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.UserTextBuffer;

/**
 * Shows how to call a FOP within an AJO program.
 *
 * @author abas Software AG
 *
 */
public class AjoCallsFopAssign extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext ctx = getDbContext();
		FOe.input("ow1/FOP.ASSIGN.VALUE");

		UserTextBuffer userTextBuffer =
				BufferFactory.newInstance(true).getUserTextBuffer();
		if (userTextBuffer.isVarDefined("xrvalue")) {
			double value = userTextBuffer.getDoubleValue("xrvalue");
			ctx.out().println("R7.2: " + value);
		}

		return 0;
	}

}
