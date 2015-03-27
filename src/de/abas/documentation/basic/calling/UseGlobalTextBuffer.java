package de.abas.documentation.basic.calling;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.GlobalTextBuffer;

/**
 * Shows how to use the BufferFactory class to get values from the global text
 * buffer.
 *
 * @author abas Software AG
 *
 */
public class UseGlobalTextBuffer extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext ctx = getDbContext();

		BufferFactory bufferFactory = BufferFactory.newInstance(true);

		GlobalTextBuffer globalTextBuffer = bufferFactory.getGlobalTextBuffer();
		ctx.out().println(
				"Client:    " + globalTextBuffer.getStringValue("client"));
		ctx.out().println(
				"User: " + globalTextBuffer.getStringValue("operatorCode"));
		ctx.out().println(
				"Program:   " + globalTextBuffer.getStringValue("execdMainProg"));
		ctx.out().println(
				"Date:      " + globalTextBuffer.getStringValue("date"));
		ctx.out().println(
				"Time:       " + globalTextBuffer.getStringValue("currTime"));

		return 0;
	}
}
