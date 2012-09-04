package bigBang.definitions.shared;

import java.io.Serializable;

public class SubPolicy
	extends SubPolicyStub
{
	private static final long serialVersionUID = 1L;

	public static class HeaderField
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String fieldId;
		public String fieldName;
		public InsurancePolicyOLD.FieldType type;
		public String unitsLabel;
		public String refersToId;
		public int order; // JMMM: Variável auxiliar para ordenação. Ignorar no Client Side
//		public boolean variesByObject; <- Not needed. Should always be false.
//		public boolean variesByExercise; <- Not needed. Should always be false.

		public String value;
	}

	public static class Coverage
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public static class Variability implements Serializable
		{
			private static final long serialVersionUID = 1L;
			
			public int columnIndex;
			public boolean variesByObject;
			public boolean variesByExercise;
		}

		public String coverageId;
		public String coverageName;
		public boolean mandatory;
		public int order; // JMMM: Variável auxiliar para ordenação. Ignorar no Client Side
		public Boolean presentInPolicy;
		public Variability[] variability;
	}

	public static class ColumnHeader
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String label;
		public InsurancePolicyOLD.FieldType type;
		public String unitsLabel;
		public String refersToId;
	}

	public static class TableSection
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public static class TableField
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public String fieldId;
			public String coverageId;
			public int columnIndex;
			public String value;
		}

		public String pageId;
		public TableField[] data;

		public TableSection()
		{
			pageId = null;
		}
	}

	public static class ExtraField
		extends HeaderField
	{
		private static final long serialVersionUID = 1L;

		public String coverageId;
		public String coverageName; // JMMM: Variável auxiliar para ordenação. Ignorar no Client Side
		public boolean mandatory; // JMMM: Variável auxiliar para ordenação. Ignorar no Client Side
		public int covorder; // JMMM: Variável auxiliar para ordenação. Ignorar no Client Side
	}

	public String managerId;
	public String managerName;
	public String startDate;
	public String fractioningId;
	public String expirationDate;
	public String notes;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public Double premium; // Numérico com duas casas decimais: prémio comercial.
	public String docushare;
	public String inheritSubLineId;
	
	public Contact[] contacts;
	public Document[] documents;

	public HeaderField[] headerFields;
	public Coverage[] coverages;
	public ColumnHeader[] columns;
	public TableSection[] tableData;
	public ExtraField[] extraData;

	public SubPolicy()
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
