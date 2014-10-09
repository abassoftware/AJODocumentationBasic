package de.abas.training.eventhandler;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.ScreenEventHandler;
import de.abas.erp.axi2.event.ScreenEvent;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.common.type.enums.EnumEditorAction;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.company.Password;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.employee.Employee;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;
import de.abas.erp.db.util.QueryUtil;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
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

}
