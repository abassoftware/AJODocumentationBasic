package de.abas.training.eventhandler;

import java.util.ArrayList;
import java.util.List;

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
	 * Checks whether current user is inhouse contact of the customer that is
	 * going to be edited.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException Throws EventException with error message, if
	 * editing the customer is not permitted for the current user.
	 */
	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		// checks whether screen is in edit mode
		if (event.getCommand() == EnumEditorAction.Edit) {
			// gets the current user's operator code from V-13-00
			BufferFactoryImpl bufferFactoryImpl = new BufferFactoryImpl(true);
			GlobalTextBuffer globalTextBuffer =
					bufferFactoryImpl.getGlobalTextBuffer();
			String operatorCode =
					globalTextBuffer.getStringValue("operatorCode");
			// selects the password configuration with this operator code
			String criteria = "operID=" + operatorCode + ";@englvar=(Yes)";
			Selection<Password> selection =
					ExpertSelection.create(Password.class, criteria);
			Password password = QueryUtil.getFirst(ctx, selection);
			if (password != null) {
				// gets the employee referenced in the password configuration
				Employee employee = password.getEmployeePwdRef();
				if (employee != null) {
					// compares whether the inhouse contact equals this employee
					if (head.getInhouseContact() != employee) {
						throw new EventException("Action not permitted!");
					}
				}
			}
		}
	}

	@ScreenEventHandler(type = ScreenEventType.CANCEL)
	public void screenCancel(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		// TODO Auto-generated method stub
	}

	@ScreenEventHandler(type = ScreenEventType.END)
	public void screenEnd(ScreenEndEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		// TODO Auto-generated method stub
	}

	@ScreenEventHandler(type = ScreenEventType.EXIT)
	public void screenExit(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, CustomerEditor head) throws EventException {
		// TODO Auto-generated method stub
	}

	/**
	 * Checks whether all mandatory fields are filled.
	 * 
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The CustomerEditor instance.
	 * @throws EventException The exception thrown if not all mandatory fields are filled.
	 */
	@ScreenEventHandler(type = ScreenEventType.VALIDATION)
	public void screenValidation(ScreenEvent event,
			ScreenControl screenControl, DbContext ctx, CustomerEditor head)
			throws EventException {
		List<TypedField<CustomerEditor>> fields = new ArrayList<TypedField<CustomerEditor>>();
		fields = markMandatoryFields(screenControl, head, fields);
		if(!fields.isEmpty()) {
			screenControl.moveCursor(head, fields.get(0));
			String listOfFields = getFieldNames(fields);
			throw new EventException("The following fields are empty but mandatory:\n" + listOfFields);
		}
	}

	/**
	 * Stores the names of the fields stored in the List in a String separating them with a line break.
	 * 
	 * @param fields The list of fields.
	 * @return The string containing the names of the fields.
	 */
	private String getFieldNames(List<TypedField<CustomerEditor>> fields) {
		String listOfFields = "";
		for (TypedField<CustomerEditor> field : fields) {
			listOfFields += field.getName() + "\n";
		}
		return listOfFields;
	}

	/**
	 * Checks whether to mark the mandatory fields.
	 * 
	 * @param screenControl The ScreenControl instance.
	 * @param head The CustomerEditor instance.
	 * @param fields The empty list to story unfilled mandatory fields.
	 * @return The list containing all unfilled mandatory fields.
	 */
	private List<TypedField<CustomerEditor>> markMandatoryFields(ScreenControl screenControl,
			CustomerEditor head, List<TypedField<CustomerEditor>> fields) {
		fields = markMandatoryField(screenControl, head, head.getDescrOperLang().isEmpty(),
				CustomerEditor.META.descrOperLang, fields);
		fields = markMandatoryField(screenControl, head, head.getAddr().isEmpty(),
				CustomerEditor.META.addr, fields);
		fields = markMandatoryField(screenControl, head, head.getStreet().isEmpty(),
				CustomerEditor.META.street, fields);
		fields = markMandatoryField(screenControl, head, head.getZipCode().isEmpty(),
				CustomerEditor.META.zipCode, fields);
		fields = markMandatoryField(screenControl, head, head.getTown().isEmpty(),
				CustomerEditor.META.town, fields);
		fields = markMandatoryField(screenControl, head, head.getStateOfTaxOffice() == null,
				CustomerEditor.META.stateOfTaxOffice, fields);
		fields = markMandatoryField(screenControl, head, head.getContactPerson().isEmpty(),
				CustomerEditor.META.contactPerson, fields);
		fields = markMandatoryField(screenControl, head, head.getInhouseContact() == null,
				CustomerEditor.META.inhouseContact, fields);
		fields = markMandatoryField(screenControl, head, head.getPayTerm() == null,
				CustomerEditor.META.payTerm, fields);
		return fields;
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
	private List<TypedField<CustomerEditor>> markMandatoryField(ScreenControl screenControl,
			CustomerEditor head, boolean isEmpty,
			TypedField<CustomerEditor> field, List<TypedField<CustomerEditor>> fields) {
		screenControl.setColor(head, field, Color.DEFAULT, Color.DEFAULT);
		if (isEmpty) {
			screenControl.setColor(head, field, Color.DEFAULT, Color.RED);
			fields.add(field);
		}
		return fields;
	}

}
