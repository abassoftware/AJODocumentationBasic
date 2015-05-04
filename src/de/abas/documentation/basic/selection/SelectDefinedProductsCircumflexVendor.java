package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.common.type.enums.EnumProcurementType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.vendor.Vendor;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to join ambiguous reference fields.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedProductsCircumflexVendor extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Call parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedProductsCircumflexVendor().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);

		SelectionBuilder<Product> selectionBuilder =
				SelectionBuilder.create(Product.class);
		selectionBuilder.add(Conditions.eq(Product.META.procureMode,
				EnumProcurementType.ExternalProcurement));
		selectionBuilder.add(Conditions.eq(Product.META.vendor + "^"
				+ Vendor.META.ctryCode, "F"));

		FieldSet<FieldValueProvider> fieldSet = null;
		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSet =
					FieldSet.of(Product.META.idno.getName(),
							Product.META.swd.getName(),
							Product.META.descrOperLang.getName(),
							Product.META.salesPrice.getName(),
							Product.META.vendor.getName());
		}

		Query<Product> query = dbContext.createQuery(selectionBuilder.build());

		if (fieldSet != null) {
			query.setFields(fieldSet);
		}

		for (Product product : query) {
			dbContext.out().println(
					product.getIdno()
					+ " - "
					+ product.getSwd()
					+ " - "
					+ product.getDescrOperLang()
					+ " - "
					+ product.getSalesPrice()
					+ " - Vendor: "
					+ product.getString(Product.META.vendor + "^"
							+ Vendor.META.swd)
							+ " - "
							+ product.getString(Product.META.vendor + "^"
									+ Vendor.META.ctryCode));
		}
		return 0;
	}

}
