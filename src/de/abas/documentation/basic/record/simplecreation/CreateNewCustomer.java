package de.abas.documentation.basic.record.simplecreation;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.schema.customer.CustomerEditor;

public class CreateNewCustomer extends AbstractAjoAccess {

	/**
	 * Instantiates CreateNewCustomer class and runs its run()-method as a
	 * client program.
	 *
	 * @param args Method arguments
	 */
	public static void main(String[] args) {
		CreateNewCustomer createNewCustomer = new CreateNewCustomer();
		createNewCustomer.runClientProgram(args);
	}

	@Override
	public void run(String[] args) {
		CustomerEditor newCustomer = null;
		try {
			// gets instance of CustomerEditor
			newCustomer = getDbContext().newObject(CustomerEditor.class);
			// sets fields for new customer
			newCustomer.setSwd("SAVACENTER");
			newCustomer.setDescr("Sav-A-Center, Springfield");
			newCustomer.setAddr("Sav-A-Center");
			newCustomer.setStreet("4716 Nickel Road");
			newCustomer.setZipCode("90017");
			newCustomer.setTown("Los Angeles");
			newCustomer.setCtryCode("US");
			// commits and reopens editor
			newCustomer.commitAndReopen();
			// gets the new customer's idno
			String idno = newCustomer.getIdno();
			// aborts the CustomerEditor to prevent lock situations
			newCustomer.abort();
			// outputs the new customer's idno
			getDbContext().out().println(
					"The following customer was created: " + idno);
		}
		catch (CommandException e) {
			getDbContext().out().println(
					"Error in editor command: " + "Something went wrong.");
		}
		finally {
			// aborts the editor if it is active to prevent lock situations
			if (newCustomer != null) {
				if (newCustomer.active()) {
					newCustomer.abort();
				}
			}
		}

	}

}
