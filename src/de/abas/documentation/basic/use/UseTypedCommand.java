package de.abas.documentation.basic.use;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.common.type.AbasDate;
import de.abas.erp.common.type.enums.EnumEntryTypeStockAdjustment;
import de.abas.erp.common.type.enums.EnumTypeCommands;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.EditorCommand;
import de.abas.erp.db.EditorCommandFactory;
import de.abas.erp.db.EditorObject;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.storagequantity.StockAdjustmentEditor;
import de.abas.erp.db.schema.storagequantity.StockAdjustmentEditor.Row;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

public class UseTypedCommand extends AbstractAjoAccess {

	public static void main(String[] args) {
		UseTypedCommand useTypedCommand = new UseTypedCommand();
		useTypedCommand.runClientProgram(args);
	}

	DbContext ctx;

	@Override
	public int run(String[] args) {
		ctx = getDbContext();

		EditorCommand editorCommand =
				EditorCommandFactory.typedCmd(EnumTypeCommands.Stockadjustment);

		try {
			EditorObject editorObject = ctx.openEditor(editorCommand);
			if (editorObject instanceof StockAdjustmentEditor) {
				StockAdjustmentEditor stockAdjustmentEditor =
						(StockAdjustmentEditor) editorObject;

				stockAdjustmentEditor.setProduct(getSelectedProduct("10001"));
				stockAdjustmentEditor.setDocNo("UseTypedCommand_example");
				stockAdjustmentEditor.setDateDoc(new AbasDate());
				stockAdjustmentEditor
				.setEntType(EnumEntryTypeStockAdjustment.Receipt);

				stockAdjustmentEditor.table().clear();

				Row appendRow = stockAdjustmentEditor.table().appendRow();
				appendRow.setUnitQty(1);
				appendRow.setString(StockAdjustmentEditor.Row.META.location2,
						"L01.002");

				stockAdjustmentEditor.commit();
				ctx.out().println("Stock adjustment successful");

			}
		}
		catch (CommandException e) {
			ctx.out().println(e.getMessage());
			return 1;
		}

		return 0;
	}

	/**
	 * Gets the product with the specific idno.
	 *
	 * @param idno The idno of the product to select.
	 * @return The product with the idno.
	 */
	private Product getSelectedProduct(String idno) {
		SelectionBuilder<Product> selectionBuilder =
				SelectionBuilder.create(Product.class);
		selectionBuilder.add(Conditions.eq(Product.META.idno, idno));
		return QueryUtil.getFirst(ctx, selectionBuilder.build());
	}

}
