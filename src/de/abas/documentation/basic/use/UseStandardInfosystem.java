package de.abas.documentation.basic.use;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.infosystem.standard.la.StockLevelInformation;
import de.abas.erp.db.infosystem.standard.la.StockLevelInformation.Row;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;
import de.abas.erp.db.util.QueryUtil;

public class UseStandardInfosystem extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters for this class.
	 */
	public static void main(String[] args) {
		UseStandardInfosystem useStandardInfosystem = new UseStandardInfosystem();
		useStandardInfosystem.runClientProgram(args);
	}

	DbContext ctx;

	@Override
	public int run(String[] args) {
		ctx = getDbContext();

		ctx.getSettings().setDisplayMode(DisplayMode.DISPLAY);

		StockLevelInformation stockLevelInformation =
				ctx.openInfosystem(StockLevelInformation.class);
		stockLevelInformation.setArtikel(getSelectedProduct("10001"));
		stockLevelInformation.invokeStart();

		Iterable<Row> rows = stockLevelInformation.table().getRows();
		for (Row row : rows) {
			ctx.out().println(
					row.getLgruppe().getSwd() + " - " + row.getLager().getSwd()
					+ " - " + row.getLplatz().getSwd() + " - "
					+ row.getLemge() + " - " + row.getString("leinheit"));
		}

		return 0;
	}

	/**
	 * Gets the product with the specific idno.
	 *
	 * @param idno The idno of the product to select.
	 * @return The product with the idno.
	 */
	private Product getSelectedProduct(String idno) {
		SelectionBuilder<Product> selectionBuilder =
				SelectionBuilder.create(Product.class);
		selectionBuilder.add(Conditions.eq(Product.META.idno, idno));
		return QueryUtil.getFirst(ctx, selectionBuilder.build());
	}

}
