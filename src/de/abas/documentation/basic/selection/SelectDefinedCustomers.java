package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.customer.Customer;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;

/**
 * Shows how to select a specific range of customers by idno.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedCustomers extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedProducts().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		SelectionBuilder<Customer> selectionBuilder =
				SelectionBuilder.create(Customer.class);

		selectionBuilder.add(Conditions
				.between(Customer.META.idno, "20000", "30000"));

		FieldSet<FieldValueProvider> fieldSet = null;
		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSet =
					FieldSet.of(Customer.META.idno.toString(),
							Customer.META.swd.toString(),
							Customer.META.descrOperLang.toString(),
							Customer.META.turnoverFY.toString());
		}
		Query<Customer> query = dbContext.createQuery(selectionBuilder.build());
		if (fieldSet != null) {
			query.setFields(fieldSet);
		}

		for (Customer customer : query) {
			dbContext.out().println(
					customer.getIdno() + " - " + customer.getSwd() + " - "
							+ customer.getDescrOperLang() + " - "
							+ customer.getTurnoverFY());
		}
		return 0;
	}

}
