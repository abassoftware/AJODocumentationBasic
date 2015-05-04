package de.abas.documentation.basic.selection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.customer.Customer;
import de.abas.erp.db.schema.employee.Employee;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to work with reference fields in selections.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedCustomersJoinInhouseContact extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedCustomersJoinInhouseContact().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);

		SelectionBuilder<Customer> selectionBuilder =
				SelectionBuilder.create(Customer.class);
		selectionBuilder.add(Conditions.eq(
				Customer.META.inhouseContact.join(Employee.META.descrOperLang),
				"Hans-Jürgen Wohlfart"));

		FieldSet<FieldValueProvider> fieldSet = null;
		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSet =
					FieldSet.of(Customer.META.idno.getName(),
							Customer.META.swd.getName(),
							Customer.META.inhouseContact.getName());
		}

		Query<Customer> query = dbContext.createQuery(selectionBuilder.build());

		if (fieldSet != null) {
			query.setFields(fieldSet);
		}

		for (Customer customer : query) {
			dbContext.out().println(
					customer.getIdno() + " - " + customer.getSwd() + " - "
							+ customer.getInhouseContact().getIdno() + " - "
							+ customer.getInhouseContact().getDescrOperLang());
		}
		return 0;
	}

}
