package de.abas.documentation.basic.rowselection;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.FieldSet;
import de.abas.erp.db.FieldValueProvider;
import de.abas.erp.db.RowQuery;
import de.abas.erp.db.schema.qualifications.Qualifications;
import de.abas.erp.db.schema.serviceemployees.Engineer;
import de.abas.erp.db.schema.serviceemployees.Engineer.Row;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.RowSelectionBuilder;
import de.abas.erp.db.settings.DisplayMode;

/**
 * Shows how to use the join method for selection criteria of head and table.
 *
 * @author abas Software AG
 *
 */
public class SelectDefinedEngineersJoinQualification extends AbstractAjoAccess {

	/**
	 * Main method.
	 *
	 * @param args Calling parameters.
	 */
	public static void main(String[] args) {
		new SelectDefinedEngineersJoinQualification().runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		DbContext dbContext = getDbContext();
		dbContext.getSettings().setDisplayMode(DisplayMode.DISPLAY);

		RowSelectionBuilder<Engineer, Row> rowSelectionBuilder =
				RowSelectionBuilder.create(Engineer.class, Engineer.Row.class);
		rowSelectionBuilder.addForHead(Conditions.starts(Engineer.META.swd, "S"));
		rowSelectionBuilder.add(Conditions.eq(Engineer.Row.META.serviceQual
				.join(Qualifications.META.descrOperLang),
				"Sprachkenntnisse in Englisch"));

		FieldSet<FieldValueProvider> fieldSetHead = null;
		FieldSet<FieldValueProvider> fieldSetTable = null;

		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			fieldSetHead =
					FieldSet.of(Engineer.META.idno.getName(),
							Engineer.META.swd.getName());
			fieldSetTable = FieldSet.of(Engineer.Row.META.serviceQual.getName());
		}

		RowQuery<Engineer, Row> rowQuery =
				dbContext.createQuery(rowSelectionBuilder.build());

		if ((fieldSetHead != null) & (fieldSetTable != null)) {
			rowQuery.setFieldsForHead(fieldSetHead);
			rowQuery.setFields(fieldSetTable);
		}

		String tmpIndo = "";
		for (Row row : rowQuery) {

			Engineer engineer = row.header();
			if (!engineer.getIdno().equals(tmpIndo)) {
				dbContext.out().println(
						engineer.getIdno() + " - " + engineer.getSwd());
			}
			dbContext.out().println(
					"-- " + row.getServiceQual().getIdno() + " - "
							+ row.getServiceQual().getDescrOperLang());

			tmpIndo = engineer.getIdno();
		}
		return 0;
	}

}
