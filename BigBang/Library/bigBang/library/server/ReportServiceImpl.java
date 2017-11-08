package bigBang.library.server;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.Strong;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

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

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingSet;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.ReportDef;
import com.premiumminds.BigBang.Jewel.SysObjects.ExcelConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.HTMLConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.PrintConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionSetBase;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class ReportServiceImpl
	extends EngineImplementor
	implements ReportService
{
	private static final long serialVersionUID = 1L;

	public static GenericElement[] sGenerateParamReport(String itemId, String[] paramValues)
		throws BigBangException
	{
		ReportDef lobjReport;
		Method lrefMethod;
		GenericElement[] larrResult;

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			lrefMethod = lobjReport.getMethod();
			larrResult = (GenericElement[])lrefMethod.invoke(null, new java.lang.Object[] {paramValues});
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getCause().getMessage(), e);
		}

		return larrResult;
	}

	public static GenericElement[] sGeneratePrintSetReport(String itemId, String printSetId)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.PrintSet lobjSet;
		GenericElement lobjBuffer;

		try
		{
			lobjSet = com.premiumminds.BigBang.Jewel.Objects.PrintSet.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(printSetId));

			Table printSetGenericInfo = lobjSet.buildReportTable();
			lobjBuffer = printSetGenericInfo; 
			
			// Gets the extra info
			UUID setOwnerType = (UUID) lobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.OWNERTYPE);
			if (setOwnerType!=null && setOwnerType.equals(Constants.ObjID_InsurerAccountingSet)) {
				// If it is associated to a InsurerAccountingSet, it gets the corresponding company
				UUID setOwner = (UUID) lobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.OWNER);
				if (setOwner!=null) {
					InsurerAccountingSet set;
					try {
						set = InsurerAccountingSet.GetInstance(Engine.getCurrentNameSpace(), setOwner);
						
						Table extraInfo = new Table();
						
						TR printInfoRow = new TR();
						TD printInfoCell = new TD();
						printInfoCell.addElement(printSetGenericInfo);
						printInfoRow.addElement(printInfoCell);
						extraInfo.addElement(printInfoRow);
						
						TR titleRow = new TR();
						TD titleCell = new TD();
						Strong sectionTitle = new Strong("Informação de prestação de contas");
						sectionTitle.setStyle("font-size: 12px;");
						titleCell.addElement(sectionTitle);
						titleCell.setStyle("padding-top:30px;");
						titleRow.addElement(titleCell);
						extraInfo.addElement(titleRow);
						
						TR setHeaderRow = new TR();
						TD setHeaderCell = new TD();
						setHeaderCell.addElement(set.buildHeaderSection());
						setHeaderCell.setStyle("padding-top:10px;");
						setHeaderRow.addElement(setHeaderCell);
						extraInfo.addElement(setHeaderRow);
						
						int nrMaps = set.getCurrentMaps().length;
						Table infoTable = new Table();
						for (int i=0; i<nrMaps; i++) {
							TD mapCell = new TD();
							TR mapRow = new TR();
							mapCell.addElement(set.buildDataSection(i+1));
							mapCell.setStyle("padding-top:10px;");
							mapRow.addElement(mapCell);
							infoTable.addElement(mapRow);
						}
						TR setInfoRow = new TR();
						TD setInfoCell = new TD();
						setInfoCell.addElement(infoTable);
						setInfoRow.addElement(setInfoCell);
						extraInfo.addElement(setInfoRow);
						
						return new GenericElement[] { extraInfo };
						
					} catch (BigBangJewelException e) {
					}
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new GenericElement[] { lobjBuffer };
	}

	public static GenericElement[] sGenerateTransactionSetReport(String itemId, String transactionSetId)
		throws BigBangException
	{
		ReportDef lobjReport;
		UUID lidTransactions;
		TransactionSetBase lobjSet;
		TransactionMapBase[] larrMaps;
		int llngCount;
		GenericElement[] larrResult;
		int i;

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			lidTransactions = Engine.FindEntity(Engine.getCurrentNameSpace(), (UUID)lobjReport.getAt(ReportDef.I.TRANSACTIONTYPE));
			lobjSet = (TransactionSetBase)Engine.GetWorkInstance(lidTransactions, UUID.fromString(transactionSetId));
			larrMaps = lobjSet.getCurrentMaps();
			llngCount = larrMaps.length;

			larrResult = new GenericElement[llngCount + 1];

			larrResult[0] = lobjSet.buildHeaderSection();

			for ( i = 1; i <= llngCount; i++ )
				larrResult[i] = lobjSet.buildDataSection(i);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult;
	}

	public static String sReportToHTML(GenericElement[] parrReport)
		throws BigBangException
	{
		String[] larrContent;
		int i;
		FileXfer lobjFile;
		UUID lidAux;

		larrContent = new String[parrReport.length];
		for ( i = 0; i < parrReport.length; i++ )
			larrContent[i] = parrReport[i].toString();

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

	public static String sReportToXL(GenericElement[] parrReport)
		throws BigBangException
	{
		FileXfer lobjFile;
		UUID lidAux;

		try
		{
			lobjFile = ExcelConnector.buildExcel(parrReport);
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

		try
		{
			if ( Utils.getCurrentAgent() != null )
				return new PrintSet[0];
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

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
			UUID templateId = (UUID)lobjReport.getAt(ReportDef.I.TEMPLATE);
			if (templateId.equals(Constants.TID_InsurerAccounting)) {
				String sqlQuery = lrefObjects.SQLForSelectByMembers(new int[] {com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.TEMPLATE},
						new java.lang.Object[] {templateId},null);
				sqlQuery = sqlQuery + " AND [t1].SetDate > DATEADD(year,-1,GETDATE())";
				sqlQuery = sqlQuery + " ORDER  BY [t1].[printdate] ASC, [t1].[_tscreate] ";
				lrsObjects = ldb.OpenRecordset(sqlQuery);
			} else {
				lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.TEMPLATE},
						new java.lang.Object[] {templateId},
						new int[] {com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.PRINTEDON});
			}
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
        TransactionSet[] larrResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			if ( Utils.getCurrentAgent() != null )
				return new TransactionSet[0];
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

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

		larrResult = larrAux.toArray(new TransactionSet[larrAux.size()]);

		Arrays.sort(larrResult, new Comparator<TransactionSet>()
		{
			public int compare(TransactionSet o1, TransactionSet o2)
			{
				if (o1.isComplete == o2.isComplete)
					return o2.date.compareTo(o1.date);
				if (o1.isComplete)
					return 1;
				return -1;
			}
		});

		return larrResult;
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
		GenericElement[] larrBuffer;
		Report lobjResult;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrBuffer = sGenerateParamReport(itemId, paramValues);

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

		return sReportToHTML(sGenerateParamReport(itemId, paramValues));
	}

	public String generateParamAsXL(String itemId, String[] paramValues)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sReportToXL(sGenerateParamReport(itemId, paramValues));
	}

	public Report generatePrintSetReport(String itemId, String printSetId)
		throws SessionExpiredException, BigBangException
	{
		GenericElement lobjBuffer;
		Report lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjBuffer = sGeneratePrintSetReport(itemId, printSetId)[0];

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

		return sReportToHTML(sGeneratePrintSetReport(itemId, printSetId));
	}

	public String generatePrintSetAsXL(String itemId, String printSetId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sReportToXL(sGeneratePrintSetReport(itemId, printSetId));
	}

	public Report generateTransactionSetReport(String itemId, String transactionSetId)
		throws SessionExpiredException, BigBangException
	{
		ReportDef lobjReport;
		UUID lidTransactions;
		TransactionSetBase lobjSet;
		TransactionMapBase[] larrMaps;
		int llngCount;
		GenericElement[] lobjBuffer;
		int i;
		boolean b;
		Report lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjBuffer = sGenerateTransactionSetReport(itemId, transactionSetId);

		try
		{
			lobjReport = ReportDef.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(itemId));
			lidTransactions = Engine.FindEntity(Engine.getCurrentNameSpace(), (UUID)lobjReport.getAt(ReportDef.I.TRANSACTIONTYPE));
			lobjSet = (TransactionSetBase)Engine.GetWorkInstance(lidTransactions, UUID.fromString(transactionSetId));
			larrMaps = lobjSet.getCurrentMaps();
			llngCount = larrMaps.length;
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Report();
		lobjResult.sections = new Report.Section[llngCount + 1];
		lobjResult.sections[0] = new Report.Section();
		lobjResult.sections[0].htmlContent = lobjBuffer[0].toString();

		b = false;
		for ( i = 1; i <= llngCount; i++ )
		{
			lobjResult.sections[i] = new Report.Section();
			lobjResult.sections[i].htmlContent = lobjBuffer[i].toString();

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

		return sReportToHTML(sGenerateTransactionSetReport(itemId, transactionSetId));
	}

	public String generateTransSetAsXL(String itemId, String transactionSetId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sReportToXL(sGenerateTransactionSetReport(itemId, transactionSetId));
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
		lobjResult.refersToId = (lidAux == null ? null : lidAux.toString());
		lobjResult.defaultValue = (String)pobjParam.getAt(com.premiumminds.BigBang.Jewel.Objects.ReportParam.I.DEFAULTVALUE);

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
				
		// Gets the extra info
		UUID setOwnerType = (UUID) pobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.OWNERTYPE);
		if (setOwnerType!=null && setOwnerType.equals(Constants.ObjID_InsurerAccountingSet)) {
			// If it is associated to a insurerAccountingMap, it gets the corresponding company
			UUID setOwner = (UUID) pobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.OWNER);
			if (setOwner!=null) {
				InsurerAccountingSet set;
				String companyName = null;
				try {
					set = InsurerAccountingSet.GetInstance(Engine.getCurrentNameSpace(), setOwner);
					TransactionMapBase[] currentMaps = set.getCurrentMaps();
					if (currentMaps != null && currentMaps[0] != null && currentMaps[0].getAt(TransactionMapBase.I.OWNER) != null) {
						if (currentMaps[0] instanceof InsurerAccountingMap) {
							companyName = ((InsurerAccountingMap) currentMaps[0]).getCompany().getLabel();
						} 
					}
				} catch (BigBangJewelException e) {
				}

				if (companyName!=null && companyName.length()>0) {
					lobjResult.extraInfo1 = companyName;
				}
			}
		}

		return lobjResult;
	}

	private static TransactionSet toClient(TransactionSetBase pobjSet) throws BigBangJewelException
	{
		TransactionSet lobjResult;

		lobjResult = new TransactionSet();
		lobjResult.id = pobjSet.getKey().toString();
		lobjResult.date = ((Timestamp)pobjSet.getAt(TransactionSetBase.I.DATE)).toString().substring(0, 10);
		lobjResult.user = ((UUID)pobjSet.getAt(TransactionSetBase.I.USER)).toString();
		TransactionMapBase[] currentMaps = null;
		String companyName = "";
		
		currentMaps = pobjSet.getCurrentMaps();
		if (currentMaps != null && currentMaps[0] != null && currentMaps[0].getAt(TransactionMapBase.I.OWNER) != null) {
			if (currentMaps[0] instanceof InsurerAccountingMap) {
				companyName = ((InsurerAccountingMap) currentMaps[0]).getCompany().getLabel();
			} 
			if (currentMaps[0] instanceof MediatorAccountingMap) {
				Mediator mediator = Mediator.GetInstance(Engine.getCurrentNameSpace(), ((UUID)currentMaps[0].getAt(TransactionMapBase.I.OWNER))); 
				if (mediator != null) {
					companyName = mediator.getLabel();
				}
			} 
		}
		
		lobjResult.company = companyName;
		
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
