package de.abas.documentation.basic.expertselection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.Product.Row;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;

/**
 * Shows how to use expert selection for head fields.
 *
 * @author abas Software AG
 *
 */
public class ExpertSelectionProductCriteria extends AbstractAjoAccess {

	public static void main(String[] args) {
		new ExpertSelectionProductCriteria().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();

		String criteria =
				"productListElem^idno=10062;idno=10058!10060;swd=;@filingmode=(Active);@rows=(No)";
		Selection<Product> selection =
				ExpertSelection.create(Product.class, criteria);

		Query<Product> query = dbContext.createQuery(selection);

		for (Product product : query) {
			dbContext.out().println(
					product.getIdno() + " - " + product.getSwd() + " - "
							+ product.getDispatcherProd().getAddr() + " - "
							+ product.getDescrOperLang());

			Iterable<Row> rows = product.table().getRows();
			for (Row row : rows) {
				dbContext.out().println(
						"-- " + row.getProductListElem().getIdno() + " - "
								+ row.getProductListElem().getSwd() + " - "
								+ row.getElemQty());
			}
		}
		return 0;
	}

}
