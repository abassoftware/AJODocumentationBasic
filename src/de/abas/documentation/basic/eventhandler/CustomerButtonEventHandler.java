package de.abas.documentation.basic.eventhandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.ButtonEventHandler;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.event.ButtonEvent;
import de.abas.erp.axi2.type.ButtonEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

@EventHandler(head = CustomerEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class CustomerButtonEventHandler {

	/**
	 * Logs whenever an email is send to a customer.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Can throw EventException, which might include an
	 * error code, while processing event.
	 */
	@ButtonEventHandler(field = "sendEmail", type = ButtonEventType.AFTER)
	public void sendEmailAfter(ButtonEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		BufferedWriter bufferedWriter = null;
		try {
			Date date = Calendar.getInstance().getTime();
			bufferedWriter =
					new BufferedWriter(new FileWriter("win/tmp/emaillog.txt"));
			bufferedWriter.append(date + ": Mail sent to customer "
					+ head.getIdno() + " " + head.getSwd() + " ("
					+ head.getTelexAddr() + ")");
			bufferedWriter.close();
		}
		catch (IOException e) {
			ctx.out().println("An error occurred: " + e.getMessage());
		}
		finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				}
				catch (IOException e) {
					ctx.out().println(
							"An error occurred while trying to close BufferedWriter instance: "
									+ e.getMessage());
				}
			}
		}
	}

	/**
	 * Checks whether email is valid.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Throws EventException with error message, if email
	 * is invalid.
	 */
	@ButtonEventHandler(field = "sendEmail", type = ButtonEventType.BEFORE)
	public void sendEmailBefore(ButtonEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		// uses regular expression to check whether email is valid
		if (!head.getTelexAddr().matches(
				"[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+")) {
			throw new EventException("Please enter a valid email address.");
		}
	}

}
