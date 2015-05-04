package de.abas.documentation.basic.record.objectsfromtxt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.db.EditorObject;
import de.abas.erp.db.schema.company.Summary;
import de.abas.erp.db.schema.customer.CustomerContactEditor;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.regions.RegionCountryEconomicArea;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;
import de.abas.erp.db.util.QueryUtil;

public class CreateNewCustomersFromTXT extends AbstractAjoAccess {

	@Override
	public int run(String[] args) {
		// number of line of text file that is currently read
		int lineNo = 0;
		// arrays in which to store field names and values
		String[] customerFields = null;
		String[] customerContactFields = null;
		// instances of the editors
		CustomerEditor customerEditor = null;
		CustomerContactEditor customerContactEditor = null;

		String line = "";
		String pathToImportFile = "C:/Users/abas/Documents/customer.txt";
		String pathToLogFile = "C:/Users/abas/Documents/log.txt";

		try (BufferedReader bufferedReader =
				new BufferedReader(new FileReader(pathToImportFile));
				BufferedWriter bufferedWriter =
						new BufferedWriter(new FileWriter(pathToLogFile))) {
			bufferedWriter.append("Starting import:\n");
			// consecutively reads lines of text file until end of file reached
			while ((line = bufferedReader.readLine()) != null) {
				// the first line of the text file specifies the fields of the
				// customers which will be created
				if (lineNo == 0) {
					customerFields = getCustomerFields(line);
					log(customerFields, bufferedWriter,
							"Getting customer fields:\nFields: ");
				}
				// the second line of the text file specifies the fields of the
				// customer contacts which will be created
				else if (lineNo == 1) {
					customerContactFields = getCustomerContactFields(line);
					log(customerContactFields, bufferedWriter,
							"\nGetting customer contact fields:\nFields: ");
				}
				// the field values
				else {
					if (lineNo == 2) {
						bufferedWriter.append("\nGetting field values:\n");
					}
					customerEditor =
							getFieldValues(bufferedWriter, customerFields,
									customerContactFields, customerEditor,
									customerContactEditor, line);
				}
				// increments line number
				lineNo++;
			}
			bufferedWriter.append("Import successfully completed.\n");
		}
		catch (FileNotFoundException e) {
			logException(pathToLogFile, e.getMessage());
		}
		catch (IOException e) {
			logException(pathToLogFile, e.getMessage());
		}
		finally {
			abortEditor(customerEditor);
			abortEditor(customerContactEditor);
		}
		return 0;
	}

	/**
	 * Aborts the editor if it is not null and still active.
	 *
	 * @param editor The editor to abort.
	 */
	private void abortEditor(EditorObject editor) {
		if (editor != null) {
			if (editor.active()) {
				editor.abort();
			}
		}
	}

	/**
	 * Gets the customer contact fields from TXT-file.
	 *
	 * @param line The current line of the TXT-file.
	 * @return The customer contact fields as an array.
	 */
	private String[] getCustomerContactFields(String line) {
		String[] customerContactFields;
		customerContactFields = line.substring(1).split(";");
		return customerContactFields;
	}

	/**
	 * Gets customer contact field values from TXT-file.
	 *
	 * @param bufferedWriter The BufferedWriter instance.
	 * @param customerContactFields The customer contact fields to fill.
	 * @param customerEditor The customer editor to link customer contact with
	 * the according customer.
	 * @param line The current line of the TXT-file.
	 * @throws IOException Signals that an I/O exception of some sort has
	 * occurred.
	 */
	private void getCustomerContactValues(BufferedWriter bufferedWriter,
			String[] customerContactFields, CustomerEditor customerEditor,
			String line) throws IOException {
		CustomerContactEditor customerContactEditor;
		String[] customerContactValues;
		customerContactValues = getCustomerContactFields(line);
		log(customerContactValues, bufferedWriter, "");
		// instantiates the CustomerContactEditor
		customerContactEditor =
				getDbContext().newObject(CustomerContactEditor.class);
		// sets the fields
		setCustomerContactFields(customerContactFields, customerContactValues,
				customerEditor, customerContactEditor);
		// stores the new customer contact
		customerContactEditor.commit();
		bufferedWriter.append("\nCustomer contact "
				+ customerContactEditor.objectId().getIdno()
				+ " successfully created.\n");
	}

	/**
	 * Gets the customer fields from TXT-file.
	 *
	 * @param line The current line of the TXT-file.
	 * @return The customer fields as an array.
	 */
	private String[] getCustomerFields(String line) {
		String[] customerFields;
		customerFields = line.split(";");
		return customerFields;
	}

	/**
	 * Gets customer field values from TXT-file.
	 *
	 * @param bufferedWriter The BufferedWriter instance.
	 * @param customerFields The customer fields to fill.
	 * @param line The current line of the TXT-file.
	 * @throws IOException Signals that an I/O exception of some sort has
	 * occurred.
	 */
	private CustomerEditor getCustomerValues(BufferedWriter bufferedWriter,
			String[] customerFields, String line) throws IOException {
		CustomerEditor customerEditor;
		String[] customerValues;
		customerValues = getCustomerFields(line);
		log(customerValues, bufferedWriter, "");
		// instantiates the CustomerEditor
		customerEditor = getDbContext().newObject(CustomerEditor.class);
		// sets the fields
		setCustomerFields(customerFields, customerValues, customerEditor);
		// stores the new customer
		customerEditor.commit();
		bufferedWriter.append("\nCustomer " + customerEditor.objectId().getIdno()
				+ " successfully created.\n");
		return customerEditor;
	}

