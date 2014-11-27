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
	public void run(String[] args) {
		// initializes BufferedReader and Buffered Writer
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
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
		try {
			// instantiates BufferedWriter and BufferedReader instances
			// according to the current database context
			bufferedReader = instantiateBufferedReader();
			bufferedWriter = instantiateBufferedWriter();
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
			// closes BufferedReader instance to release system resources
			bufferedWriter.append("Import successfully completed.\n");
			bufferedReader.close();
			bufferedWriter.close();
		}
		catch (FileNotFoundException e) {
			handleException(bufferedReader, bufferedWriter, productEditor, e,
					"Could not retrieve file! For further information refer to log-file.");
		}
		catch (IOException e) {
			handleException(bufferedReader, bufferedWriter, productEditor, e,
					"An I/O error occurred!");
		}
		finally {
			closeBufferedReader(bufferedReader);
			closeBufferedWriter(bufferedWriter);
		}
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
	 * Closes BufferedReader if it is not null and handles IOException.
	 *
	 * @param bufferedReader The BufferedReader to close.
	 */
	protected void closeBufferedReader(BufferedReader bufferedReader) {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			}
			catch (IOException e) {
				context.out().println(e.getMessage());
			}
		}
	}

	/**
	 * Closes BufferedWriter if it is not null and handles IOException.
	 *
	 * @param bufferedWriter The BufferedWriter to close.
	 */
	protected void closeBufferedWriter(BufferedWriter bufferedWriter) {
		if (bufferedWriter != null) {
			try {
				bufferedWriter.close();
			}
			catch (IOException e) {
				context.out().println(e.getMessage());
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
	 * @param bufferedReader The BufferedReader instance.
	 * @param bufferedWriter The BufferedWriter instance.
	 * @param productEditor The ProductEditor instance.
	 * @param exception The exception which occurred.
	 * @param message The message to display.
	 */
	protected void handleException(BufferedReader bufferedReader,
			BufferedWriter bufferedWriter, ProductEditor productEditor,
			Exception exception, String message) {
		// closes BufferedReader instance to release system resources
		closeBufferedReader(bufferedReader);
		context.out().println(message);
		logException(bufferedWriter, message + "\n" + exception.getMessage());
		// aborts the editors to prevent lock situations
		abortEditor(productEditor);
		// closes BufferedWriter instance to release system resources
		closeBufferedWriter(bufferedWriter);
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
			bufferedReader =
					new BufferedReader(new FileReader(CLIENT_TXT_PATH));
		}
		else {
			bufferedReader =
					new BufferedReader(new FileReader(SERVER_TXT_PATH));
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
			bufferedWriter =
					new BufferedWriter(new FileWriter(CLIENT_LOG_PATH));
		}
		else {
			bufferedWriter =
					new BufferedWriter(new FileWriter(SERVER_LOG_PATH));
		}
		return bufferedWriter;
	}

	/**
	 * Logs the exception message
	 *
	 * @param bufferedWriter The BufferedWriter instance.
	 * @param message The exception message.
	 */
	protected void logException(BufferedWriter bufferedWriter, String message) {
		try {
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
		bufferedWriter.write("Product " + product.getIdno() + "("
				+ product.getSwd() + ") successfully created.\n");
	}
}