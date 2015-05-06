package de.abas.documentation.basic.expertselection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.Product.Row;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;

/**
 * Shows how to use expert selection and define row selection criteria.
 *
 * @author abas Software AG
 *
 */
public class ExpertSelectionProductRowCriteria extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();

		String criteria =
				"productListElem^idno=10062;idno=10058!10060;swd=;"
						+ "@filingmode=(Active);@rows=(Yes)";
		Selection<Row> selection =
				ExpertSelection.create(Product.Row.class, criteria);

		Query<Row> query = dbContext.createQuery(selection);
		String tmpIdno = "";
		for (Row row : query) {
			Product product = row.header();
			if (!tmpIdno.equals(product.getIdno())) {
				// Head
				dbContext.out().println(
						product.getIdno() + " - " + product.getSwd() + " - "
								+ product.getDescrOperLang());
			}

			// Table
			dbContext.out().println(
					"-- " + row.getProductListElem().getIdno() + " - "
							+ row.getProductListElem().getSwd() + " - "
							+ row.getProductListElem().getString("salesPrice"));

			tmpIdno = product.getIdno();
		}
		return 0;
	}

}
