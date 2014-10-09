package de.abas.training.eventhandler;

import de.abas.erp.api.gui.TextBox;
import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.event.FieldEvent;
import de.abas.erp.axi2.event.FieldFillEvent;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.GlobalTextBuffer;

@EventHandler(head = CustomerEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class CustomerFieldEventHandler {

	/**
	 * EventHandler method for field contactPerson for FieldEventType EXIT. If
	 * the field is filled or changed the address field is changed accordingly.
	 * If there is no c/o line in the address field an c/o line is added
	 * containing the contactPerson. If there is already a c/o line, it is
	 * changed.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Can throw EventException, which might include an
	 * error code, while processing event.
	 */
	@FieldEventHandler(field = "contactPerson", type = FieldEventType.EXIT)
	public void contactPersonExit(FieldEvent event,
			ScreenControl screenControl, DbContext ctx, CustomerEditor head)
			throws EventException {
		if (!head.getContactPerson().isEmpty()) {
			// if address field is empty, c/o line can just be generated into
			// the
			// field
			if (head.getAddr().isEmpty()) {
				head.setAddr("c/o " + head.getContactPerson());
			}
			// if address field already contains c/o line, the c/o line is
			// updated
			else if (head.getAddr().contains("c/o")) {
				head.setAddr(head.getAddr().split("c/o")[0] + "c/o "
						+ head.getContactPerson());
			}
			// if address field already has content but no c/o line, the c/o
			// line is
			// appended
			else {
				head.setAddr(head.getAddr() + "\nc/o "
						+ head.getContactPerson());
			}
		}
		else {
			// if contactPerson field was emptied, the c/o line is removed
			if (head.getAddr().contains("c/o")) {
				head.setAddr(head.getAddr().split("c/o")[0]);
			}
		}
	}

	/**
	 * EventHandler method for field contactPerson for FieldEventType FILL.
	 * Checks whether changes were made to the field and displays TextBox if the
	 * old field value and the new field value differ.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Can throw EventException, which might include an
	 * error code, while processing event.
	 */
	@FieldEventHandler(field = "contactPerson", type = FieldEventType.FILL)
	public void contactPersonFill(FieldFillEvent event,
			ScreenControl screenControl, DbContext ctx, CustomerEditor head)
			throws EventException {
		// access to V-13-00 to get value of evtfeldneu which holds the new
		// value of
		// the current field
		BufferFactory bufferFactory = BufferFactory.newInstance();
		GlobalTextBuffer globalTextBuffer = bufferFactory.getGlobalTextBuffer();
		String contactPersonNewValue =
				globalTextBuffer.getStringValue("evtfeldneu");
		// checks whether old and new value differ and shows TextBox if they do
		if (!head.getContactPerson().equals(contactPersonNewValue)) {
			new TextBox(ctx, "Contact Person changed",
					"You changed the contact person.\n"
							+ "You might also need to change the salutation.")
					.show();
		}
	}

	/**
	 * EventHandler method for field zipCode for FieldEventType VALIDATION.
	 * Checks whether entered zipCode is valid for German customers and hinders
	 * user to leave field as long as zipCode is invalid.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Throws EventException with error message, if
	 * zipCode is invalid.
	 */
	@FieldEventHandler(field = "zipCode", type = FieldEventType.VALIDATION)
	public void zipCodeValidation(FieldEvent event,
			ScreenControl screenControl, DbContext ctx, CustomerEditor head)
			throws EventException {
		if (!head.getZipCode().isEmpty()) {
			if (head.getStateOfTaxOffice().getCtryCode2Char().equals("DE")) {
				// checks entered ZIP code against regular expression
				if (!head.getZipCode().matches(
						"((0[1-9])|([1-9][0-9]))[0-9]{3}")) {
					throw new EventException(
							"The ZIP-code you entered is invalid!");
				}
			}
		}
	}

}
