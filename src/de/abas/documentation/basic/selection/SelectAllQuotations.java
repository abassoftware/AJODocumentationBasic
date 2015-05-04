package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.sales.Quotation;
import de.abas.erp.db.schema.sales.Quotation.Row;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to select and display information about each quotation element's head
 * and table.
 *
 * @author abas Software AG
 *
 */
public class SelectAllQuotations extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);
		SelectionBuilder<Quotation> selectionBuilder =
				SelectionBuilder.create(Quotation.class);

		Query<Quotation> query = dbContext.createQuery(selectionBuilder.build());
		for (Quotation quotation : query) {
			// Head
			dbContext.out().println(
					quotation.getIdno() + " - " + quotation.getSwd() + " - "
							+ quotation.getDateFrom());
			// Table
			Iterable<Row> rows = quotation.table().getRows();
			for (Row row : rows) {
				dbContext.out().println(
						"--" + row.getProduct().getIdno() + " - "
								+ row.getProduct().getSwd() + " - "
								+ row.getUnitQty() + " - "
								+ row.getString(Quotation.Row.META.tradeUnit));
			}
		}
		return 0;
	}

}
