package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PaymentData;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class Payment
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public PaymentData[] marrData;
	private UUID midReceipt;

	public Payment(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_Payment;
	}

	public String ShortDesc()
	{
		return "Cobrança";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;
		int i;

		lstrBuilder = new StringBuilder("O recibo foi dado como pago.");

		if ( (marrData != null) && (marrData.length > 0) )
		{
			lstrBuilder.append(pstrLineBreak).append("Meios de pagamento:").append(pstrLineBreak);
			for ( i = 0; i < marrData.length; i++ )
			{
				marrData[i].Describe(lstrBuilder, pstrLineBreak);
				lstrBuilder.append(pstrLineBreak);
			}
		}

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;
		Payment lopRemote;
		BigDecimal ldblTotal;
		int i;
		Calendar ldtToday;

		midReceipt = GetProcess().GetDataKey();

		if ( (marrData == null) || (marrData.length == 0) )
			throw new JewelPetriException("Erro: Deve especificar o(s) meio(s) de pagamento.");

		ldblTotal = new BigDecimal(0);
		for ( i = 0; i < marrData.length; i++ )
		{
			ldblTotal = ldblTotal.add(marrData[i].mdblValue);
			if ( (marrData[i].midReceipt != null) && marrData[i].mbCreateCounter )
			{
				if ( midReceipt.equals(marrData[i].midReceipt) )
					throw new JewelPetriException("Erro: Não pode compensar um recibo consigo próprio.");

				try
				{
					lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), marrData[i].midReceipt);
				}
				catch (Throwable e)
				{
					throw new JewelPetriException(e.getMessage(), e);
				}

				lopRemote = new Payment(lobjReceipt.GetProcessID());
				lopRemote.marrData = new PaymentData[] {new PaymentData()};
				lopRemote.marrData[0].midPaymentType = marrData[i].midPaymentType;
				lopRemote.marrData[0].mdblValue = marrData[i].mdblValue;
				lopRemote.marrData[0].midBank = null;
				lopRemote.marrData[0].mstrCheque = null;
				lopRemote.marrData[0].midReceipt = midReceipt;
				lopRemote.marrData[0].mbCreateCounter = false;
				lopRemote.marrData[0].midLog = null;
				lopRemote.Execute(pdb);
				marrData[i].midLog = lopRemote.getLog().getKey();
			}
		}

		try
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( ldblTotal.subtract((BigDecimal)lobjReceipt.getAt(3)).abs().compareTo(new BigDecimal(0.01)) > 0 )
			throw new JewelPetriException("Erro: Valor total dos pagamentos não está correcto.");

		ldtToday = Calendar.getInstance();
		ldtToday.set(Calendar.HOUR_OF_DAY, 0);
		ldtToday.set(Calendar.MINUTE, 0);
		ldtToday.set(Calendar.SECOND, 0);
		ldtToday.set(Calendar.MILLISECOND, 0);
		if ( ldtToday.getTime().getTime() > ((Timestamp)lobjReceipt.getAt(11)).getTime() )
			TriggerOp(new TriggerForceDAS(GetProcess().getKey()), pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;
		int i;

		lstrBuilder = new StringBuilder("A cobrança do recibo será retirada.");

		if ( (marrData != null) && (marrData.length > 0) )
		{
			for ( i = 0; i < marrData.length; i++ )
			{
				if ( (marrData[i].midReceipt != null) && marrData[i].mbCreateCounter )
				{
					lstrBuilder.append(pstrLineBreak).append("A cobrança dos recibos compensados será também retirada.")
							.append(pstrLineBreak);
					break;
				}
			}
		}

		return lstrBuilder.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;
		int i;

		lstrBuilder = new StringBuilder("A cobrança do recibo foi retirada.");

		if ( (marrData != null) && (marrData.length > 0) )
		{
			for ( i = 0; i < marrData.length; i++ )
			{
				if ( (marrData[i].midReceipt != null) && marrData[i].mbCreateCounter )
				{
					lstrBuilder.append(pstrLineBreak).append("A cobrança dos recibos compensados foi também retirada.")
							.append(pstrLineBreak);
					break;
				}
			}
		}

		return lstrBuilder.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;
		UndoPayment lopRemote;
		int i;

		midReceipt = GetProcess().GetDataKey();

		if ( (marrData != null) && (marrData.length > 0) )
		{
			for ( i = 0; i < marrData.length; i++ )
			{
				if ( (marrData[i].midReceipt != null) && marrData[i].mbCreateCounter )
				{
					try
					{
						lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), marrData[i].midReceipt);
					}
					catch (Throwable e)
					{
						throw new JewelPetriException(e.getMessage(), e);
					}

					lopRemote = new UndoPayment(lobjReceipt.GetProcessID());
					lopRemote.midSourceLog = marrData[i].midLog;
					lopRemote.midNameSpace = Engine.getCurrentNameSpace();
					lopRemote.Execute(pdb);
				}
			}
		}
	}

	public UndoSet[] GetSets()
	{
		ArrayList<UUID> larrReceipts;
		UndoSet lobjSet;
		int i;

		larrReceipts = new ArrayList<UUID>();
		larrReceipts.add(midReceipt);

		if ( (marrData != null) && (marrData.length > 0) )
		{
			for ( i = 0; i < marrData.length; i++ )
			{
				if ( (marrData[i].midReceipt != null) && marrData[i].mbCreateCounter )
					larrReceipts.add(marrData[i].midReceipt);
			}
		}

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Receipt;
		lobjSet.marrChanged = larrReceipts.toArray(new UUID[larrReceipts.size()]);

		return new UndoSet[]{lobjSet};
	}
}
