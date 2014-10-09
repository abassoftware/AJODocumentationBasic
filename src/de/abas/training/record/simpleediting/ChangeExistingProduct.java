package de.abas.training.record.simpleediting;

import de.abas.erp.api.gui.TextBox;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.training.common.AbstractAjoAccess;

public class ChangeExistingProduct extends AbstractAjoAccess {

	/**
	 * Instantiates ChangeExistingProduct class and runs its run()-method as a
	 * client program.
	 *
	 * @param args Method arguments
	 */
	public static void main(String[] args) {
		ChangeExistingProduct changeExistingProduct =
				new ChangeExistingProduct();
		changeExistingProduct.runClientProgram();
	}

	@Override
	public void run() {
		// loads the product with the id (1,2,0)
		Product product =
				getDbContext().load(Product.class, new IdImpl("(1,2,0)"));
		ProductEditor changeProduct = null;
		try {
			// initiates ProductEditor
			changeProduct = product.createEditor();
			// opens ProductEditor in UPDATE mode
			changeProduct.open(EditorAction.UPDATE);
			// sets new field values
			changeProduct.setPackDimLength(2.5);
			changeProduct.setPackDimHeight(0.1);
			changeProduct.setPackDimWidth(0.1);
			// commits ProductEditor
			changeProduct.commit();
			// outputs success message
			new TextBox(getDbContext(), "Success", "Product with idno "
					+ product.getIdno() + " was successfully changed.").show();
		}
		catch (CommandException e) {
			getDbContext().out().println(
					"Error\nError in Editor command. "
							+ "Something went wrong.");
		}
		finally {
			// aborts the editor if it is active to prevent lock situations
			if (changeProduct != null) {
				if (changeProduct.active()) {
					changeProduct.abort();
				}
			}
		}
	}

}
