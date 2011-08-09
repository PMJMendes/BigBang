package bigBang.library.interfaces;

import bigBang.library.shared.ProcessUndoItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UndoServiceAsync
	extends SearchServiceAsync
{
	void undo(String undoItemId, AsyncCallback<ProcessUndoItem> callback);
}
