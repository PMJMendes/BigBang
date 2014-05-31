package bigBang.module.casualtyModule.shared;

import bigBang.definitions.shared.SearchParameter;

public class CasualtySearchParameter
	extends SearchParameter
{
	private static final long serialVersionUID = 1L;

	public String dateFrom;
	public String dateTo;
	public Boolean caseStudy;
	public String managerId;
	public String ownerId;
	public String insuredObject;

	public boolean includeClosed;
	public boolean closedOnly;
}
