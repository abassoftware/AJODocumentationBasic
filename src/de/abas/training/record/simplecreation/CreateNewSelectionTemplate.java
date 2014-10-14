package de.abas.training.record.simplecreation;

import de.abas.ceks.jedp.CantBeginEditException;
import de.abas.ceks.jedp.CantSaveException;
import de.abas.ceks.jedp.EDPEditor;
import de.abas.erp.common.type.enums.EnumDatabaseDynamicE35;
import de.abas.erp.common.type.enums.EnumOperator;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.schema.company.SelectionTemplateEditor;
import de.abas.erp.db.schema.company.SelectionTemplateEditor.Row;
import de.abas.erp.db.util.LegacyUtil;
import de.abas.training.common.AbstractAjoAccess;

public class CreateNewSelectionTemplate extends AbstractAjoAccess {

	/**
	 * Instantiates CreateNewCustomer class and runs its run()-method as a
	 * client program.
	 *
	 * @param args Method arguments
	 */
	public static void main(String[] args) {
		CreateNewSelectionTemplate createNewSelectionTemplate =
				new CreateNewSelectionTemplate();
		createNewSelectionTemplate.runClientProgram(args);
	}

	@Override
	public void run(String[] args) {
		EDPEditor edpEditor = null;
		SelectionTemplateEditor selectionTemplateEditor = null;
		try {
			// instead of directly calling
			// getDbContext().newObject(SelectionTemplateEditor.class),
			// you need to instantiate an EDPEditor
			edpEditor = LegacyUtil.getSession(getDbContext()).createEditor();
			// you then need to tell the EDPEditor instance
			// to be the EditorObject you want it to be,
			// in this case an instance of SelectionTemplateEditor
			edpEditor.beginEditNew("12", "(SelectionBar)");
			selectionTemplateEditor =
					LegacyUtil.wrap(getDbContext(),
							SelectionTemplateEditor.class, edpEditor);
			// sets fields for new SelectionTemplate
			selectionTemplateEditor.setSwd("MYTASKS");
			selectionTemplateEditor.setDescr("My tasks");
			selectionTemplateEditor
			.setDatabase(EnumDatabaseDynamicE35.Transaction);
			selectionTemplateEditor.setGrpList("86:2");
			// sets row fields for new SelectionTemplate
			// (this is necessary as rows are mandatory in SelectionTemplates)
			Row idnoRow = selectionTemplateEditor.table().appendRow();
			idnoRow.setDenom("idno");
			idnoRow.setCharacteristicName("nummer");
			idnoRow.setOperator(EnumOperator.Normally);
			Row swdRow = selectionTemplateEditor.table().appendRow();
			swdRow.setDenom("swd");
			swdRow.setCharacteristicName("such");
			swdRow.setOperator(EnumOperator.Normally);
			Row editorRow = selectionTemplateEditor.table().appendRow();
			editorRow.setDenom("editor");
			editorRow.setCharacteristicName("bearbeit");
			editorRow.setOperator(EnumOperator.Normally);
			// commits and reopens editor
			selectionTemplateEditor.commitAndReopen();
			// get the new SelectionTemplate's idno
			String idno = selectionTemplateEditor.getIdno();
			// outputs the new SelectioNTemplate's idno
			getDbContext().out().println(
					"The following selection bar was created: " + idno);
			// aborts the selectionTemplateEditor to prevent lock situations
			selectionTemplateEditor.abort();
			// saves the edpEditor if it is active
			if (edpEditor.isActive()) {
				edpEditor.endEditSave();
			}
		}
		catch (CantBeginEditException e) {
			getDbContext().out().println("Error: " + e.getMessage());
		}
		catch (CommandException e) {
			getDbContext().out().println(
					"Error in Editor command: " + "Something went wrong");
		}
		catch (CantSaveException e) {
			getDbContext().out().println("Error: " + e.getMessage());
		}
		finally {
			// aborts the editor if it is active to prevent lock situations
			if (selectionTemplateEditor != null) {
				if (selectionTemplateEditor.active()) {
					selectionTemplateEditor.abort();
				}
			}
			// aborts the editor if it is active to prevent lock situations
			if (edpEditor != null) {
				if (edpEditor.isActive()) {
					edpEditor.breakEditOperation();
					edpEditor.endEditCancel();
				}
			}
		}

	}

}
