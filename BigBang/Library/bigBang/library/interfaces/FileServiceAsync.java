package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.*;

public interface FileServiceAsync
	extends Service, DependentItemSubServiceAsync
{
	void process(String formatId, String storageId, AsyncCallback<Void> callback);
	void Discard(String pstrID, AsyncCallback<Void> callback);
	void Discard(String[] parrIDs, AsyncCallback<Void> callback);
}
