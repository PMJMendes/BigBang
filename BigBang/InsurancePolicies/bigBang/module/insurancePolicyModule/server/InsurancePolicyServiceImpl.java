package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DebitNoteBatch;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.InfoOrDocumentRequestServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.server.TransferManagerServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.expenseModule.server.ExpenseServiceImpl;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;
import bigBang.module.quoteRequestModule.server.NegotiationServiceImpl;
import bigBang.module.receiptModule.server.ReceiptServiceImpl;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Data.DebitNoteData;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Data.NegotiationData;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateDebitNote;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateInfoRequest;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateNegotiation;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateSubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.DeletePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ExecMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Policy.PerformComputations;
import com.premiumminds.BigBang.Jewel.Operations.Policy.TransferToClient;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ValidatePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.VoidPolicy;

public class InsurancePolicyServiceImpl
	extends SearchServiceBase
	implements InsurancePolicyService
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
        Policy lobjPolicy;
		IProcess lobjProc;
		IStep lobjStep;
		InsurancePolicyStub lobjStub;

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
            	lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), lrsPolicies);
    			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjPolicy.GetProcessID());

    			if ( operationId != null )
    			{
    				lobjStep = lobjProc.GetOperation(UUID.fromString(operationId), ldb);
    				if ( (lobjStep == null) || (Jewel.Petri.Constants.LevelID_Invalid.equals(lobjStep.GetLevel())) )
    					continue;
    			}

            	lobjStub = new InsurancePolicyStub();
            	ServerToClient.buildPolicyStub(lobjStub, lobjPolicy);

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
		Policy lobjPolicy;
		PolicyCoverage[] larrCoverages;
		ArrayList<TipifiedListItem> larrResult;
		TipifiedListItem lobjAux;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( filterId == null )
			throw new BigBangException("Erro: Apólice não existente.");

		try
		{
			if ( Constants.ObjID_PolicyCoverage.equals(UUID.fromString(listId)) )
			{
				lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(filterId));
				larrCoverages = lobjPolicy.GetCurrentCoverages();
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

	public InsurancePolicy getEmptyPolicy(String subLineId, String clientId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new ServerToClient().getEmptyPolicy(UUID.fromString(subLineId), UUID.fromString(clientId));
	}

	public InsurancePolicy getPolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new ServerToClient().getPolicy(UUID.fromString(policyId));
	}

	public InsurancePolicy editPolicy(InsurancePolicy policy)
		throws SessionExpiredException, BigBangException, BigBangPolicyValidationException
	{
		Policy lobjPolicy;
		PolicyData lobjData;
		ManageData lopMPD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policy.id));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( !lobjPolicy.GetSubLine().getKey().equals(UUID.fromString(policy.subLineId)) )
			throw new BigBangException("Erro: Não pode alterar a modalidade da apólice.");

		lobjData = new ClientToServer().getPDataForEdit(lobjPolicy, policy);

		try
		{
			lopMPD = new ManageData(lobjPolicy.GetProcessID());
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

		return new ServerToClient().getPolicy(lobjPolicy.getKey());
	}

	public InsurancePolicy performCalculations(String policyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyCalculationException
	{
		Policy lobjPolicy;
		PerformComputations lopPC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopPC = new PerformComputations(lobjPolicy.GetProcessID());

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

		return new ServerToClient().getPolicy(lobjPolicy.getKey());
	}

	public void validatePolicy(String policyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyValidationException
	{
		Policy lobjPolicy;
		ValidatePolicy lopVP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVP = new ValidatePolicy(lobjPolicy.GetProcessID());

		try
		{
			lopVP.Execute();
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
	public InsuredObject includeObject(String policyId, InsuredObject object)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject includeObjectFromClient(String policyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excludeObject(String policyId, String objectId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InsurancePolicy openNewExercise(String policyId, Exercise exercise)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public InsurancePolicy transferToClient(String policyId, String newClientId)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		TransferToClient lopTTC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopTTC = new TransferToClient(lobjPolicy.GetProcessID());
		lopTTC.midNewClient = UUID.fromString(newClientId);

		try
		{
			lopTTC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getPolicy(policyId);
	}

	public void createDebitNote(String policyId, DebitNote note)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateDebitNote lopCDN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCDN = new CreateDebitNote(lobjPolicy.GetProcessID());
		lopCDN.mobjData = new DebitNoteData();
		lopCDN.mobjData.midProcess = lobjPolicy.GetProcessID();
		lopCDN.mobjData.mdblValue = new BigDecimal(note.value+"");
		lopCDN.mobjData.mdtMaturity = Timestamp.valueOf(note.maturityDate + " 00:00:00.0");

		try
		{
			lopCDN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateInfoRequest lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(request.parentDataObjectId));

			lopCIR = new CreateInfoRequest(lobjPolicy.GetProcessID());
			lopCIR.midRequestType = UUID.fromString(request.requestTypeId);
			lopCIR.mobjMessage = MessageBridge.outgoingToServer(request.message);
			lopCIR.mlngDays = request.replylimit;

			lopCIR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lopCIR.midRequestObject);
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		ExecMgrXFer lobjCMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transfer.dataObjectIds[0]));

			lobjCMX = new ExecMgrXFer(lobjPolicy.GetProcessID());
			lobjCMX.midNewManager = UUID.fromString(transfer.newManagerId);
			lobjCMX.midMassProcess = null;

			lobjCMX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return transfer;
	}

	public SubPolicy createSubPolicy(SubPolicy subPolicy)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		SubPolicyData lobjData;
		CreateSubPolicy lopCSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(subPolicy.mainPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjData = new ClientToServer().getSPDataForCreate(subPolicy);

		try
		{
			lopCSP = new CreateSubPolicy(lobjPolicy.GetProcessID());
			lopCSP.mobjData = lobjData;

			lopCSP.mobjContactOps = null;
			lopCSP.mobjDocOps = null;

			lopCSP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new ServerToClient().getSubPolicy(lopCSP.mobjData.mid);
	}

	public Receipt createReceipt(String policyId, Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateReceipt lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));

			lopCR = new CreateReceipt(lobjPolicy.GetProcessID());
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

			lopCR.mobjImage = null;

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
		Policy lobjPolicy;
		CreateExpense lopCE;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( !Constants.ObjID_Policy.equals(UUID.fromString(expense.referenceTypeId)) )
			throw new BigBangException("Erro: Tentativa de criar despesa de saúde de apólice adesão a partir de uma apólice.");

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(expense.referenceId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCE = new CreateExpense(lobjPolicy.GetProcessID());
		lopCE.mobjData = new ExpenseData();
		lopCE.mobjData.mid = null;
		lopCE.mobjData.mstrNumber = null;
		lopCE.mobjData.mdtDate = Timestamp.valueOf(expense.expenseDate + " 00:00:00.0");
		lopCE.mobjData.midPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
		lopCE.mobjData.midSubPolicyObject = null;
		lopCE.mobjData.midPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
		lopCE.mobjData.midSubPolicyCoverage = null;
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

	public Negotiation createNegotiation(Negotiation negotiation)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateNegotiation lopCN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(negotiation.ownerId));

			lopCN = new CreateNegotiation(lobjPolicy.GetProcessID());
			lopCN.mobjData = new NegotiationData();

			lopCN.mobjData.mid = null;
			lopCN.mobjData.mstrNotes = negotiation.notes;
			lopCN.mobjData.mdtLimitDate = (negotiation.limitDate == null ? null :
					Timestamp.valueOf(negotiation.limitDate + " 00:00:00.0"));

			lopCN.mobjData.midManager = null;
			lopCN.mobjData.midProcess = null;

			lopCN.mobjData.mobjPrevValues = null;

			if ( (negotiation.contacts != null) && (negotiation.contacts.length > 0) )
			{
				lopCN.mobjContactOps = new ContactOps();
				lopCN.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(negotiation.contacts);
			}
			else
				lopCN.mobjContactOps = null;
			if ( (negotiation.documents != null) && (negotiation.documents.length > 0) )
			{
				lopCN.mobjDocOps = new DocOps();
				lopCN.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(negotiation.documents);
			}
			else
				lopCN.mobjDocOps = null;

			lopCN.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return NegotiationServiceImpl.sGetNegotiation(lopCN.mobjData.mid);
	}

	public void createSubPolicyReceipts(DebitNoteBatch batch)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		Timestamp ldtFrom;
		Timestamp ldtTo;
		Timestamp ldtLimit;
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy[] larrSubs;
		MasterDB ldb;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateReceipt lopCR;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		ldtFrom = Timestamp.valueOf(batch.maturityDate + " 00:00:00.0");
		ldtTo = Timestamp.valueOf(batch.endDate + " 00:00:00.0");
		ldtLimit = Timestamp.valueOf(batch.limitDate + " 00:00:00.0");

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(batch.policyId));
			larrSubs = lobjPolicy.GetCurrentSubPoliciesForDebit(ldtFrom);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

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

		try
		{
			for ( i = 0; i < larrSubs.length; i++ )
			{
				lopCR = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateReceipt(larrSubs[i].GetProcessID());
				lopCR.mbForceDebitNote = true;
				lopCR.mobjData = new ReceiptData();
				lopCR.mobjData.mstrNumber = null;
				lopCR.mobjData.midType = Constants.RecType_Continuing;
//				lopCR.mobjData.mdblTotal;
//				lopCR.mobjData.mdblCommercial;
//				lopCR.mobjData.mdblCommissions;
				lopCR.mobjData.mdblRetrocessions = BigDecimal.ZERO;
				lopCR.mobjData.mdblFAT = BigDecimal.ZERO;
				lopCR.mobjData.mdblBonusMalus = null;
				lopCR.mobjData.mbIsMalus = null;
//				lopCR.mobjData.mdtIssue;
				lopCR.mobjData.mdtMaturity = ldtFrom;
				lopCR.mobjData.mdtEnd = ldtTo;
				lopCR.mobjData.mdtDue = ldtLimit;
				lopCR.mobjData.midMediator = larrSubs[i].GetOwner().getMediator().getKey();
//				lopCR.mobjData.mstrNotes;
//				lopCR.mobjData.mstrDescription;

				lopCR.Execute(ldb);
			}
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

	public InsurancePolicy voidPolicy(PolicyVoiding voiding)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		ObjectBase lobjMotive;
		VoidPolicy lopVP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(voiding.policyId));
			lobjMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_PolicyVoidingMotives), UUID.fromString(voiding.motiveId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVP = new VoidPolicy(lobjPolicy.GetProcessID());
		lopVP.mdtEffectDate = Timestamp.valueOf(voiding.effectDate + " 00:00:00.0");
		lopVP.mstrMotive = lobjMotive.getLabel();
		lopVP.mstrNotes = voiding.notes;

		try
		{
			lopVP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getPolicy(voiding.policyId);
	}

	public void deletePolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		DeletePolicy lobjDP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));

			lobjDP = new DeletePolicy(lobjPolicy.GetProcessID());
			lobjDP.midPolicy = UUID.fromString(policyId);
			lobjDP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		transfer.objectTypeId = Constants.ObjID_Policy.toString();

		return TransferManagerServiceImpl.sCreateMassTransfer(transfer, Constants.ProcID_Policy);
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Policy;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:SubLine:Line:Category]", "[:SubLine:Line:Category:Name]",
				"[:SubLine:Line]", "[:SubLine:Line:Name]", "[:SubLine]", "[:SubLine:Name]", "[:Case Study]", "[:Status]",
				"[:Status:Status]", "[:Status:Level]", "[:Client]", "[:Client:Number]", "[:Client:Name]", "[:Company]",
				"[:Company:Name]", "[:Company:Acronym]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		InsurancePolicySearchParameter lParam;
		String lstrAux;
		IEntity lrefObjects;

		if ( !(pParam instanceof InsurancePolicySearchParameter) )
			return false;
		lParam = (InsurancePolicySearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Client:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR CAST([:Client:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Client] = '").append(lParam.ownerId).append("'");
		}

		if ( lParam.subLineId != null )
		{
			pstrBuffer.append(" AND [:SubLine] = '").append(lParam.subLineId).append("'");
		}
		else if ( lParam.lineId != null )
		{
			pstrBuffer.append(" AND [:SubLine:Line] = '").append(lParam.lineId).append("'");
		}
		else if ( lParam.categoryId != null )
		{
			pstrBuffer.append(" AND [:SubLine:Line:Category] = '").append(lParam.categoryId).append("'");
		}

		if ( lParam.insuranceAgencyId != null )
		{
			pstrBuffer.append(" AND [:Company] = '").append(lParam.insuranceAgencyId).append("'");
		}

		if ( lParam.mediatorId != null )
		{
			pstrBuffer.append(" AND [:Mediator] = '").append(lParam.mediatorId).append("'");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.insuredObject != null )
		{
			lstrAux = lParam.insuredObject.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([PK] IN (SELECT [:Policy] FROM (");
			try
			{
				lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
				pstrBuffer.append(lrefObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxObj1] WHERE [:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR [:Process] IN (SELECT [:Sub Policy:Process:Parent] FROM (");
			try
			{
				lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyObject));
				pstrBuffer.append(lrefObjects.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxObj2] WHERE [:Name] LIKE N'%").append(lstrAux).append("%'))");
		}

		if ( (lParam.caseStudy != null) && lParam.caseStudy )
		{
			pstrBuffer.append(" AND [:Case Study] = 1");
		}

		if ( InsurancePolicySearchParameter.AllowedStates.LIVE.equals(lParam.allowedStates) )
		{
			pstrBuffer.append(" AND [:Status:Level] < 2");
		}

		if ( InsurancePolicySearchParameter.AllowedStates.NONLIVE.equals(lParam.allowedStates) )
		{
			pstrBuffer.append(" AND [:Status:Level] = 2");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		InsurancePolicySortParameter lParam;
		IEntity lrefClients;

		if ( !(pParam instanceof InsurancePolicySortParameter) )
			return false;
		lParam = (InsurancePolicySortParameter)pParam;

		if ( lParam.field == InsurancePolicySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == InsurancePolicySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == InsurancePolicySortParameter.SortableField.CATEGORY_LINE_SUBLINE )
		{
			pstrBuffer.append("[:SubLine:Name]");
			if ( lParam.order == SortOrder.ASC )
				pstrBuffer.append(" ASC");
			if ( lParam.order == SortOrder.DESC )
				pstrBuffer.append(" DESC");
			pstrBuffer.append(", [:SubLine:Line:Name]");
			if ( lParam.order == SortOrder.ASC )
				pstrBuffer.append(" ASC");
			if ( lParam.order == SortOrder.DESC )
				pstrBuffer.append(" DESC");
			pstrBuffer.append(", [:SubLine:Line:Category:Name]");
		}

		if ( lParam.field == InsurancePolicySortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("(SELECT [:Name] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == InsurancePolicySortParameter.SortableField.CLIENT_NUMBER )
		{
			pstrBuffer.append("(SELECT [:Number] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		String lstrObject;
		InsurancePolicyStub lobjResult;

		lstrObject = "(Erro a obter as unidades de risco)";
		try
		{
			lstrObject = Policy.GetInstance(Engine.getCurrentNameSpace(), pid).GetObjectFootprint();
		}
		catch (Throwable e)
		{
		}

		lobjResult = new InsurancePolicyStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.processId = ((UUID)parrValues[1]).toString();
		lobjResult.clientId = ((UUID)parrValues[12]).toString();
		lobjResult.clientNumber = ((Integer)parrValues[13]).toString();
		lobjResult.clientName = (String)parrValues[14];
		lobjResult.categoryId = ((UUID)parrValues[2]).toString();
		lobjResult.categoryName = (String)parrValues[3];
		lobjResult.lineId = ((UUID)parrValues[4]).toString();
		lobjResult.lineName = (String)parrValues[5];
		lobjResult.subLineId = ((UUID)parrValues[6]).toString();
		lobjResult.subLineName = (String)parrValues[7];
		lobjResult.insuredObject = lstrObject;
		lobjResult.caseStudy = (Boolean)parrValues[8];
		lobjResult.statusId = ((UUID)parrValues[9]).toString();
		lobjResult.statusText = (String)parrValues[10];
		lobjResult.insuranceAgencyId = ((UUID)parrValues[15]).toString();
		lobjResult.insuranceAgencyName = (String)parrValues[16];
		lobjResult.insuranceAgencyShort = (String)parrValues[17];
		switch ( (Integer)parrValues[11] )
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
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		InsurancePolicySearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof InsurancePolicySearchParameter) )
				continue;
			lParam = (InsurancePolicySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ")
					.append("WHEN [:Client:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', [:Client:Name]) ")
					.append("WHEN CAST([:Client:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', CAST([:Client:Number] AS NVARCHAR(20))) ")
					.append("WHEN [:SubLine:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Name]) ")
					.append("WHEN [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Line:Name]) ")
					.append("WHEN [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Line:Category:Name]) ")
					.append("ELSE 0 END");
		}

		return lbFound;
	}
}
