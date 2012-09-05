package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
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
import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.Permission;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
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
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoInsurer;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.Tax;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateDebitNote;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateInfoRequest;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateNegotiation;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Policy.DeletePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ExecMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Policy.PerformComputations;
import com.premiumminds.BigBang.Jewel.Operations.Policy.TransferToClient;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ValidatePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.VoidPolicy;

public class InsurancePolicyServiceImpl
	extends SearchServiceBase
	implements InsurancePolicyService
{
	private static final long serialVersionUID = 1L;
	
	private static class FieldStructure
	{
		public Coverage[] marrCoverages;
		public Tax[][] marrFields;
	}

	private static class FieldContents
	{
		public UUID midValue;
		public String mstrValue;
	}

	public static FieldContainer.FieldType sGetFieldTypeByID(UUID pidFieldType)
	{
		if ( Constants.FieldID_Boolean.equals(pidFieldType) )
			return FieldContainer.FieldType.BOOLEAN;
		if ( Constants.FieldID_Date.equals(pidFieldType) )
			return FieldContainer.FieldType.DATE;
		if ( Constants.FieldID_List.equals(pidFieldType) )
			return FieldContainer.FieldType.LIST;
		if ( Constants.FieldID_Number.equals(pidFieldType) )
			return FieldContainer.FieldType.NUMERIC;
		if ( Constants.FieldID_Reference.equals(pidFieldType) )
			return FieldContainer.FieldType.REFERENCE;
		if ( Constants.FieldID_Text.equals(pidFieldType) )
			return FieldContainer.FieldType.TEXT;
		return null;
	}

	public static InsurancePolicy sGetEmptyPolicy(UUID pidSubLine, UUID pidClient)
		throws BigBangException
	{
		SubLine lobjSubLine;
		FieldStructure lobjStructure;
		InsurancePolicy lobjResult;
		ComplexFieldContainer.ExerciseData lobjExercise;

		try
		{
			lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), pidSubLine);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjStructure = sGetSubLineStructure(lobjSubLine);

		lobjResult = sGetPolicyStructure(lobjStructure);
		sFillStaticEmptyPolicy(lobjResult, lobjSubLine, pidClient);

		if ( Constants.ExID_None.equals(lobjSubLine.getExerciseType()) )
		{
			lobjResult.hasExercises = false;
			lobjResult.exerciseData = null;
		}
		else
		{
			lobjResult.hasExercises = true;

			lobjExercise = sGetExerciseStructure(lobjStructure);
			lobjExercise.label = (Constants.ExID_Variable.equals(lobjSubLine.getExerciseType()) ?  "Período Inicial" : "(1º Ano)");

			lobjResult.exerciseData = new ComplexFieldContainer.ExerciseData[] {lobjExercise};
		}

		return lobjResult;
	}

	public static InsurancePolicy sGetPolicy(UUID pidPolicy)
		throws BigBangException
	{
		Policy lobjPolicy;
		SubLine lobjSubLine;
		Map<UUID, FieldContents> larrData;
		FieldStructure lobjStructure;
		InsurancePolicy lobjResult;
		PolicyExercise[] larrExercises;
		ComplexFieldContainer.ExerciseData lobjExercise;
		int i;

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), pidPolicy);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjSubLine = lobjPolicy.GetSubLine();

		lobjStructure = sGetSubLineStructure(lobjSubLine);

		lobjResult = sGetPolicyStructure(lobjStructure);
		sFillStaticPolicy(lobjResult, lobjPolicy);

		larrData = sExtractData(lobjPolicy, null, null);
		sFillContainer(lobjResult, larrData);

		if ( Constants.ExID_None.equals(lobjSubLine.getExerciseType()) )
		{
			lobjResult.hasExercises = false;
			lobjResult.exerciseData = null;
		}
		else
		{
			lobjResult.hasExercises = true;

			try
			{
				larrExercises = lobjPolicy.GetCurrentExercises();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			if ( (larrExercises == null) || (larrExercises.length == 0) )
			{
				lobjExercise = sGetExerciseStructure(lobjStructure);
				lobjExercise.label = (Constants.ExID_Variable.equals(lobjSubLine.getExerciseType()) ?  "Período Inicial" : "(Ano Corrente)");

				lobjResult.exerciseData = new ComplexFieldContainer.ExerciseData[] {lobjExercise};
			}
			else
			{
				lobjResult.exerciseData = new ComplexFieldContainer.ExerciseData[larrExercises.length];

				for ( i = 0; i < larrExercises.length; i++ )
				{
					lobjExercise = sGetExerciseStructure(lobjStructure);
					sFillStaticExercise(lobjExercise, larrExercises[i]);

					larrData = sExtractData(lobjPolicy, null, larrExercises[i].getKey());
					sFillContainer(lobjExercise, larrData);

					lobjResult.exerciseData[i] = lobjExercise;
				}
			}
		}


		return lobjResult;
	}

	public static InsuredObject sGetEmptyObject(UUID pidPolicy)
		throws BigBangException
	{
		Policy lobjPolicy;
		SubLine lobjSubLine;
		FieldStructure lobjStructure;
		InsuredObject lobjResult;
		PolicyExercise[] larrExercises;
		ComplexFieldContainer.ExerciseData lobjExercise;
		int i;

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), pidPolicy);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjSubLine = lobjPolicy.GetSubLine();

		lobjStructure = sGetSubLineStructure(lobjSubLine);

		lobjResult = sGetObjectStructure(lobjStructure);

		if ( Constants.ExID_None.equals(lobjSubLine.getExerciseType()) )
		{
			lobjResult.exerciseData = null;
		}
		else
		{
			try
			{
				larrExercises = lobjPolicy.GetCurrentExercises();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			if ( (larrExercises == null) || (larrExercises.length == 0) )
			{
				lobjExercise = sGetIntersectStructure(lobjStructure);
				lobjExercise.label = (Constants.ExID_Variable.equals(lobjSubLine.getExerciseType()) ?  "Período Inicial" : "(Ano Corrente)");

				lobjResult.exerciseData = new ComplexFieldContainer.ExerciseData[] {lobjExercise};
			}
			else
			{
				lobjResult.exerciseData = new ComplexFieldContainer.ExerciseData[larrExercises.length];

				for ( i = 0; i < larrExercises.length; i++ )
				{
					lobjExercise = sGetIntersectStructure(lobjStructure);
					sFillStaticExercise(lobjExercise, larrExercises[i]);

					lobjResult.exerciseData[i] = lobjExercise;
				}
			}
		}

		return lobjResult;
	}

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
		Client lobjClient;
		SubLine lobjSubLine;
		Line lobjLine;
		Category lobjCategory;
		ObjectBase lobjStatus;
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

    			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
    			lobjSubLine = lobjPolicy.GetSubLine();
    			lobjLine = lobjSubLine.getLine();
    			lobjCategory = lobjLine.getCategory();
    			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
    					(UUID)lobjPolicy.getAt(13));

            	lobjStub = new InsurancePolicyStub();

            	lobjStub.id = lobjPolicy.getKey().toString();
            	lobjStub.processId = lobjProc.getKey().toString();
            	lobjStub.number = (String)lobjPolicy.getAt(0);
            	lobjStub.clientId = lobjClient.getKey().toString();
            	lobjStub.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
            	lobjStub.clientName = lobjClient.getLabel();
            	lobjStub.categoryId = lobjCategory.getKey().toString();
            	lobjStub.categoryName = lobjCategory.getLabel();
            	lobjStub.lineId = lobjLine.getKey().toString();
            	lobjStub.lineName = lobjLine.getLabel();
            	lobjStub.subLineId = lobjSubLine.getKey().toString();
            	lobjStub.subLineName = lobjSubLine.getLabel();
        		lobjStub.insuredObject = lobjPolicy.GetObjectFootprint();
            	lobjStub.caseStudy = (Boolean)lobjPolicy.getAt(12);
            	lobjStub.statusId = lobjStatus.getKey().toString();
            	lobjStub.statusText = lobjStatus.getLabel();
        		switch ( (Integer)lobjStatus.getAt(1) )
        		{
        		case 0:
        			lobjStub.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
        			break;

        		case 1:
        			lobjStub.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
        			break;

        		case 2:
        			lobjStub.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
        			break;
        		}

    			lobjStub.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

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

	public InsurancePolicy getEmptyPolicy(String subLineId, String clientId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetEmptyPolicy(UUID.fromString(subLineId), UUID.fromString(clientId));
	}

	public InsuredObject getEmptyObject(String subLineId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public InsurancePolicy getPolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetPolicy(UUID.fromString(policyId));
	}

	public InsuredObject getPolicyObject(String objectId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public InsurancePolicy editPolicy(InsurancePolicy policy)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
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

		return sGetPolicy(lobjPolicy.getKey());
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
	public InsuredObjectOLD includeObject(String policyId, InsuredObjectOLD object)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObjectOLD includeObjectFromClient(String policyId)
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
	public Exercise openNewExercise(String policyId, Exercise exercise)
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
			lopCR.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium);
			lopCR.mobjData.mdblCommercial = (receipt.salesPremium == null ? null : new BigDecimal(receipt.salesPremium));
			lopCR.mobjData.mdblCommissions = (receipt.comissions == null ? new BigDecimal(0) : new BigDecimal(receipt.comissions));
			lopCR.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? null : new BigDecimal(receipt.retrocessions));
			lopCR.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue));
			lopCR.mobjData.mdblBonusMalus = (receipt.bonusMalus == null ? null : new BigDecimal(receipt.bonusMalus));
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
		lopCE.mobjData.mdblDamages = new BigDecimal(expense.value+"");
		lopCE.mobjData.mdblSettlement = (expense.settlement == null ? null : new BigDecimal(expense.settlement));
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
				"[:Status:Status]", "[:Status:Level], [:Client], [:Client:Number], [:Client:Name]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		InsurancePolicySearchParameter lParam;
		String lstrAux;
