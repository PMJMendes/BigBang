package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
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
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
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
		UUID lidClient;
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
			lidClient = lobjProc.GetParent().GetData().getKey();
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
		lobjResult.clientId = lidClient.toString();
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
		lobjResult.caseStudy = (Boolean)lobjPolicy.getAt(12);

		lobjResult.managerId = lobjProc.GetManagerID().toString();

		return lobjResult;
	}

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

			lopMPD.mobjData.mid = null;

			lopMPD.mobjData.mstrNumber = policy.number;
			lopMPD.mobjData.midCompany = UUID.fromString(policy.insuranceAgencyId);
			lopMPD.mobjData.midSubLine = UUID.fromString(policy.subLineId);
			lopMPD.mobjData.mdtBeginDate = ( policy.startDate == null ? null : Timestamp.valueOf(policy.startDate) );
			lopMPD.mobjData.midDuration = UUID.fromString(policy.durationId);
			lopMPD.mobjData.midFractioning = UUID.fromString(policy.fractioningId);
			lopMPD.mobjData.mlngMaturityDay = policy.maturityDay;
			lopMPD.mobjData.mlngMaturityMonth = policy.maturityMonth;
			lopMPD.mobjData.mdtEndDate = ( policy.expirationDate == null ? null : Timestamp.valueOf(policy.expirationDate) );
			lopMPD.mobjData.mstrNotes = policy.notes;
			lopMPD.mobjData.midMediator = ( policy.mediatorId == null ? null : UUID.fromString(policy.mediatorId) );

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
			lopCR.mobjData.mdblCommercial = (receipt.comercialPremium == null ? null : new BigDecimal(receipt.comercialPremium));
			lopCR.mobjData.mdblCommissions = (receipt.comissions == null ? new BigDecimal(0) : new BigDecimal(receipt.comissions));
			lopCR.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? new BigDecimal(0) :
					new BigDecimal(receipt.retrocessions));
			lopCR.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue));
			lopCR.mobjData.mdtIssue = Timestamp.valueOf(receipt.issueDate);
			lopCR.mobjData.mdtMaturity = (receipt.maturityDate == null ? null : Timestamp.valueOf(receipt.maturityDate));
			lopCR.mobjData.mdtEnd = (receipt.endDate == null ? null : Timestamp.valueOf(receipt.endDate));
			lopCR.mobjData.mdtDue = (receipt.dueDate == null ? null : Timestamp.valueOf(receipt.dueDate));
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
						receipt.contacts, Constants.ObjID_Client);
			}
			else
				lopCR.mobjContactOps = null;
			if ( (receipt.documents != null) && (receipt.documents.length > 0) )
			{
				lopCR.mobjDocOps = new DocOps();
				lopCR.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(lopCR.mobjDocOps,
						receipt.documents, Constants.ObjID_Client);
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

	@Override
	public InsurancePolicy voidPolicy(String policyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
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
