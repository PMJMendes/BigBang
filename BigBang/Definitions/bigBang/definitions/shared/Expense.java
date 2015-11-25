package bigBang.definitions.shared;

import java.io.Serializable;

public class Expense
	extends ExpenseStub
{
	private static final long serialVersionUID = 1L;

	public static class Acceptance
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String expenseId;
		public Double settlement; //Em moeda
	}

	public static class ReturnEx
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String expenseId;
		public String reason;
	}

	public String categoryName;
	public String lineName;
	public String subLineName;
	public String managerId;
	public Double settlement; //Em moeda
	public boolean isManual;
	public String notes;
	public String referenceSubLineId; //TODO usar para obter a lista de coberturas

	public String inheritInsurerId;
	public String inheritInsurerName;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public String inheritMasterClientId;
	public String inheritMasterClientName;
	public String inheritMasterMediatorId;
	public String inheritMasterMediatorName;
}
