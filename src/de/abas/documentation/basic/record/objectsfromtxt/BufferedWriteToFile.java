package de.abas.documentation.basic.record.objectsfromtxt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import de.abas.documentation.basic.common.AbstractAjoAccess;

public class BufferedWriteToFile extends AbstractAjoAccess {

	public static void main(String[] args) {
		BufferedWriteToFile writeToFile = new BufferedWriteToFile();
		writeToFile.runClientProgram(args);
	}

	@Override
	public int run(String[] args) {
		String pathToLogFile = "C:/Users/abas/Documents/log.txt";
		try (BufferedReader bufferedReader =
				new BufferedReader(new FileReader(
						"C:/Users/abas/Documents/customer.txt"));
				BufferedWriter bufferedWriter =
						new BufferedWriter(new FileWriter(pathToLogFile))) {
			bufferedWriter.append("Starting import: "
					+ Calendar.getInstance().getTime() + "\n");
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				// get field names and values from file
				// create new object
				// set fields with the according values
				// save changes
			}
			bufferedWriter.append("Import successfully completed.\n");
			bufferedWriter.close();
		}
		catch (FileNotFoundException e) {
			try (BufferedWriter bufferedWriter =
					new BufferedWriter(new FileWriter(pathToLogFile))) {
				bufferedWriter.append("\nAn error occurred: " + e.getMessage()
						+ "\n");
			}
			catch (IOException ex) {
				getDbContext().out().println(ex.getMessage());
			}
		}
		catch (IOException e) {
			try (BufferedWriter bufferedWriter =
					new BufferedWriter(new FileWriter(pathToLogFile))) {
				bufferedWriter.append("\nAn error occurred: " + e.getMessage()
						+ "\n");
			}
			catch (IOException ex) {
				getDbContext().out().println(ex.getMessage());
			}
		}
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(pathToLogFile));
			bufferedWriter.append("Test logging \n");
		}
		catch (IOException e1) {
			getDbContext().out().println(e1.getMessage());
		}
		finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			}
			catch (IOException e) {
				getDbContext().out().println(
						"An error occurred while trying to close the BufferedWriter instance: "
								+ e.getMessage());
			}
		}
		return 0;

	}

}
