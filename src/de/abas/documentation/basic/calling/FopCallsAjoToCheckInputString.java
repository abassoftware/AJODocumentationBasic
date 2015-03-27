package de.abas.documentation.basic.calling;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.ContextRunnable;
import de.abas.eks.jfop.remote.FOPSessionContext;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.UserTextBuffer;

/**
 * Shows how to call an AJO program from FOP.
 *
 * @author abas Software AG
 *
 */
public class FopCallsAjoToCheckInputString implements ContextRunnable {

	@Override
	public int runFop(FOPSessionContext arg0, String[] arg1) throws FOPException {
		UserTextBuffer userTextBuffer =
				BufferFactory.newInstance(true).getUserTextBuffer();

		// xtinput and xtmessage have to be defined in FOP
		if (userTextBuffer.isVarDefined("xtinput")
				& userTextBuffer.isVarDefined("xtmessage")) {
			if (!userTextBuffer.getStringValue("xtinput").equals("")) {
				if (!userTextBuffer.getStringValue("xtinput")
						.matches("[a-zA-Z0-9]+")) {
					userTextBuffer.setValue("xtmessage",
							"You can only enter alphabetical letters and numbers.");
					return 1;
				}
				else {
					return 0;
				}
			}
			else {
				userTextBuffer.setValue("xtmessage",
						"You cannot leave this field empty.");
				return 1;
			}
		}
		else {
			userTextBuffer.setValue("xtmessage",
					"xtinput or xtmessage were not defined.");
			return 1;
		}
	}

}
