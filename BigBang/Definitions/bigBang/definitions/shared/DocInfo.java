package bigBang.definitions.shared;

import java.io.Serializable;

public class DocInfo
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String name;
	public String value;
	public boolean displayAtPortal;

	public DocInfo()
	{
	}

	public DocInfo(DocInfo orig)
	{
		this.name = orig.name;
		this.value = orig.value;
		this.displayAtPortal = orig.displayAtPortal;
	}
}
