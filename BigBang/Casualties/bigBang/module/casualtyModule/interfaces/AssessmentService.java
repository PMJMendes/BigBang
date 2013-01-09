package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("AssessmentService")
public interface AssessmentService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static AssessmentServiceAsync instance;
		public static AssessmentServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(AssessmentService.class);
			}
			return instance;
		}
	}

	public Assessment getAssessment(String id) throws SessionExpiredException, BigBangException;

	public Assessment editAssessment(Assessment assessment) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public Assessment closeProcess(String id) throws SessionExpiredException, BigBangException;
}
