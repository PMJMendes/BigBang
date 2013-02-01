package bigBang.module.expenseModule.shared;

import bigBang.definitions.shared.SearchParameter;

public class ExpenseSearchParameter
	extends SearchParameter
{
	private static final long serialVersionUID = 1L;

	public String ownerId;
	public String dateFrom;
	public String dateTo;
	public String managerId;
	public String insurerId;
	public String insuredObject;
}