//		IEntity lrefClients;
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
//					.append(" OR [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
//				pstrBuffer.append(lrefClients.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//        		throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%'")
//					.append(" OR CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%'))");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Client] = '").append(lParam.ownerId).append("'");
//			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
//			try
//			{
//				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
//				pstrBuffer.append(lrefClients.SQLForSelectMulti());
//			}
//			catch (Throwable e)
//			{
//        		throw new BigBangException(e.getMessage(), e);
//			}
//			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
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
		lobjResult.categoryId = parrValues[2].toString();
		lobjResult.categoryName = (String)parrValues[3];
		lobjResult.lineId = parrValues[4].toString();
		lobjResult.lineName = (String)parrValues[5];
		lobjResult.subLineId = parrValues[6].toString();
		lobjResult.subLineName = (String)parrValues[7];
		lobjResult.insuredObject = lstrObject;
		lobjResult.caseStudy = (Boolean)parrValues[8];
		lobjResult.statusId = ((UUID)parrValues[9]).toString();
		lobjResult.statusText = (String)parrValues[10];
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
	
	private static FieldStructure sGetSubLineStructure(SubLine pobjSubLine)
		throws BigBangException
	{
		FieldStructure lobjResult;
		int i;

		lobjResult = new FieldStructure();

		try
		{
			lobjResult.marrCoverages = pobjSubLine.GetCurrentCoverages();
			sSortCoverages(lobjResult.marrCoverages);

			lobjResult.marrFields = new Tax[lobjResult.marrCoverages.length][];
			for ( i = 0; i < lobjResult.marrCoverages.length; i++ )
			{
				lobjResult.marrFields[i] = lobjResult.marrCoverages[i].GetCurrentTaxes();
				sSortFields(lobjResult.marrFields[i]);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}

	private static InsurancePolicy sGetPolicyStructure(FieldStructure pobjStructure)
		throws BigBangException
	{
		InsurancePolicy lobjResult;
		int llngMaxCol;
		int i, j, k, l;

		lobjResult = new InsurancePolicy();

		llngMaxCol = -1;
		for ( i = 0; i < pobjStructure.marrCoverages.length; i++ )
		{
			for ( j = 0; j < pobjStructure.marrFields[i].length; j++ )
			{
				if ( pobjStructure.marrFields[i][j].IsVisible() && (pobjStructure.marrFields[i][j].GetColumnOrder() > llngMaxCol) )
					llngMaxCol = pobjStructure.marrFields[i][j].GetColumnOrder();
			}
		}

		lobjResult.coverages = new InsurancePolicy.Coverage[pobjStructure.marrCoverages.length - 1];
		lobjResult.columns = new InsurancePolicy.ColumnHeader[llngMaxCol + 1];

		l = 0;
		for ( i = 0; i < pobjStructure.marrCoverages.length; i++ )
		{
			if ( !pobjStructure.marrCoverages[i].IsHeader() )
			{
				lobjResult.coverages[l] = new InsurancePolicy.Coverage();
				lobjResult.coverages[l].coverageId = pobjStructure.marrCoverages[i].getKey().toString();
				lobjResult.coverages[l].coverageName = pobjStructure.marrCoverages[i].getLabel();
				lobjResult.coverages[l].mandatory = pobjStructure.marrCoverages[i].IsMandatory();
				lobjResult.coverages[l].order = pobjStructure.marrCoverages[i].GetOrder();
				lobjResult.coverages[l].presentInPolicy = ( lobjResult.coverages[l].mandatory ? true : null );
				l++;

				for ( j = 0; j < pobjStructure.marrFields[i].length; j++ )
				{
					if ( !pobjStructure.marrFields[i][j].IsVisible() )
						continue;

					k = pobjStructure.marrFields[i][j].GetColumnOrder();
					if ( (k >= 0) && (lobjResult.columns[k] == null) )
					{
						lobjResult.columns[k] = new InsurancePolicy.ColumnHeader();
						lobjResult.columns[k].label = pobjStructure.marrFields[i][j].getLabel();
						lobjResult.columns[k].type = sGetFieldTypeByID(pobjStructure.marrFields[i][j].GetFieldType());
						lobjResult.columns[k].unitsLabel = pobjStructure.marrFields[i][j].GetUnitsLabel();
						lobjResult.columns[k].refersToId = ( pobjStructure.marrFields[i][j].GetRefersToID() == null ? null :
								pobjStructure.marrFields[i][j].GetRefersToID().toString() );
					}
				}
			}
		}

		sBuildFieldContainer(lobjResult, pobjStructure, false, false);

		return lobjResult;
	}

	private static ComplexFieldContainer.ExerciseData sGetExerciseStructure(FieldStructure pobjStructure)
	{
		ComplexFieldContainer.ExerciseData lobjResult;
		
		lobjResult = new ComplexFieldContainer.ExerciseData();

		sBuildFieldContainer(lobjResult, pobjStructure, false, true);

		return lobjResult;
	}

	private static InsuredObject sGetObjectStructure(FieldStructure pobjStructure)
	{
		InsuredObject lobjResult;

		lobjResult = new InsuredObject();

		sBuildFieldContainer(lobjResult, pobjStructure, true, false);

		return lobjResult;
	}

	private static ComplexFieldContainer.ExerciseData sGetIntersectStructure(FieldStructure pobjStructure)
	{
		ComplexFieldContainer.ExerciseData lobjResult;

		lobjResult = new ComplexFieldContainer.ExerciseData();

		sBuildFieldContainer(lobjResult, pobjStructure, true, true);

		return lobjResult;
	}

	private static void sSortCoverages(Coverage[] parrCoverages)
	{
		Arrays.sort(parrCoverages, new Comparator<Coverage>()
		{
			public int compare(Coverage o1, Coverage o2)
			{
				if ( o1.IsMandatory() == o2.IsMandatory() )
				{
					if ( o1.GetOrder() == o2.GetOrder() )
						return o1.getKey().compareTo(o2.getKey());
					return o1.GetOrder() - o2.GetOrder();
				}
				if ( o1.IsMandatory() )
					return -1;
				return 1;
			}
		});
	}

	private static void sSortFields(Tax[] parrFields)
	{
		Arrays.sort(parrFields, new Comparator<Tax>()
		{
			public int compare(Tax o1, Tax o2)
			{
				if ( o1.IsVisible() == o2.IsVisible() )
				{
					if ( o1.GetColumnOrder() == o2.GetColumnOrder() )
					{
						if ( o1.GetFieldType().equals(o2.GetFieldType()) )
						{
							if ( (o1.GetRefersToID() == null && o1.GetRefersToID() == null) ||
									o1.GetRefersToID().equals(o1.GetRefersToID()) )
								return o1.getLabel().compareTo(o2.getLabel());
							if ( o1.GetRefersToID() == null )
								return -1;
							if ( o2.GetRefersToID() == null )
								return 1;
							return o1.GetRefersToID().compareTo(o2.GetRefersToID());
						}
						return o1.GetFieldType().compareTo(o2.GetFieldType());
					}
					if ( (o1.GetColumnOrder() < 0) || (o2.GetColumnOrder() < 0) )
						return o2.GetColumnOrder() - o1.GetColumnOrder();
					return o1.GetColumnOrder() - o2.GetColumnOrder();
				}
				if ( o1.IsVisible() )
					return -1;
				return 1;
			}
		});
	}

	private static void sBuildFieldContainer(FieldContainer pobjContainer, FieldStructure parrStructure, boolean pbObjVar, boolean pbExVar)
	{
		ArrayList<FieldContainer.HeaderField> larrHeaders;
		ArrayList<FieldContainer.ColumnField> larrColumns;
		ArrayList<FieldContainer.ExtraField> larrExtras;
		FieldContainer.HeaderField lobjHeader;
		FieldContainer.ColumnField lobjColumn;
		FieldContainer.ExtraField lobjExtra;
		int i, j;

		larrHeaders = new ArrayList<FieldContainer.HeaderField>();
		larrColumns = new ArrayList<FieldContainer.ColumnField>();
		larrExtras = new ArrayList<FieldContainer.ExtraField>();

		for ( i = 0; i < parrStructure.marrCoverages.length; i++ )
		{
			if ( parrStructure.marrCoverages[i].IsHeader() )
			{
				for ( j = 0; j < parrStructure.marrFields[i].length; j++ )
				{
					if ( !parrStructure.marrFields[i][j].IsVisible() ||
							(parrStructure.marrFields[i][j].GetVariesByObject() != pbObjVar) ||
							(parrStructure.marrFields[i][j].GetVariesByExercise() != pbExVar) )
						continue;

					lobjHeader = new FieldContainer.HeaderField();
					sBuildField(lobjHeader, parrStructure.marrFields[i][j]);
					larrHeaders.add(lobjHeader);
				}
			}
			else
			{
				for ( j = 0; j < parrStructure.marrFields[i].length; j++ )
				{
					if ( !parrStructure.marrFields[i][j].IsVisible() ||
							(parrStructure.marrFields[i][j].GetVariesByObject() != pbObjVar) ||
							(parrStructure.marrFields[i][j].GetVariesByExercise() != pbExVar) )
						continue;

					if ( parrStructure.marrFields[i][j].GetColumnOrder() < 0 )
					{
						lobjExtra = new FieldContainer.ExtraField();
						sBuildField(lobjExtra, parrStructure.marrFields[i][j]);
						lobjExtra.coverageIndex = i;
						larrExtras.add(lobjExtra);
					}
					else
					{
						lobjColumn = new FieldContainer.ColumnField();
						sBuildField(lobjColumn, parrStructure.marrFields[i][j]);
						lobjColumn.coverageIndex = i;
						lobjColumn.columnIndex = parrStructure.marrFields[i][j].GetColumnOrder();
						larrColumns.add(lobjColumn);
					}
				}
			}
		}

		pobjContainer.headerFields = larrHeaders.toArray(new FieldContainer.HeaderField[larrHeaders.size()]);
		pobjContainer.columnFields = larrColumns.toArray(new FieldContainer.ColumnField[larrColumns.size()]);
		pobjContainer.extraFields = larrExtras.toArray(new FieldContainer.ExtraField[larrExtras.size()]);
	}
	
	private static void sBuildField(FieldContainer.HeaderField pobjResult, Tax pobjSource)
	{
		pobjResult.fieldId = pobjSource.getKey().toString();
		pobjResult.fieldName = pobjSource.getLabel();
		pobjResult.type = sGetFieldTypeByID(pobjSource.GetFieldType());
		pobjResult.unitsLabel = pobjSource.GetUnitsLabel();
		pobjResult.refersToId = ( pobjSource.GetRefersToID() == null ? null : pobjSource.GetRefersToID().toString() );
		pobjResult.order = pobjSource.GetColumnOrder();

		pobjResult.value = pobjSource.GetDefaultValue();
	}
	
	private static void sFillStaticEmptyPolicy(InsurancePolicy pobjDest, SubLine pobjSubLine, UUID pidClient)
		throws BigBangException
	{
		Client lobjClient;
		Line lobjLine;
		Category lobjCategory;

		try
		{
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), pidClient);
			lobjLine = pobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		pobjDest.clientId = lobjClient.getKey().toString();
		pobjDest.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		pobjDest.clientName = lobjClient.getLabel();
		pobjDest.categoryId = lobjCategory.getKey().toString();
		pobjDest.categoryName = lobjCategory.getLabel();
		pobjDest.lineId = lobjLine.getKey().toString();
		pobjDest.lineName = lobjLine.getLabel();
		pobjDest.subLineId = pobjSubLine.getKey().toString();
		pobjDest.subLineName = pobjSubLine.getLabel();
	}
	
	private static void sFillStaticPolicy(InsurancePolicy pobjDest, Policy pobjSource)
		throws BigBangException
	{
		IProcess lobjProc;
		Permission[] larrPerms;
		Client lobjClient;
		SubLine lobjSubLine;
		Line lobjLine;
		Category lobjCategory;
		String lstrObject;
		ObjectBase lobjStatus;
		Mediator lobjMed;
		PolicyCoInsurer[] larrCoInsurers;
		int i;

		try
		{
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), pobjSource.GetProcessID());
			larrPerms = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
			lobjSubLine = pobjSource.GetSubLine();
			lobjLine = lobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();
			lstrObject = pobjSource.GetObjectFootprint();
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
					(UUID)pobjSource.getAt(13));
			lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjClient.getAt(8));
			larrCoInsurers = pobjSource.GetCurrentCoInsurers();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		pobjDest.id = pobjSource.getKey().toString();

		pobjDest.processId = lobjProc.getKey().toString();
		pobjDest.permissions = larrPerms;

		pobjDest.number = (String)pobjSource.getAt(0);
		pobjDest.clientId = lobjClient.getKey().toString();
		pobjDest.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		pobjDest.clientName = lobjClient.getLabel();
		pobjDest.categoryId = lobjCategory.getKey().toString();
		pobjDest.categoryName = lobjCategory.getLabel();
		pobjDest.lineId = lobjLine.getKey().toString();
		pobjDest.lineName = lobjLine.getLabel();
		pobjDest.subLineId = lobjSubLine.getKey().toString();
		pobjDest.subLineName = lobjSubLine.getLabel();
		pobjDest.insuredObject = lstrObject;
		pobjDest.caseStudy = (Boolean)pobjSource.getAt(12);
		pobjDest.statusId = lobjStatus.getKey().toString();
		pobjDest.statusText = lobjStatus.getLabel();
		switch ( (Integer)lobjStatus.getAt(1) )
		{
		case 0:
			pobjDest.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
			break;

		case 1:
			pobjDest.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
			break;

		case 2:
			pobjDest.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
			break;
		}

		pobjDest.managerId = lobjProc.GetManagerID().toString();
		pobjDest.insuranceAgencyId = ((UUID)pobjSource.getAt(2)).toString();
		pobjDest.startDate = (pobjSource.getAt(4) == null ? null :
				((Timestamp)pobjSource.getAt(4)).toString().substring(0, 10));
		pobjDest.durationId = ((UUID)pobjSource.getAt(5)).toString();
		pobjDest.fractioningId = ((UUID)pobjSource.getAt(6)).toString();
		pobjDest.maturityDay = (pobjSource.getAt(7) == null ? 0 : (Integer)pobjSource.getAt(7));
		pobjDest.maturityMonth = (pobjSource.getAt(8) == null ? 0 : (Integer)pobjSource.getAt(8));
		pobjDest.expirationDate = (pobjSource.getAt(9) == null ? null :
				((Timestamp)pobjSource.getAt(9)).toString().substring(0, 10));
		pobjDest.notes = (String)pobjSource.getAt(10);
		pobjDest.mediatorId = (pobjSource.getAt(11) == null ? null : ((UUID)pobjSource.getAt(11)).toString());
		pobjDest.inheritMediatorId = lobjMed.getKey().toString();
		pobjDest.inheritMediatorName = lobjMed.getLabel();
		pobjDest.premium = (pobjSource.getAt(14) == null ? null : ((BigDecimal)pobjSource.getAt(14)).doubleValue());
		pobjDest.operationalProfileId = (pobjSource.getAt(18) == null ? null : ((UUID)pobjSource.getAt(18)).toString());
		pobjDest.docushare = (String)pobjSource.getAt(15);

		if ( larrCoInsurers.length > 0 )
		{
			pobjDest.coInsurers = new InsurancePolicy.CoInsurer[larrCoInsurers.length];
			for ( i = 0; i < larrCoInsurers.length; i++ )
			{
				pobjDest.coInsurers[i] = new InsurancePolicy.CoInsurer();
				pobjDest.coInsurers[i].insuranceAgencyId = ((UUID)larrCoInsurers[i].getAt(1)).toString();
				pobjDest.coInsurers[i].percent = ((BigDecimal)larrCoInsurers[i].getAt(2)).doubleValue();
			}
		}
		else
			pobjDest.coInsurers = null;
	}
	
	private static void sFillStaticExercise(ComplexFieldContainer.ExerciseData pobjDest, PolicyExercise pobjSource)
		throws BigBangException
	{
		pobjDest.label = pobjSource.getLabel();
		pobjDest.startDate = ( pobjSource.getAt(2) == null ? null :
				((Timestamp)pobjSource.getAt(2)).toString().substring(0, 10) );
		pobjDest.endDate = ( pobjSource.getAt(3) == null ? null :
				((Timestamp)pobjSource.getAt(3)).toString().substring(0, 10) );
	}

	private static Map<UUID, FieldContents> sExtractData(Policy lobjPolicy, UUID pidObject, UUID pidExercise)
		throws BigBangException
	{
		PolicyValue[] larrValues;
		HashMap<UUID, FieldContents> larrResult;
		FieldContents lobjAux;
		int i;

		try
		{
			larrValues = lobjPolicy.GetCurrentKeyedValues(pidObject, pidExercise);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrResult = new HashMap<UUID, FieldContents>();

		for ( i = 0; i < larrValues.length; i++ )
		{
			lobjAux = new FieldContents();
			lobjAux.midValue = larrValues[i].getKey();
			lobjAux.mstrValue = larrValues[i].getLabel();
			larrResult.put(larrValues[i].GetTax().getKey(), lobjAux);
		}

		return larrResult;
	}

	private static void sFillContainer(FieldContainer pobjContainer, Map<UUID, FieldContents> parrData)
	{
		int i;

		for ( i = 0; i < pobjContainer.headerFields.length; i++ )
			sFillField(pobjContainer.headerFields[i], parrData);

		for ( i = 0; i < pobjContainer.columnFields.length; i++ )
			sFillField(pobjContainer.columnFields[i], parrData);

		for ( i = 0; i < pobjContainer.extraFields.length; i++ )
			sFillField(pobjContainer.extraFields[i], parrData);
	}

	private static void sFillField(FieldContainer.HeaderField pobjField, Map<UUID, FieldContents> parrData)
	{
		FieldContents lobjAux;

		lobjAux = parrData.get(UUID.fromString(pobjField.fieldId));

		if ( lobjAux != null )
		{
			pobjField.valueId = lobjAux.midValue.toString();
			pobjField.value = lobjAux.mstrValue;
		}
	}
}
