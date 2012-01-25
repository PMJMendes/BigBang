package bigBang.library.shared;

import java.io.Serializable;

public class BigBangProcess
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String dataTypeId;
	public String dataId;
	public String processTypeId;
	public String processId;
	public String tag;
	public String ownerDataId;
	public String ownerDataTypeId;
	public String ownerProcId;
	public String ownerProcTypeId;
}
