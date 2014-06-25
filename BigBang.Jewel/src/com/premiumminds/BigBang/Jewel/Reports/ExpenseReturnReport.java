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
		String lstrName;
		String lstrAddr1;
		String lstrAddr2;
		ObjectBase lobjZipCode;
		Expense lobjExpense;
		UUID lidKey;
		PolicyObject lobjObject;
		SubPolicyObject lobjSObject;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		IProcess lobjProc;
		Policy lobjPolicy;
		Company lobjCompany;
		Coverage lobjCoverage;
		String lstrObject;
		int i;

		lstrName = null;
		lstrAddr1 = null;
		lstrAddr2 = null;
		lobjZipCode = null;

		lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
		lobjExpense = Expense.GetInstance(Engine.getCurrentNameSpace(), marrExpenseIDs[0]);
		lidKey = (UUID)lobjExpense.getAt(Expense.I.POLICYOBJECT);
		if ( lidKey != null )
		{
			lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), lidKey);
			lstrName = lobjObject.getLabel();
			lstrAddr1 = (String)lobjObject.getAt(PolicyObject.I.ADDRESS1);
			lstrAddr2 = (String)lobjObject.getAt(PolicyObject.I.ADDRESS2);
			if ( lobjObject.getAt(PolicyObject.I.ZIPCODE) != null )
			{
				try
				{
					lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
							(UUID)lobjObject.getAt(PolicyObject.I.ZIPCODE));
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
		}
		else
		{
			lidKey = (UUID)lobjExpense.getAt(Expense.I.SUBPOLICYOBJECT);
			if ( lidKey != null )
			{
				lobjSObject = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), lidKey);
				lstrName = lobjSObject.getLabel();
				lstrAddr1 = (String)lobjSObject.getAt(SubPolicyObject.I.ADDRESS1);
				lstrAddr2 = (String)lobjSObject.getAt(SubPolicyObject.I.ADDRESS2);
				if ( lobjSObject.getAt(SubPolicyObject.I.ZIPCODE) != null )
				{
					try
					{
						lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
								(UUID)lobjSObject.getAt(SubPolicyObject.I.ZIPCODE));
					}
					catch (Throwable e)
					{
						throw new BigBangJewelException(e.getMessage(), e);
					}
				}
			}
			else
				lstrName = lobjClient.getLabel();
		}

		if ( (lstrAddr1 == null) && (lstrAddr2 == null) && (lobjZipCode == null) )
		{
			lstrAddr1 = (String)lobjClient.getAt(2);
			lstrAddr2 = (String)lobjClient.getAt(3);
			if ( lobjClient.getAt(4) != null )
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
		}

		ldtAux = new Timestamp(new java.util.Date().getTime());

		larrParams = new HashMap<String, String>();
		larrParams.put("ClientName", lstrName);
		larrParams.put("ClientAddress1", (lstrAddr1 == null ? "" : lstrAddr1));
		larrParams.put("ClientAddress2", (lstrAddr2 == null ? "" : lstrAddr2));
		larrParams.put("ClientZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("ClientZipLocal", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", ldtAux.toString().substring(0, 10));

		larrTables = new String[marrExpenseIDs.length][];
		mlngCount = 0;
		mdblTotal = BigDecimal.ZERO;
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
			larrTables[i][6] = ( lobjExpense.getAt(Expense.I.REJECTION) == null ? "" :
					(String)lobjExpense.getAt(Expense.I.REJECTION) );

			mlngCount++;
			mdblTotal = ( lobjExpense.getAt(Expense.I.SETTLEMENT) == null ? mdblTotal :
					mdblTotal.add((BigDecimal)lobjExpense.getAt(Expense.I.DAMAGES)) );
		}

		larrParams.put("Count", "" + mlngCount + " despesa" + (mlngCount == 1 ? "" : "s"));
		larrParams.put("Total", String.format("%,.2f", mdblTotal));

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
