package de.abas.documentation.basic.rowselection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.RowQuery;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.Product.Row;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.RowSelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to define selection criteria for head and table.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedProductsRowSelectionBuilder extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedProductsRowSelectionBuilder().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);

		RowSelectionBuilder<Product, Row> rowSelectionBuilder =
				RowSelectionBuilder.create(Product.class, Product.Row.class);
		rowSelectionBuilder.addForHead(Conditions.between(Product.META.idno,
				"10000", "10100"));
		rowSelectionBuilder.add(Conditions.eq(
				Product.Row.META.productListElem.toString(), "GELENK"));

		FieldSet<FieldValueProvider> fieldSetHead = null;
		FieldSet<FieldValueProvider> fieldSetTable = null;

		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSetHead =
					FieldSet.of(Product.META.idno.getName(),
							Product.META.swd.getName(),
							Product.META.descrOperLang.getName());
			fieldSetTable =
					FieldSet.of(Product.Row.META.productListElem.getName(),
							Product.Row.META.elemQty.getName(),
							Product.Row.META.countUnit.getName());
		}

		RowQuery<Product, Row> query =
				dbContext.createQuery(rowSelectionBuilder.build());

		if ((fieldSetHead != null) & (fieldSetTable != null)) {
			query.setFields(fieldSetTable);
			query.setFieldsForHead(fieldSetHead);
		}

		String tmpIdno = "";
		for (Row row : query) {
			Product product = row.header();
			if (!product.getIdno().equals(tmpIdno)) {
				dbContext.out().println(
						product.getIdno() + " - " + product.getSwd() + " - "
								+ product.getDescrOperLang());
			}
			dbContext.out().println(
					"-- " + row.getProductListElem().getIdno() + " - "
							+ row.getProductListElem().getSwd());

			tmpIdno = product.getIdno();
		}
		return 0;
	}

}
