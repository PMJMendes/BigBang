package bigBang.library.interfaces;

import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportParam;
import bigBang.definitions.shared.TransactionSet;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportServiceAsync
	extends Service
{
	void getProcessItems(String objectTypeId, AsyncCallback<ReportItem[]> callback);
	void getParams(String itemId, AsyncCallback<ReportParam[]> callback);
	void getPrintSets(String itemId, AsyncCallback<PrintSet[]> callback);
	void getTransactionSets(String itemId, AsyncCallback<TransactionSet[]> callback);
	void getSubItems(String itemId, AsyncCallback<ReportItem[]> callback);
	void generateParamReport(String itemId, String[] paramValues, AsyncCallback<Report> callback);
	void generatePrintSetReport(String itemId, String printSetId, AsyncCallback<Report> callback);
	void generateTransactionSetReport(String itemId, String transactionSetId, AsyncCallback<Report> callback);
	void RunVerb(String argument, AsyncCallback<Void> callback);
}
