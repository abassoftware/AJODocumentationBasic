package de.abas.documentation.basic.textbox;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.api.gui.TextBox;

/**
 * Shows how to display a simple text box.
 *
 * @author abas Software AG
 *
 */
public class SimpleTextBox extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		TextBox textBox =
				new TextBox(getDbContext(), "Title of the TextBox",
						"Content of the TextBox");
		textBox.show();
		return 0;

	}

}
