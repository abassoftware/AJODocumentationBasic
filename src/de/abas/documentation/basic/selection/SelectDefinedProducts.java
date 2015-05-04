package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.selection.SelectionBuilder.Conjunction;
import de.abas.erp.db.settings.DisplayMode;

/**
 * How to use more than one selection criteria and specify the conjunction.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedProducts extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);
		SelectionBuilder<Product> selectionBuilder =
				SelectionBuilder.create(Product.class);

		selectionBuilder
				.add(Conditions.between(Product.META.idno, "10000", "10050"));
		selectionBuilder.add(Conditions.gt(Product.META.salesPrice, 0.0));
		selectionBuilder.setTermConjunction(Conjunction.OR);

		Query<Product> query = dbContext.createQuery(selectionBuilder.build());
		for (Product product : query) {
			dbContext.out().println(
					product.getIdno() + " - " + product.getSwd() + " - "
							+ product.getSalesPrice() + " - "
							+ product.getString(Product.META.salesPriceUnit));
		}
		return 0;
	}

}
