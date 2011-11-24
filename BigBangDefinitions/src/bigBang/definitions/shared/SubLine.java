package bigBang.definitions.shared;

import java.io.Serializable;

public class SubLine
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String lineId;
	public String objectTypeId; // Novo!
	public Coverage[] coverages;

	public SubLine()
	{
		coverages = new Coverage[0];
	}
	
	public SubLine(SubLine original) {
		this.id = original.id;
		this.name = original.name;
		this.lineId = original.lineId;
		this.coverages = original.coverages;
	}
}
