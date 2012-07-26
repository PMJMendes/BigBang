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
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingDetail;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;

public class MediatorAccountingReport
	extends ReportBase
{
	public UUID midMap;
	public int mlngCount;
	public BigDecimal mdblTotalPremiums;
	public BigDecimal mdblTotalComms;
	public BigDecimal mdblTotalRetros;

	protected UUID GetTemplateID()
	{
		return Constants.TID_MediatorAccounting;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		MediatorAccountingMap lobjMap;
		TransactionDetailBase[] larrDetails;
		Mediator lobjMediator;
		ObjectBase lobjZipCode;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Receipt lobjReceipt;
		MediatorAccountingDetail lobjDetail;
		IProcess lobjProc;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		Client lobjClient;
		int i;

		lobjMap = MediatorAccountingMap.GetInstance(Engine.getCurrentNameSpace(), midMap);
		larrDetails = lobjMap.getCurrentDetails();

		lobjMediator = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjMap.getAt(TransactionMapBase.I.OWNER));
		if ( lobjMediator.getAt(7) == null )
			lobjZipCode = null;
		else
		{
			try
			{
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjMediator.getAt(7));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
		ldtAux = new Timestamp(new java.util.Date().getTime());

		larrParams = new HashMap<String, String>();
		larrParams.put("MediatorName", (String)lobjMediator.getAt(0));
		larrParams.put("MediatorAddress1", (lobjMediator.getAt(2) == null ? "" : (String)lobjMediator.getAt(2)));
		larrParams.put("MediatorAddress2", (lobjMediator.getAt(3) == null ? "" : (String)lobjMediator.getAt(3)));
		larrParams.put("MediatorZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("MediatorZipLocal", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", ldtAux.toString().substring(0, 10));

		larrTables = new String[larrDetails.length][];
		mlngCount = 0;
		mdblTotalPremiums = new BigDecimal(0);
		mdblTotalComms = new BigDecimal(0);
		mdblTotalRetros = new BigDecimal(0);
		for ( i = 0; i < larrDetails.length; i++ )
		{
			lobjDetail = (MediatorAccountingDetail)larrDetails[i];
			lobjReceipt = larrDetails[i].getReceipt();
			try
			{
				lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProc.GetParent().GetScriptID()) )
				{
					lobjPolicy = (Policy)lobjProc.GetParent().GetData();
					lobjSubPolicy = null;
				}
				else
				{
					lobjPolicy = (Policy)lobjProc.GetParent().GetParent().GetData();
					lobjSubPolicy = (SubPolicy)lobjProc.GetParent().GetData();
				}
				lobjClient = lobjPolicy.GetClient();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			larrTables[i] = new String[8];
			larrTables[i][0] = lobjClient.getLabel();
			larrTables[i][1] = lobjReceipt.getLabel();
			larrTables[i][2] = lobjReceipt.getReceiptType();
			larrTables[i][3] = (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel());
			larrTables[i][4] = lobjPolicy.GetSubLine().getDescription();
			larrTables[i][5] = String.format("%,.2f", lobjDetail.getPremium());
			larrTables[i][6] = String.format("%,.2f", lobjDetail.getCommission());
			larrTables[i][7] = String.format("%,.2f", lobjDetail.getRetrocession());

			mlngCount++;
			mdblTotalPremiums = mdblTotalPremiums.add(lobjDetail.getPremium());
			mdblTotalComms = mdblTotalComms.add(lobjDetail.getCommission());
			mdblTotalRetros = mdblTotalRetros.add(lobjDetail.getRetrocession());
		}

		larrParams.put("Count", "" + mlngCount);
		larrParams.put("Total", String.format("%,.2f", mdblTotalRetros));

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
