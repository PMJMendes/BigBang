package bigBang.module.casualtyModule.shared;

import bigBang.definitions.shared.SearchParameter;

public class MedicalFileSearchParameter
	extends SearchParameter
{
	private static final long serialVersionUID = 1L;

	public String insuredObject;

	public boolean includeClosed;
}
