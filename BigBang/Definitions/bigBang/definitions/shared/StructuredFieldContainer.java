package bigBang.definitions.shared;

import java.io.Serializable;

public class StructuredFieldContainer
	extends ComplexFieldContainer
{
	private static final long serialVersionUID = 1L;

	public static class Coverage
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String coverageId;
		public String coverageName;
		public boolean mandatory;
		public int order;

		public String serverId;
		public Boolean presentInPolicy;

		public Coverage()
		{
		}

		public Coverage(Coverage orig)
		{
			this.coverageId = orig.coverageId;
			this.coverageName = orig.coverageName;
			this.mandatory = orig.mandatory;
			this.order = orig.order;
			this.serverId = orig.serverId;
			this.presentInPolicy = orig.presentInPolicy;
		}
	}

	public static class ColumnHeader
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String label;
		public FieldType type;
		public String unitsLabel;
		public String refersToId;

		public ColumnHeader()
		{
		}

		public ColumnHeader(ColumnHeader orig)
		{
			this.label = orig.label;
			this.type = orig.type;
			this.unitsLabel = orig.unitsLabel;
			this.refersToId = orig.refersToId;
		}
	}

	public Coverage[] coverages;
	public ColumnHeader[] columns;

	public InsuredObject emptyObject;
	public InsuredObject[] changedObjects;

	public StructuredFieldContainer()
	{
	}

	public StructuredFieldContainer(ComplexFieldContainer orig)
	{
		super(orig);
	}

	public StructuredFieldContainer(StructuredFieldContainer orig)
	{
		super(orig);

		int i;

		this.emptyObject = (orig.emptyObject == null ? null : new InsuredObject(orig.emptyObject));

		if ( orig.coverages == null )
			this.coverages = null;
		else
		{
			this.coverages = new Coverage[orig.coverages.length];
			for ( i = 0; i < this.coverages.length; i++ )
				this.coverages[i] = (orig.coverages[i] == null ? null : new Coverage(orig.coverages[i]));
		}

		if ( orig.columns == null )
			this.columns = null;
		else
		{
			this.columns = new ColumnHeader[orig.columns.length];
			for ( i = 0; i < this.columns.length; i++ )
				this.columns[i] = (orig.columns[i] == null ? null : new ColumnHeader(orig.columns[i]));
		}

		if ( orig.changedObjects == null )
			this.changedObjects = null;
		else
		{
			this.changedObjects = new InsuredObject[orig.changedObjects.length];
			for ( i = 0; i < this.changedObjects.length; i++ )
				this.changedObjects[i] = (orig.changedObjects[i] == null ? null : new InsuredObject(orig.changedObjects[i]));
		}
	}
}
