package bigBang.definitions.shared;

public class HistoryItemStub
	extends SearchResult
{
	public class AlteredItem
	{
		public String typeId;
		public String[] instanceIds;
	}

	private static final long serialVersionUID = 1L;

	public String username;
	public String timeStamp;
	public String opName;
}
