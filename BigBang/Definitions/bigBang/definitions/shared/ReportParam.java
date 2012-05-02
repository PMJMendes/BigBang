package bigBang.definitions.shared;

import java.io.Serializable;

public class ReportParam
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static enum ParamType
	{
		NUMERIC,
		TEXT,
		LIST,
		REFERENCE,
		BOOLEAN, //true = "1", false="0" 
		DATE
	}

	public String label;
	public ParamType type;
	public String unitsLabel;
	public String refersToId;
}
