package bigBang.module.expenseModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.expenseModule.interfaces.ExpenseService;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Operations.Expense.DeleteExpense;
import com.premiumminds.BigBang.Jewel.Operations.Expense.ManageData;

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
		Expense lobjResult;

		try
		{
			lobjExpense = com.premiumminds.BigBang.Jewel.Objects.Expense.GetInstance(Engine.getCurrentNameSpace(), pidExpense);
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
			lobjParent = lobjProcess.GetParent().GetData();
			if ( Constants.ProcID_Policy.equals(lobjProcess.GetParent().GetScriptID()) )
			{
				lobjClient = (Client)lobjProcess.GetParent().GetParent().GetData();
				lobjInsured = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(3));
				lobjPolCov = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(5));
				lobjCoverage = ((PolicyCoverage)lobjPolCov).GetCoverage();
			}
			else
			{
				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjParent.getAt(2));
				lobjInsured = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(4));
				lobjPolCov = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(6));
				lobjCoverage = ((SubPolicyCoverage)lobjPolCov).GetCoverage();
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
		lobjResult.clientNumber = ( lobjClient == null ? null : (String)lobjClient.getAt(1) );
		lobjResult.clientName = ( lobjClient == null ? null : lobjClient.getLabel() );
		lobjResult.referenceId = ( lobjParent == null ? null : lobjParent.getKey().toString() );
		lobjResult.referenceTypeId = ( lobjParent == null ? null : lobjParent.getDefinition().getDefObject().getKey().toString() );
		lobjResult.referenceNumber = ( lobjParent == null ? null : lobjParent.getLabel() );
		lobjResult.expenseDate = ((Timestamp)lobjExpense.getAt(2)).toString().substring(0, 10);
		lobjResult.insuredObjectId = ( lobjInsured == null ? null : lobjInsured.getKey().toString() );
		lobjResult.insuredObjectName = ( lobjInsured == null ? null : lobjInsured.getLabel() );
		lobjResult.coverageId = ( lobjPolCov == null ? null : lobjPolCov.getKey().toString() );
		lobjResult.coverageName = ( lobjCoverage == null ? null : lobjCoverage.getLabel() );
		lobjResult.value = ((BigDecimal)lobjExpense.getAt(7)).toPlainString();
		lobjResult.isOpen =  lobjProcess.IsRunning();
		lobjResult.categoryName = lobjCoverage.GetSubLine().getLine().getCategory().getLabel();
		lobjResult.lineName = lobjCoverage.GetSubLine().getLine().getLabel();
		lobjResult.subLineName = lobjCoverage.GetSubLine().getLabel();
		lobjResult.managerId = lobjProcess.GetManagerID().toString();
		lobjResult.settlement = ((BigDecimal)lobjExpense.getAt(8)).toPlainString();
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
		lopMD.mobjData.mdblDamages = (expense.value == null ? null : new BigDecimal(expense.value));
		lopMD.mobjData.mdblSettlement = (expense.settlement == null ? null : new BigDecimal(expense.settlement));
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
