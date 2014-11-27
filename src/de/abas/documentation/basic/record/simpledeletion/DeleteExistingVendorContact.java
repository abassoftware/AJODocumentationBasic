package de.abas.documentation.basic.record.simpledeletion;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.schema.vendor.VendorContact;
import de.abas.erp.db.schema.vendor.VendorContactEditor;

public class DeleteExistingVendorContact extends AbstractAjoAccess {

	/**
	 * Instantiates ChangeExistingProduct class and runs its run()-method as a
	 * client program.
	 *
	 * @param args Method arguments
	 */
	public static void main(String[] args) {
		DeleteExistingVendorContact deleteExistingVendorContact =
				new DeleteExistingVendorContact();
		deleteExistingVendorContact.runClientProgram(args);
	}

	@Override
	public void run(String[] args) {
		// loads VendorContact with the ID (261, 1, 0)
		VendorContact vendorContact =
				getDbContext().load(VendorContact.class,
						new IdImpl("(261,1,0)"));
		String idno = vendorContact.getIdno();
		String descr = vendorContact.getDescr();
		VendorContactEditor vendorContactEditor = null;
		try {
			// creates VendorContactEditor
			vendorContactEditor = vendorContact.createEditor();
			// opens VendorContactEditor for deletion
			vendorContactEditor.open(EditorAction.DELETE);
			// commits VendorContactEditor
			vendorContactEditor.commit();
			// displays success message
			getDbContext().out().println(
					"Vendor contact " + idno + " " + descr
							+ " was successfully deleted.");
		}
		catch (CommandException e) {
			getDbContext().out().println(
					"Error\nError in Editor command. Something went wrong.");
		}
		finally {
			// aborts the editor if it is active to prevent lock situations
			if (vendorContactEditor != null) {
				if (vendorContactEditor.active()) {
					vendorContactEditor.abort();
				}
			}
		}
	}

}
