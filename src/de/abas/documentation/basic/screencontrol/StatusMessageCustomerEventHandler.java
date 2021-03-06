package de.abas.documentation.basic.screencontrol;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi.screen.ScreenControl.StatusBarArea;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.event.FieldEvent;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.Color;

@EventHandler(head = CustomerEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class StatusMessageCustomerEventHandler {

	/**
	 * Hinders the user to leave the field telexAddr (email) if the entered email
	 * address is not valid. If the validation process was successful, an appropriate
	 * text is displayed in the status bar. The text is displayed in a green font
	 * color.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Thrown if the email address is not valid.
	 */
	@FieldEventHandler(field = "telexAddr", type = FieldEventType.VALIDATION)
	public void telexAddrValidation(FieldEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		if (head.getTelexAddr().matches(
				"^[A-Za-z0-9\\.\\-_]+@[A-Za-z0-9\\.\\-_]+\\.[a-z]+$")) {
			screenControl.setNote("Email address validated.");
			screenControl.setColorStatusBar(StatusBarArea.TEXT, Color.GREEN,
					Color.DEFAULT);
		}
		else {
			if (!head.getTelexAddr().isEmpty()) {
				throw new EventException(
						"The email address you entered is not valid.");
			}
		}
	}
}
