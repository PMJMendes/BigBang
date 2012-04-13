package bigBang.library.server;

import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportItem.ReportParam;
import bigBang.definitions.shared.TransactionSet;
import bigBang.library.interfaces.ReportService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class ReportServiceImpl
	extends EngineImplementor
	implements ReportService
{
	private static final long serialVersionUID = 1L;

	@Override
	public ReportItem[] getProcessItems(String objectTypeId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportParam[] getParams(String itemId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintSet[] getPrintSets(String itemId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionSet[] getTransactionSets(String itemId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportItem[] getSubItems(String itemId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report generateParamReport(String itemId, String[] paramValues)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report generatePrintSetReport(String itemId, String printSetId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report generateTransactionSetReport(String itemId,
			String transactionSetId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RunVerb(String argument) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		
	}
}
