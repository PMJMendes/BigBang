package bigBang.library.server;

import bigBang.definitions.shared.ZipCode;
import bigBang.library.interfaces.ZipCodeService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class ZipCodeServiceImpl
	extends EngineImplementor
	implements ZipCodeService
{
	private static final long serialVersionUID = 1L;

	public ZipCode getZipCode(String code)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}
}
