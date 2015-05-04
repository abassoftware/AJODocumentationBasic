package de.abas.documentation.basic.record.objectsfromtxt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import de.abas.documentation.basic.common.AbstractAjoAccess;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.EditorObject;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;

/**
 * Shows how to read product information from file, create the products accordingly
 * and log the progress.
 *
 * @author abas Software AG
 *
 */
public class CreateNewProductsFromTXT extends AbstractAjoAccess {

	public static void main(String[] args) {
		CreateNewProductsFromTXT createNewProductsFromTXT =
				new CreateNewProductsFromTXT();
		createNewProductsFromTXT.runClientProgram(args);
	}

	// database context
	DbContext context = getDbContext();
	// paths for client mode
	private String CLIENT_TXT_PATH = "C:/Users/abas/Documents/New Products.txt";
	private String CLIENT_LOG_PATH = "C:/Users/abas/Documents/Log.txt";
	// paths for server mode
	private String SERVER_TXT_PATH = "owfiles/NEW.PRODUCTS.TXT";

	private String SERVER_LOG_PATH = "owfiles/LOG.TXT";

	@Override
	public int run(String[] args) {
		// Instantiates ProductEditor
		ProductEditor productEditor = null;
		// Contents of current line of file
		String line;
		// All fields
		String[] fields = null;
		// All values
		String[] values;
		// current line number in file
		int lineNo = 0;
		try (BufferedReader bufferedReader = instantiateBufferedReader();
				BufferedWriter bufferedWriter = instantiateBufferedWriter()) {
			// logs start of import
			bufferedWriter.append("Starting import:\n");
			// consecutively reads lines of text file until end of file reached
			while ((line = bufferedReader.readLine()) != null) {
				// the first line of the file contains the fields
				if (lineNo == 0) {
					fields = line.split(";");
				}
				// all further lines of the file contain the values
				else if (lineNo > 0) {
					values = line.split(";");
					// creates products according to field values
					createProduct(productEditor, bufferedWriter, fields, values);
				}
				// increments line number
				lineNo++;
			}
			bufferedWriter.append("Import successfully completed.\n");
		}
		catch (FileNotFoundException e) {
			handleException(productEditor, e,
					"Could not retrieve file! For further information refer to log-file.");
		}
		catch (IOException e) {
			handleException(productEditor, e, "An I/O error occurred!");
		}
		return 0;
	}

	/**
	 * Aborts the editor if it is not null and still active.
	 *
	 * @param editor The editor to abort.
	 */
	protected void abortEditor(EditorObject editor) {
		if (editor != null) {
			if (editor.active()) {
				editor.abort();
			}
		}
	}

	/**
	 * Creates the product according to the field values.
	 *
	 * @param productEditor The ProductEditor instance.
	 * @param bufferedWriter The BufferedWriter instance.
	 * @param fields The fields.
	 * @param values The values.
	 * @throws IOException Throws an IOException if the named file exists but is
	 * a directory rather than a regular file, does not exist but cannot be
	 * created, or cannot be opened for any other reason.
	 */
	protected void createProduct(ProductEditor productEditor,
			BufferedWriter bufferedWriter, String[] fields, String[] values)
			throws IOException {
		productEditor = context.newObject(ProductEditor.class);
		for (int i = 0; i < values.length; i++) {
			productEditor.setString(fields[i], values[i]);
		}
		productEditor.commit();
		logNewProduct(bufferedWriter, productEditor);
	}

	/**
	 * Handles the exceptions.
	 *
	 * @param productEditor The ProductEditor instance.
	 * @param exception The exception which occurred.
	 * @param message The message to display.
	 */
	protected void handleException(ProductEditor productEditor, Exception exception,
			String message) {
		context.out().println(message);
		logException(message + "\n" + exception.getMessage());
		// aborts the editors to prevent lock situations
		abortEditor(productEditor);
	}

	/**
	 * Instantiates the BufferedReader according to the current database
	 * context.
	 *
	 * @return The BufferedReader instance.
	 * @throws FileNotFoundException Throws FileNotFoundException if the named
	 * file does not exist, is a directory rather than a regular file, or for
	 * some other reason cannot be opened for reading.
	 */
	protected BufferedReader instantiateBufferedReader()
			throws FileNotFoundException {
		BufferedReader bufferedReader;
		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			bufferedReader = new BufferedReader(new FileReader(CLIENT_TXT_PATH));
		}
		else {
			bufferedReader = new BufferedReader(new FileReader(SERVER_TXT_PATH));
		}
		return bufferedReader;
	}

	/**
	 * Instantiates the BufferedWriter according to the current database
	 * context.
	 *
	 * @return The BufferedWriter instance.
	 * @throws IOException Throws an IOException if the named file exists but is
	 * a directory rather than a regular file, does not exist but cannot be
	 * created, or cannot be opened for any other reason.
	 */
	protected BufferedWriter instantiateBufferedWriter() throws IOException {
		BufferedWriter bufferedWriter;
		if (getMode().equals(ContextMode.CLIENT_MODE.toString())) {
			bufferedWriter = new BufferedWriter(new FileWriter(CLIENT_LOG_PATH));
		}
		else {
			bufferedWriter = new BufferedWriter(new FileWriter(SERVER_LOG_PATH));
		}
		return bufferedWriter;
	}

	/**
	 * Logs the exception message
	 *
	 * @param message The exception message.
	 */
	protected void logException(String message) {
		try (BufferedWriter bufferedWriter = instantiateBufferedWriter()) {
			bufferedWriter.append(message);
		}
		catch (IOException e) {
			context.out().println(e.getMessage());
		}
	}

	/**
	 * Logs the creation of a product.
	 *
	 * @param bufferedWriter The BufferedWriter instance.
	 * @param productEditor The ProductEditor instance.
	 * @throws IOException Throws an IOException if the named file exists but is
	 * a directory rather than a regular file, does not exist but cannot be
	 * created, or cannot be opened for any other reason.
	 */
	protected void logNewProduct(BufferedWriter bufferedWriter,
			ProductEditor productEditor) throws IOException {
		Product product = productEditor.objectId();
		bufferedWriter.write("Product " + product.getIdno() + "(" + product.getSwd()
				+ ") successfully created.\n");
	}
}