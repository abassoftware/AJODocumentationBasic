package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.Order;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to sort a selection's result set.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedProductsSorted extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedProductsSorted().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);
		SelectionBuilder<Product> selectionBuilder =
				SelectionBuilder.create(Product.class);

		selectionBuilder.add(Conditions.notEmpty(Product.META.salesPrice));
		selectionBuilder.addOrder(Order.desc(Product.META.salesPrice));

		FieldSet<FieldValueProvider> fieldSet = null;

		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSet =
					FieldSet.of(Product.META.idno.toString(),
							Product.META.swd.toString(),
							Product.META.salesPrice.toString(),
							Product.META.salesPriceUnit.toString());
		}

		Query<Product> query = dbContext.createQuery(selectionBuilder.build());

		if (fieldSet != null) {
			query.setFields(fieldSet);
		}

		for (Product product : query) {
			dbContext.out().println(
					product.getIdno() + " - " + product.getSwd() + " - "
							+ product.getSalesPrice() + " - "
							+ product.getString(Product.META.salesPriceUnit));
		}
		return 0;
	}

}
