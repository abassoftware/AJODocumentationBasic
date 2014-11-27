package de.abas.documentation.basic.textbox;

import de.abas.erp.api.gui.ButtonSet;
import de.abas.erp.api.gui.TextBox;
import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.ScreenEventHandler;
import de.abas.erp.axi2.event.ScreenEvent;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.common.type.enums.EnumDialogBox;
import de.abas.erp.common.type.enums.EnumEditorAction;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

/**
 * EventHandler for database Product
 *
 * @author abas Software AG
 *
 */
@EventHandler(head = ProductEditor.class)
@RunFopWith(EventHandlerRunner.class)
public class NewProductEventHandler {

	/**
	 * Checks whether a new product is ready to use.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The ProductEditor instance.
	 * @throws EventException Thrown if an error occurred.
	 */
	@ScreenEventHandler(type = ScreenEventType.VALIDATION)
	public void screenValidation(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, ProductEditor head) throws EventException {
		if (event.getCommand().equals(EnumEditorAction.New)) {
			TextBox textBox =
					new TextBox(ctx, "Product definition completed?",
							"Is this product ready to use?");
			textBox.setButtons(ButtonSet.YES_NO);
			EnumDialogBox boxResult = textBox.show();
			ctx.out().println(boxResult.getDisplayString());
			if (boxResult.equals(EnumDialogBox.No)) {
				if (head.getCode().isEmpty()) {
					head.setCode(",S,");
				}
				else {
					if (head.getCode().endsWith(",")) {
						head.setCode(head.getCode() + "S,");
					}
					else {
						head.setCode(head.getCode() + ",S,");
					}
				}
			}
		}
	}

}
