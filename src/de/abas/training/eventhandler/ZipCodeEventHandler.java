package de.abas.training.eventhandler;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.event.FieldEvent;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

/**
 * Customer EventHandler class.
 *
 * @author abas Software AG
 *
 */
@EventHandler(head = CustomerEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class ZipCodeEventHandler {

	/**
	 * Checks whether ZIP code is valid for a specified country code.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Thrown if the ZIP code is invalid.
	 */
	@FieldEventHandler(field = "zipCode", type = FieldEventType.VALIDATION)
	public void zipCodeValidation(FieldEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		String ctryCode = head.getCtryCode();
		if (!head.getZipCode().isEmpty()) {
			if (ctryCode.equals("E")) {
				if (!((head.getZipCode().matches("[1-4][0-9]{4}")
						|| (head.getZipCode().matches("[0][1-9][0-9]{3}")) || (head
								.getZipCode().matches("[5][0-2][0-9]{3}"))))) {
					throw new EventException(
							"ZIP code is invalid! Please enter a valid spanish zip code.",
							1);
				}
			}
			else if (ctryCode.equals("CH")) {
				if (!head.getZipCode().matches("[1-9][0-9]{3}")) {
					throw new EventException(
							"ZIP code is invalid! Please enter a valid swiss zip code.",
							1);
				}
			}
		}
	}

}
