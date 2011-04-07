package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.user.client.rpc.*;

@RemoteServiceRelativePath("file")
public interface FileService
	extends RemoteService
{
	String Discard(String pstrID) throws SessionExpiredException, BigBangException;
}
