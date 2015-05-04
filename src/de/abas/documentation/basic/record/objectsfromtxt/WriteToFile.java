package de.abas.documentation.basic.record.objectsfromtxt;

import java.io.FileWriter;
import java.io.IOException;

import de.abas.documentation.basic.common.AbstractAjoAccess;

/**
 * Shows how to write to a file using a FileWriter instance.
 *
 * @author abas Software AG
 *
 */
public class WriteToFile extends AbstractAjoAccess {

	public static void main(String[] args) {
		WriteToFile writeToFile = new WriteToFile();
		writeToFile.runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		// instantiates the FileWriter instance, automatically closes instance in
		// order to release all system resources associated with it, when the
		// instance is no longer needed
		try (FileWriter fileWriter =
				new FileWriter("C:/Users/abas/Documents/FileWriterTest.txt")) {
			// writes to the file (older method)
			fileWriter.write("The FileWriter class is writing into a file.");
			// writes to the file (newer method)
			fileWriter.append("\nIt is possible to append to the "
					+ "end of a file using the append-method "
					+ "\nor to override the file using the "
					+ "write-method of the FileWriter class.");
		}
		catch (IOException e) {
			getDbContext().out().println(e.getMessage());
		}
		return 0;
	}

}
