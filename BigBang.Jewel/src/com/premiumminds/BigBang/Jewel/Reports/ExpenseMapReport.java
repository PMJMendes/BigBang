package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
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
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Expense;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class ExpenseMapReport
	extends ReportBase
{
	public UUID midPolicy;
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
		Policy lobjPolicy;
		Company lobjCompany;
		Contact lobjContact;
		boolean lbUseContact;
		ObjectBase lobjZipCode;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Expense lobjExpense;
		IProcess lobjProc;
		String lstrObject;
		Coverage lobjCoverage;
		int i;

		lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);
		lobjCompany = lobjPolicy.GetCompany();
		lobjContact = lobjCompany.GetContactByType(Constants.CtTypeID_ExpenseMaps);
		lbUseContact = (lobjContact != null) &&
				((lobjContact.getAt(3) != null) || (lobjContact.getAt(4) != null) || (lobjContact.getAt(5) != null));
		if ( (lbUseContact ? lobjContact.getAt(5) : lobjCompany.getAt(8)) == null )
			lobjZipCode = null;
		else
		{
			try
			{
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)(lbUseContact ? lobjContact.getAt(5) : lobjCompany.getAt(8)));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
		ldtAux = new Timestamp(new java.util.Date().getTime());

		larrParams = new HashMap<String, String>();
		larrParams.put("InsurerName", lobjCompany.getLabel());
		larrParams.put("InsurerContact", (lobjContact == null ? "" : lobjContact.getLabel()));
		larrParams.put("InsurerAddress1", (lbUseContact ? (lobjContact.getAt(3) == null ? "" : (String)lobjContact.getAt(3)) :
				(lobjCompany.getAt(6) == null ? "" : (String)lobjCompany.getAt(6))));
		larrParams.put("InsurerAddress2", (lbUseContact ? (lobjContact.getAt(4) == null ? "" : (String)lobjContact.getAt(4)) :
				(lobjCompany.getAt(7) == null ? "" : (String)lobjCompany.getAt(7))));
		larrParams.put("InsurerZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("InsurerZipLocal", ((lobjZipCode == null) || (lobjZipCode.getAt(1) == null) ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", ldtAux.toString().substring(0, 10));
		larrParams.put("Policy", lobjPolicy.getLabel());
		larrParams.put("ClientName", lobjPolicy.GetClient().getLabel());

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
					lstrObject = ( lobjExpense.getAt(Expense.I.POLICYOBJECT) == null ? (String)lobjExpense.getAt(Expense.I.GENERICOBJECT) :
							PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExpense.getAt(Expense.I.POLICYOBJECT)).getLabel() );
					lobjCoverage = ( lobjExpense.getAt(Expense.I.POLICYCOVERAGE) == null ? null :
							PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(),
							(UUID)lobjExpense.getAt(Expense.I.POLICYCOVERAGE)).GetCoverage() );
				}
				else
				{
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

			larrTables[i] = new String[4];
			larrTables[i][0] = (lstrObject == null ? "" : lstrObject);
			larrTables[i][1] = (lobjCoverage == null ? "" : lobjCoverage.getLabel());
			larrTables[i][2] = ((Timestamp)lobjExpense.getAt(Expense.I.DATE)).toString().substring(0, 10);
			larrTables[i][3] = String.format("%,.2f", ((BigDecimal)lobjExpense.getAt(Expense.I.DAMAGES)));

			mlngCount++;
			mdblTotal = mdblTotal.add((BigDecimal)lobjExpense.getAt(Expense.I.DAMAGES));
		}

		larrParams.put("Count", "" + mlngCount);
		larrParams.put("Total", String.format("%,.2f", mdblTotal));

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
