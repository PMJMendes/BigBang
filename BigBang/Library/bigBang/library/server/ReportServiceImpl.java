package bigBang.library.server;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.GenericElement;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportParam;
import bigBang.definitions.shared.TransactionSet;
import bigBang.library.interfaces.ReportService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.ReportDef;
import com.premiumminds.BigBang.Jewel.SysObjects.ExcelConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.HTMLConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.PrintConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionSetBase;

public class ReportServiceImpl
	extends EngineImplementor
	implements ReportService
{
	private static final long serialVersionUID = 1L;

	public static String reportToHTML(Report pobjReport)
		throws BigBangException
	{
		String[] larrContent;
		int i;
		FileXfer lobjFile;
		UUID lidAux;

		larrContent = new String[pobjReport.sections.length];
		for ( i = 0; i < pobjReport.sections.length; i++ )
			larrContent[i] = pobjReport.sections[i].htmlContent;

		try
		{
			lobjFile = HTMLConnector.buildHTML(larrContent);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

    	lidAux = UUID.randomUUID();
    	FileServiceImpl.GetFileXferStorage().put(lidAux, lobjFile);

		return lidAux.toString();
	}

	public static String reportToXL(Report pobjReport)
		throws BigBangException
	{
		String[] larrContent;
		int i;
		FileXfer lobjFile;
		UUID lidAux;

		larrContent = new String[pobjReport.sections.length];
		for ( i = 0; i < pobjReport.sections.length; i++ )
			larrContent[i] = pobjReport.sections[i].htmlContent;

		try
		{
			lobjFile = ExcelConnector.buildExcel(larrContent);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

    	lidAux = UUID.randomUUID();
    	FileServiceImpl.GetFileXferStorage().put(lidAux, lobjFile);

		return lidAux.toString();
	}

	public ReportItem[] getProcessItems(String objectTypeId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<ReportItem> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<ReportItem>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReportDef)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {ReportDef.I.OBJECT, ReportDef.I.PARENT},
					new java.lang.Object[] {UUID.fromString(objectTypeId), (UUID)null}, new int[] {ReportDef.I.ORDER});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(toClient(ReportDef.GetInstance(Engine.getCurrentNameSpace(), lrsObjects)));
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new ReportItem[larrAux.size()]);
	}

	public ReportParam[] getParams(String itemId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.ReportDef lobjReport;
		com.premiumminds.BigBang.Jewel.Objects.ReportParam[] larrParams;
		ReportParam[] larrResult;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReport = com.premiumminds.BigBang.Jewel.Objects.ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			larrParams = lobjReport.GetParams();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrResult = new ReportParam[larrParams.length];
		for ( i = 0; i < larrResult.length; i++ )
			larrResult[i] = toClient(larrParams[i]);

		return larrResult;
	}

	public PrintSet[] getPrintSets(String itemId)
		throws SessionExpiredException, BigBangException
	{
		ReportDef lobjReport;
		ArrayList<PrintSet> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<PrintSet>();

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PrintSet)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.TEMPLATE},
					new java.lang.Object[] {(UUID)lobjReport.getAt(ReportDef.I.TEMPLATE)},
					new int[] {com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.PRINTEDON});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(toClient(com.premiumminds.BigBang.Jewel.Objects.PrintSet.GetInstance(Engine.getCurrentNameSpace(), lrsObjects)));
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new PrintSet[larrAux.size()]);
	}

	public TransactionSet[] getTransactionSets(String itemId)
		throws SessionExpiredException, BigBangException
	{
		ReportDef lobjReport;
		ArrayList<TransactionSet> larrAux;
		IEntity lrefTransactions;
        MasterDB ldb;
        ResultSet lrsObjects;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<TransactionSet>();

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			lrefTransactions = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					(UUID)lobjReport.getAt(ReportDef.I.TRANSACTIONTYPE))); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefTransactions.SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(toClient((TransactionSetBase)Engine.GetWorkInstance(lrefTransactions.getKey(), lrsObjects)));
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new TransactionSet[larrAux.size()]);
	}

	public ReportItem[] getSubItems(String itemId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.ReportDef lobjReport;
		com.premiumminds.BigBang.Jewel.Objects.ReportDef[] larrChildren;
		ReportItem[] larrResult;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReport = com.premiumminds.BigBang.Jewel.Objects.ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			larrChildren = lobjReport.GetChildren();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrResult = new ReportItem[larrChildren.length];
		for ( i = 0; i < larrResult.length; i++ )
			larrResult[i] = toClient(larrChildren[i]);

		return larrResult;
	}

	public Report generateParamReport(String itemId, String[] paramValues)
		throws SessionExpiredException, BigBangException
	{
		ReportDef lobjReport;
		Report lobjResult;
		Method lrefMethod;
		GenericElement[] larrBuffer;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			lrefMethod = lobjReport.getMethod();
			larrBuffer = (GenericElement[])lrefMethod.invoke(null, new java.lang.Object[] {paramValues});
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Report();
		lobjResult.sections = new Report.Section[larrBuffer.length];
		for ( i = 0; i < larrBuffer.length; i++ )
		{
			lobjResult.sections[i] = new Report.Section();
			lobjResult.sections[i].htmlContent = larrBuffer[i].toString();
			lobjResult.sections[i].verbs = new Report.Section.Verb[0];
		}

		return lobjResult;
	}

	public String generateParamAsHTML(String itemId, String[] paramValues)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return reportToHTML(generateParamReport(itemId, paramValues));
	}

	public String generateParamAsXL(String itemId, String[] paramValues)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return reportToXL(generateParamReport(itemId, paramValues));
	}

	public Report generatePrintSetReport(String itemId, String printSetId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.PrintSet lobjSet;
		GenericElement lobjBuffer;
		Report lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSet = com.premiumminds.BigBang.Jewel.Objects.PrintSet.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(printSetId));

			lobjBuffer = lobjSet.buildReportTable();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Report();
		lobjResult.sections = new Report.Section[] {new Report.Section()};
		lobjResult.sections[0].htmlContent = lobjBuffer.toString();
		lobjResult.sections[0].verbs = new Report.Section.Verb[] {new Report.Section.Verb()};
		lobjResult.sections[0].verbs[0].label = "Imprimir Documentos";
		lobjResult.sections[0].verbs[0].argument = "P:" + printSetId;

		return lobjResult;
	}

	public String generatePrintSetAsHTML(String itemId, String printSetId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return reportToHTML(generatePrintSetReport(itemId, printSetId));
	}

	public String generatePrintSetAsXL(String itemId, String printSetId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return reportToXL(generatePrintSetReport(itemId, printSetId));
	}

	public Report generateTransactionSetReport(String itemId, String transactionSetId)
		throws SessionExpiredException, BigBangException
	{
		ReportDef lobjReport;
		UUID lidTransactions;
		TransactionSetBase lobjSet;
		TransactionMapBase[] larrMaps;
		int llngCount;
		GenericElement lobjBuffer;
		int i;
		boolean b;
		Report lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			lidTransactions = Engine.FindEntity(Engine.getCurrentNameSpace(), (UUID)lobjReport.getAt(ReportDef.I.TRANSACTIONTYPE));
			lobjSet = (TransactionSetBase)Engine.GetWorkInstance(lidTransactions, UUID.fromString(transactionSetId));
			larrMaps = lobjSet.getCurrentMaps();
			llngCount = larrMaps.length;

			lobjBuffer = lobjSet.buildHeaderSection();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Report();
		lobjResult.sections = new Report.Section[llngCount + 1];
		lobjResult.sections[0] = new Report.Section();
		lobjResult.sections[0].htmlContent = lobjBuffer.toString();

		b = false;
		for ( i = 1; i <= llngCount; i++ )
		{
			try
			{
				lobjBuffer = lobjSet.buildDataSection(i);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjResult.sections[i] = new Report.Section();
			lobjResult.sections[i].htmlContent = lobjBuffer.toString();

			if ( larrMaps[i - 1].isSettled() )
				lobjResult.sections[i].verbs = new Report.Section.Verb[0];
			else
			{
				b = true;
				lobjResult.sections[i].verbs = new Report.Section.Verb[] {new Report.Section.Verb()};
				lobjResult.sections[i].verbs[0].label = "Saldar";
				lobjResult.sections[i].verbs[0].argument = "S:" + lobjReport.getKey().toString() + ":" +
						lobjSet.getKey().toString() + ":" + larrMaps[i - 1].getKey().toString();
			}
		}

		if ( !b )
			lobjResult.sections[0].verbs = new Report.Section.Verb[0];
		else
		{
			lobjResult.sections[0].verbs = new Report.Section.Verb[] {new Report.Section.Verb()};
			lobjResult.sections[0].verbs[0].label = "Saldar Todas";
			lobjResult.sections[0].verbs[0].argument = "T:" + lobjReport.getKey().toString() + ":" +
					lobjSet.getKey().toString();
		}

		return lobjResult;
	}

	public String generateTransSetAsHTML(String itemId, String transactionSetId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return reportToHTML(generateTransactionSetReport(itemId, transactionSetId));
	}

	public String generateTransSetAsXL(String itemId, String transactionSetId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return reportToXL(generateTransactionSetReport(itemId, transactionSetId));
	}

	public void RunVerb(String argument)
		throws SessionExpiredException, BigBangException
	{
		String[] larrAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = argument.split(":");

		if ( "P".equals(larrAux[0]) )
		{
			RunPrintVerb(larrAux);
			return;
		}

		if ( "S".equals(larrAux[0]) )
		{
			RunSettleVerb(larrAux);
			return;
		}

		if ( "T".equals(larrAux[0]) )
		{
			RunSettleAllVerb(larrAux);
			return;
		}
	}

	private static ReportItem toClient(com.premiumminds.BigBang.Jewel.Objects.ReportDef pobjDef)
	{
		ReportItem lobjResult;

		lobjResult = new ReportItem();
		lobjResult.id = pobjDef.getKey().toString();
		lobjResult.label = pobjDef.getLabel();
		lobjResult.type = GetItemTypeByID((UUID)pobjDef.getAt(com.premiumminds.BigBang.Jewel.Objects.ReportDef.I.TYPE));

		return lobjResult;
	}

	private static ReportParam toClient(com.premiumminds.BigBang.Jewel.Objects.ReportParam pobjParam)
	{
		UUID lidAux;
		ReportParam lobjResult;

		lidAux = (UUID)pobjParam.getAt(com.premiumminds.BigBang.Jewel.Objects.ReportParam.I.REFERENCETO);

		lobjResult = new ReportParam();
		lobjResult.id = pobjParam.getKey().toString();
		lobjResult.label = pobjParam.getLabel();
		lobjResult.type = GetParamTypeByID((UUID)pobjParam.getAt(com.premiumminds.BigBang.Jewel.Objects.ReportParam.I.TYPE));
		lobjResult.unitsLabel = (String)pobjParam.getAt(com.premiumminds.BigBang.Jewel.Objects.ReportParam.I.UNITS);
		lobjResult.refersToId = (lidAux == null ? null : lidAux.toString());

		return lobjResult;
	}

	private static PrintSet toClient(com.premiumminds.BigBang.Jewel.Objects.PrintSet pobjSet)
	{
		PrintSet lobjResult;

		lobjResult = new PrintSet();
		lobjResult.id = pobjSet.getKey().toString();
		lobjResult.date = ((Timestamp)pobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.DATE)).toString().substring(0, 10);
		lobjResult.userName = pobjSet.getUser().getDisplayName();
		lobjResult.printDate = ( pobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.PRINTEDON) == null ?
				null : ((Timestamp)pobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.PRINTEDON)).toString().substring(0, 10) );

		return lobjResult;
	}

	private static TransactionSet toClient(TransactionSetBase pobjSet)
	{
		TransactionSet lobjResult;

		lobjResult = new TransactionSet();
		lobjResult.id = pobjSet.getKey().toString();
		lobjResult.date = ((Timestamp)pobjSet.getAt(TransactionSetBase.I.DATE)).toString().substring(0, 10);
		lobjResult.user = ((UUID)pobjSet.getAt(TransactionSetBase.I.USER)).toString();
		try
		{
			lobjResult.isComplete = pobjSet.isComplete();
			lobjResult.userName = User.GetInstance(pobjSet.getNameSpace(), (UUID)pobjSet.getAt(TransactionSetBase.I.USER)).getDisplayName();
		}
		catch (Throwable e)
		{
			lobjResult.isComplete = false;
		}

		return lobjResult;
	}

	private static ReportItem.ItemType GetItemTypeByID(UUID pidFieldType)
	{
		if ( Constants.RTypeID_Parameter.equals(pidFieldType) )
			return ReportItem.ItemType.PARAM;
		if ( Constants.RTypeID_PrintSet.equals(pidFieldType) )
			return ReportItem.ItemType.PRINTSET;
		if ( Constants.RTypeID_Transaction.equals(pidFieldType) )
			return ReportItem.ItemType.TRANSACTIONSET;
		if ( Constants.RTypeID_SubMenu.equals(pidFieldType) )
			return ReportItem.ItemType.CATEGORY;
		return null;
	}

	private static ReportParam.ParamType GetParamTypeByID(UUID pidFieldType)
	{
		if ( Constants.FieldID_Boolean.equals(pidFieldType) )
			return ReportParam.ParamType.BOOLEAN;
		if ( Constants.FieldID_Date.equals(pidFieldType) )
			return ReportParam.ParamType.DATE;
		if ( Constants.FieldID_List.equals(pidFieldType) )
			return ReportParam.ParamType.LIST;
		if ( Constants.FieldID_Number.equals(pidFieldType) )
			return ReportParam.ParamType.NUMERIC;
		if ( Constants.FieldID_Reference.equals(pidFieldType) )
			return ReportParam.ParamType.REFERENCE;
		if ( Constants.FieldID_Text.equals(pidFieldType) )
			return ReportParam.ParamType.TEXT;
		return null;
	}

	private static void RunPrintVerb(String[] parrArgs)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.PrintSet lobjSet;

		try
		{
			lobjSet = com.premiumminds.BigBang.Jewel.Objects.PrintSet.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(parrArgs[1]));
			PrintConnector.printSet(lobjSet);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private static void RunSettleVerb(String[] parrArgs)
		throws BigBangException
	{
		ReportDef lobjReport;
		UUID lidTransactions;
		TransactionSetBase lobjSet;
		UUID lidMaps;
		TransactionMapBase lobjMap;
		MasterDB ldb;

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(parrArgs[1]));
			lidTransactions = Engine.FindEntity(Engine.getCurrentNameSpace(), (UUID)lobjReport.getAt(ReportDef.I.TRANSACTIONTYPE));
			lobjSet = (TransactionSetBase)Engine.GetWorkInstance(lidTransactions, UUID.fromString(parrArgs[2]));
			lidMaps = Engine.FindEntity(Engine.getCurrentNameSpace(), lobjSet.getSubObjectType());
			lobjMap = (TransactionMapBase)Engine.GetWorkInstance(lidMaps, UUID.fromString(parrArgs[3]));

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjMap.Settle(ldb, null);
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private static void RunSettleAllVerb(String[] parrArgs)
		throws BigBangException
	{
		ReportDef lobjReport;
		UUID lidTransactions;
		TransactionSetBase lobjSet;
		MasterDB ldb;

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(parrArgs[1]));
			lidTransactions = Engine.FindEntity(Engine.getCurrentNameSpace(), (UUID)lobjReport.getAt(ReportDef.I.TRANSACTIONTYPE));
			lobjSet = (TransactionSetBase)Engine.GetWorkInstance(lidTransactions, UUID.fromString(parrArgs[2]));

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjSet.SettleAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