	/**
	 * Gets the field values from the TXT-file.
	 *
	 * @param bufferedWriter The BufferedWriter instance.
	 * @param customerFields The field names for the customers.
	 * @param customerContactFields The field names for the customer contacts.
	 * @param customerEditor The customer editor
	 * @param customerContactEditor The customer contact editor.
	 * @param line The current line of the TXT-file
	 * @throws IOException Signals that an I/O exception of some sort has
	 * occurred.
	 */
	private CustomerEditor getFieldValues(BufferedWriter bufferedWriter,
			String[] customerFields, String[] customerContactFields,
			CustomerEditor customerEditor,
			CustomerContactEditor customerContactEditor, String line)
			throws IOException {
		// customer field values
		if (!line.startsWith(";")) {
			customerEditor = getCustomerValues(bufferedWriter, customerFields, line);
		}
		// customer contact field values
		else {
			if (customerEditor != null) {
				getCustomerContactValues(bufferedWriter, customerContactFields,
						customerEditor, line);
			}
		}
		return customerEditor;
	}

	/**
	 * Logs the extraction of fields and values from TXT-file.
	 *
	 * @param fields The fields or values from TXT-file.
	 * @param bufferedWriter The BufferedWriter instance
	 * @throws IOException Signals that an I/O exception of some sort has
	 * occurred.
	 */
	private void log(String[] fields, BufferedWriter bufferedWriter, String message)
			throws IOException {
		bufferedWriter.append(message);
		for (String field : fields) {
			bufferedWriter.append(field + " ");
		}
	}

	/**
	 * Logs the exception message
	 *
	 * @param pathToLogFile Path to and name of the log file.
	 * @param message The exception message.
	 */
	private void logException(String pathToLogFile, String message) {
		try (BufferedWriter bufferedWriter =
				new BufferedWriter(new FileWriter(pathToLogFile))) {
			bufferedWriter.append("\nAn error occurred: " + message + "\n");
		}
		catch (IOException e) {
			getDbContext().out().println(e.getMessage());
		}
	}

	/**
	 * Sets all fields specified in customerContactFields as well as the field
	 * descr.
	 *
	 * @param customerContactFields Fields to set.
	 * @param customerContactValues Values of the fields.
	 * @param customerEditor The customer object to refer to.
	 * @param customerContactEditor Instance of CustomerContactEditor to create
	 * new customer contact.
	 */
	private void setCustomerContactFields(String[] customerContactFields,
			String[] customerContactValues, CustomerEditor customerEditor,
			CustomerContactEditor customerContactEditor) {
		customerContactEditor.setCompanyARAP(customerEditor);
		for (int i = 0; i < customerContactFields.length; i++) {
			if (customerContactFields[i].equals("salutation")) {
				setSalutation(customerContactValues[i], customerContactEditor);
			}
			else {
				customerContactEditor.setString(customerContactFields[i],
						customerContactValues[i]);
			}
		}
		customerContactEditor.setDescr(customerContactEditor.getAddr() + ", "
				+ customerContactEditor.getTown());
	}

	/**
	 * Sets all fields specified in customerFields as well as the field descr.
	 *
	 * @param customerFields Fields to set.
	 * @param customerValues Values of the fields.
	 * @param customerEditor Instance of CustomerEditor to create new customer.
	 */
	private void setCustomerFields(String[] customerFields, String[] customerValues,
			CustomerEditor customerEditor) {
		for (int i = 0; i < customerFields.length; i++) {
			if (customerFields[i].equals("stateOfTaxOffice")) {
				setState(customerValues[i], customerEditor);
			}
			else {
				customerEditor.setString(customerFields[i], customerValues[i]);
			}
		}
		customerEditor.setDescr(customerEditor.getAddr() + ", "
				+ customerEditor.getTown());
	}

	/**
	 * Sets salutation according to the abbreviation in the which was specified
	 * in the text file.
	 *
	 * @param salutation Value for salutation provided in text file.
	 * @param customerContactEditor Instance of CustomerContactEditor to create
	 * new customer contact.
	 */
	private void setSalutation(String salutation,
			CustomerContactEditor customerContactEditor) {
		if (salutation.equals("m")) {
			customerContactEditor.setSalutation(getDbContext().load(Summary.class,
					new IdImpl("(173,12,0)")));
		}
		else if (salutation.equals("f")) {
			customerContactEditor.setSalutation(getDbContext().load(Summary.class,
					new IdImpl("(174,12,0)")));
		}
	}

	/**
	 * Sets state according to ISO code provided in text file.
	 *
	 * @param stateISO The ISO code of the state.
	 * @param customerEditor Instance of the CustomerEditor to create new
	 * customer.
	 */
	private void setState(String stateISO, CustomerEditor customerEditor) {
		String criteria = "ctryCode2Char=" + stateISO + ";@englvar=(Yes);";
		Selection<RegionCountryEconomicArea> selection =
				ExpertSelection.create(RegionCountryEconomicArea.class, criteria);
		RegionCountryEconomicArea regionCountryEconomicArea =
				QueryUtil.getFirst(getDbContext(), selection);
		customerEditor.setStateOfTaxOffice(regionCountryEconomicArea);
	}

}
