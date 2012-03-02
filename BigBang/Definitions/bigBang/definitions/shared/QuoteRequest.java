package bigBang.definitions.shared;

import java.io.Serializable;

import bigBang.definitions.shared.InsurancePolicy.FieldType;

public class QuoteRequest
	extends QuoteRequestStub
{
	private static final long serialVersionUID = 1L;

	public static class HeaderField
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String fieldId;
		public String fieldName;
		public FieldType type;
		public String unitsLabel;
		public String refersToId;
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
			public boolean variesByExercise; //Always false
		}

		public String coverageId;
		public String coverageName;
		public boolean mandatory;
		public Boolean presentInRequestSubLine;
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
	}

	public static class RequestSubLine
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String subLineId;
		public String headerText;
		public HeaderField[] headerFields;
		public Coverage[] coverages;
		public ColumnHeader[] columns;
		public TableSection[] tableData;
		public ExtraField[] extraData;
	}
	
	
	public String managerId;
	public String mediatorId;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public String notes;
	public String docushare;

	public Contact[] contacts;
	public Document[] documents;

	public RequestSubLine[] requestData;
}
