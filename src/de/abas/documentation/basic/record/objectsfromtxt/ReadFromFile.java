package de.abas.documentation.basic.record.objectsfromtxt;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import de.abas.documentation.basic.common.AbstractAjoAccess;

public class ReadFromFile extends AbstractAjoAccess {

	public static void main(String[] args) {
		ReadFromFile readFromFile = new ReadFromFile();
		readFromFile.runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		// String in which to store content of file
		String fileContent = "";
		// Stores the current character as integer as it is returned by
		// FileReader.read()
		int intContent;
		// instantiates a FileReader
		FileReader fileReader = null;
		try {
			// The FileReader instance of the file to read
			fileReader =
					new FileReader("C:/Users/abas/Documents/FileReaderTest.txt");
			do {
				// the current character as integer
				intContent = fileReader.read();
				// -1 is returned if file end is reached
				if (intContent != -1) {
					// the integer value is first converted in char
					char charContent = (char) intContent;
					// the char is then added to the String containing the
					// file's
					// content
					fileContent = fileContent + charContent;
				}
			}
			// the loop ends if the end of file is reached
			while (intContent != -1);
			// the FileReader instance must be closed in order to release all
			// system resources associated with it
			fileReader.close();
			// outputs content of file as stored in the String fileContent
			getDbContext().out().println(fileContent);
		}
		catch (FileNotFoundException e) {
			// the FileReader instance must be closed in order to release all
			// system resources associated with it
			closeFileReader(fileReader);
			// outputs error message
			getDbContext().out().println(e.getMessage());
		}
		catch (IOException e) {
			// the FileReader instance must be closed in order to release all
			// system resources associated with it
			closeFileReader(fileReader);
			// output error message
			getDbContext().out().println(e.getMessage());
		}
		finally {
			// the FileReader instance must be closed in order to release all
			// system resources associated with it
			closeFileReader(fileReader);
		}
		return 0;
	}

	/**
	 * Closes FileReader if it is not null and handles IOException.
	 *
	 * @param fileReader The FileReader to close.
	 */
	protected void closeFileReader(FileReader fileReader) {
		if (fileReader != null) {
			try {
				fileReader.close();
			}
			catch (IOException e) {
				getDbContext().out().println(e.getMessage());
			}
		}
	}

}
