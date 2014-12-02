package de.abas.documentation.basic.screencontrol;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.annotation.ScreenEventHandler;
import de.abas.erp.axi2.event.FieldEvent;
import de.abas.erp.axi2.event.ScreenEvent;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.customer.CustomerContact;
import de.abas.erp.db.schema.customer.CustomerContactEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

/**
 * This class shows how to position the cursor using the ScreenControl class.
 *
 * @author abas Software AG
 *
 */
@EventHandler(head = CustomerContactEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class CursorPositioningCustomerContactEventHandler {

	/**
	 * When exiting the addr field the cursor is positioned in the salutation field.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerContactEditor instance.
	 * @throws EventException Thrown if an error occurs.
	 */
	@FieldEventHandler(field = "addr", type = FieldEventType.EXIT)
	public void addrExit(FieldEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerContactEditor head) throws EventException {
		screenControl.moveCursor(head, CustomerContact.META.salutation);
	}

	/**
	 * When exiting the companyARAP field the cursor is positioned in the addr field.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerContactEditor instance.
	 * @throws EventException Thrown if an error occurs.
	 */
	@FieldEventHandler(field = "companyARAP", type = FieldEventType.EXIT)
	public void companyARAPExit(FieldEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerContactEditor head) throws EventException {
		screenControl.moveCursor(head, CustomerContact.META.addr);
	}

	/**
	 * On screen enter the cursor is positioned in the field companyARAP.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerContactEditor instance.
	 * @throws EventException Thrown if an error occurs.
	 */
	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerContactEditor head) throws EventException {
		screenControl.moveCursor(head, CustomerContact.META.companyARAP);
	}

}
