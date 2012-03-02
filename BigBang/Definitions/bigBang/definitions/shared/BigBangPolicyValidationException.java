package bigBang.definitions.shared;

public class BigBangPolicyValidationException
	extends Exception
{
	private static final long serialVersionUID = 1L;

	public BigBangPolicyValidationException()
	{
	}

	public BigBangPolicyValidationException(String pstrMessage)
	{
		super(pstrMessage);
	}

	public BigBangPolicyValidationException(Throwable e)
	{
		super(e);
	}

	public BigBangPolicyValidationException(String pstrMessage, Throwable e)
	{
		super(pstrMessage, e);
	}
}
