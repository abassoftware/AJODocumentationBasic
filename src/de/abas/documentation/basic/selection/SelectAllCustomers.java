package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.customer.Customer;
import de.abas.erp.db.selection.SelectionBuilder;

/**
 * Shows how to select and display information about all customer objects.
 *
 * @author abas Software AG.
 *
 */
public class SelectAllCustomers extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		SelectionBuilder<Customer> selectionBuilder =
				SelectionBuilder.create(Customer.class);

		Query<Customer> query = dbContext.createQuery(selectionBuilder.build());
		for (Customer customer : query) {
			dbContext.out().println(
					customer.getIdno() + " - " + customer.getSwd() + " - "
							+ customer.getDescrOperLang() + " - "
							+ customer.getTurnoverFY());
		}
		return 0;
	}

}
