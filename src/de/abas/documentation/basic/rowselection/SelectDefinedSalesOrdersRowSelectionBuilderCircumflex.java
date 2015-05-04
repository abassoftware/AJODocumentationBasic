package de.abas.documentation.basic.rowselection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.RowQuery;
import de.abas.erp.db.schema.customer.Customer;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.sales.SalesOrder;
import de.abas.erp.db.schema.sales.SalesOrder.Row;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.RowSelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to join ambiguous reference fields when using selection criteria for
 * head and row fields.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedSalesOrdersRowSelectionBuilderCircumflex extends
AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedSalesOrdersRowSelectionBuilderCircumflex()
		.runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);

		RowSelectionBuilder.create(SalesOrder.class, SalesOrder.Row.class);
		RowSelectionBuilder<SalesOrder, Row> rowSelectionBuilder =
				RowSelectionBuilder.create(SalesOrder.class, SalesOrder.Row.class);

		rowSelectionBuilder.addForHead(Conditions.between(SalesOrder.META.customer
				+ "^" + Customer.META.zipCode, "70000", "79999"));
		rowSelectionBuilder.add(Conditions.eq(SalesOrder.Row.META.product + "^"
				+ Product.META.swd, "KAFFEE"));

		FieldSet<FieldValueProvider> fieldSetHead = null;
		FieldSet<FieldValueProvider> fieldSetTable = null;

		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSetHead =
					FieldSet.of(SalesOrder.META.idno.getName(),
							SalesOrder.META.swd.getName(),
							SalesOrder.META.customer.getName());
			fieldSetTable =
					FieldSet.of(SalesOrder.Row.META.product.getName(),
							SalesOrder.Row.META.unitQty.getName(),
							SalesOrder.Row.META.tradeUnit.getName());
		}

		RowQuery<SalesOrder, Row> rowQuery =
				dbContext.createQuery(rowSelectionBuilder.build());

		if ((fieldSetHead != null) & (fieldSetTable != null)) {
			rowQuery.setFieldsForHead(fieldSetHead);
			rowQuery.setFields(fieldSetTable);
		}

		String tmpIdno = "";
		for (Row row : rowQuery) {
			SalesOrder salesOrder = row.header();
			if (!salesOrder.getIdno().equals(tmpIdno)) {
				dbContext.out().println(
						salesOrder.getIdno() + " - " + salesOrder.getSwd() + " - "
								+ salesOrder.getString("customer^zipCode"));
			}
			dbContext.out().println(
					"-- " + row.getProduct().getIdno() + " - "
							+ row.getString("product^swd") + " - "
							+ row.getUnitQty());
			tmpIdno = salesOrder.getIdno();
		}
		return 0;
	}

}
