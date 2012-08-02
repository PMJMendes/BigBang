package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("PrintService")
public interface PrintService extends RemoteService {

	public static class Util {
		private static PrintServiceAsync instance;
		public static PrintServiceAsync getInstance(){
			if(instance == null) {
				instance = GWT.create(PrintService.class);
			}
			return instance;
		}
	}
	
	String[] getAvailablePrinterNames() throws SessionExpiredException, BigBangException;
	
}
