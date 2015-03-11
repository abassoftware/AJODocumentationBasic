package de.abas.documentation.basic.menubuilder;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.api.gui.MenuBuilder;
import de.abas.erp.db.DbContext;

/**
 * Shows how to use a MenuBuilder with a String key.
 *
 * @author abas Software AG
 *
 */
public class UseMenuKeyString extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext ctx = getDbContext();

		MenuBuilder<String> menuBuilder = new MenuBuilder<String>(ctx, "Selection");
		createMenu(menuBuilder);
		String select = menuBuilder.show();
		ctx.out().println("selected: " + select);

		return 0;
	}

	/**
	 * Adds items to MenuBuilder instance.
	 *
	 * @param menuBuilder Instance of MenuBuilder.
	 */
	private void createMenu(MenuBuilder<String> menuBuilder) {
		menuBuilder.addItem("1", "Customer");
		menuBuilder.addItem("2", "Product");
		menuBuilder.addItem("3", "SalesOrder");
		menuBuilder.addItem("4", "Invoice");
	}
}
