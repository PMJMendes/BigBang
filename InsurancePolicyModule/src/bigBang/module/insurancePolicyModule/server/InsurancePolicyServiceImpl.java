package bigBang.module.insurancePolicyModule.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
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

	public void deletePolicy(String policyId, String processId)
		throws SessionExpiredException, BigBangException
	{
		DeletePolicy lobjDP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjDP = new DeletePolicy(UUID.fromString(processId));
		lobjDP.midPolicy = UUID.fromString(policyId);

		try
		{
			lobjDP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
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
				"[:SubLine:Line]", "[:SubLine:Line:Name]", "[:SubLine]", "[:SubLine:Name]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		return false;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
	{
		return false;
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
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		return lobjResult;
	}
}
