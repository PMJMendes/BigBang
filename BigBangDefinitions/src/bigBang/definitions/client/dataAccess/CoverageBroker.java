package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;

public interface CoverageBroker extends DataBrokerInterface<Line> {

	//LINES
	
	/**
	 * Fetches all available Lines
	 * @param handler The handler to be notified on response
	 */
	public void getLines(ResponseHandler<Line[]> handler);
	
	/**
	 * Fetches the Line for a given id
	 * @param lineId The Line id
	 * @param handler The handler to be notified on response
	 */
	public void getLine(String lineId, ResponseHandler<Line> handler);

	/**
	 * Creates a new line
	 * @param line The line to be created
	 * @param handler The handler to be notified on response
	 */
	public void addLine(Line line, ResponseHandler<Line> handler);
	
	/**
	 * Updates the Line for a given id
	 * @param line The Line
	 * @param handler The handler to be notified on response
	 */
	public void updateLine(Line line, ResponseHandler<Line> handler);

	/**
	 * Removes the Line for a given id
	 * @param lineId The Line id
	 * @param handler The handler to be notified on response
	 */
	public void removeLine(String lineId, ResponseHandler<Line> handler);
	

	//SUB LINES
	
	/**
	 * Fetches all the sub lines for a given parent line id
	 * @param parentLineId The parent line id
	 * @param handler The handler to be notified on response
	 */
	public void getSubLines(String parentLineId, ResponseHandler<SubLine[]> handler);
	
	/**
	 * Fetches a sub line for a given parent line id
	 * @param parentLineId The parent line id
	 * @param subLineId The sub line id
	 * @param handler The handler to be notified on response
	 */
	public void getSubLine(String parentLineId, String subLineId, ResponseHandler<SubLine> handler);
	
	/**
	 * Creates a new Sub Line 
	 * @param parentLineId The id of the line parent to the new sub line
	 * @param subLine The sub line to be created
	 * @param handler The handler to be notified on response
	 */
	public void addSubLine(SubLine subLine, ResponseHandler<SubLine> handler);
	
	/**
	 * Updates a sub line for a given id
	 * @param parentLineId The parent line id
	 * @param subLineId The sub line id
	 * @param handlerThe handler to be notified on response
	 */
	public void updateSubLine(SubLine subLine, ResponseHandler<SubLine> handler);
	
	/**
	 * Removes a sub line for a given id
	 * @param parentLineId The parent line id
	 * @param subLineId The sub line id
	 * @param handler The handler to be notified on response
	 */
	public void removeSubLine(String subLineId, ResponseHandler<Void> handler);
	
	
	//COVERAGES
	
	/**
	 * Fetches all the coverages for a given parent line id
	 * @param parentSubLineId The parent line id
	 * @param handler The handler to be notified on response
	 */
	public void getCoverages(String parentLineId, String parentSubLineId, ResponseHandler<Coverage[]> handler);
	
	/**
	 * Fetches a coverage for a given parent line id
	 * @param parentSubLineId The parent line id
	 * @param coverageId The coverage id
	 * @param handler The handler to be notified on response
	 */
	public void getCoverage(String parentLineId, String parentSubLineId, String coverageId, ResponseHandler<Coverage> handler);
	
	/**
	 * Creates a new coverage
	 * @param parentSubLineId The id of the sub line parent to this coverage
	 * @param coverage The coverage to be created
	 * @param handler The handler to be notified on response
	 */
	public void addCoverage(String parentLineId, Coverage coverage, ResponseHandler<Coverage> handler);
	
	/**
	 * Updates a coverage for a given id
	 * @param parentSubLineId The parent line id
	 * @param coverageId The coverage id
	 * @param handlerThe handler to be notified on response
	 */
	public void updateCoverage(String parentLineId, Coverage coverage, ResponseHandler<Coverage> handler);
	
	/**
	 * Removes a coverage for a given id
	 * @param parentSubLineId The parent line id
	 * @param coverageId The coverage id
	 * @param handler The handler to be notified on response
	 */
	public void removeCoverage(String parentLineId, String parentSubLineId, String coverageId, ResponseHandler<Void> handler);
	
	
	//Taxes
	
	/**
	 * Fetches all the taxes for a given parent coverage id
	 * @param parentCoverageId The parent coverage id
	 * @param handler The handler to be notified on response
	 */
	public void getTaxes(String parentLineId, String parentSubLineId, String parentCoverageId, ResponseHandler<Tax[]> handler);
	
	/**
	 * Fetches a tax for a given parent coverage id
	 * @param parentCoverageId The parent coverage id
	 * @param taxID The tax id
	 * @param handler The handler to be notified on response
	 */
	public void getTax(String parentLineId, String parentSubLineId, String parentCoverageId, String taxId, ResponseHandler<Tax> handler);
	
	/**
	 * Creates a new Tax
	 * @param parentCoverageId The id of the coverage parent to the new tax
	 * @param tax The tax to be created
	 * @param handler The handler to be notified on response
	 */
	public void addTax(String parentLineId, String parentSubLineId, Tax tax, ResponseHandler<Tax> handler);
	
	/**
	 * Updates a sub line for a given id
	 * @param parentCoverageId The parent coverage id
	 * @param tax The tax to be updated
	 * @param handlerThe handler to be notified on response
	 */
	public void updateTax(String parentLineId, String parentSubLineId, Tax tax, ResponseHandler<Tax> handler);
	
	/**
	 * Removes a sub line for a given id
	 * @param parentCoverageId The id of the coverage parent to this tax
	 * @param taxId The id of the tax to be removed
	 * @param handler The handler to be notified on response
	 */
	public void removeTax(String parentLineId, String parentSubLineId, String parentCoverageId, String taxId, ResponseHandler<Void> handler);
	
}
