package bigBang.module.quoteRequestModule.shared;

import bigBang.definitions.shared.SearchParameter;

public class QuoteRequestSearchParameter
	extends SearchParameter
{
	private static final long serialVersionUID = 1L;
	
	public String ownerId;
	public String categoryId;
	public String lineId;
	public String subLineId;
	public String insuranceAgencyId;
	public String mediatorId;
	public String managerId;
	public Boolean caseStudy;

	public boolean includeClosed;
}
