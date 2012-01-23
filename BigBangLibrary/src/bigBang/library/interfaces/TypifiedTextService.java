package bigBang.library.interfaces;

import bigBang.definitions.shared.TypifiedText;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TypifiedTextService")
public interface TypifiedTextService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static TypifiedTextServiceAsync instance;
		public static TypifiedTextServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(TypifiedTextService.class);
			}
			return instance;
		}
	}

	public TypifiedText[] getTexts(String tag) throws SessionExpiredException, BigBangException;
	public TypifiedText createText(TypifiedText text) throws SessionExpiredException, BigBangException;
	public TypifiedText saveText(TypifiedText text) throws SessionExpiredException, BigBangException;
	public void deleteText(String textId) throws SessionExpiredException, BigBangException;
}
