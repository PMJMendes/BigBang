package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

public class SubLine
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String lineId;
	public Coverage[] coverages;

	public SubLine()
	{
		coverages = new Coverage[0];
	}
}
