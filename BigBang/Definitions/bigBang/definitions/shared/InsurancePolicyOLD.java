package bigBang.definitions.shared;

import java.io.Serializable;

public class InsurancePolicyOLD
	extends InsurancePolicyStubOLD
{
	private static final long serialVersionUID = 1L;

	public static enum FieldType {
		NUMERIC,
		TEXT,
		LIST,
		REFERENCE,
		BOOLEAN,
		DATE
	}

	public static class HeaderField
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String fieldId;
		public String fieldName;
		public FieldType type;
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
		public FieldType type;
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

	public static class CoInsurer
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
	
		public String insuranceAgencyId;
		public Double percent;
	}

	public String managerId;
	public String insuranceAgencyId;
	public String startDate;
	public String durationId;
	public String fractioningId;
	public Integer maturityDay;
	public Integer maturityMonth; //1 to 12
	public String expirationDate;
	public String notes;
	public String mediatorId;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public Double premium; // Numérico com duas casas decimais: prémio comercial.
	public String operationalProfileId;
	public String docushare;

	public CoInsurer[] coInsurers;

	public Contact[] contacts;
	public Document[] documents;

	public HeaderField[] headerFields;
	public Coverage[] coverages;
	public ColumnHeader[] columns;
	public TableSection[] tableData;
	public ExtraField[] extraData;
	
	public InsurancePolicyOLD()
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
