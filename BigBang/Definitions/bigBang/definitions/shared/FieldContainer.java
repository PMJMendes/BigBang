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

		public HeaderField()
		{
		}

		public HeaderField(HeaderField orig)
		{
			this.fieldId = orig.fieldId;
			this.fieldName = orig.fieldName;
			this.type = orig.type;
			this.unitsLabel = orig.unitsLabel;
			this.refersToId = orig.refersToId;
			this.order = orig.order;
			this.readOnly = orig.readOnly;
			this.valueId = orig.valueId;
			this.value = orig.value;
		}
	}

	public static class ExtraField
		extends HeaderField
	{
		private static final long serialVersionUID = 1L;

		public int coverageIndex;

		public ExtraField()
		{
		}

		public ExtraField(ExtraField orig)
		{
			super(orig);

			this.coverageIndex = orig.coverageIndex;
		}
	}

	public static class ColumnField
		extends ExtraField
	{
		private static final long serialVersionUID = 1L;

		public int columnIndex;

		public ColumnField()
		{
		}

		public ColumnField(ColumnField orig)
		{
			super(orig);

			this.columnIndex = orig.columnIndex;
		}
	}

	public HeaderField[] headerFields;
	public ColumnField[] columnFields;
	public ExtraField[] extraFields;

	public FieldContainer()
	{
	}

	public FieldContainer(FieldContainer orig)
	{
		int i;

		this.id = orig.id;
		this.processId = orig.processId;
		this.permissions = orig.permissions;

		if ( orig.headerFields == null )
			this.headerFields = null;
		else
		{
			this.headerFields = new HeaderField[orig.headerFields.length];
			for ( i = 0; i < this.headerFields.length; i++ )
				this.headerFields[i] = (orig.headerFields[i] == null ? null : new HeaderField(orig.headerFields[i]));
		}

		if ( orig.columnFields == null )
			this.columnFields = null;
		else
		{
			this.columnFields = new ColumnField[orig.columnFields.length];
			for ( i = 0; i < this.columnFields.length; i++ )
				this.columnFields[i] = (orig.columnFields[i] == null ? null : new ColumnField(orig.columnFields[i]));
		}

		if ( orig.extraFields == null )
			this.extraFields = null;
		else
		{
			this.extraFields = new ExtraField[orig.extraFields.length];
			for ( i = 0; i < this.extraFields.length; i++ )
				this.extraFields[i] = (orig.extraFields[i] == null ? null : new ExtraField(orig.extraFields[i]));
		}
	}
}
