package bigBang.library.shared;

import java.io.Serializable;
import java.util.Map;

public class ProcessUndoItem
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String username;
	public String timeStamp;
	public String shortDescription;
	public String description;
	public String undoDescription;
	public Map<String, String[]> alteredEntities; //key:Entity type id / value: entity instance id
	
	public boolean canUndo;
}
