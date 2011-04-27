package bigBang.library.interfaces;

import bigBang.library.shared.ProcessUndoItem;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UndoService extends RemoteService {
	
	public ProcessUndoItem[] getProcessUndoItems(String processId);
	
	public ProcessUndoItem undo(String undoItemId);
	
}
