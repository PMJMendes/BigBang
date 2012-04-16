package bigBang.library.interfaces;

import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportParam;
import bigBang.definitions.shared.TransactionSet;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ReportService")
public interface ReportService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ReportServiceAsync instance;
		public static ReportServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ReportService.class);
			}
			return instance;
		}
	}

	ReportItem[] getProcessItems(String objectTypeId) throws SessionExpiredException, BigBangException;

	ReportParam[] getParams(String itemId) throws SessionExpiredException, BigBangException;
	PrintSet[] getPrintSets(String itemId) throws SessionExpiredException, BigBangException;
	TransactionSet[] getTransactionSets(String itemId) throws SessionExpiredException, BigBangException;
	ReportItem[] getSubItems(String itemId) throws SessionExpiredException, BigBangException;

	Report generateParamReport(String itemId, String[] paramValues) throws SessionExpiredException, BigBangException;
	Report generatePrintSetReport(String itemId, String printSetId) throws SessionExpiredException, BigBangException;
	Report generateTransactionSetReport(String itemId, String transactionSetId) throws SessionExpiredException, BigBangException;

	void RunVerb(String argument) throws SessionExpiredException, BigBangException;
}
