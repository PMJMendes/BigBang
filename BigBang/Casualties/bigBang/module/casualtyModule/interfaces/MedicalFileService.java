package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MedicalFileService")
public interface MedicalFileService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static MedicalFileServiceAsync instance;
		public static MedicalFileServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(MedicalFileService.class);
			}
			return instance;
		}
	}

	public MedicalFile getMedicalFile(String id) throws SessionExpiredException, BigBangException;

	public MedicalFile editMedicalFile(MedicalFile medicalFile) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public MedicalFile closeProcess(String id) throws SessionExpiredException, BigBangException;
}
