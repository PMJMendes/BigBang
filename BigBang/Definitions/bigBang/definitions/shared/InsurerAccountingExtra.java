package bigBang.definitions.shared;

import java.io.Serializable;

public class InsurerAccountingExtra
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String insurerId;
	public String text;
	public Double value;
	public Boolean isCommsissions;
}
