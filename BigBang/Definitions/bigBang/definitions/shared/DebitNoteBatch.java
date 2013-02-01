package bigBang.definitions.shared;

import java.io.Serializable;

public class DebitNoteBatch
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String policyId;

	public String fractioningId;
	public String maturityDate;
	public String endDate;
	public String limitDate;
}
