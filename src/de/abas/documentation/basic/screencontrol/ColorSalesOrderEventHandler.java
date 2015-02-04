package de.abas.documentation.basic.screencontrol;

import java.math.BigDecimal;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.event.FieldEvent;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.sales.SalesOrder;
import de.abas.erp.db.schema.sales.SalesOrderEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.Color;

@EventHandler(head = SalesOrderEditor.class, row = SalesOrderEditor.Row.class)
@RunFopWith(EventHandlerRunner.class)
public class ColorSalesOrderEventHandler {

	/**
	 * The current table row is colored if the sales price of the chosen product is
	 * 0.00 and the sales price field's font color is set to red.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The SalesOrderEditor instance.
	 * @param currentRow The current table row's SalesOrderEditor.Row instance.
	 * @throws EventException Thrown if an error occurred.
	 */
	@FieldEventHandler(field = "product", type = FieldEventType.EXIT, table = true)
	public void rowProductExit(FieldEvent event, ScreenControl screenControl,
			DbContext ctx, SalesOrderEditor head, SalesOrderEditor.Row currentRow)
			throws EventException {
		if (currentRow.getPrice().compareTo(BigDecimal.ZERO) == 0) {
			screenControl.setColor(currentRow, Color.YELLOW, Color.TEAL);
			screenControl.setColor(currentRow, SalesOrder.Row.META.price, Color.RED,
					Color.DEFAULT);
		}
		else {
			screenControl.setColor(currentRow, Color.DEFAULT, Color.DEFAULT);
			screenControl.setColor(currentRow, SalesOrder.Row.META.price,
					Color.DEFAULT, Color.DEFAULT);
		}
	}

}
