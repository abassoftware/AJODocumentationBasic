package de.abas.documentation.basic.menubuilder;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.api.gui.MenuBuilder;
import de.abas.erp.api.gui.TextBox;
import de.abas.erp.db.DbContext;

/**
 * Shows how to use a MenuBuilder with an object key.
 *
 * @author abas Software AG
 *
 */
public class UseMenuKeyObject extends AbstractAjoAccess {

	@Override
	public void run(String[] args) {
		DbContext ctx = getDbContext();

		MenuBuilder<MyMenuElement> menuBuilder =
				new MenuBuilder<MyMenuElement>(ctx, "Selection");
		createMenu(menuBuilder);
		MyMenuElement select = menuBuilder.show();
		if (select == null) {
			new TextBox(ctx, "Note", "Menu was cancelled").show();
			return;
		}
		ctx.out().println(
				"selected: " + select.getDatabasNo() + ":" + select.getGroupNo()
						+ " -> " + select.getDatabasName() + ":"
						+ select.getGroupName());
	}

	/**
	 * Adds the menu items to the MenuBuilder instance.
	 *
	 * @param menuBuilder The MenuBuilder instance.
	 */
	private void createMenu(MenuBuilder<MyMenuElement> menuBuilder) {
		menuBuilder.addItem(new MyMenuElement("0", "1", "Customer", "Customer"),
				"Customer");
		menuBuilder.addItem(new MyMenuElement("2", "1", "Part", "Product"),
				"Product");
		menuBuilder.addItem(new MyMenuElement("3", "22", "Sales", "SalesOrder"),
				"SalesOrder");
		menuBuilder.addItem(new MyMenuElement("3", "24", "Sales", "Invoice"),
				"Invoice");
	}

}
