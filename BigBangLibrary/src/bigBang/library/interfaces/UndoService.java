package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ProcessUndoItem;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("UndoService")
public interface UndoService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static UndoServiceAsync instance;
		public static UndoServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(UndoService.class);
			}
			return instance;
		}
	}

	public ProcessUndoItem[] getProcessUndoItems(String processId) throws SessionExpiredException, BigBangException;
	public ProcessUndoItem undo(String undoItemId) throws SessionExpiredException, BigBangException;
}
