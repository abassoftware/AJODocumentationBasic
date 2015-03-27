package de.abas.documentation.basic.calling;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.eks.jfop.remote.FOe;
import de.abas.erp.db.DbContext;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.UserTextBuffer;

/**
 * Shows how to set variables in the user text buffer.
 *
 * @author abas Software AG
 *
 */
public class UseUserTextBufferSetDefinedVariable extends AbstractAjoAccess {

	DbContext ctx;

	@Override
	public int run(String[] args) {
		ctx = getDbContext();

		// ..!interpreter english translate noabbrev
		// ..*****************************************************************************
		// .. FOP-Name : DEFINE.VARIABLE.USER.TEXT.BUFFER
		// .. Date : 27.03.2015
		// .. Author : abas Software AG
		// .. Responsible : abas Software AG
		// .. Supervisor : abas Software AG
		// .. Copyright : (c) 2015
		// .. Function :
		// ..*****************************************************************************
		// .. ow1/DEFINE.VARIABLE.USER.TEXT.BUFFER
		// ..
		// .type text xtsurName ? F|defined(U|xtsurName) = G|false
		// .type text xtfirstName ? F|defined(U|xtfirstName) = G|false
		// ..
		// .formula U|xtsurName = "XXX"
		// .formula U|xtfirstName = "YYY"
		// .continue
		FOe.input("ow1/DEFINE.VARIABLE.USER.TEXT.BUFFER");

		BufferFactory bufferFactory = BufferFactory.newInstance(true);
		UserTextBuffer userTextBuffer = bufferFactory.getUserTextBuffer();

		String field = "xtsurName";
		String value = "Miller";
		displayFieldValue(userTextBuffer, field);
		setFieldValue(userTextBuffer, field, value);

		field = "xtfirstName";
		value = "Mike";
		displayFieldValue(userTextBuffer, field);
		setFieldValue(userTextBuffer, field, value);

		// ..!interpreter english translate noabbrev
		// ..*****************************************************************************
		// .. FOP-Name : DISPLAY.VARIABLE.USER.TEXT.BUFFER
		// .. Date : 27.03.2015
		// .. Author : abas Software AG
		// .. Responsible : abas Software AG
		// .. Supervisor : abas Software AG
		// .. Copyright : (c) 2015
		// .. Function :
		// ..*****************************************************************************
		// .. ow1/DISPLAY.VARIABLE.USER.TEXT.BUFFER
		// ..
		// ?-> 'U|xtsurName', 'U|xtfirstName' ? (F|defined(U|xtsurName) &
		// F|defined(U|xtfirstName))
		// ..
		// .continue
		FOe.input("ow1/DISPLAY.VARIABLE.USER.TEXT.BUFFER");

		return 0;
	}

	/**
	 * Displays the specified field in the user text buffer.
	 *
	 * @param userTextBuffer The UserTextBuffer instance.
	 * @param field The field to display.
	 */
	private void displayFieldValue(UserTextBuffer userTextBuffer, String field) {
		ctx.out().println(userTextBuffer.getStringValue(field));

	}

	/**
	 * Sets the value of the specified user text buffer field.
	 *
	 * @param userTextBuffer The UserTextBuffer instance.
	 * @param field The field to set.
	 * @param value The value to set the field to.
	 */
	private void setFieldValue(UserTextBuffer userTextBuffer, String field,
			String value) {
		if (userTextBuffer.isVarDefined(field)) {
			userTextBuffer.setValue(field, value);
		}
	}

}
