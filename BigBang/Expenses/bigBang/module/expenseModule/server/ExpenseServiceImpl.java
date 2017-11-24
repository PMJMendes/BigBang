package bigBang.module.expenseModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.ProcessData;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.ScanHandle;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.ImageItem;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.expenseModule.interfaces.ExpenseService;
import bigBang.module.expenseModule.shared.ExpenseSearchParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Expense.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.Expense.DeleteExpense;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Expense.NotifyClient;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ReceiveAcceptance;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ReceiveReception;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ReceiveReturn;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ReturnToClient;
import com.premiumminds.BigBang.Jewel.Operations.Expense.SendNotification;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

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
		Policy lobjPolicy;
		Mediator lobjMed;
		Company lobjComp;
		Client lobjMasterClient;
		Mediator lobjMasterMed;
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
				lobjPolicy = (Policy)lobjProcess.GetParent().GetData();
				lobjClient = lobjPolicy.GetClient();
				if ( lobjExpense.getAt(3) != null )
					lobjInsured = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(3));
				if ( lobjExpense.getAt(5) != null )
				{
					lobjPolCov = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(5));
					lobjCoverage = ((PolicyCoverage)lobjPolCov).GetCoverage();
				}
				lobjSubLine = ((Policy)lobjProcess.GetParent().GetData()).GetSubLine();
				lobjMasterClient = null;
				lobjMasterMed = null;
			}
			else
			{
				lobjPolicy = (Policy)lobjProcess.GetParent().GetParent().GetData();
				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjParent.getAt(2));
				if ( lobjExpense.getAt(4) != null )
					lobjInsured = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(4));
				if ( lobjExpense.getAt(6) != null )
				{
					lobjPolCov = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(6));
					lobjCoverage = ((SubPolicyCoverage)lobjPolCov).GetCoverage();
				}
				lobjSubLine = ((Policy)lobjProcess.GetParent().GetParent().GetData()).GetSubLine();
				lobjMasterClient = lobjPolicy.GetClient();
				lobjMasterMed = lobjMasterClient.getMediator();
			}
			lobjMed = lobjPolicy.getMediator();
			lobjComp = lobjPolicy.GetCompany();
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
		lobjResult.insuredObjectName = ( lobjInsured == null ? (String)lobjExpense.getAt(12) : lobjInsured.getLabel() );
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
		lobjResult.referenceSubLineId = (lobjSubLine == null ? null : lobjSubLine.getKey().toString());

		lobjResult.inheritInsurerId = lobjComp.getKey().toString();
		lobjResult.inheritInsurerName = lobjComp.getLabel();
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.inheritMasterClientId = (lobjMasterClient == null ? null : lobjMasterClient.getKey().toString());
		lobjResult.inheritMasterClientName = (lobjMasterClient == null ? null : lobjMasterClient.getLabel());
		lobjResult.inheritMasterMediatorId = (lobjMasterMed == null ? null : lobjMasterMed.getKey().toString());
		lobjResult.inheritMasterMediatorName = (lobjMasterMed == null ? null : lobjMasterMed.getLabel());

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

	public ImageItem getItemAsImage(String pstrItem, int pageNumber)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		throw new BigBangException("Erro: Funcionalidade não implementada.");
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
		lopMD.mobjData.mstrGenericObject = (expense.insuredObjectId == null ? expense.insuredObjectName : null);
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

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjExpense.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Expense,
				lobjExpense.getKey(), Constants.MsgDir_Outgoing, null);

		lopCC.isSend = true;

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public Conversation receiveMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 519 ", e);
		}

		lopCC = new CreateConversation(lobjExpense.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming ); // On NULL, default is INCOMING
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Incoming : Constants.MsgDir_Outgoing ) );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		
		javax.mail.Message storedMessage = null;
		try {
			storedMessage = MailConnector.getStoredMessage(null, conversation.messages[0].emailId);
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage() + " 540 ", e);
		}
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_Expense,
				lobjExpense.getKey(), lopCC.mobjData.midStartDir, storedMessage);

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 551 ", e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
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

	public Expense serialCreateExpense(Expense expense, ScanHandle source)
		throws SessionExpiredException, BigBangException
	{
		ExpenseData lobjData;
		DSBridgeData lobjImage;
		ProcessData lobjParent;
		com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense lopPCE;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateExpense lopSPCE;
		Operation lop;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjData = new ExpenseData();
		lobjData.mid = null;
		lobjData.mstrNumber = null;
		lobjData.mdtDate = Timestamp.valueOf(expense.expenseDate + " 00:00:00.0");
		lobjData.midPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
		lobjData.midSubPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
		lobjData.midPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
		lobjData.midSubPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
		lobjData.mstrGenericObject = (expense.insuredObjectId == null ? expense.insuredObjectName : null);
		lobjData.mdblDamages = new BigDecimal(expense.value+"");
		lobjData.mdblSettlement = (expense.settlement == null ? null : new BigDecimal(expense.settlement + ""));
		lobjData.mbIsManual = expense.isManual;
		lobjData.mstrNotes = expense.notes;
		lobjData.midManager = null;
		lobjData.midProcess = null;
		lobjData.mobjPrevValues = null;

		if ( source != null )
		{
			lobjImage = new DSBridgeData();
			lobjImage.mbDocushare = source.docushare;
			lobjImage.mstrDSHandle = source.handle;
			lobjImage.mstrDSLoc = source.locationHandle;
			lobjImage.mstrDSTitle = null;
			lobjImage.mbDelete = true;
		}
		else
			lobjImage = null;

		try
		{
			if ( Constants.ObjID_Policy.equals(UUID.fromString(expense.referenceTypeId)) )
			{
				lobjParent = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(expense.referenceId));
				lopPCE = new com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense(lobjParent.GetProcessID());
				lobjData.midSubPolicyObject = null;
				lobjData.midSubPolicyCoverage = null;
				lopPCE.mobjData = lobjData;
				lopPCE.mobjImage = lobjImage;
				lopPCE.mobjContactOps = null;
				lopPCE.mobjDocOps = null;
				lop = lopPCE;
			}
			else
			{
				lobjParent = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(expense.referenceId));
				lopSPCE = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateExpense(lobjParent.GetProcessID());
				lobjData.midPolicyObject = null;
				lobjData.midPolicyCoverage = null;
				lopSPCE.mobjData = lobjData;
				lopSPCE.mobjImage = lobjImage;
				lopSPCE.mobjContactOps = null;
				lopSPCE.mobjDocOps = null;
				lop = lopSPCE;
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}


		try
		{
			lop.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( Constants.ObjID_Policy.equals(UUID.fromString(expense.referenceTypeId)) )
			return ExpenseServiceImpl.sGetExpense(((com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense)lop).mobjData.mid);
		else
			return ExpenseServiceImpl.sGetExpense(((com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateExpense)lop).mobjData.mid);
	}

	public void massSendNotification(String[] expenseIds)
		throws SessionExpiredException, BigBangException
	{
		HashMap<UUID, ArrayList<UUID>> larrExpenses;
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		UUID lidPolicy;
		ArrayList<UUID> larrByPolicy;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetPolicy;
		DocOps lobjDocOps;
		SendNotification lopSN;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrExpenses = new HashMap<UUID, ArrayList<UUID>>();
		for ( i = 0; i < expenseIds.length; i++ )
		{
			try
			{
				lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(expenseIds[i]));
				lidPolicy = (UUID)lobjExpense.getAbsolutePolicy().getKey();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByPolicy = larrExpenses.get(lidPolicy);
			if ( larrByPolicy == null )
			{
				larrByPolicy = new ArrayList<UUID>();
				larrExpenses.put(lidPolicy, larrByPolicy);
			}
			larrByPolicy.add(lobjExpense.getKey());
		}

		lidSet = null;
		for(UUID lidP : larrExpenses.keySet())
		{
			lidSetPolicy = null;
			lobjDocOps = null;
			larrByPolicy = larrExpenses.get(lidP);
			larrFinal = larrByPolicy.toArray(new UUID[larrByPolicy.size()]);
			for ( i = 0; i < larrFinal.length; i++ )
			{
				try
				{
					lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), larrFinal[i]);

					lopSN = new SendNotification(lobjExpense.GetProcessID());
					lopSN.marrExpenseIDs = larrFinal;
					lopSN.mbUseSets = true;
					lopSN.midSet = lidSet;
					lopSN.midSetDocument = lidSetPolicy;
					lopSN.mobjDocOps = lobjDocOps;

					lopSN.Execute();

					lobjDocOps = lopSN.mobjDocOps;
					lidSetPolicy = lopSN.midSetDocument;
					lidSet = lopSN.midSet;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
	}

	public void massReceiveReception(String[] expenseIds, ScanHandle source)
		throws SessionExpiredException, BigBangException
	{
		MasterDB ldb;
		DocOps lobjDocOps;
		DSBridgeData lobjImage;
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		ReceiveReception lopRR;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjDocOps = null;
		lobjImage = null;

		try
		{
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

		for ( i = 0; i < expenseIds.length; i++ )
		{
			if ( (lobjDocOps == null) && (source != null) )
			{
				lobjImage = new DSBridgeData();
				lobjImage.mbDocushare = source.docushare;
				lobjImage.mstrDSHandle = source.handle;
				lobjImage.mstrDSLoc = source.locationHandle;
				lobjImage.mstrDSTitle = null;
				lobjImage.mbDelete = true;
			}

			try
			{
				lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(expenseIds[i]));

				lopRR = new ReceiveReception(lobjExpense.GetProcessID());
				lopRR.mobjImage = lobjImage;
				lopRR.mobjDocOps = lobjDocOps;

				lopRR.Execute(ldb);
			}
			catch (Throwable e)
			{
				try { ldb.Rollback(); } catch (Throwable e1) {}
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}

			lobjDocOps = lopRR.mobjDocOps;
			lobjImage = null;
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

	public void massNotifyClient(String[] expenseIds)
		throws SessionExpiredException, BigBangException
	{
		HashMap<String, ArrayList<UUID>> larrExpenses;
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		UUID lidKey;
		String lstrKey;
		IProcess lobjProcess;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetObject;
		DocOps lobjDocOps;
		NotifyClient lopNC;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrExpenses = new HashMap<String, ArrayList<UUID>>();
		for ( i = 0; i < expenseIds.length; i++ )
		{
			try
			{
				lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(expenseIds[i]));
				lidKey = (UUID)lobjExpense.getAt(com.premiumminds.BigBang.Jewel.Objects.Expense.I.POLICYOBJECT);
				if ( lidKey == null )
					lidKey = (UUID)lobjExpense.getAt(com.premiumminds.BigBang.Jewel.Objects.Expense.I.SUBPOLICYOBJECT);
				lstrKey = ( lidKey == null ? (String)lobjExpense.getAt(com.premiumminds.BigBang.Jewel.Objects.Expense.I.GENERICOBJECT) :
						lidKey.toString() );
				if ( lstrKey == null )
				{
					lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
					if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
						lidClient = lobjProcess.GetParent().GetParent().GetDataKey();
					else
						lidClient = (UUID)lobjProcess.GetParent().GetData().getAt(2);
					lstrKey = lidClient.toString();
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrExpenses.get(lstrKey);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrExpenses.put(lstrKey, larrByClient);
			}
			larrByClient.add(lobjExpense.getKey());
		}

		lidSet = null;
		for(String lstr : larrExpenses.keySet())
		{
			lidSetObject = null;
			lobjDocOps = null;
			larrByClient = larrExpenses.get(lstr);
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
					lopNC.midSetDocument = lidSetObject;
					lopNC.mobjDocOps = lobjDocOps;

					lopNC.Execute();

					lobjDocOps = lopNC.mobjDocOps;
					lidSetObject = lopNC.midSetDocument;
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
		HashMap<String, ArrayList<UUID>> larrExpenses;
		com.premiumminds.BigBang.Jewel.Objects.Expense lobjExpense;
		UUID lidKey;
		String lstrKey;
		IProcess lobjProcess;
		UUID lidClient;
		ArrayList<UUID> larrByClient;
		UUID[] larrFinal;
		UUID lidSet;
		UUID lidSetObject;
		DocOps lobjDocOps;
		ReturnToClient lopRTC;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrExpenses = new HashMap<String, ArrayList<UUID>>();
		for ( i = 0; i < expenseIds.length; i++ )
		{
			try
			{
				lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(expenseIds[i]));
				lidKey = (UUID)lobjExpense.getAt(com.premiumminds.BigBang.Jewel.Objects.Expense.I.POLICYOBJECT);
				if ( lidKey == null )
					lidKey = (UUID)lobjExpense.getAt(com.premiumminds.BigBang.Jewel.Objects.Expense.I.SUBPOLICYOBJECT);
				lstrKey = ( lidKey == null ? (String)lobjExpense.getAt(com.premiumminds.BigBang.Jewel.Objects.Expense.I.GENERICOBJECT) :
						lidKey.toString() );
				if ( lstrKey == null )
				{
					lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
					if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
						lidClient = lobjProcess.GetParent().GetParent().GetDataKey();
					else
						lidClient = (UUID)lobjProcess.GetParent().GetData().getAt(2);
					lstrKey = lidClient.toString();
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			larrByClient = larrExpenses.get(lstrKey);
			if ( larrByClient == null )
			{
				larrByClient = new ArrayList<UUID>();
				larrExpenses.put(lstrKey, larrByClient);
			}
			larrByClient.add(lobjExpense.getKey());
		}

		lidSet = null;
		for(String lstr : larrExpenses.keySet())
		{
			lidSetObject = null;
			lobjDocOps = null;
			larrByClient = larrExpenses.get(lstr);
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
					lopRTC.midSetDocument = lidSetObject;
					lopRTC.mobjDocOps = lobjDocOps;

					lopRTC.Execute();

					lobjDocOps = lopRTC.mobjDocOps;
					lidSetObject = lopRTC.midSetDocument;
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
				"[:Sub Policy Coverage]", "[:Damages]", "[:Generic Object]"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;

		pstrBuffer.append(" AND (");
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
		pstrBuffer.append(") [AuxPols] WHERE ([:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR ([:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Client:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR [:Client:Group:Mediator] = '").append(pidMediator.toString()).append("')))))");
		pstrBuffer.append(" OR ");
		pstrBuffer.append("([:Process:Parent] IN (SELECT [:Process] FROM (");
		try
		{
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			pstrBuffer.append(lrefSubPolicies.SQLForSelectMulti());
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxSPols] WHERE ([:Subscriber:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR [:Subscriber:Group:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR [:Policy:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR ([:Policy:Mediator] IS NULL");
		pstrBuffer.append(" AND ([:Policy:Client:Mediator] = '").append(pidMediator.toString()).append("'");
		pstrBuffer.append(" OR [:Policy:Client:Group:Mediator] = '").append(pidMediator.toString()).append("')))))");
		pstrBuffer.append(")");
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
		IEntity lrefPolObjects;
		IEntity lrefSubPolObjects;
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
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
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

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.insurerId != null )
		{
			pstrBuffer.append(" AND (([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPols] WHERE [:Company] = '").append(lParam.insurerId).append("'))");
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
			pstrBuffer.append(") [AuxSPols] WHERE ([:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSMPols] WHERE [:Company] = '").append(lParam.insurerId).append("')))))");
		}

		if ( lParam.insuredObject != null )
		{
			lstrAux = lParam.insuredObject.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Generic Object] LIKE '%").append(lstrAux).append("%'")
					.append(" OR [:Policy Object] IN (SELECT [PK] FROM (");
			try
			{
				lrefPolObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
				pstrBuffer.append(lrefPolObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxPolObjects] WHERE [:Name] LIKE '%").append(lstrAux).append("%')")
					.append(" OR [:Sub Policy Object] IN (SELECT [PK] FROM (");
			try
			{
				lrefSubPolObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyObject));
				pstrBuffer.append(lrefSubPolObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubPolObjects] WHERE [:Name] LIKE '%").append(lstrAux).append("%'))");
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
		
		// jcamilo: Added the sorting order
		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

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
		lobjResult.insuredObjectName = ( lobjInsured == null ? (String)parrValues[8] : lobjInsured.getLabel() );
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
