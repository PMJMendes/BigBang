package bigBang.definitions.shared;

public class HistoryItem
	extends HistoryItemStub
{
	public class AlteredItem
	{
		public String typeId;
		public String[] instanceIds;
	}

	private static final long serialVersionUID = 1L;

	public String shortDescription;
	public String description;
	public String undoDescription;
	public AlteredItem[] alteredEntities; //key:Entity type id / value: entity instance id

	public boolean canUndo;
}
