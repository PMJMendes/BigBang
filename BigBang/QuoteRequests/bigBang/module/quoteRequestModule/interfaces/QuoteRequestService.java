package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.Remap;
import bigBang.library.interfaces.DependentItemSubService;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("QuoteRequestService")
public interface QuoteRequestService
	extends SearchService, DependentItemSubService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static QuoteRequestServiceAsync instance;
		public static QuoteRequestServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(QuoteRequestService.class);
			}
			return instance;
		}
	}

	public QuoteRequest getRequest(String requestId) throws SessionExpiredException, BigBangException;
	public QuoteRequest.TableSection getPage(String requestId, String subLineId, String objectId)
			throws SessionExpiredException, BigBangException;

	public Remap[] openRequestScratchPad(String requestId) throws SessionExpiredException, BigBangException;
	public QuoteRequest initRequestInPad(QuoteRequest request) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public QuoteRequest getRequestInPad(String requestId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public QuoteRequest updateHeader(QuoteRequest request)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public QuoteRequest.RequestSubLine addSubLineToPad(String requestId, String subLineId)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public void deleteSubLineFromPad(String subLineId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public QuoteRequest.TableSection getPageForEdit(String requestId, String subLineId, String objectId)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public QuoteRequest.TableSection savePage(QuoteRequest.TableSection data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public QuoteRequestObject getObjectInPad(String objectId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public QuoteRequestObject createObjectInPad(String requestId, String objectTypeId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public QuoteRequestObject createObjectFromClientInPad(String requestId) throws SessionExpiredException, BigBangException;
	public QuoteRequestObject updateObjectInPad(QuoteRequestObject data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public void deleteObjectInPad(String objectId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public Remap[] commitPad(String requestId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Remap[] discardPad(String requestId) throws SessionExpiredException, BigBangException;

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public QuoteRequest closeProcess(String requestId, String notes) throws SessionExpiredException, BigBangException;

	public void deleteRequest(String requestId, String reason) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;
}
