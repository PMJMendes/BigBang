package bigBang.module.expenseModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.module.expenseModule.interfaces.ExpenseService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;

public class ExpenseServiceImpl
	extends SearchServiceBase
	implements ExpenseService
{
	private static final long serialVersionUID = 1L;

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
		return false;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		return false;
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
				lobjInsured = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[3]);
			else
				lobjInsured = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[4]);
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
				lobjPolCov = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[5]);
				lobjCoverage = ((PolicyCoverage)lobjPolCov).GetCoverage();
			}
			else
			{
				lobjPolCov = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[6]);
				lobjCoverage = ((SubPolicyCoverage)lobjPolCov).GetCoverage();
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
		lobjResult.clientNumber = ( lobjClient == null ? null : (String)lobjClient.getAt(1) );
		lobjResult.clientName = ( lobjClient == null ? null : lobjClient.getLabel() );
		lobjResult.referenceId = ( lobjParent == null ? null : lobjParent.getKey().toString() );
		lobjResult.referenceTypeId = ( lobjParent == null ? null : lobjParent.getDefinition().getDefObject().getKey().toString() );
		lobjResult.referenceNumber = ( lobjParent == null ? null : lobjParent.getLabel() );
		lobjResult.expenseDate = ((Timestamp)parrValues[2]).toString().substring(0, 10);
		lobjResult.insuredObjectId = ( lobjInsured == null ? null : lobjInsured.getKey().toString() );
		lobjResult.insuredObjectName = ( lobjInsured == null ? null : lobjInsured.getLabel() );
		lobjResult.coverageId = ( lobjPolCov == null ? null : lobjPolCov.getKey().toString() );
		lobjResult.coverageName = ( lobjCoverage == null ? null : lobjCoverage.getLabel() );
		lobjResult.value = ((BigDecimal)parrValues[7]).toPlainString();
		lobjResult.isOpen =  lobjProcess.IsRunning();

		return lobjResult;
	}
}
