package bigBang.library.shared;

public class CorruptedPadException
	extends BigBangException
{
	private static final long serialVersionUID = 1L;

	public CorruptedPadException()
	{
	}
	
	public CorruptedPadException(String pstrMessage)
	{
		super(pstrMessage);
	}

	public CorruptedPadException(Throwable e)
	{
		super(e);
	}

	public CorruptedPadException(String pstrMessage, Throwable e)
	{
		super(pstrMessage, e);
	}
}
