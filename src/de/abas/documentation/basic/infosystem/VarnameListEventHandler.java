package de.abas.documentation.basic.infosystem;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.ButtonEventHandler;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.event.ButtonEvent;
import de.abas.erp.axi2.type.ButtonEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.infosystem.custom.ow1.ControlVarnameList;
import de.abas.erp.db.schema.company.Vartab;
import de.abas.erp.db.schema.company.Vartab.Row;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

/**
 * The InfosystemVarnamelistEventHandler handles all registered events for the
 * infosystem VARNAMELIST.
 *
 * The infosystem VARNAMELIST displays all German variable names and their according
 * English names of the variable table entered as filter criterion in the head of the
 * infosystem.
 *
 * @author abas Software AG
 *
 */
@EventHandler(head = ControlVarnameList.class, row = ControlVarnameList.Row.class)
@RunFopWith(EventHandlerRunner.class)
public class VarnameListEventHandler {

	/**
	 * If the start button of the infosystem VARNAMELIST is pressed, after checking a
	 * variable table was selected as basis for the selection, the German and English
	 * variable names of all variables of this variable table are loaded as table
	 * rows.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance
	 * @param ctx The database context.
	 * @param head The ControlVarnameList instance.
	 * @throws EventException The exception thrown if an error occurs.
	 */
	@ButtonEventHandler(field = "start", type = ButtonEventType.AFTER)
	public void startAfter(ButtonEvent event, ScreenControl screenControl,
			DbContext ctx, ControlVarnameList head) throws EventException {
		Vartab vartab = head.getYvartab();
		// only tries to load all variables if a variable table was entered as
		// filter criterion
		if (vartab != null) {
			head.table().clear();
			// iterates the table rows of the selected variable table
			Iterable<Row> rows = vartab.table().getRows();
			for (Row row : rows) {
				de.abas.erp.db.infosystem.custom.ow1.ControlVarnameList.Row appendRow =
						head.table().appendRow();
				appendRow.setTygername(row.getVarName());
				appendRow.setTyengname(row.getVarNameEnglish());
			}
		}
		else {
			screenControl.moveCursor(head, ControlVarnameList.META.yvartab);
			throw new EventException("Please select a variable table first.", 1);
		}
	}

}
