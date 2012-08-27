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
		public Double settlement; //Em €
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
	public Double settlement; //Em €
	public boolean isManual;
	public String notes;
	public String referenceSubLineId; //TODO usar para obter a lista de coberturas
}
