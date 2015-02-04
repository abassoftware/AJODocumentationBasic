package de.abas.documentation.basic.screencontrol;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.ScreenEventHandler;
import de.abas.erp.axi2.event.ScreenEvent;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.infosystem.custom.ow1.ControlVarnameList;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.jfop.base.buffer.BufferFactory;
import de.abas.jfop.base.buffer.GlobalTextBuffer;

@EventHandler(head = ControlVarnameList.class, row = ControlVarnameList.Row.class)
@RunFopWith(EventHandlerRunner.class)
public class VarnameListEventHandler {

	/**
	 * Protects start button of infosystem for all users whose operator code does not
	 * match CK, CH, TK or ABAS.
	 *
	 * @param event The event that occurred.
	 * @param screenControl The ScreenControl instance.
	 * @param ctx The database context.
	 * @param head The InfosystemVARNAMELIST instance.
	 * @throws EventException Thrown if an error occurred.
	 */
	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl,
			DbContext ctx, ControlVarnameList head) throws EventException {

		// access to global variable table (V-13-00)
		BufferFactory bufferFactory = BufferFactory.newInstance();
		GlobalTextBuffer globalTextBuffer = bufferFactory.getGlobalTextBuffer();
		String operatorID = globalTextBuffer.getStringValue("operatorCode");

		// protect start button if users do not have operator ID CK, CH, TK or ABAS
		if (!(operatorID.equals("CK") || operatorID.equals("CH")
				|| operatorID.equals("TK") || operatorID.equals("ABAS"))) {
			screenControl.setProtection(head, ControlVarnameList.META.start, true);
		}
	}
}
