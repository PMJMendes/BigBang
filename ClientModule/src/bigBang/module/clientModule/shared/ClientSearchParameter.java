package bigBang.module.clientModule.shared;

import bigBang.definitions.client.dataAccess.SearchParameter;

public class ClientSearchParameter
	extends SearchParameter
{

	private static final long serialVersionUID = 1L;
	
	public String[] postalCodes;
	public String managerId;
	public String costCenterId;
	public String[] CAEs;

	public String mediatorId;
	public String opProfileId;
	public String workerSizeId;
	public String salesVolumeId;
	public String maritalStatusId;
	public String[] professionIds;
	public String birthDateFrom;
	public String birthDateTo;
	public String activityNotes;
	public String notes;
	
	public ClientSearchParameter(){
		this.postalCodes = new String[0];
		this.CAEs = new String[0];
		this.professionIds = new String[0];
	}
}
