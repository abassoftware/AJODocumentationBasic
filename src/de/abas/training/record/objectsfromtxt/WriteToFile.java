package de.abas.training.record.objectsfromtxt;

import java.io.FileWriter;
import java.io.IOException;

import de.abas.training.common.AbstractAjoAccess;

public class WriteToFile extends AbstractAjoAccess {

	public static void main(String[] args) {
		WriteToFile writeToFile = new WriteToFile();
		writeToFile.runClientProgram(args);
	}

	@Override
	public void run(String[] args) {
		FileWriter fileWriter = null;
		try {
			// the path of the file to write to
			String pathAndFile = "C:/Users/abas/Documents/FileWriterTest.txt";
			// the file writer instance
			fileWriter = new FileWriter(pathAndFile);
			// writes into the file, overriding existing content
			fileWriter.write("The FileWriter class is writing into a file.");
			// appends to the end of the file
			fileWriter.append("\nIt is possible to append to the "
					+ "end of a file using the append-method "
					+ "\nor to override the file using the "
					+ "write-method of the FileWriter class.");
			// closes the FileWriter instance in order to release all system
			// resources associated with it
			fileWriter.close();
		}
		catch (IOException e) {
			// the FileWriter instance must be closed in order to release all
			// system resources associated with it
			closeFileWriter(fileWriter);
			getDbContext().out().println(e.getMessage());
		}
		finally {
			// the FileReader instance must be closed in order to release all
			// system resources associated with it
			closeFileWriter(fileWriter);
		}
	}

	/**
	 * Closes FileReader if it is not null and handles IOException.
	 *
	 * @param fileWriter The FileWriter to close.
	 */
	protected void closeFileWriter(FileWriter fileWriter) {
		if (fileWriter != null) {
			try {
				fileWriter.close();
			}
			catch (IOException e) {
				getDbContext().out().println(e.getMessage());
			}
		}
	}

}
