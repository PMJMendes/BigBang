package bigBang.module.expenseModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ExternRequestServiceImpl;
import bigBang.library.server.InfoOrDocumentRequestServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.expenseModule.interfaces.ExpenseService;
import bigBang.module.expenseModule.shared.ExpenseSearchParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Expense.CreateExternRequest;
import com.premiumminds.BigBang.Jewel.Operations.Expense.CreateInfoRequest;
import com.premiumminds.BigBang.Jewel.Operations.Expense.DeleteExpense;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Expense.NotifyClient;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ReceiveAcceptance;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ReceiveReturn;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ReturnToClient;
import com.premiumminds.BigBang.Jewel.Operations.Expense.SendNotification;

public class ExpenseServiceImpl
	extends SearchServiceBase
	implements ExpenseService
{
	private static final long serialVersionUID = 1L;

	public static Expense sGetExpense(UUID pidExpense)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		IProcess lobjProcess;
		ObjectBase lobjParent;
		Client lobjClient;
		ObjectBase lobjInsured;
		ObjectBase lobjPolCov;
		Coverage lobjCoverage;
		SubLine lobjSubLine;
		Expense lobjResult;

		lobjInsured = null;
		lobjPolCov = null;
		lobjCoverage = null;
		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), pidExpense);
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
			lobjParent = lobjProcess.GetParent().GetData();
			if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
			{
				lobjClient = (Client)lobjProcess.GetParent().GetParent().GetData();
				if ( lobjExpense.getAt(3) != null )
					lobjInsured = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(3));
				if ( lobjExpense.getAt(5) != null )
				{
					lobjPolCov = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(5));
					lobjCoverage = ((PolicyCoverage)lobjPolCov).GetCoverage();
				}
				lobjSubLine = ((Policy)lobjProcess.GetParent().GetData()).GetSubLine();
			}
			else
			{
				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjParent.getAt(2));
				if ( lobjExpense.getAt(4) != null )
					lobjInsured = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(4));
				if ( lobjExpense.getAt(6) != null )
				{
					lobjPolCov = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(6));
					lobjCoverage = ((SubPolicyCoverage)lobjPolCov).GetCoverage();
				}
				lobjSubLine = ((Policy)lobjProcess.GetParent().GetParent().GetData()).GetSubLine();
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Expense();
		lobjResult.id = pidExpense.toString();
		lobjResult.number = lobjExpense.getLabel();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.clientId = ( lobjClient == null ? null : lobjClient.getKey().toString() );
		lobjResult.clientNumber = ( lobjClient == null ? null : ((Integer)lobjClient.getAt(1)).toString() );
		lobjResult.clientName = ( lobjClient == null ? null : lobjClient.getLabel() );
		lobjResult.referenceId = ( lobjParent == null ? null : lobjParent.getKey().toString() );
		lobjResult.referenceTypeId = ( lobjParent == null ? null : lobjParent.getDefinition().getDefObject().getKey().toString() );
		lobjResult.referenceNumber = ( lobjParent == null ? null : lobjParent.getLabel() );
		lobjResult.expenseDate = ((Timestamp)lobjExpense.getAt(2)).toString().substring(0, 10);
		lobjResult.insuredObjectId = ( lobjInsured == null ? null : lobjInsured.getKey().toString() );
		lobjResult.insuredObjectName = ( lobjInsured == null ? null : lobjInsured.getLabel() );
		lobjResult.coverageId = ( lobjPolCov == null ? null : lobjPolCov.getKey().toString() );
		lobjResult.coverageName = ( lobjCoverage == null ? null : lobjCoverage.getLabel() );
		lobjResult.value = ((BigDecimal)lobjExpense.getAt(7)).doubleValue();
		lobjResult.isOpen =  lobjProcess.IsRunning();
		lobjResult.categoryName = (lobjSubLine == null ? null : lobjSubLine.getLine().getCategory().getLabel());
		lobjResult.lineName = (lobjSubLine == null ? null : lobjSubLine.getLine().getLabel());
		lobjResult.subLineName = (lobjSubLine == null ? null : lobjSubLine.getLabel());
		lobjResult.managerId = lobjProcess.GetManagerID().toString();
		lobjResult.settlement = ( lobjExpense.getAt(8) == null ? null : ((BigDecimal)lobjExpense.getAt(8)).doubleValue() );
		lobjResult.isManual = (Boolean)lobjExpense.getAt(9);
		lobjResult.notes = (String)lobjExpense.getAt(10);

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public Expense getExpense(String expenseId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetExpense(UUID.fromString(expenseId));
	}

	public Expense editExpense(Expense expense)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		IProcess lobjProcess;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(expense.id));
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMD = new ManageData(lobjProcess.getKey());
		lopMD.mobjData = new ExpenseData();
		lopMD.mobjData.mid = lobjExpense.getKey();
		lopMD.mobjData.mstrNumber = expense.number;
		lopMD.mobjData.mdtDate = Timestamp.valueOf(expense.expenseDate + " 00:00:00.0");
		if ( Constants.ObjID_Policy.equals(UUID.fromString(expense.referenceTypeId)) )
		{
			lopMD.mobjData.midPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
			lopMD.mobjData.midSubPolicyObject = null;
			lopMD.mobjData.midPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
			lopMD.mobjData.midSubPolicyCoverage = null;
		}
		else
		{
			lopMD.mobjData.midPolicyObject = null;
			lopMD.mobjData.midSubPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
			lopMD.mobjData.midPolicyCoverage = null;
			lopMD.mobjData.midSubPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
		}
		lopMD.mobjData.mdblDamages = (expense.value == null ? null : new BigDecimal(expense.value+""));
		lopMD.mobjData.mdblSettlement = (expense.settlement == null ? null : new BigDecimal(expense.settlement+""));
		lopMD.mobjData.mbIsManual = expense.isManual;
		lopMD.mobjData.mstrNotes = expense.notes;
		lopMD.mobjData.midManager = lobjProcess.GetManagerID();
		lopMD.mobjData.midProcess = lobjProcess.getKey();
		lopMD.mobjData.mobjPrevValues = null;
		lopMD.mobjContactOps = null;
		lopMD.mobjDocOps = null;

		try
		{
			lopMD.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetExpense(lobjExpense.getKey());
	}

	public Expense sendNotification(String expenseId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		SendNotification lopSN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(expenseId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopSN = new SendNotification(lobjExpense.GetProcessID());
		lopSN.marrExpenseIDs = new UUID[] {UUID.fromString(expenseId)};
		lopSN.mbUseSets = false;

		try
		{
			lopSN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetExpense(lobjExpense.getKey());
	}

	public Expense receiveAcceptance(Acceptance acceptance)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		ReceiveAcceptance lopRA;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(acceptance.expenseId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRA = new ReceiveAcceptance(lobjExpense.GetProcessID());
		lopRA.mdblSettlement = (acceptance.settlement == null ? null : new BigDecimal(acceptance.settlement+""));

		try
		{
			lopRA.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetExpense(lobjExpense.getKey());
	}

	public Expense receiveReturn(ReturnEx returnEx)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		ReceiveReturn lopRA;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(returnEx.expenseId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRA = new ReceiveReturn(lobjExpense.GetProcessID());
		lopRA.mstrReason = returnEx.reason;

		try
		{
			lopRA.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetExpense(lobjExpense.getKey());
	}

	public Expense notifyClient(String expenseId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		NotifyClient lopNC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(expenseId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopNC = new NotifyClient(lobjExpense.GetProcessID());
		lopNC.marrExpenseIDs = new UUID[] {UUID.fromString(expenseId)};
		lopNC.mbUseSets = false;

		try
		{
			lopNC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetExpense(lobjExpense.getKey());
	}

	public Expense returnToClient(String expenseId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		ReturnToClient lopRTC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(expenseId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopRTC = new ReturnToClient(lobjExpense.GetProcessID());
		lopRTC.marrExpenseIDs = new UUID[] {UUID.fromString(expenseId)};
		lopRTC.mbUseSets = false;

		try
		{
			lopRTC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetExpense(lobjExpense.getKey());
	}

	public InfoOrDocumentRequest createInfoRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		CreateInfoRequest lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		lopCIR = new CreateInfoRequest(lobjExpense.GetProcessID());
		lopCIR.midRequestType = UUID.fromString(request.requestTypeId);
		lopCIR.mobjMessage = MessageBridge.outgoingToServer(request.message);
		lopCIR.mlngDays = request.replylimit;

		try
		{
			lopCIR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lopCIR.midRequestObject);
	}

	public ExternalInfoRequest createExternalRequest(ExternalInfoRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		CreateExternRequest lopCER;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));

			lopCER = new CreateExternRequest(lobjExpense.GetProcessID());
			lopCER.mstrSubject = request.subject;
			lopCER.mobjMessage = MessageBridge.incomingToServer(request.message, Constants.ObjID_Expense);
			lopCER.mlngDays = request.replylimit;

			lopCER.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e); 
		}

		return ExternRequestServiceImpl.sGetRequest(lopCER.midRequestObject);
	}

	public void deleteExpense(String expenseId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		DeleteExpense lopDE;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(expenseId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopDE = new DeleteExpense(lobjExpense.GetProcessID());
		lopDE.midExpense = lobjExpense.getKey();
		lopDE.mstrReason = reason;

		try
		{
			lopDE.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public void massSendNotification(String[] expenseIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrExpenses;
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		IProcess lobjProcess;
		UUID lidCompany;
		ArrayList<UUID> larrByCompany;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetCompany;
		DocOps lobjDocOps;
		SendNotification lopSN;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrExpenses = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < expenseIds.length; i++ )
		{
			try
			{
				lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(expenseIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidCompany = (UUID)lobjProcess.GetParent().GetData().getAt(2);
				else
					lidCompany = (UUID)lobjProcess.GetParent().GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByCompany = larrExpenses.get(lidCompany);
			if ( larrByCompany == null )
			{
				larrByCompany = new ArrayList<UUID>();
				larrExpenses.put(lidCompany, larrByCompany);
			}
			larrByCompany.add(lobjExpense.getKey());
		}

		lidSet = null;
		for(UUID lidC : larrExpenses.keySet())
		{
			lidSetCompany = null;
			lobjDocOps = null;
			larrByCompany = larrExpenses.get(lidC);
			larrFinal = larrByCompany.toArray(new UUID[larrByCompany.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopSN = new SendNotification(lobjExpense.GetProcessID());
					lopSN.marrExpenseIDs = larrFinal;
					lopSN.mbUseSets = true;
					lopSN.midSet = lidSet;
					lopSN.midSetDocument = lidSetCompany;
					lopSN.mobjDocOps = lobjDocOps;

					lopSN.Execute();

					lobjDocOps = lopSN.mobjDocOps;
					lidSetCompany = lopSN.midSetDocument;
					lidSet = lopSN.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massNotifyClient(String[] expenseIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrExpenses;
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		IProcess lobjProcess;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		NotifyClient lopNC;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrExpenses = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < expenseIds.length; i++ )
		{
			try
			{
				lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(expenseIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidClient = lobjProcess.GetParent().GetParent().GetDataKey();
				else
					lidClient = (UUID)lobjProcess.GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrExpenses.get(lidClient);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrExpenses.put(lidClient, larrByClient);
			}
			larrByClient.add(lobjExpense.getKey());
		}

		lidSet = null;
		for(UUID lidC : larrExpenses.keySet())
		{
			lidSetClient = null;
			lobjDocOps = null;
			larrByClient = larrExpenses.get(lidC);
			larrFinal = larrByClient.toArray(new UUID[larrByClient.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopNC = new NotifyClient(lobjExpense.GetProcessID());
					lopNC.marrExpenseIDs = larrFinal;
					lopNC.mbUseSets = true;
					lopNC.midSet = lidSet;
					lopNC.midSetDocument = lidSetClient;
					lopNC.mobjDocOps = lobjDocOps;

					lopNC.Execute();

					lobjDocOps = lopNC.mobjDocOps;
					lidSetClient = lopNC.midSetDocument;
					lidSet = lopNC.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massReturnToClient(String[] expenseIds)
		throws SessionExpiredException, BigBangException
	{
		Hashtable<UUID, ArrayList<UUID>> larrExpenses;
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		IProcess lobjProcess;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetClient;
		DocOps lobjDocOps;
		ReturnToClient lopRTC;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrExpenses = new Hashtable<UUID, ArrayList<UUID>>();
		for ( i = 0; i < expenseIds.length; i++ )
		{
			try
			{
				lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(expenseIds[i]));
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
					lidClient = lobjProcess.GetParent().GetParent().GetDataKey();
				else
					lidClient = (UUID)lobjProcess.GetParent().GetData().getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrExpenses.get(lidClient);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrExpenses.put(lidClient, larrByClient);
			}
			larrByClient.add(lobjExpense.getKey());
		}

		lidSet = null;
		for(UUID lidC : larrExpenses.keySet())
		{
			lidSetClient = null;
			lobjDocOps = null;
			larrByClient = larrExpenses.get(lidC);
			larrFinal = larrByClient.toArray(new UUID[larrByClient.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopRTC = new ReturnToClient(lobjExpense.GetProcessID());
					lopRTC.marrExpenseIDs = larrFinal;
					lopRTC.mbUseSets = true;
					lopRTC.midSet = lidSet;
					lopRTC.midSetDocument = lidSetClient;
					lopRTC.mobjDocOps = lobjDocOps;

					lopRTC.Execute();

					lobjDocOps = lopRTC.mobjDocOps;
					lidSetClient = lopRTC.midSetDocument;
					lidSet = lopRTC.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Expense;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Date]", "[:Policy Object]", "[:Sub Policy Object]", "[:Policy Coverage]",
				"[:Sub Policy Coverage]", "[:Damages]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ExpenseSearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;
		IEntity lrefObjects;
		IEntity lrefCoverages;
		IEntity lrefClients;
        Calendar ldtAux;

		if ( !(pParam instanceof ExpenseSearchParameter) )
			return false;
		lParam = (ExpenseSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Damages] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(LEFT(CONVERT(NVARCHAR, [:Date], 120), 10) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([PK] IN (SELECT [:Policy] FROM(");
			try
			{
				lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
				pstrBuffer.append(lrefObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxObjs] WHERE (");
			pstrBuffer.append("([:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(")))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([PK] IN (SELECT [:Policy] FROM(");
			try
			{
				lrefCoverages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyCoverage));
				pstrBuffer.append(lrefCoverages.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxCovs] WHERE (");
			pstrBuffer.append("([:Coverage:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(")))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE (");
			pstrBuffer.append("([:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append("))))))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSPols] WHERE (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Subscriber:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("(CAST([:Subscriber:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([PK] IN (SELECT [:Sub Policy] FROM(");
			try
			{
				lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyObject));
				pstrBuffer.append(lrefObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSObjs] WHERE (");
			pstrBuffer.append("([:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(")))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([PK] IN (SELECT [:Sub Policy] FROM(");
			try
			{
				lrefCoverages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyCoverage));
				pstrBuffer.append(lrefCoverages.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSCovs] WHERE (");
			pstrBuffer.append("([:Coverage:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(")))");
			pstrBuffer.append(" OR ");
			pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSMPols] WHERE (");
			pstrBuffer.append("([:Number] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(")))))))");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND (([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("'))");
			pstrBuffer.append(" OR ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')))");
		}

		if ( lParam.dateFrom != null )
		{
			pstrBuffer.append(" AND [:Date] >= '").append(lParam.dateFrom).append("'");
		}

		if ( lParam.dateTo != null )
		{
			pstrBuffer.append(" AND [:Date] < '");
        	ldtAux = Calendar.getInstance();
        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.dateTo + " 00:00:00.0").getTime());
        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
			pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10)).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		ExpenseSortParameter lParam;

		if ( !(pParam instanceof ExpenseSortParameter) )
			return false;
		lParam = (ExpenseSortParameter)pParam;

		if ( lParam.field == ExpenseSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == ExpenseSortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == ExpenseSortParameter.SortableField.DATE )
			pstrBuffer.append("[:Date]");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		ObjectBase lobjParent;
		Client lobjClient;
		ObjectBase lobjInsured;
		ObjectBase lobjPolCov;
		Coverage lobjCoverage;
		ExpenseStub lobjResult;

		lobjProcess = null;
		lobjParent = null;
		lobjClient = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			lobjParent = lobjProcess.GetParent().GetData();
			if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
				lobjClient = (Client)lobjProcess.GetParent().GetParent().GetData();
			else
				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjParent.getAt(2));
		}
		catch (Throwable e)
		{
		}
		lobjInsured = null;
		try
		{
			if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
			{
				if ( parrValues[3] != null )
					lobjInsured = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[3]);
			}
			else
			{
				if ( parrValues[4] != null )
					lobjInsured = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[4]);
			}
		}
		catch (Throwable e)
		{
		}
		lobjPolCov = null;
		lobjCoverage = null;
		try
		{
			if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
			{
				if ( parrValues[5] != null )
				{
					lobjPolCov = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[5]);
					lobjCoverage = ((PolicyCoverage)lobjPolCov).GetCoverage();
				}
			}
			else
			{
				if ( parrValues[6] != null )
				{
					lobjPolCov = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[6]);
					lobjCoverage = ((SubPolicyCoverage)lobjPolCov).GetCoverage();
				}
			}
		}
		catch (Throwable e)
		{
		}

		lobjResult = new ExpenseStub();
		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.processId = ((UUID)parrValues[1]).toString();
		lobjResult.clientId = ( lobjClient == null ? null : lobjClient.getKey().toString() );
		lobjResult.clientNumber = ( lobjClient == null ? null : ((Integer)lobjClient.getAt(1)).toString() );
		lobjResult.clientName = ( lobjClient == null ? null : lobjClient.getLabel() );
		lobjResult.referenceId = ( lobjParent == null ? null : lobjParent.getKey().toString() );
		lobjResult.referenceTypeId = ( lobjParent == null ? null : lobjParent.getDefinition().getDefObject().getKey().toString() );
		lobjResult.referenceNumber = ( lobjParent == null ? null : lobjParent.getLabel() );
		lobjResult.expenseDate = ((Timestamp)parrValues[2]).toString().substring(0, 10);
		lobjResult.insuredObjectId = ( lobjInsured == null ? null : lobjInsured.getKey().toString() );
		lobjResult.insuredObjectName = ( lobjInsured == null ? null : lobjInsured.getLabel() );
		lobjResult.coverageId = ( lobjPolCov == null ? null : lobjPolCov.getKey().toString() );
		lobjResult.coverageName = ( lobjCoverage == null ? null : lobjCoverage.getLabel() );
		lobjResult.value = ((BigDecimal)parrValues[7]).doubleValue();
		lobjResult.isOpen =  lobjProcess.IsRunning();

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		ExpenseSearchParameter lParam;
		String lstrAux;
//		IEntity lrefPolicies;
//		IEntity lrefClients;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof ExpenseSearchParameter) )
				continue;
			lParam = (ExpenseSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ");
			pstrBuffer.append("0 END");
		}

		return lbFound;
	}
}
