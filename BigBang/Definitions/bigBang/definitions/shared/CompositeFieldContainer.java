package bigBang.definitions.shared;

public class CompositeFieldContainer
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static class SubLineFieldContainer
		extends StructuredFieldContainer
	{
		private static final long serialVersionUID = 1L;

		public static enum Change
		{
			NONE,
			CREATED,
			MODIFIED,
			DELETED
		}

		public String subLineId;
		
		public Change change;

		public SubLineFieldContainer()
		{
		}

		public SubLineFieldContainer(SubLineFieldContainer orig)
		{
			super(orig);

			this.subLineId = orig.subLineId;
			this.change = orig.change;
		}
	}

	public SubLineFieldContainer[] subLineData;

	public CompositeFieldContainer()
	{
	}

	public CompositeFieldContainer(CompositeFieldContainer orig)
	{
//		super(orig);

		int i;

		if ( orig.subLineData == null )
			this.subLineData = null;
		else
		{
			this.subLineData = new SubLineFieldContainer[orig.subLineData.length];
			for ( i = 0; i < this.subLineData.length; i++ )
				this.subLineData[i] = (orig.subLineData[i] == null ? null : new SubLineFieldContainer(orig.subLineData[i]));
		}
	}
}
