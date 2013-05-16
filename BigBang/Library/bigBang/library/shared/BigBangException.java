package bigBang.library.shared;

public class BigBangException
	extends Exception
{
	private static final long serialVersionUID = 1L;

	public BigBangException()
	{
	}
	
	public BigBangException(String pstrMessage)
	{
		super(pstrMessage);
	}

	public BigBangException(Throwable e)
	{
		super(e);
	}

	public BigBangException(String pstrMessage, Throwable e)
	{
		super(pstrMessage, e);
	}
}
