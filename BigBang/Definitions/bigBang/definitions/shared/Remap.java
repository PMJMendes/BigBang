package bigBang.definitions.shared;

import java.io.Serializable;

public class Remap
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static class RemapId
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String oldId;
		public String newId; // No commit, este ID vem a null se o item foi apagado do pad
		public boolean newIdIsInPad;
	}

	public String typeId;
	public RemapId[] remapIds;
}
