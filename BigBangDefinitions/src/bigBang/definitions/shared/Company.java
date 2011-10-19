package bigBang.definitions.shared;

import java.io.Serializable;

public class Company extends InsuredObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String clientNumber; //NULL if not a client
	public String nipc;
	public String mainCaeId;
	public String activityNotes;
	public String productDescription;
	public String revenueId;
	public String accountableEntityInEU;

}
