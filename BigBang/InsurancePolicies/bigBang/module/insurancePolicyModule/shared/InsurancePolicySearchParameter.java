package bigBang.module.insurancePolicyModule.shared;

import bigBang.definitions.shared.SearchParameter;

public class InsurancePolicySearchParameter	
	extends SearchParameter
{
	private static final long serialVersionUID = 1L;

	public static enum AllowedStates
	{
		ALL,
		LIVE,
		NONLIVE
	}

	public String ownerId;
	public String categoryId;
	public String lineId;
	public String subLineId;
	public String insuranceAgencyId;
	public String mediatorId;
	public String managerId;
	public String insuredObject;
	public Boolean caseStudy;
	public AllowedStates allowedStates;
}
