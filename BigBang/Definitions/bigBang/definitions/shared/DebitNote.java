package bigBang.definitions.shared;

import java.io.Serializable;

public class DebitNote
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String number; //Vai a null na criação
	public String value;
	public String maturityDate;
}
