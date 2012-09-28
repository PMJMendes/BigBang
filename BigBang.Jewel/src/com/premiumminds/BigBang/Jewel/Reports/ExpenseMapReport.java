package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Expense;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class ExpenseMapReport
	extends ReportBase
{
	public UUID midCompany;
	public UUID[] marrExpenseIDs;
	public BigDecimal mdblTotal;
	public int mlngCount;

	protected UUID GetTemplateID()
	{
		return Constants.TID_ExpenseMap;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Company lobjCompany;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Expense lobjExpense;
		IProcess lobjProc;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		String lstrObject;
		Coverage lobjCoverage;
		int i;

		lobjCompany = Company.GetInstance(Engine.getCurrentNameSpace(), midCompany);
		ldtAux = new Timestamp(new java.util.Date().getTime());

		larrParams = new HashMap<String, String>();
		larrParams.put("Insurer", lobjCompany.getLabel());
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
					lobjSubPolicy = null;
					lstrObject = ( lobjExpense.getAt(Expense.I.POLICYOBJECT) == null ? (String)lobjExpense.getAt(Expense.I.GENERICOBJECT) :
							PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(Expense.I.POLICYOBJECT)).getLabel() );
					lobjCoverage = ( lobjExpense.getAt(Expense.I.POLICYCOVERAGE) == null ? null :
							PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjExpense.getAt(Expense.I.POLICYCOVERAGE)).GetCoverage() );
				}
				else
				{
					lobjPolicy = (Policy)lobjProc.GetParent().GetParent().GetData();
					lobjSubPolicy = (SubPolicy)lobjProc.GetParent().GetData();
					lstrObject = null;
					lstrObject = ( lobjExpense.getAt(Expense.I.SUBPOLICYOBJECT) == null ? (String)lobjExpense.getAt(Expense.I.GENERICOBJECT) :
							SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(Expense.I.SUBPOLICYOBJECT)).getLabel() );
					lobjCoverage = ( lobjExpense.getAt(Expense.I.POLICYCOVERAGE) == null ? null :
							SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjExpense.getAt(Expense.I.SUBPOLICYCOVERAGE)).GetCoverage() );
				}
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			larrTables[i] = new String[5];
			larrTables[i][0] = (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel());
			larrTables[i][1] = (lstrObject == null ? "" : lstrObject);
			larrTables[i][2] = (lobjCoverage == null ? "" : lobjCoverage.getLabel());
			larrTables[i][3] = ((Timestamp)lobjExpense.getAt(Expense.I.DATE)).toString().substring(0, 10);
			larrTables[i][4] = ((BigDecimal)lobjExpense.getAt(Expense.I.DAMAGES)).toPlainString();

			mlngCount++;
			mdblTotal = mdblTotal.add((BigDecimal)lobjExpense.getAt(Expense.I.DAMAGES));
		}

		larrParams.put("Count", "" + mlngCount + " despesa" + (mlngCount == 1 ? "" : "s"));
		larrParams.put("Total", mdblTotal.toPlainString());

		java.util.Arrays.sort(larrTables, new Comparator<String[]>()
		{
			class StringComparator
				implements Comparator<java.lang.String>
			{
				public int compare(String o1, String o2)
				{
					if ( o1 == null )
					{
						if ( o2 == null )
							return 0;
						return 1;
					}
					return o1.compareTo(o2);
				}
			}

			public int compare(String[] o1, String[] o2)
			{
				StringComparator c;
				int i, n;

				c = new StringComparator();

				n = 0;
				i = 0;
				while ( (n == 0) && (i < o1.length) )
				{
					n = c.compare(o1[i], o2[i]);
					i++;
				}

				return n;
			}
		});

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
