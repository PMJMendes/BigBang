package bigBang.definitions.shared;

public class HealthExpense
	extends HealthExpenseStub
{
	private static final long serialVersionUID = 1L;

	public String categoryName;
	public String lineName;
	public String subLineName;
	public String managerId;
	public String settlement; //Em €
	public boolean isManual;
	public String notes;
}
