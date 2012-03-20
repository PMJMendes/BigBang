package bigBang.definitions.shared;

import java.io.Serializable;

public class BigBangProcess
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String dataTypeId;
	public String dataId;
	public String dataLabel;
	public String processTypeId;
	public String processId;
	public String tag;
	public String ownerDataId;
	public String ownerDataTypeId;
	public String ownerProcId;
	public String ownerProcTypeId;
}
