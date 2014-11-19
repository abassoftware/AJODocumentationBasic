package de.abas.training.textbox;

import de.abas.erp.api.gui.TextBox;
import de.abas.training.common.AbstractAjoAccess;

/**
 * Shows how to display a simple text box.
 *
 * @author abas Software AG
 *
 */
public class SimpleTextBox extends AbstractAjoAccess {

	@Override
	public void run(String[] args) {
		TextBox textBox =
				new TextBox(getDbContext(), "Title of the TextBox",
						"Content of the TextBox");
		textBox.show();

	}

}
