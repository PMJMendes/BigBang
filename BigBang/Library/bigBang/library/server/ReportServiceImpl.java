package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportParam;
import bigBang.definitions.shared.TransactionSet;
import bigBang.library.interfaces.ReportService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingSet;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingSet;
import com.premiumminds.BigBang.Jewel.Objects.ReportDef;

public class ReportServiceImpl
	extends EngineImplementor
	implements ReportService
{
	private static final long serialVersionUID = 1L;

	public ReportItem[] getProcessItems(String objectTypeId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<ReportItem> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

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
			lrsObjects = lrefTransactions.SelectByMembers(ldb, new int[] {com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.TEMPLATE},
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
				larrAux.add(toClient(Engine.GetWorkInstance(lrefTransactions.getKey(), lrsObjects)));
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

	@Override
	public Report generateParamReport(String itemId, String[] paramValues)
		throws SessionExpiredException, BigBangException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report generatePrintSetReport(String itemId, String printSetId)
		throws SessionExpiredException, BigBangException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report generateTransactionSetReport(String itemId, String transactionSetId)
		throws SessionExpiredException, BigBangException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RunVerb(String argument)
		throws SessionExpiredException, BigBangException
	{
		// TODO Auto-generated method stub
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
		lobjResult.user = ((UUID)pobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.USER)).toString();
		lobjResult.printDate = ((Timestamp)pobjSet.getAt(com.premiumminds.BigBang.Jewel.Objects.PrintSet.I.PRINTEDON)).toString().substring(0, 10);

		return lobjResult;
	}

	private static TransactionSet toClient(ObjectBase pobjSet)
	{
		TransactionSet lobjResult;

		lobjResult = new TransactionSet();
		lobjResult.id = pobjSet.getKey().toString();

		if ( Constants.ObjID_InsurerAccountingSet.equals(pobjSet.getDefinition().getDefObject().getKey()) )
		{
			lobjResult.date = ((Timestamp)pobjSet.getAt(InsurerAccountingSet.I.DATE)).toString().substring(0, 10);
			lobjResult.user = ((UUID)pobjSet.getAt(InsurerAccountingSet.I.USER)).toString();
			try
			{
				lobjResult.isComplete = ((InsurerAccountingSet)pobjSet).isComplete();
			}
			catch (Throwable e)
			{
				lobjResult.isComplete = false;
			}
		}

		if ( Constants.ObjID_MediatorAccountingSet.equals(pobjSet.getDefinition().getDefObject().getKey()) )
		{
			lobjResult.date = ((Timestamp)pobjSet.getAt(MediatorAccountingSet.I.DATE)).toString().substring(0, 10);
			lobjResult.user = ((UUID)pobjSet.getAt(MediatorAccountingSet.I.USER)).toString();
			try
			{
				lobjResult.isComplete = ((MediatorAccountingSet)pobjSet).isComplete();
			}
			catch (Throwable e)
			{
				lobjResult.isComplete = false;
			}
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
}
