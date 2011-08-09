package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;

public interface CoverageDataBrokerClient extends DataBrokerClient<Line> {

	//LINES
	
	/**
	 * Sets the lines known to the broker
	 * @param lines The lines to be set
	 */
	public void setLines(Line[] lines);
	
	/**
	 * Adds a line to the client
	 * @param line The line to be added
	 */
	public void addLine(Line line);
	
	/**
	 * Updates a line in the client
	 * @param line The line to be updated
	 */
	public void updateLine(Line line);
	
	/**
	 * Removes a line from the client
	 * @param lineId The line to be removed
	 */
	public void removeLine(String lineId);
	
	
	//SUB LINES
	
	/**
	 * Sets the sub lines known to the broker
	 * @param parentLineId The line parent to the sub lines
	 * @param subLines The sub lines to be set
	 */
	public void setSubLines(String parentLineId, SubLine[] subLines);
	
	/**
	 * Adds a sub line to the client
	 * @param parentLineId The subline's parent line
	 * @param subLine The sub line to be added
	 */
	public void addSubLine(String parentLineId, SubLine subLine);
	
	/**
	 * Updates a line in the client
	 * @param parentLineId The id of the line parent to the sub line
	 * @param subLine The sub line to be updated
	 */
	public void updateSubLine(String parentLineId, SubLine subLine);
	
	/**
	 * Removes a sub line from the client
	 * @param parentLineId The line parent to the sub line
	 * @param subLineId The id of the sub line to be removed
	 */
	public void removeSubLine(String parentLineId, String subLineId);

	
	//COVERAGES
	
	/**
	 * Sets the coverages known to the broker
	 * @param parentSubLineId The id of the sub line parent to the coverage
	 * @param coverages The coverages to be set
	 */
	public void setCoverages(String parentSubLineId, Coverage[] coverages);
	
	/**
	 * Adds a coverage to the client
	 * @param parentSubLineId The id of the parent sub line
	 * @param coverage The coverage to be added
	 */
	public void addCoverage(String parentSubLineId, Coverage coverage);
	
	/**
	 * Updates a sub line in the client
	 * @param parentSubLineId The id of the sub line parent to the coverage
	 * @param coverage The coverage to be updated
	 */
	public void updateCoverage(String parentSubLineId, Coverage coverage);
	
	/**
	 * Removes a coverage from the client
	 * @param parentSubLineId The id of the sub line parent to the coverage
	 * @param coverageId The id of the coverage to be removed
	 */
	public void removeCoverage(String parentSubLineId, String coverageId);
	
	
	//TAXES
	
	/**
	 * Sets the coverage taxes known to the broker
	 * @param parentCoverageId The id of the coverage parent to the taxes
	 * @param taxes The taxes to be set
	 */
	public void setTaxes(String parentCoverageId, Tax[] taxes);
	
	/**
	 * Adds a tax to the client 
	 * @param parentCoverageId The id of the coverage parent to the tax
	 * @param tax The tax to be added
	 */
	public void addTax(String parentCoverageId, Tax tax);
	
	/**
	 * Updates a Tax in the client
	 * @param parentCoverageId The id of the coverage parent to the tax
	 * @param tax The tax to be updated
	 */
	public void updateTax(String parentCoverageId, Tax tax);
	
	/**
	 * Removes a tax from the client
	 * @param parentCoverageId The id of the coverage parent to the tax
	 * @param taxId The id of the tax to be removed
	 */
	public void removeTax(String parentCoverageId, String taxId);

}
