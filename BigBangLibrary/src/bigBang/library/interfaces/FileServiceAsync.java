package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.*;

public interface FileServiceAsync
	extends Service
{
	void Discard(String pstrID, AsyncCallback<Void> callback);
	void Discard(String[] parrIDs, AsyncCallback<Void> callback);
}
