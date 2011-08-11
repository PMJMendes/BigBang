package bigBang.definitions.shared;

import java.io.Serializable;

public class HistoryItemStub
	extends SearchResult
{
	public static class AlteredItem implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String typeId;
		public String[] instanceIds;
	}

	private static final long serialVersionUID = 1L;

	public String username;
	public String timeStamp;
	public String opName;
	public AlteredItem[] alteredEntities; //key:Entity type id / value: entity instance id
}
