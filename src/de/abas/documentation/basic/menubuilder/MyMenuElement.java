package de.abas.documentation.basic.menubuilder;

/**
 * Class defining individual menu elements.
 *
 * @author abas Software AG
 *
 */
public class MyMenuElement {

	private String databaseNo;
	private String groupNo;
	private String databaseName;
	private String groupName;

	/**
	 * Constructor
	 *
	 * @param databasNo Database number of database.
	 * @param groupNo Group number of database.
	 * @param databaseName Database name.
	 * @param groupName Group name.
	 */
	public MyMenuElement(String databasNo, String groupNo, String databaseName,
			String groupName) {
		super();
		databaseNo = databasNo;
		this.groupNo = groupNo;
		this.databaseName = databaseName;
		this.groupName = groupName;
	}

	/**
	 * Getter for databaseName.
	 *
	 * @return Database name.
	 */
	public String getDatabasName() {
		return databaseName;
	}

	/**
	 * Getter of databaseNo.
	 *
	 * @return Database number of database.
	 */
	public String getDatabasNo() {
		return databaseNo;
	}

	/**
	 * Getter of groupName.
	 *
	 * @return Group name.
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Getter of groupNo.
	 *
	 * @return Group number of database.
	 */
	public String getGroupNo() {
		return groupNo;
	}

}
