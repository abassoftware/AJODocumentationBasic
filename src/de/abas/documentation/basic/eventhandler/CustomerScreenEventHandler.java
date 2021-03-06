package de.abas.documentation.basic.eventhandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.abas.eks.jfop.remote.FOe;
import de.abas.erp.api.gui.TextBox;
import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.ScreenEventHandler;
import de.abas.erp.axi2.event.ScreenEndEvent;
import de.abas.erp.axi2.event.ScreenEvent;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.common.type.enums.EnumEditorAction;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.field.TypedField;
import de.abas.erp.db.infosystem.standard.op.OutstandingItems;
import de.abas.erp.db.schema.company.Password;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.employee.Employee;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;
import de.abas.erp.db.util.QueryUtil;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.Color;
import de.abas.jfop.base.buffer.GlobalTextBuffer;
import de.abas.jfop.base.buffer.internal.BufferFactoryImpl;

@EventHandler(head = CustomerEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class CustomerScreenEventHandler {

	/**
	 * Stores the names of the fields stored in the List in a String separating them
	 * with a line break.
	 *
	 * @param fields The list of fields.
	 * @return The string containing the names of the fields.
	 */
	private String getFieldNames(List<TypedField<CustomerEditor>> fields) {
		String listOfFields = "";
		for (final TypedField<CustomerEditor> field : fields) {
			listOfFields += field.getName() + "\n";
		}
		return listOfFields;
	}

	/**
	 * Colors the mandatory field if empty.
	 *
	 * @param screenControl The ScreenControl instance.
	 * @param head The CustomerEditor instance.
	 * @param isEmpty Whether or not the field is empty.
	 * @param field The field.
	 * @param fields The list to add the field to if it is empty.
	 * @return The list of empty fields.
	 */
	private List<TypedField<CustomerEditor>>
	markMandatoryField(ScreenControl screenControl, CustomerEditor head,
			boolean isEmpty, TypedField<CustomerEditor> field,
			List<TypedField<CustomerEditor>> fields) {
		screenControl.setColor(head, field, Color.DEFAULT, Color.DEFAULT);
		if (isEmpty) {
			screenControl.setColor(head, field, Color.DEFAULT, Color.RED);
			fields.add(field);
		}
		return fields;
	}

	/**
	 * Checks whether to mark the mandatory fields.
	 *
	 * @param screenControl The ScreenControl instance.
	 * @param head The CustomerEditor instance.
	 * @param fields The empty list to story unfilled mandatory fields.
	 * @return The list containing all unfilled mandatory fields.
	 */
	private List<TypedField<CustomerEditor>> markMandatoryFields(
			ScreenControl screenControl, CustomerEditor head,
			List<TypedField<CustomerEditor>> fields) {
		fields =
				markMandatoryField(screenControl, head, head.getDescrOperLang()
						.isEmpty(), CustomerEditor.META.descrOperLang, fields);
		fields =
				markMandatoryField(screenControl, head, head.getAddr().isEmpty(),
						CustomerEditor.META.addr, fields);
		fields =
				markMandatoryField(screenControl, head, head.getStreet().isEmpty(),
						CustomerEditor.META.street, fields);
		fields =
				markMandatoryField(screenControl, head, head.getZipCode().isEmpty(),
						CustomerEditor.META.zipCode, fields);
		fields =
				markMandatoryField(screenControl, head, head.getTown().isEmpty(),
						CustomerEditor.META.town, fields);
		fields =
				markMandatoryField(screenControl, head,
						head.getStateOfTaxOffice() == null,
						CustomerEditor.META.stateOfTaxOffice, fields);
		fields =
				markMandatoryField(screenControl, head, head.getContactPerson()
						.isEmpty(), CustomerEditor.META.contactPerson, fields);
		fields =
				markMandatoryField(screenControl, head,
						head.getInhouseContact() == null,
						CustomerEditor.META.inhouseContact, fields);
		fields =
				markMandatoryField(screenControl, head, head.getPayTerm() == null,
				CustomerEditor.META.payTerm, fields);
		return fields;
	}

	/**
	 * Displays customer in View mode if cancel button is pressed.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Thrown to close the current window.
	 */
	@ScreenEventHandler(type = ScreenEventType.CANCEL)
	public void screenCancel(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		FOe.command("-PARALLEL <(Customer)>" + head.getId().toString() + "<(View)>");
		throw new EventException("");
	}

	/**
	 * Checks whether outstanding items for current customer are greater than the
	 * credit limit.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException The exception thrown if an error occurred.
	 */
	@ScreenEventHandler(type = ScreenEventType.END)
	public void screenEnd(ScreenEndEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		final OutstandingItems outstandingItems =
				ctx.openInfosystem(OutstandingItems.class);
		outstandingItems.setBervon(head.getIdno());
		outstandingItems.setBerbis(head.getIdno());
		outstandingItems.invokeStart();
		final BigDecimal ofsoll = outstandingItems.getOfsoll();
		if (ofsoll.compareTo(head.getCredLim()) == 1) {
			new TextBox(ctx, "Credit limit reached",
					"This customers credit limit is exeeded.").show();
		}
	}

	/**
	 * Checks whether current user is inhouse contact of the customer that is going
	 * to be edited.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Throws EventException with error message, if editing
	 * the customer is not permitted for the current user.
	 */
	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		// checks whether screen is in edit mode
		if (event.getCommand() == EnumEditorAction.Edit) {
			// gets the current user's operator code from V-13-00
			final BufferFactoryImpl bufferFactoryImpl = new BufferFactoryImpl(true);
			final GlobalTextBuffer globalTextBuffer =
					bufferFactoryImpl.getGlobalTextBuffer();
			final String operatorCode =
					globalTextBuffer.getStringValue("operatorCode");
			// selects the password configuration with this operator code
			final String criteria = "operID=" + operatorCode + ";@englvar=(Yes)";
			final Selection<Password> selection =
					ExpertSelection.create(Password.class, criteria);
			final Password password = QueryUtil.getFirst(ctx, selection);
			if (password != null) {
				// gets the employee referenced in the password configuration
				final Employee employee = password.getEmployeePwdRef();
				if (employee != null) {
					// compares whether the inhouse contact equals this employee
					if (head.getInhouseContact() != employee) {
						throw new EventException("Action not permitted!");
					}
				}
			}
		}
	}

	/**
	 * Sends email to specified email address if a new customer is created.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Thrown if an error occurred.
	 */
	@ScreenEventHandler(type = ScreenEventType.EXIT)
	public void screenExit(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		if (event.getCommand().equals(EnumEditorAction.New)) {
			if ((head.getInhouseContact() != null)
					&& (!head.getInhouseContact().getEmailAddr().isEmpty())) {
				sendMail(head.getInhouseContact().getEmailAddr(),
						"New customer created: <a href=\"abasurl:<(Customer)>"
								+ head.objectId().getId()
								+ "<(View)>?DDE=001&Client=jasc\">"
								+ head.objectId().getIdno() + " " + head.getSwd()
								+ "</a>", ctx);
			}
		}

	}

	/**
	 * Checks whether all mandatory fields are filled.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException The exception thrown if not all mandatory fields are
	 * filled.
	 */
	@ScreenEventHandler(type = ScreenEventType.VALIDATION)
	public void screenValidation(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		List<TypedField<CustomerEditor>> fields =
				new ArrayList<TypedField<CustomerEditor>>();
		fields = markMandatoryFields(screenControl, head, fields);
		if (!fields.isEmpty()) {
			screenControl.moveCursor(head, fields.get(0));
			final String listOfFields = getFieldNames(fields);
			throw new EventException(
					"The following fields are empty but mandatory:\n" + listOfFields);
		}
	}

	/**
	 * Method to send an email.
	 *
	 * @param to The recipient's email address.
	 * @param message The message to send as body of the email.
	 * @param ctx The database context.
	 */
	public void sendMail(String to, String message, DbContext ctx) {
		String from = "noreply@abas.de";
		String host = "smtp.abas.de";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smpt.port", "25");
		properties.setProperty("mail.smtp.auth", "false");
		Session session = Session.getInstance(properties);
		try {
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(from));
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			mimeMessage.setSubject("New Customer");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
		}
		catch (MessagingException e) {
			ctx.out().println(e.getMessage());
			ctx.out().println(e.getStackTrace());
		}
	}

}
