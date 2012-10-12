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
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class ReceiptReturnLetterReport
	extends ReportBase
{
	public UUID midInsurer;
	public UUID[] marrReceiptIDs;
	public BigDecimal mdblTotal;
	public int mlngCount;

	protected UUID GetTemplateID()
	{
		return Constants.TID_ReceiptReturnLetter;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Company lobjCompany;
		ObjectBase lobjZipCode, lobjType;
		Contact lobjContact;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Receipt lobjReceipt;
		IProcess lobjProc;
		Policy lobjPolicy;
		Category lobjCat;
		boolean lbUseContact;
		String lstrText;
		int i;

		lobjCompany = Company.GetInstance(Engine.getCurrentNameSpace(), midInsurer);
		lobjContact = lobjCompany.GetContactByType(Constants.CtTypeID_ReceiptReturn);
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
		larrParams.put("ContactName", (lobjContact == null ? "" : lobjContact.getLabel()));
		larrParams.put("CompanyName", lobjCompany.getLabel());
		larrParams.put("ContactAddress1", (lbUseContact ? (lobjContact.getAt(3) == null ? "" : (String)lobjContact.getAt(3)) :
				(lobjCompany.getAt(6) == null ? "" : (String)lobjCompany.getAt(6))));
		larrParams.put("ContactAddress2", (lbUseContact ? (lobjContact.getAt(4) == null ? "" : (String)lobjContact.getAt(4)) :
				(lobjCompany.getAt(7) == null ? "" : (String)lobjCompany.getAt(7))));
		larrParams.put("ContactZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("ContactZipLocal", ((lobjZipCode == null) || (lobjZipCode.getAt(1) == null) ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", ldtAux.toString().substring(0, 10));

		larrTables = new String[marrReceiptIDs.length][];
		mlngCount = 0;
		mdblTotal = new BigDecimal(0);
		for ( i = 0; i < larrTables.length; i++ )
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), marrReceiptIDs[i]);
			try
			{
				lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProc.GetParent().GetScriptID()) )
					lobjPolicy = (Policy)lobjProc.GetParent().GetData();
				else
					lobjPolicy = (Policy)lobjProc.GetParent().GetParent().GetData();
				lobjCat = lobjPolicy.GetSubLine().getLine().getCategory();
				lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptType),
						(UUID)lobjReceipt.getAt(1));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
			if ( lobjReceipt.getAt(15) == null )
				lstrText = "";
			else
			{
				lstrText = (String)lobjReceipt.getAt(15);
				if ( lstrText.indexOf('|') > 0 )
					lstrText = lstrText.split("|")[1].trim();
			}

			larrTables[i] = new String[6];
			larrTables[i][0] = lobjReceipt.getLabel();
			larrTables[i][1] = lobjPolicy.getLabel();
			larrTables[i][2] = lobjCat.getLabel();
			larrTables[i][3] = String.format("%,.2f", ((BigDecimal)lobjReceipt.getAt(3)));
			larrTables[i][4] = (String)lobjType.getAt(1);
			larrTables[i][5] = (lobjReceipt.getAt(15) == null ? "Falta de Pagamento" : lstrText);

			mlngCount++;
			mdblTotal = mdblTotal.add((BigDecimal)lobjReceipt.getAt(3));
		}

		larrParams.put("Count", "" + mlngCount + " recibo" + (mlngCount == 1 ? "" : "s"));
		larrParams.put("Total", String.format("%,.2f", mdblTotal));

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
