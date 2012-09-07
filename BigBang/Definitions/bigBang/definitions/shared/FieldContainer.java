package bigBang.definitions.shared;

import java.io.Serializable;

public class FieldContainer
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static enum FieldType
	{
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
		public int order;

		public boolean readOnly;

		public String valueId; // JMMM: Variável auxiliar server side
		public String value;
	}

	public static class ExtraField
		extends HeaderField
	{
		private static final long serialVersionUID = 1L;

		public int coverageIndex;
	}

	public static class ColumnField
		extends ExtraField
	{
		private static final long serialVersionUID = 1L;

		public int columnIndex;
	}

	public HeaderField[] headerFields;
	public ColumnField[] columnFields;
	public ExtraField[] extraFields;
}
