package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.sales.SalesOrder;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to use a specific key in a selection.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedSalesOrdersUsingKey extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedSalesOrdersUsingKey().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);
		SelectionBuilder<SalesOrder> selectionBuilder =
				SelectionBuilder.create(SalesOrder.class);

		selectionBuilder.setKey(SalesOrder.ENTRY_DATE);

		FieldSet<FieldValueProvider> fieldSet = null;
		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSet = FieldSet.of("idno", "swd", "totalNetAmt", "dateFrom", "curr");
		}

		Query<SalesOrder> query = dbContext.createQuery(selectionBuilder.build());

		if (fieldSet != null) {
			query.setFields(fieldSet);
		}

		for (SalesOrder salesOrder : query) {
			dbContext.out().println(
					salesOrder.getIdno() + " - " + salesOrder.getSwd() + " - "
							+ salesOrder.getDateFrom() + " - "
							+ salesOrder.getTotalNetAmt() + " - "
							+ salesOrder.getString(SalesOrder.META.curr));
		}
		return 0;
	}

}
