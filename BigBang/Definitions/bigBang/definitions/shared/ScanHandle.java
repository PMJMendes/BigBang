package bigBang.definitions.shared;

import java.io.Serializable;

public class ScanHandle
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public boolean docushare;
	public String handle;
	public String locationHandle; //Pode vir a null se fôr do Temporary Scan Repository
}
