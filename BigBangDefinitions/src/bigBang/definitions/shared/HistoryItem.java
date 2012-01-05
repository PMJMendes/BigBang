package bigBang.definitions.shared;

import java.io.Serializable;

public class HistoryItem
	extends HistoryItemStub
{
	private static final long serialVersionUID = 1L;

	public static class AlteredItem implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String typeId;
		public String[] createdIds;
		public String[] modifiedIds;
		public String[] deletedIds;
	}

	public String shortDescription;
	public String description;
	public String undoDescription;

	public boolean canUndo;

	public String otherObjectTypeId; // Null se não houver de todo
	public String otherObjectId; // Null se já houve mas foi apagado

	public AlteredItem[] alteredEntities; //key:Entity type id / value: entity instance id
}
