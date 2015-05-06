package de.abas.documentation.basic.shortcuts;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;

public class ContentAssist extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		String selectionCriteria = "idno=10058!10060;";
		Selection<Product> selection =
				ExpertSelection.create(Product.class, selectionCriteria);

		return 0;
	}
}
