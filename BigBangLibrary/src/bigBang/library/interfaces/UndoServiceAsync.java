package bigBang.library.interfaces;

import bigBang.library.shared.ProcessUndoItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UndoServiceAsync extends Service {

	void getProcessUndoItems(String processId,
			AsyncCallback<ProcessUndoItem[]> callback);

	void undo(String undoItemId, AsyncCallback<ProcessUndoItem> callback);

}
