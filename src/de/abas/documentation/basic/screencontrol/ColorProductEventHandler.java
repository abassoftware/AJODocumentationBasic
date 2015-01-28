package de.abas.documentation.basic.screencontrol;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.event.FieldEvent;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.common.type.enums.EnumSchedulingMode;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.Color;

@EventHandler(head = ProductEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class ColorProductEventHandler {

	/**
	 * When exiting the schedulingMode field it is determined whether or not the
	 * schedulingMode is set to MinimumStockLevelRelated. If the schedulingMode is
	 * MinimumStockLevelRelated the field minStock is colored.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The ProductEditor instance.
	 * @throws EventException Thrown if an error occurred.
	 */
	@FieldEventHandler(field = "schedulingMode", type = FieldEventType.EXIT)
	public void schedulingModeExit(FieldEvent event, ScreenControl screenControl,
			DbContext ctx, ProductEditor head) throws EventException {
		if (head.getSchedulingMode().equals(
				EnumSchedulingMode.MinimumStockLevelRelated)) {
			screenControl.setColor(head, Product.META.minStock, Color.YELLOW,
					Color.DARK_TEAL);
		}
		else {
			screenControl.setColor(head, Product.META.minStock, Color.DEFAULT,
					Color.DEFAULT);
		}
	}

}
