package bigBang.definitions.shared;

public class InsurancePolicy
	extends InsurancePolicyStub
{
	private static final long serialVersionUID = 1L;

	public class HeaderField
	{
		public String fieldId;
		public String fieldName;
		public String unitsId;
		public String value;
	}

	public class Coverage
	{
		public String coverageId;
		public String coverageName;
		public boolean mandatory;
	}

	public class ColumnHeader
	{
		public String label;
		public String unitsId;
		public boolean variesByObject;
		public boolean variesByExercise;
	}

	public class TableSection
	{
		public class TableField
		{
			public String coverageId;
			public int columnIndex;
			public String value;
		}

		public String insuredObjectId; // Can be null
		public String exerciseId; // Can be null
		public TableField[] data;
	}

	public class ExtraField
		extends HeaderField
	{
		public String coverageId;
	}

	public String managerId;
	public String insuranceAgencyId;
	public String startDate;
	public String durationId;
	public String fractioningId;
	public int maturityDay;
	public int maturityMonth; //1 to 12
	public String expirationDate;
	public String notes;
	public String mediatorId;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public Contact[] contacts;
	public Document[] documents;

	public HeaderField[] headerFields;
	public Coverage[] coverages;
	public ColumnHeader[] columns;
	public TableSection[] tableData;
	public ExtraField[] extraData;

	public InsurancePolicy()
	{
		contacts = new Contact[0];
		documents = new Document[0];
		headerFields = new HeaderField[0];
		coverages = new Coverage[0];
		columns = new ColumnHeader[0];
		tableData = new TableSection[0];
		extraData = new ExtraField[0];
	}
}
