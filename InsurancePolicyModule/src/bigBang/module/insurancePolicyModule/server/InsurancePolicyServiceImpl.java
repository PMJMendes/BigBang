package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.client.shared.InsurancePolicySortParameter;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.AcceptXFer;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreatePolicyMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Policy.DeletePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ManagePolicyData;

public class InsurancePolicyServiceImpl
	extends SearchServiceBase
	implements InsurancePolicyService
{
	private static final long serialVersionUID = 1L;

	public InsurancePolicy getPolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		UUID lid;
		Policy lobjPolicy;
		InsurancePolicy lobjResult;
		IProcess lobjProc;
		Client lobjClient;
		Mediator lobjMed;
		ObjectBase lobjAux;
		UUID lidLine;
		UUID lidCategory;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lid = UUID.fromString(policyId);
		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), lid);
			if ( lobjPolicy.GetProcessID() == null )
				throw new BigBangException("Erro: Apólice sem processo de suporte. (Apólice n. "
						+ lobjPolicy.getAt(0).toString() + ")");
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjPolicy.GetProcessID());
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
			lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjClient.getAt(8));
			lobjAux = SubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjPolicy.getAt(3));
			lidLine = (UUID)lobjAux.getAt(1);
			lobjAux = Line.GetInstance(Engine.getCurrentNameSpace(), lidLine);
			lidCategory = (UUID)lobjAux.getAt(1);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new InsurancePolicy();

		lobjResult.id = lobjPolicy.getKey().toString();
		lobjResult.number = (String)lobjPolicy.getAt(0);
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.categoryId = lidCategory.toString();
		lobjResult.lineId = lidLine.toString();
		lobjResult.subLineId = ((UUID)lobjPolicy.getAt(3)).toString();
		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.insuranceAgencyId = ((UUID)lobjPolicy.getAt(2)).toString();
		lobjResult.startDate = (lobjPolicy.getAt(4) == null ? null : ((Timestamp)lobjPolicy.getAt(4)).toString());
		lobjResult.durationId = ((UUID)lobjPolicy.getAt(5)).toString();
		lobjResult.fractioningId = ((UUID)lobjPolicy.getAt(6)).toString();
		lobjResult.maturityDay = (lobjPolicy.getAt(7) == null ? 0 : (Integer)lobjPolicy.getAt(7));
		lobjResult.maturityMonth = (lobjPolicy.getAt(8) == null ? 0 : (Integer)lobjPolicy.getAt(8));
		lobjResult.expirationDate = (lobjPolicy.getAt(9) == null ? null : ((Timestamp)lobjPolicy.getAt(9)).toString());
		lobjResult.notes = (String)lobjPolicy.getAt(10);
		lobjResult.mediatorId = (lobjPolicy.getAt(11) == null ? null : ((UUID)lobjPolicy.getAt(11)).toString());
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.caseStudy = (Boolean)lobjPolicy.getAt(12);

		lobjResult.managerId = lobjProc.GetManagerID().toString();

		return lobjResult;
	}

	@SuppressWarnings("deprecation")
	public InsurancePolicy editPolicy(InsurancePolicy policy)
		throws SessionExpiredException, BigBangException
	{
		ManagePolicyData lopMPD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMPD = new ManagePolicyData(UUID.fromString(policy.processId));
			lopMPD.mobjData = new PolicyData();

			lopMPD.mobjData.mid = UUID.fromString(policy.id);

			lopMPD.mobjData.mstrNumber = policy.number;
			lopMPD.mobjData.midCompany = UUID.fromString(policy.insuranceAgencyId);
			lopMPD.mobjData.midSubLine = UUID.fromString(policy.subLineId);
			lopMPD.mobjData.mdtBeginDate = ( policy.startDate == null ? null : new Timestamp(Timestamp.parse(policy.startDate)) );
			lopMPD.mobjData.midDuration = UUID.fromString(policy.durationId);
			lopMPD.mobjData.midFractioning = UUID.fromString(policy.fractioningId);
			lopMPD.mobjData.mlngMaturityDay = policy.maturityDay;
			lopMPD.mobjData.mlngMaturityMonth = policy.maturityMonth;
			lopMPD.mobjData.mdtEndDate = ( policy.expirationDate == null ? null : Timestamp.valueOf(policy.expirationDate) );
			lopMPD.mobjData.mstrNotes = policy.notes;
			lopMPD.mobjData.midMediator = ( policy.mediatorId == null ? null : UUID.fromString(policy.mediatorId) );
			lopMPD.mobjData.mbCaseStudy = policy.caseStudy;

			lopMPD.mobjData.midManager = null;
			lopMPD.mobjData.midProcess = null;

			lopMPD.mobjData.mobjPrevValues = null;

			lopMPD.mobjContactOps = null;
			lopMPD.mobjDocOps = null;

			lopMPD.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		policy.managerId = lopMPD.mobjData.midManager.toString();
		return policy;
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

	@SuppressWarnings("deprecation")
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
			lopCR.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? new BigDecimal(0) :
					new BigDecimal(receipt.retrocessions));
			lopCR.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue));
			lopCR.mobjData.mdtIssue = new Timestamp(Timestamp.parse(receipt.issueDate));
			lopCR.mobjData.mdtMaturity = (receipt.maturityDate == null ? null : new Timestamp(Timestamp.parse(receipt.maturityDate)));
			lopCR.mobjData.mdtEnd = (receipt.endDate == null ? null : new Timestamp(Timestamp.parse(receipt.endDate)));
			lopCR.mobjData.mdtDue = (receipt.dueDate == null ? null : new Timestamp(Timestamp.parse(receipt.dueDate)));
			lopCR.mobjData.midMediator = (receipt.mediatorId == null ? null : UUID.fromString(receipt.mediatorId));
			lopCR.mobjData.mstrNotes = receipt.notes;
			lopCR.mobjData.mstrDescription = receipt.description;

			lopCR.mobjData.midManager = ( receipt.managerId == null ? null : UUID.fromString(receipt.managerId) );
			lopCR.mobjData.midProcess = null;

			lopCR.mobjData.mobjPrevValues = null;

			if ( (receipt.contacts != null) && (receipt.contacts.length > 0) )
			{
				lopCR.mobjContactOps = new ContactOps();
				lopCR.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(lopCR.mobjContactOps,
						receipt.contacts, Constants.ObjID_Policy);
			}
			else
				lopCR.mobjContactOps = null;
			if ( (receipt.documents != null) && (receipt.documents.length > 0) )
			{
				lopCR.mobjDocOps = new DocOps();
				lopCR.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(lopCR.mobjDocOps,
						receipt.documents, Constants.ObjID_Policy);
			}
			else
				lopCR.mobjDocOps = null;

			lopCR.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		receipt.id = lopCR.mobjData.mid.toString();
		receipt.processId = lopCR.mobjData.midProcess.toString();
		receipt.managerId = lopCR.mobjData.midManager.toString();
		receipt.mediatorId = lopCR.mobjData.midMediator.toString();
		if ( (receipt.contacts != null) && (receipt.contacts.length > 0) )
			ContactsServiceImpl.WalkContactTree(lopCR.mobjContactOps.marrCreate, receipt.contacts);
		if ( (receipt.documents != null) && (receipt.documents.length > 0) )
			DocumentServiceImpl.WalkDocTree(lopCR.mobjDocOps.marrCreate, receipt.documents);

		return receipt;
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		CreatePolicyMgrXFer lobjCMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjCMX = new CreatePolicyMgrXFer(UUID.fromString(transfer.managedProcessIds[0]));
		lobjCMX.midNewManager = UUID.fromString(transfer.newManagerId);
		lobjCMX.mbMassTransfer = false;

		try
		{
			lobjCMX.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		transfer.directTransfer = lobjCMX.mbDirectTransfer;
		if ( transfer.directTransfer )
		{
			transfer.id = null;
			transfer.processId = null;
			transfer.status = ManagerTransfer.Status.DIRECT;
		}
		else
		{
			transfer.id = lobjCMX.midTransferObject.toString();
			transfer.processId = lobjCMX.midCreatedSubproc.toString();
			transfer.status = ManagerTransfer.Status.PENDING;
		}

		return transfer;
	}

	@Override
	public InsurancePolicy voidPolicy(String policyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoOrDocumentRequest createInfoOrDocumentRequest(
			InfoOrDocumentRequest request) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		UUID lidManager;
		SQLServer ldb;
		MgrXFer lobjXFer;
		IProcess lobjProc;
		UUID [] larrProcessIDs;
		int i;
		IScript lobjScript;
		CreatePolicyMgrXFer lobjCMX;
		AcceptXFer lopAX;
		AgendaItem lobjItem;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	lidManager = UUID.fromString(transfer.newManagerId);
    	if ( lidManager == null )
    		throw new BigBangException("Erro: Novo gestor não indicado.");

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

		lobjXFer = null;
		lobjProc = null;
		larrProcessIDs = new UUID[transfer.managedProcessIds.length];
		for ( i = 0 ; i < larrProcessIDs.length; i++ )
			larrProcessIDs[i] = UUID.fromString(transfer.managedProcessIds[i]);

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjXFer.setAt(1, null);
			lobjXFer.setAt(2, lidManager);
			lobjXFer.setAt(3, "Transferência de Gestor de Apólice");
			lobjXFer.setAt(4, Engine.getCurrentUser());
			lobjXFer.setAt(5, true);
			lobjXFer.setAt(6, Constants.ObjID_Policy);
			lobjXFer.SaveToDb(ldb);
			lobjXFer.InitNew(larrProcessIDs, ldb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_MgrXFer);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjXFer.getKey(), null, ldb);

			for ( i = 0; i < transfer.managedProcessIds.length; i++ )
			{
				lobjCMX = new CreatePolicyMgrXFer(UUID.fromString(transfer.managedProcessIds[i]));
				lobjCMX.midNewManager = lidManager;
				lobjCMX.mbMassTransfer = true;
				lobjCMX.midTransferObject = lobjXFer.getKey();
				lobjCMX.midCreatedSubproc = lobjProc.getKey();
				lobjCMX.Execute(ldb);
			}

			if ( lidManager.equals(Engine.getCurrentUser()) )
			{
				lopAX = new AcceptXFer(lobjProc.getKey());
				lopAX.mbMassTransfer = true;
				lopAX.Execute(ldb);
			}
			else
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Apólice");
				lobjItem.setAt(1, Engine.getCurrentUser());
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Valid);
				lobjItem.SaveToDb(ldb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_CancelXFer}, ldb);

				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Apólice");
				lobjItem.setAt(1, lidManager);
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(ldb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()},
						new UUID[] {Constants.OPID_AcceptXFer, Constants.OPID_CancelXFer}, ldb);
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (SQLException e1) {}
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

		transfer.directTransfer = lidManager.equals(Engine.getCurrentUser());
		if ( transfer.directTransfer )
		{
			transfer.id = null;
			transfer.processId = null;
			transfer.status = ManagerTransfer.Status.DIRECT;
		}
		else
		{
			transfer.id = lobjXFer.getKey().toString();
			transfer.processId = lobjProc.getKey().toString();
			transfer.status = ManagerTransfer.Status.PENDING;
		}

		return transfer;
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Policy;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:SubLine:Line:Category]", "[:SubLine:Line:Category:Name]",
				"[:SubLine:Line]", "[:SubLine:Line:Name]", "[:SubLine]", "[:SubLine:Name], [:Case Study]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		InsurancePolicySearchParameter lParam;
		String lstrAux;
		IEntity lrefClients;

		if ( !(pParam instanceof InsurancePolicySearchParameter) )
			return false;
		lParam = (InsurancePolicySearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%'))");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
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

		if ( lParam.caseStudy != null )
		{
			pstrBuffer.append(" AND [:Case Study] = ").append(lParam.caseStudy ? "1" : "0");
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
		IProcess lobjProcess;
		InsurancePolicyStub lobjResult;
		Client lobjClient;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				lobjClient = (Client)lobjProcess.GetParent().GetData();
			}
			catch (Throwable e)
			{
				lobjClient = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjClient = null;
		}

		lobjResult = new InsurancePolicyStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.categoryId = parrValues[2].toString();
		lobjResult.categoryName = (String)parrValues[3];
		lobjResult.lineId = parrValues[4].toString();
		lobjResult.lineName = (String)parrValues[5];
		lobjResult.subLineId = parrValues[6].toString();
		lobjResult.subLineName = (String)parrValues[7];
		lobjResult.caseStudy = (Boolean)parrValues[8];
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		InsurancePolicySearchParameter lParam;
		String lstrAux;
		IEntity lrefClients;
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
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', CAST((SELECT [:Number] FROM (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent]) AS NVARCHAR(20))) ELSE ")
					.append("CASE WHEN [:SubLine:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Name]) ELSE ")
					.append("CASE WHEN [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Line:Name]) ELSE ")
					.append("CASE WHEN [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Line:Category:Name]) ELSE ")
					.append("0 END END END END END END");
		}

		return lbFound;
	}
}
