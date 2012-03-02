package bigBang.definitions.shared;

import java.io.Serializable;

public class Line
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String categoryId;
	public String categoryName;
	public SubLine[] subLines;

	public Line()
	{
		subLines = new SubLine[0];
	}
	
	public Line(Line original) {
		this.id = original.id;
		this.name = original.name;
		this.categoryId = original.categoryId;
		this.categoryName = original.categoryName;
		this.subLines = original.subLines;
	}
}
