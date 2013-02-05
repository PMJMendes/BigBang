package bigBang.definitions.shared;

public class GlobalFieldContainer
	extends CompositeFieldContainer
{
	private static final long serialVersionUID = 1L;

	public CompositeObject emptyObject;
	public CompositeObject[] changedObjects;

	public GlobalFieldContainer()
	{
	}

	public GlobalFieldContainer(GlobalFieldContainer orig)
	{
		super(orig);

		int i;

		this.emptyObject = (orig.emptyObject == null ? null : new CompositeObject(orig.emptyObject));

		if ( orig.changedObjects == null )
			this.changedObjects = null;
		else
		{
			this.changedObjects = new CompositeObject[orig.changedObjects.length];
			for ( i = 0; i < this.changedObjects.length; i++ )
				this.changedObjects[i] = (orig.changedObjects[i] == null ? null : new CompositeObject(orig.changedObjects[i]));
		}
	}
}
