package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class HealthDebitNoteReport
	extends ReportBase
{
	public UUID midReceipt;
	public String mstrNumber;
	public String mstrValue;

	protected UUID GetTemplateID()
	{
		return Constants.TID_HealthDebitNote;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Receipt lobjReceipt;
		SubPolicy lobjSubPolicy;
		SubPolicyObject[] larrObjects;
		Policy lobjPolicy;
		Client lobjClient;
		ObjectBase lobjZipCode;
		Timestamp ldtAux;
		StringBuilder lstrDesc;
		int i;
		HashMap<String, String> larrParams;
		String[][] larrTables;

		lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
		lobjSubPolicy = lobjReceipt.getSubPolicy();
		lobjPolicy = lobjReceipt.getAbsolutePolicy();
		lobjClient = lobjSubPolicy.GetClient();
		larrObjects = lobjSubPolicy.GetCurrentObjects();
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

		lstrDesc = new StringBuilder((String)lobjReceipt.getAt(Receipt.I.NOTES));
		if ( larrObjects != null )
		{
			for ( i = 0; i < larrObjects.length; i++ )
			{
				if ( (larrObjects[i].getAt(SubPolicyObject.I.EXCLUSIONDATE) != null) &&
						(((Timestamp)larrObjects[i].getAt(SubPolicyObject.I.EXCLUSIONDATE)).
								before((Timestamp)lobjReceipt.getAt(Receipt.I.MATURITYDATE))) )
					continue;

				lstrDesc.append("\r").append(larrObjects[i].getLabel());
			}
		}

		mstrNumber = lobjReceipt.getLabel();
		mstrValue = String.format("%,.2f", (BigDecimal)lobjReceipt.getAt(Receipt.I.TOTALPREMIUM));

		larrParams = new HashMap<String, String>();
		larrParams.put("ClientName", (String)lobjClient.getAt(0));
		larrParams.put("ClientAddress1", (lobjClient.getAt(2) == null ? "" : (String)lobjClient.getAt(2)));
		larrParams.put("ClientAddress2", (lobjClient.getAt(3) == null ? "" : (String)lobjClient.getAt(3)));
		larrParams.put("ClientZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("ClientZipLocal", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", ldtAux.toString().substring(0, 10));
		larrParams.put("RecNumber", mstrNumber);
		larrParams.put("RecText", lstrDesc.toString());
		larrParams.put("RecValue", mstrValue);
		larrParams.put("LimitDate", lobjReceipt.getExternalDueDate().toString().substring(0, 10));

		larrTables = new String[1][];
		larrTables[0] = new String[6];
		larrTables[0][0] = lobjPolicy.getLabel();
		larrTables[0][1] = mstrNumber;
		larrTables[0][2] = (lobjReceipt.getAt(9) == null ? "" : ((Timestamp)lobjReceipt.getAt(9)).toString().substring(0, 10));
		larrTables[0][3] = (lobjReceipt.getAt(10) == null ? "" : ((Timestamp)lobjReceipt.getAt(10)).toString().substring(0, 10));
		larrTables[0][4] = mstrValue;
		larrTables[0][5] = lobjReceipt.getExternalDueDate();

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
