package de.abas.training.common;

import java.io.FileWriter;
import java.io.IOException;

import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.ContextRunnable;
import de.abas.eks.jfop.remote.FOPSessionContext;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.DbMessage;
import de.abas.erp.db.MessageListener;
import de.abas.erp.db.util.ContextHelper;

/**
 * The class AbstractAjoAccess is an utility class to connect to the ERP client.
 * It is therefore easier to run applications in client and server mode.
 * 
 * @author abas Software AG
 * @version 1.0
 *
 */
public abstract class AbstractAjoAccess implements ContextRunnable {

	/**
	 * Context Mode in which the application is running.
	 * 
	 * @author abas Software AG
	 * @version 1.0
	 *
	 */
	public enum ContextMode {
		UNDEFINED {
			@Override
			public String toString() {
				return "";
			}
		},
		SERVER_MODE {
			@Override
			public String toString() {
				return "server-mode";
			}
		},
		CLIENT_MODE {
			@Override
			public String toString() {
				return "client-mode";
			}
		}
	}

	/**
	 * Connection status.
	 * 
	 * @author abas Software AG
	 * @version 1.0
	 *
	 */
	public enum Status {
		UNDEFINED {
			@Override
			public String toString() {
				return "";
			}
		},
		OK_MODE {

			@Override
			public String toString() {
				return "ok-mode";
			}

		},
		ERROR_MODE {

			@Override
			public String toString() {
				return "error-mode";
			}
		}
	}

	// Standard connection properties
	private String hostname = "hades";
	private String mandant = "jasc";

	private String password = "sy";

	private int port = 6550;

	private FileWriter fileWriterLogging;

	// initializes DbContext
	private DbContext dbContext = null;
	private ContextMode mode = ContextMode.UNDEFINED;

	/**
	 * Method to add a default message listener.
	 */
	public void addDefaultMessageListener() {
		getDbContext().addMessageListener(new MessageListener() {

			@Override
			public void receiveMessage(DbMessage message) {
				getDbContext().out().println(message);
			}

		});
	}

	/**
	 * Final method to enable EDP-logging.
	 */
	public final void enableLogging() {
		enableLogging("tmp/" + getClass().getSimpleName() + ".log");
	}

	/**
	 * Method to enable EDP-logging.
	 * 
	 * @param fileName The name of the log file.
	 */
	public void enableLogging(String fileName) {
		try {
			fileWriterLogging = new FileWriter(fileName);
			getDbContext().setLogger(fileWriterLogging);
		}
		catch (IOException e) {
			getDbContext().out().println(e.getMessage());
		}
	}

	/**
	 * Gets the database context.
	 * 
	 * @return The database context.
	 */
	public DbContext getDbContext() {
		if (dbContext == null) {
			dbContext =
					ContextHelper.createClientContext(hostname, port, mandant,
							password, this.getClass().getSimpleName());
			mode = ContextMode.CLIENT_MODE;
		}
		return dbContext;
	}

	/**
	 * Gets the host name.
	 * 
	 * @return The host name.
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Gets the client.
	 * 
	 * @return Returns the client.
	 */
	public String getMandant() {
		return mandant;
	}

	/**
	 * Gets the context mode.
	 * 
	 * @return The context mode.
	 */
	public String getMode() {
		return mode.toString();
	}

	/**
	 * Gets the port.
	 * 
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * The abstract run method which is to be implemented by any child classes
	 * of AbstractAjoAccess. This method is run in server and client mode and
	 * holds the actual program logic.
	 */
	public abstract void run();

	/**
	 * Method to be implemented in the main method. Accesses the client.
	 */
	public final void runClientProgram() {
		run();
		// Protokollierung abschalten
		disableLogging();
		// Datenbankkontext schließen
		getDbContext().close();
	}

	// accesses server
	@Override
	public int runFop(FOPSessionContext fopSessionContext, String[] args)
			throws FOPException {
		dbContext = fopSessionContext.getDbContext();
		mode = ContextMode.SERVER_MODE;

		run();
		return 0;
	}

	/**
	 * Sets the host name.
	 * 
	 * @param hostname The value to set the host name.
	 */
	public void setHostname(String hostname) {
		if (isClientContextRunning()) {
			this.hostname = hostname;
			dbContext.close();
			dbContext = null;
		}
	}

	/**
	 * Sets the client.
	 * 
	 * @param mandant The value to set the client.
	 */
	public void setMandant(String mandant) {
		if (isClientContextRunning()) {
			this.mandant = mandant;
			dbContext.close();
			dbContext = null;
		}
	}

	/**
	 * Sets the password.
	 * 
	 * @param password The value to set the password.
	 */
	public void setPassword(String password) {
		if (isClientContextRunning()) {
			this.password = password;
			dbContext.close();
			dbContext = null;
		}
	}

	/**
	 * Sets the port.
	 * 
	 * @param port The value to set the port.
	 */
	public void setPort(int port) {
		if (isClientContextRunning()) {
			this.port = port;
			dbContext.close();
			dbContext = null;
		}
	}

	/**
	 * Method to disable EDP-logging.
	 */
	private void disableLogging() {
		if (null != fileWriterLogging) {
			try {
				fileWriterLogging.close();
			}
			catch (IOException e) {
				getDbContext().out().println(e.getMessage());
			}
			finally {
				fileWriterLogging = null;
			}
		}
		getDbContext().setLogger(null);
	}

	/**
	 * Determines whether the client mode is running.
	 * 
	 * @return Returns true if client mode is running, otherwise false.
	 */
	private boolean isClientContextRunning() {
		if (mode.equals(ContextMode.CLIENT_MODE)) {
			return true;
		}
		else {
			dbContext.out().println(
					"No Client-Mode running -> parameter may not be chanched");
			return false;
		}
	}
}
