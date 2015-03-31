package de.abas.documentation.basic.bom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.Product.Row;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

public class ProductionListRecursion extends AbstractAjoAccess {

	public static void main(String[] args) {
		ProductionListRecursion productionListRecursion =
				new ProductionListRecursion();
		productionListRecursion.runClientProgram(args);
	}

	DbContext ctx;

	@Override
	public int run(String[] args) {
		ctx = getDbContext();
		BufferedReader bufferedReader = null;
		try {
			ctx.out().println("Enter product idno: ");
			bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String idno = bufferedReader.readLine();
			Product product = getSelectedProduct(idno);
			if (product != null) {
				ctx.out().println(
						"Product: " + product.getIdno() + " - " + product.getSwd());
				printAllLevelsOfProductionList(product, 0);
			}
			else {
				ctx.out().println(
						"The product you were trying to select does not exist!");
				return 1;
			}
		}
		catch (IOException e) {
			ctx.out().println("Could not read from console: " + e.getMessage());
			return 1;
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
					ctx.out().println(
							"An error occurred while trying to close the BufferedReader instance: "
									+ e.getMessage());
				}
			}
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

	/**
	 * Indents according to indentationLevel.
	 *
	 * @param indentationLevel Level of indentation.
	 * @return The indentation as String.
	 */
	private String makeIndentation(int indentationLevel) {
		String indentation = "";
		for (int i = 0; i < indentationLevel; i++) {
			indentation = indentation + "  ";
		}
		return indentation;
	}

	/**
	 * Recursive method to print all levels of a product's production list.
	 *
	 * @param product The current product.
	 * @param indentationLevel The level of indentation.
	 */
	private void
			printAllLevelsOfProductionList(Product product, int indentationLevel) {
		int lowerIndentationLevel = ++indentationLevel;
		Iterable<Row> rows = product.table().getRows();
		for (Row row : rows) {
			ctx.out().println(
					makeIndentation(indentationLevel)
							+ row.getProdListElem().getIdno() + " - "
							+ row.getProductListElem().getSwd());
			if (row.getProdListElem() instanceof Product) {
				printAllLevelsOfProductionList((Product) row.getProdListElem(),
						lowerIndentationLevel);
			}
		}
	}

}
