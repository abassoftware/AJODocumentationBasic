package de.abas.documentation.basic.shortcuts;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.db.schema.part.ProductEditor.Row;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

public class ExtractMethod extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext ctx = getDbContext();

		ProductEditor editor = ctx.newObject(ProductEditor.class);

		// sets head fields
		editor.setSwd("RUNIT");
		editor.setDescr("Tech Runit");
		editor.setPackDimWidth(5);
		editor.setPackDimLength(5);
		editor.setPackDimHeight(2);
		editor.setSalesPrice(326.95);

		// selects product for production list
		String swd = "MYCPU";
		Product product = selectProductBySwd(ctx, swd);

		// sets row fields
		Row row = editor.table().appendRow();
		row.setProdListElem(product);
		row.setElemQty(2);

		// stores new product
		editor.commit();

		return 0;
	}

	/**
	 * @param ctx
	 * @param swd
	 * @return
	 */
	private Product selectProductBySwd(DbContext ctx, String swd) {
		SelectionBuilder<Product> selectionBuilder =
				SelectionBuilder.create(Product.class);
		selectionBuilder.add(Conditions.starts(Product.META.swd, swd));
		Product product = QueryUtil.getFirst(ctx, selectionBuilder.build());
		return product;
	}

}
