package bigBang.library.interfaces;

import bigBang.definitions.shared.ImageItem;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ImageSubService
	extends RemoteService
{
	ImageItem getItemAsImage(String pstrItem, int pageNumber) throws SessionExpiredException, BigBangException;
}
