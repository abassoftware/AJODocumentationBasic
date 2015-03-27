package de.abas.documentation.basic.calling;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.UserTextBuffer;

/**
 * Shows how to define new variable in the user text buffer.
 *
 * @author abas Software AG
 *
 */
public class UseUserTextBufferDefineVar extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext ctx = getDbContext();

		// for testing purposes
		//
		// ..!interpreter english translate noabbrev
		// ..*****************************************************************************
		// .. FOP-Name : CHECK.VARIABLE.USER.TEXT.BUFFER
		// .. Date : 27.03.2015
		// .. Author : abas Software AG
		// .. Responsible : abas Software AG
		// .. Supervisor : abas Software AG
		// .. Copyright : (c) 2015
		// .. Function :
		// ..*****************************************************************************
		// .. ow1/CHECK.VARIABLE.USER.TEXT.BUFFER
		// .type text xtname
		// ..
		// .formula U|xtname = "Steven"
		// ..
		// .continue
		//
		// FOe.input("ow1/CHECK.VARIABLE.USER.TEXT.BUFFER");

		BufferFactory bufferFactory = BufferFactory.newInstance(true);
		UserTextBuffer userTextBuffer = bufferFactory.getUserTextBuffer();

		String fieldName = "xtname";
		String type = "text";

		if (userTextBuffer.isVarDefined(fieldName)) {
			ctx.out().println(
					"The variable '" + fieldName + "' is already defined in FO.");
		}
		else {
			userTextBuffer.defineVar(type, fieldName);
		}
		return 0;
	}

}
