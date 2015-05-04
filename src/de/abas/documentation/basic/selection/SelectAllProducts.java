package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.Product.Row;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to select all products and display information about each product's head
 * and table.
 *
 * @author abas Software AG
 *
 */
public class SelectAllProducts extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);
		SelectionBuilder<Product> selectionBuilder =
				SelectionBuilder.create(Product.class);

		Query<Product> query = dbContext.createQuery(selectionBuilder.build());
		for (Product product : query) {
			// Head
			dbContext.out().println(
					product.getIdno() + " - " + product.getSwd() + " - "
							+ product.getDescrOperLang() + " - "
							+ product.getSalesPrice());
			// Table
			Iterable<Row> rows = product.table().getRows();
			for (Row row : rows) {
				dbContext.out().println(
						"--" + row.getProductListElem().getIdno() + " - "
								+ row.getProductListElem().getSwd() + " - "
								+ row.getElemQty() + " - " + row.getCountUnit()
								+ " - " + row.getString(Product.Row.META.countUnit));
			}
		}
		return 0;
	}

}
