package eonaminecraft;

public class ConnectionData {

	private String dbHost = "";
	private String dbPass = "";
	private String dbUser = "";
	private String dbName = "";
	/**
	 * @return the dbHost
	 */
	public String getDbHost() {
		return dbHost;
	}
	/**
	 * @param dbHost the dbHost to set
	 */
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	/**
	 * @return the dbPass
	 */
	public String getDbPass() {
		return dbPass;
	}
	/**
	 * @param dbPass the dbPass to set
	 */
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}
	/**
	 * @return the dbUser
	 */
	public String getDbUser() {
		return dbUser;
	}
	/**
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public String toString(){
		return "Host: " + dbHost + " | Name: " + dbName + " | User: " + dbUser;
	}
	
}
