package bigBang.module.insurancePolicyModule.shared;

public class BigBangPolicyCalculationException
	extends Exception
{
	private static final long serialVersionUID = 1L;

	public BigBangPolicyCalculationException()
	{
	}

	public BigBangPolicyCalculationException(String pstrMessage)
	{
		super(pstrMessage);
	}

	public BigBangPolicyCalculationException(Throwable e)
	{
		super(e);
	}

	public BigBangPolicyCalculationException(String pstrMessage, Throwable e)
	{
		super(pstrMessage, e);
	}
}
