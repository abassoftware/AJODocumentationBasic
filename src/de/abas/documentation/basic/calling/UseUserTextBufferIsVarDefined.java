package de.abas.documentation.basic.calling;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.UserTextBuffer;

/**
 * Shows how to check whether a variable is already defined in the user text buffer.
 *
 * @author abas Software AG
 *
 */
public class UseUserTextBufferIsVarDefined extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext ctx = getDbContext();
		BufferFactory bufferFactory = BufferFactory.newInstance(true);
		UserTextBuffer userTextBuffer = bufferFactory.getUserTextBuffer();
		String fieldName = "xtname";
		boolean varDefined = userTextBuffer.isVarDefined(fieldName);
		if (varDefined) {
			ctx.out().println(
					"The variable '" + fieldName
							+ "' is already defined in the user text buffer");
		}
		else {
			ctx.out().println(
					"The variable '" + fieldName
							+ "' is not defined in the user text buffer");
		}
		return 0;
	}

}
