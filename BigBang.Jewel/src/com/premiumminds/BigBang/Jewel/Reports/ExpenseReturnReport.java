package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Expense;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class ExpenseReturnReport
	extends ReportBase
{
	public UUID midClient;
	public UUID[] marrExpenseIDs;
	public BigDecimal mdblTotal;
	public int mlngCount;

	protected UUID GetTemplateID()
	{
		return Constants.TID_ExpenseReturn;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Client lobjClient;
		ObjectBase lobjZipCode;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Expense lobjExpense;
		IProcess lobjProc;
		Policy lobjPolicy;
		Company lobjCompany;
		Coverage lobjCoverage;
		String lstrObject;
		int i;

		lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
		if ( lobjClient.getAt(4) == null )
			lobjZipCode = null;
		else
		{
			try
			{
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjClient.getAt(4));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
		ldtAux = new Timestamp(new java.util.Date().getTime());

		larrParams = new HashMap<String, String>();
		larrParams.put("ClientName", (String)lobjClient.getAt(0));
		larrParams.put("ClientAddress1", (lobjClient.getAt(2) == null ? "" : (String)lobjClient.getAt(2)));
		larrParams.put("ClientAddress2", (lobjClient.getAt(3) == null ? "" : (String)lobjClient.getAt(3)));
		larrParams.put("ClientZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("ClientZipLocal", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", ldtAux.toString().substring(0, 10));

		larrTables = new String[marrExpenseIDs.length][];
		mlngCount = 0;
		mdblTotal = new BigDecimal(0);
		for ( i = 0; i < larrTables.length; i++ )
		{
			lobjExpense = Expense.GetInstance(Engine.getCurrentNameSpace(), marrExpenseIDs[i]);
			try
			{
				lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjExpense.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProc.GetParent().GetScriptID()) )
				{
					lobjPolicy = (Policy)lobjProc.GetParent().GetData();
					lstrObject = ( lobjExpense.getAt(Expense.I.POLICYOBJECT) == null ? (String)lobjExpense.getAt(Expense.I.GENERICOBJECT) :
							PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(Expense.I.POLICYOBJECT)).getLabel() );
					lobjCoverage = ( lobjExpense.getAt(Expense.I.POLICYCOVERAGE) == null ? null :
							PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjExpense.getAt(Expense.I.POLICYCOVERAGE)).GetCoverage() );
				}
				else
				{
					lobjPolicy = (Policy)lobjProc.GetParent().GetParent().GetData();
					lstrObject = ( lobjExpense.getAt(Expense.I.SUBPOLICYOBJECT) == null ? (String)lobjExpense.getAt(Expense.I.GENERICOBJECT) :
							SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(Expense.I.SUBPOLICYOBJECT)).getLabel() );
					lobjCoverage = ( lobjExpense.getAt(Expense.I.SUBPOLICYCOVERAGE) == null ? null :
							SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjExpense.getAt(Expense.I.SUBPOLICYCOVERAGE)).GetCoverage() );
				}
				lobjCompany = lobjPolicy.GetCompany();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			larrTables[i] = new String[7];
			larrTables[i][0] = lobjCompany.getLabel();
			larrTables[i][1] = lobjPolicy.getLabel();
			larrTables[i][2] = ( lstrObject == null ? "" : lstrObject );
			larrTables[i][3] = ( lobjCoverage == null ? "" : lobjCoverage.getLabel() );
			larrTables[i][4] = ((Timestamp)lobjExpense.getAt(Expense.I.DATE)).toString().substring(0, 10);
			larrTables[i][5] = String.format("%,.2f", ((BigDecimal)lobjExpense.getAt(Expense.I.DAMAGES)));
			larrTables[i][6] = ( lobjExpense.getAt(Expense.I.REJECTION) == null ? "" : (String)lobjExpense.getAt(Expense.I.REJECTION) );

			mlngCount++;
			mdblTotal = ( lobjExpense.getAt(Expense.I.SETTLEMENT) == null ? mdblTotal :
					mdblTotal.add((BigDecimal)lobjExpense.getAt(Expense.I.SETTLEMENT)) );
		}

		larrParams.put("Count", "" + mlngCount + " despesa" + (mlngCount == 1 ? "" : "s"));
		larrParams.put("Total", String.format("%,.2f", mdblTotal));

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
