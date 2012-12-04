package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IStep;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.expenseModule.server.ExpenseServiceImpl;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;
import bigBang.module.insurancePolicyModule.shared.SubPolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.SubPolicySortParameter;
import bigBang.module.receiptModule.server.ReceiptServiceImpl;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyData;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateExpense;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.DeleteSubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.PerformComputations;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.TransferToPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ValidateSubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.VoidSubPolicy;

public class SubPolicyServiceImpl
	extends SearchServiceBase
	implements SubPolicyService
{
	private static final long serialVersionUID = 1L;

	public SearchResult[] getExactResults(String label)
		throws SessionExpiredException, BigBangException
	{
		return getExactResultsByOp(label, null);
	}

	public SearchResult[] getExactResultsByOp(String label, String operationId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<SearchResult> larrResult;
        IEntity lrefPolicies;
        MasterDB ldb;
        ResultSet lrsPolicies;
        com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		IProcess lobjProc;
		IStep lobjStep;
		SubPolicyStub lobjStub;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrResult = new ArrayList<SearchResult>();

		try
        {
            lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
            ldb = new MasterDB();
        }
        catch (Throwable e)
        {
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            lrsPolicies = lrefPolicies.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {"!" + label}, null);
        }
        catch (Throwable e)
        {
        	try {ldb.Disconnect();} catch (Throwable e1) {}
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            while (lrsPolicies.next())
            {
            	lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
            			lrsPolicies);
    			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjSubPolicy.GetProcessID());

    			if ( operationId != null )
    			{
    				lobjStep = lobjProc.GetOperation(UUID.fromString(operationId), ldb);
    				if ( (lobjStep == null) || (Jewel.Petri.Constants.LevelID_Invalid.equals(lobjStep.GetLevel())) )
    					continue;
    			}

            	lobjStub = new SubPolicyStub();
            	ServerToClient.buildSubPolicyStub(lobjStub, lobjSubPolicy);

            	larrResult.add(lobjStub);
            }
        }
        catch (Throwable e)
        {
        	try {lrsPolicies.close();} catch (Throwable e2) {}
        	try {ldb.Disconnect();} catch (Throwable e1) {}
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            lrsPolicies.close();
        }
        catch (Throwable e)
        {
        	try {ldb.Disconnect();} catch (Throwable e1) {}
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

		return larrResult.toArray(new SearchResult[larrResult.size()]);
	}

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		SubPolicyCoverage[] larrCoverages;
		ArrayList<TipifiedListItem> larrResult;
		TipifiedListItem lobjAux;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( filterId == null )
			throw new BigBangException("Erro: Apólice adesão de trabalho não existente.");

		try
		{
			if ( Constants.ObjID_SubPolicyCoverage.equals(UUID.fromString(listId)) )
			{
				lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(filterId));
				larrCoverages = lobjSubPolicy.GetCurrentCoverages();
				larrResult = new ArrayList<TipifiedListItem>();
				for ( i = 0; i < larrCoverages.length; i++ )
				{
					if ( larrCoverages[i].GetCoverage().IsHeader() /*|| (larrCoverages[i].IsPresent() == null) || !larrCoverages[i].IsPresent()*/ )
						continue;
					lobjAux = new TipifiedListItem();
					lobjAux.id = larrCoverages[i].getKey().toString();
					lobjAux.value = larrCoverages[i].GetCoverage().getLabel();
					larrResult.add(lobjAux);
				}
				return larrResult.toArray(new TipifiedListItem[larrResult.size()]);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		throw new BigBangException("Erro: Lista inválida para o espaço de trabalho.");
	}

	public SubPolicy getEmptySubPolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new ServerToClient().getEmptySubPolicy(UUID.fromString(policyId));
	}

	public SubPolicy getSubPolicy(String subPolicyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new ServerToClient().getSubPolicy(UUID.fromString(subPolicyId));
	}

	public SubPolicy editSubPolicy(SubPolicy subPolicy)
		throws SessionExpiredException, BigBangException, BigBangPolicyValidationException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		SubPolicyData lobjData;
		ManageData lopMPD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicy.id));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjData = new ClientToServer().getSPDataForEdit(lobjSubPolicy, subPolicy);

		try
		{
			lopMPD = new ManageData(lobjSubPolicy.GetProcessID());
			lopMPD.mobjData = lobjData;

			lopMPD.mobjContactOps = null;
			lopMPD.mobjDocOps = null;

			lopMPD.Execute();
		}
		catch (PolicyValidationException e)
		{
			throw new BigBangPolicyValidationException(e.getMessage());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new ServerToClient().getSubPolicy(lobjSubPolicy.getKey());
	}

	public SubPolicy performCalculations(String subPolicyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyCalculationException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		PerformComputations lopPC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopPC = new PerformComputations(lobjSubPolicy.GetProcessID());

		try
		{
			lopPC.Execute();
		}
		catch (PolicyCalculationException e)
		{
			throw new BigBangPolicyCalculationException(e.getMessage());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new ServerToClient().getSubPolicy(lobjSubPolicy.getKey());
	}

	public void validateSubPolicy(String subPolicyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyValidationException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		ValidateSubPolicy lopVSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVSP = new ValidateSubPolicy(lobjSubPolicy.GetProcessID());

		try
		{
			lopVSP.Execute();
		}
		catch (PolicyValidationException e)
		{
			throw new BigBangPolicyValidationException(e.getMessage());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	@Override
	public InsuredObject includeObject(String subPolicyId, InsuredObject object)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject includeObjectFromClient(String subPolicyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excludeObject(String subPolicyId, String objectId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

	public SubPolicy transferToPolicy(String subPolicyId, String newPolicyId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		TransferToPolicy lopTTP;
		Policy lobjPolicy;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicyId));
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(newPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopTTP = new TransferToPolicy(lobjSubPolicy.GetProcessID());
		lopTTP.midNewProcess = lobjPolicy.GetProcessID();

		try
		{
			lopTTP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getSubPolicy(subPolicyId);
	}

	public SubPolicy voidSubPolicy(PolicyVoiding voiding)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		ObjectBase lobjMotive;
		VoidSubPolicy lopVSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(voiding.policyId));
			lobjMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_PolicyVoidingMotives), UUID.fromString(voiding.motiveId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVSP = new VoidSubPolicy(lobjSubPolicy.GetProcessID());
		lopVSP.mdtEffectDate = Timestamp.valueOf(voiding.effectDate + " 00:00:00.0");
		lopVSP.mstrMotive = lobjMotive.getLabel();
		lopVSP.mstrNotes = voiding.notes;

		try
		{
			lopVSP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getSubPolicy(voiding.policyId);
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPol;
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
			lobjSubPol = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjSubPol.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.messages[0].subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0]);

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
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPol;
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
			lobjSubPol = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjSubPol.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.messages[0].subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Incoming;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Outgoing );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0]);

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

	public Receipt createReceipt(String policyId, Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		CreateReceipt lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(policyId));

			lopCR = new CreateReceipt(lobjSubPolicy.GetProcessID());
			lopCR.mobjData = new ReceiptData();

			lopCR.mobjData.mid = null;

			lopCR.mobjData.mstrNumber = receipt.number;
			lopCR.mobjData.midType = UUID.fromString(receipt.typeId);
			lopCR.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium + "");
			lopCR.mobjData.mdblCommercial = (receipt.salesPremium == null ? null : new BigDecimal(receipt.salesPremium + ""));
			lopCR.mobjData.mdblCommissions = (receipt.comissions == null ? null : new BigDecimal(receipt.comissions + ""));
			lopCR.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? null : new BigDecimal(receipt.retrocessions + ""));
			lopCR.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue + ""));
			lopCR.mobjData.mdblBonusMalus = (receipt.bonusMalus == null ? null : new BigDecimal(receipt.bonusMalus + ""));
			lopCR.mobjData.mbIsMalus = receipt.isMalus;
			lopCR.mobjData.mdtIssue = Timestamp.valueOf(receipt.issueDate + " 00:00:00.0");
			lopCR.mobjData.mdtMaturity = (receipt.maturityDate == null ? null :
					Timestamp.valueOf(receipt.maturityDate + " 00:00:00.0"));
			lopCR.mobjData.mdtEnd = (receipt.endDate == null ? null : Timestamp.valueOf(receipt.endDate + " 00:00:00.0"));
			lopCR.mobjData.mdtDue = (receipt.dueDate == null ? null : Timestamp.valueOf(receipt.dueDate + " 00:00:00.0"));
			lopCR.mobjData.midMediator = (receipt.mediatorId == null ? null : UUID.fromString(receipt.mediatorId));
			lopCR.mobjData.mstrNotes = receipt.notes;
			lopCR.mobjData.mstrDescription = receipt.description;

			lopCR.mobjData.midManager = ( receipt.managerId == null ? null : UUID.fromString(receipt.managerId) );
			lopCR.mobjData.midProcess = null;

			lopCR.mobjData.mobjPrevValues = null;

			if ( (receipt.contacts != null) && (receipt.contacts.length > 0) )
			{
				lopCR.mobjContactOps = new ContactOps();
				lopCR.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(receipt.contacts);
			}
			else
				lopCR.mobjContactOps = null;
			if ( (receipt.documents != null) && (receipt.documents.length > 0) )
			{
				lopCR.mobjDocOps = new DocOps();
				lopCR.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(receipt.documents);
			}
			else
				lopCR.mobjDocOps = null;

			lopCR.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ReceiptServiceImpl.sGetReceipt(lopCR.mobjData.mid);
	}

	public Expense createExpense(Expense expense)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		CreateExpense lopCE;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( !Constants.ObjID_SubPolicy.equals(UUID.fromString(expense.referenceTypeId)) )
			throw new BigBangException("Erro: Tentativa de criar despesa de saúde de apólice a partir de uma apólice adesão.");

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(expense.referenceId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCE = new CreateExpense(lobjSubPolicy.GetProcessID());
		lopCE.mobjData = new ExpenseData();
		lopCE.mobjData.mid = null;
		lopCE.mobjData.mstrNumber = null;
		lopCE.mobjData.mdtDate = Timestamp.valueOf(expense.expenseDate + " 00:00:00.0");
		lopCE.mobjData.midPolicyObject = null;
		lopCE.mobjData.midSubPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
		lopCE.mobjData.midPolicyCoverage = null;
		lopCE.mobjData.midSubPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
		lopCE.mobjData.mstrGenericObject = (expense.insuredObjectId == null ? expense.insuredObjectName : null);
		lopCE.mobjData.mdblDamages = new BigDecimal(expense.value + "");
		lopCE.mobjData.mdblSettlement = (expense.settlement == null ? null : new BigDecimal(expense.settlement + ""));
		lopCE.mobjData.mbIsManual = expense.isManual;
		lopCE.mobjData.mstrNotes = expense.notes;
		lopCE.mobjData.midManager = null;
		lopCE.mobjData.midProcess = null;
		lopCE.mobjData.mobjPrevValues = null;
		lopCE.mobjContactOps = null;
		lopCE.mobjDocOps = null;

		try
		{
			lopCE.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ExpenseServiceImpl.sGetExpense(lopCE.mobjData.mid);
	}

	public void deleteSubPolicy(String policyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjPolicy;
		DeleteSubPolicy lobjDSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(policyId));

			lobjDSP = new DeleteSubPolicy(lobjPolicy.GetProcessID());
			lobjDSP.midSubPolicy = UUID.fromString(policyId);
			lobjDSP.mstrReason = reason;
			lobjDSP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_SubPolicy;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Subscriber]", "[:Subscriber:Number]", "[:Subscriber:Name]",
				"[:Status]", "[:Status:Status]", "[:Status:Level]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		SubPolicySearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;

		if ( !(pParam instanceof SubPolicySearchParameter) )
			return false;
		lParam = (SubPolicySearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Subscriber:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR CAST([:Subscriber:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
		}

		if ( lParam.clientId != null )
		{
			pstrBuffer.append(" AND [:Subscriber] = '").append(lParam.clientId).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		SubPolicySortParameter lParam;

		if ( !(pParam instanceof SubPolicySortParameter) )
			return false;
		lParam = (SubPolicySortParameter)pParam;

		if ( lParam.field == SubPolicySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == SubPolicySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == SubPolicySortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("[:Subscriber:Name]");
		}

		if ( lParam.field == SubPolicySortParameter.SortableField.CLIENT_NUMBER )
		{
			pstrBuffer.append("[:Subscriber:Number]");
		}

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		Policy lobjPolicy;
		Company lobjCompany;
		SubPolicyStub lobjResult;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				lobjPolicy = (Policy)lobjProcess.GetParent().GetData();
				try
				{
					lobjCompany = lobjPolicy.GetCompany();
				}
				catch (Throwable e)
				{
					lobjCompany = null;
				}
			}
			catch (Throwable e)
			{
				lobjPolicy = null;
				lobjCompany = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjPolicy = null;
			lobjCompany = null;
		}

		lobjResult = new SubPolicyStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.mainPolicyId = (lobjPolicy == null ? null : lobjPolicy.getKey().toString());
		lobjResult.mainPolicyNumber = (lobjPolicy == null ? "(Erro)" : lobjPolicy.getLabel());
		lobjResult.clientId = ((UUID)parrValues[2]).toString();
		lobjResult.clientNumber = parrValues[3].toString();
		lobjResult.clientName = (String)parrValues[4];
		lobjResult.inheritCategoryName = (lobjPolicy == null ? "(Erro)" : lobjPolicy.GetSubLine().getLine().getCategory().getLabel());
		lobjResult.inheritLineName = (lobjPolicy == null ? "(Erro)" : lobjPolicy.GetSubLine().getLine().getLabel());
		lobjResult.inheritSubLineName = (lobjPolicy == null ? "(Erro)" : lobjPolicy.GetSubLine().getLabel());
		lobjResult.inheritCompanyName = (lobjCompany == null ? "(Erro)" : lobjCompany.getLabel());
		lobjResult.statusId = ((UUID)parrValues[5]).toString();
		lobjResult.statusText = (String)parrValues[6];
		switch ( (Integer)parrValues[7] )
		{
		case 0:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
			break;

		case 1:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
			break;

		case 2:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
			break;
		}
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		SubPolicySearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof SubPolicySearchParameter) )
				continue;
			lParam = (SubPolicySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("CASE WHEN [:Subscriber:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', [:Subscriber:Name]) ELSE ")
					.append("CASE WHEN [:Subscriber:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', [:Subscriber:Number]) ELSE ")
					.append("0 END END END");
		}

		return lbFound;
	}
}
