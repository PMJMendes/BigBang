package bigBang.definitions.shared;

public class Remap
{
	public static class RemapId
	{
		public String oldId;
		public String newId;
		public boolean newIdIsInPad;
	}

	public String typeId;
	public RemapId[] remapIds;
}
