package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.AccountingData;
import com.premiumminds.BigBang.Jewel.Data.PaymentData;
import com.premiumminds.BigBang.Jewel.Objects.AccountingEntry;
import com.premiumminds.BigBang.Jewel.Objects.CostCenter;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class Payment
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public PaymentData[] marrData;
	private UUID midReceipt;
	private UUID midPrevStatus;
	private AccountingData[] marrAccounting;

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

		if ( (marrAccounting != null) && (marrAccounting.length > 0) )
		{
			lstrBuilder.append(pstrLineBreak).append("Movimentos contabilísticos:").append(pstrLineBreak);
			for ( i = 0; i < marrAccounting.length; i++ )
			{
				marrAccounting[i].Describe(lstrBuilder, pstrLineBreak);
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
		Receipt lobjReceipt, lobjCounter;
		Payment lopRemote;
		BigDecimal ldblTotal;
		int i;
		Calendar ldtToday;
		boolean lbDirect;
		UUID lidProfile;
		UUID lidType;
		boolean lbDAS;
		AccountingEntry lobjEntry;

		if ( (marrData == null) || (marrData.length == 0) )
			throw new JewelPetriException("Erro: Deve especificar o(s) meio(s) de pagamento.");

		midReceipt = GetProcess().GetDataKey();

		try
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( (marrData.length == 1) && (marrData[0].mdblValue == null) )
		{
			if ( marrData[0].midReceipt != null )
				throw new JewelPetriException("Erro: Para compensar um recibo, é obrigatório especificar os valores.");

			marrData[0].mdblValue = (BigDecimal)lobjReceipt.getAt(3);
		}

		ldblTotal = BigDecimal.ZERO;
		lbDirect = true;
		for ( i = 0; i < marrData.length; i++ )
		{
			if ( (marrData[i].midReceipt != null) && marrData[i].mbCreateCounter && midReceipt.equals(marrData[i].midReceipt) )
					throw new JewelPetriException("Erro: Não pode compensar um recibo consigo próprio.");

			if ( !Constants.PayID_DirectToInsurer.equals(marrData[i].midPaymentType) )
				lbDirect = false;

			ldblTotal = ldblTotal.add(marrData[i].mdblValue);
		}

		if ( ldblTotal.subtract((BigDecimal)lobjReceipt.getAt(3)).abs().compareTo(new BigDecimal(0.01)) >= 0 )
		{
			ldblTotal = BigDecimal.ZERO;
			for ( i = 0; i < marrData.length; i++ )
			{
				marrData[i].mdblValue = marrData[i].mdblValue.negate();
				ldblTotal = ldblTotal.add(marrData[i].mdblValue);
			}
			if ( ldblTotal.subtract((BigDecimal)lobjReceipt.getAt(3)).abs().compareTo(new BigDecimal(0.01)) >= 0 )
				throw new JewelPetriException("Erro: Valor total dos pagamentos não está correcto.");
		}

		for ( i = 0; i < marrData.length; i++ )
		{
			if ( (marrData[i].midReceipt != null) && marrData[i].mbCreateCounter )
			{
				try
				{
					lobjCounter = Receipt.GetInstance(Engine.getCurrentNameSpace(), marrData[i].midReceipt);
				}
				catch (Throwable e)
				{
					throw new JewelPetriException(e.getMessage(), e);
				}

				lopRemote = new Payment(lobjCounter.GetProcessID());
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
			lidProfile = lobjReceipt.getProfile();
			lidType = (UUID)lobjReceipt.getAt(Receipt.I.TYPE);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lbDAS = false;
		ldtToday = Calendar.getInstance();
		ldtToday.set(Calendar.HOUR_OF_DAY, 0);
		ldtToday.set(Calendar.MINUTE, 0);
		ldtToday.set(Calendar.SECOND, 0);
		ldtToday.set(Calendar.MILLISECOND, 0);
		if ( !lobjReceipt.isReverseCircuit() && !lbDirect && !Constants.RecType_Backcharge.equals(lidType) &&
				!Constants.ProfID_VIPNoDAS.equals(lidProfile) && !Constants.ProfID_EmailNoDAS.equals(lidProfile))
		{
			if ( ldtToday.getTime().getTime() > ((Timestamp)lobjReceipt.getAt(11)).getTime() )
			{
				TriggerOp(new TriggerForceDAS(GetProcess().getKey()), pdb);
				lbDAS = true;
			}
		}

		try
		{
			if ( !lbDAS )
			{
				lobjReceipt.initAccounting(pdb, ldtToday.get(Calendar.YEAR));

				marrAccounting = getAcctData(ldtToday);
				if ( marrAccounting != null )
				{
					for ( i = 0; i < marrAccounting.length; i++ )
					{
						lobjEntry = AccountingEntry.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
						marrAccounting[i].ToObject(lobjEntry);
						lobjEntry.SaveToDb(pdb);
					}
				}
			}

			midPrevStatus = (UUID)lobjReceipt.getAt(Receipt.I.STATUS);
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Paid);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
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

		if ( (marrAccounting != null) && (marrAccounting.length > 0) )
			lstrBuilder.append(pstrLineBreak).append("Serão gerados movimentos contabilísticos de compensação.").append(pstrLineBreak);

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

		if ( (marrAccounting != null) && (marrAccounting.length > 0) )
		{
			lstrBuilder.append(pstrLineBreak).append("Movimentos contabilísticos compensados:").append(pstrLineBreak);
			for ( i = 0; i < marrAccounting.length; i++ )
			{
				marrAccounting[i].Describe(lstrBuilder, pstrLineBreak);
				lstrBuilder.append(pstrLineBreak);
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
		AccountingEntry lobjEntry;

		try
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lobjReceipt.clearPaymentLog();

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

		if ( marrAccounting != null )
		{
			try
			{
				for ( i = 0; i < marrAccounting.length; i++ )
				{
					if ( marrAccounting[i].mstrSign.equals("D") )
						marrAccounting[i].mstrSign = "C";
					else
						marrAccounting[i].mstrSign = "D";
					marrAccounting[i].mstrDesc = "Reversão de " + marrAccounting[i].mstrDesc;
					lobjEntry = AccountingEntry.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					marrAccounting[i].ToObject(lobjEntry);
					lobjEntry.SaveToDb(pdb);
				}
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		if ( midPrevStatus != null )
		{
			try
			{
				lobjReceipt.setAt(Receipt.I.STATUS, midPrevStatus);
				lobjReceipt.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
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

	public AccountingData[] getAcctData(Calendar pdtDate)
		throws BigBangJewelException
	{
		ArrayList<AccountingData> larrResult;
		AccountingData lobjAux;
		Receipt lobjReceipt;
		CostCenter lobjCenter;
		BigDecimal ldblTotal, ldblTotal119;
		BigDecimal ldblComms, ldblStamp;
		BigDecimal ldblRetro;
		int i;
		String lstrAccount, lstrAcct2, lstrMedEff, lstrMedSpd;
		String lstrDescAux, lstrDescAux2;
		int llngMainBook;

		lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
		try
		{
			lobjCenter = CostCenter.GetInstance(Engine.getCurrentNameSpace(),
					(UUID)UserDecoration.GetByUserID(Engine.getCurrentNameSpace(),
							PNProcess.GetInstance(Engine.getCurrentNameSpace(),
									lobjReceipt.getAbsolutePolicy().GetProcessID()).GetManagerID())
					.getAt(Constants.FKCostCenter_In_UserDecoration));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		lstrAccount = lobjReceipt.getAbsolutePolicy().GetCompany().getEffectiveAccount();
		if ( lstrAccount == null )
			return null;

		lstrAcct2 = lobjReceipt.getAbsolutePolicy().GetCompany().getEarningsAccount();
		lstrMedEff = lobjReceipt.getMediator().getEffectiveAccount();
		lstrMedSpd = lobjReceipt.getMediator().getSpendingsAccount();

		if ( Constants.RecType_Reversal.equals(lobjReceipt.getAt(Receipt.I.TYPE)) )
		{
			lstrDescAux2 = "Cobrança / Pagamento";
			lstrDescAux = " (Estorno)";
			llngMainBook = 1;
		}
		else if ( Constants.RecType_Casualty.equals(lobjReceipt.getAt(Receipt.I.TYPE)) )
		{
			lstrDescAux2 = "Pagamento de Sinistros";
			lstrDescAux = " (Estorno)";
			llngMainBook = 4;
		}
		else
		{
			lstrDescAux2 = "Cobrança / Recebimento";
			lstrDescAux = "";
			llngMainBook = 1;
		}

		try
		{
			ldblTotal = BigDecimal.ZERO;
			ldblTotal119 = BigDecimal.ZERO;
			for ( i = 0; i < marrData.length; i++ )
			{
				if ( Constants.PayID_DirectToInsurer.equals(marrData[i].midPaymentType) )
					continue;
				if ( Constants.PayID_DirectCheque.equals(marrData[i].midPaymentType) )
					ldblTotal119 = ldblTotal119.add(marrData[i].mdblValue);
				else
					ldblTotal = ldblTotal.add(marrData[i].mdblValue);
			}
			ldblComms = (BigDecimal)lobjReceipt.getAt(Receipt.I.COMMISSIONS);
			if ( (ldblComms != null) && (ldblComms.signum() != 0) )
			{
				if ( lobjReceipt.getAbsolutePolicy().GetSubLine().getIsLife() ||
						Constants.RecType_Backcharge.equals(lobjReceipt.getAt(Receipt.I.TYPE)) )
					ldblStamp = BigDecimal.ZERO;
				else
					ldblStamp = ldblComms.multiply((new BigDecimal(2.0/102.0))).setScale(2, RoundingMode.HALF_UP);
			}
			else
				ldblStamp = null;

			ldblRetro = (BigDecimal)lobjReceipt.getAt(Receipt.I.RETROCESSIONS);

			larrResult = new ArrayList<AccountingData>();

			if ( ldblTotal.signum() != 0 )
			{
				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal("1204");
				lobjAux.mdblValue = ldblTotal.abs();
				lobjAux.mstrSign = (ldblTotal.signum() > 0 ? "D" : "C");
				lobjAux.mlngBook = llngMainBook;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = lstrDescAux2;
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);
			}

			if ( ldblTotal119.signum() != 0 )
			{
				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal("119");
				lobjAux.mdblValue = ldblTotal119.abs();
				lobjAux.mstrSign = (ldblTotal119.signum() > 0 ? "D" : "C");
				lobjAux.mlngBook = llngMainBook;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = lstrDescAux2;
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);
			}

			ldblTotal = ldblTotal.add(ldblTotal119);

			if ( ldblTotal.signum() != 0 )
			{
				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal(lstrAccount);
				lobjAux.mdblValue = ldblTotal.abs();
				lobjAux.mstrSign = (ldblTotal.signum() > 0 ? "C" : "D");
				lobjAux.mlngBook = llngMainBook;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = lstrDescAux2;
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);
			}

			if ( (ldblComms != null) && (ldblComms.signum() != 0) )
			{
				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal(lstrAcct2);
				lobjAux.mdblValue = ldblComms.abs();
				lobjAux.mstrSign = (ldblComms.signum() > 0 ? "C" : "D");
				lobjAux.mlngBook = 2;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = "Afectação de Receitas" + lstrDescAux;
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);

				if ( ldblStamp.signum() != 0 )
				{
					lobjAux = new AccountingData();
					lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
					lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
					lobjAux.mdblAccount = new BigDecimal("68123011");
					lobjAux.mdblValue = ldblStamp.abs();
					lobjAux.mstrSign = (ldblStamp.signum() > 0 ? "D" : "C");
					lobjAux.mlngBook = 2;
					lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
					lobjAux.mstrDesc = "Afectação de Receitas" + lstrDescAux;
					lobjAux.midDocType = Constants.ObjID_Receipt;
					lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
					lobjAux.midFile = null;
					lobjAux.midCostCenter = lobjCenter.getKey();
					larrResult.add(lobjAux);

					ldblComms = ldblComms.subtract(ldblStamp);
				}

				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal(lstrAccount);
				lobjAux.mdblValue = ldblComms.abs();
				lobjAux.mstrSign = (ldblComms.signum() > 0 ? "D" : "C");
				lobjAux.mlngBook = 2;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = "Afectação de Receitas" + lstrDescAux;
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);

				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal("1207");
				lobjAux.mdblValue = ldblComms.abs();
				lobjAux.mstrSign = (ldblComms.signum() > 0 ? "D" : "C");
				lobjAux.mlngBook = 6;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = "Transferência";
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);

				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal("1204");
				lobjAux.mdblValue = ldblComms.abs();
				lobjAux.mstrSign = (ldblComms.signum() > 0 ? "C" : "D");
				lobjAux.mlngBook = 6;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = "Transferência";
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);
			}

			if ( (ldblRetro != null) && (ldblRetro.signum() != 0) && (lstrMedEff != null) )
			{
				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal(lstrMedSpd);
				lobjAux.mdblValue = ldblRetro.abs();
				lobjAux.mstrSign = (ldblRetro.signum() > 0 ? "D" : "C");
				lobjAux.mlngBook = 3;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = "Retrocessões" + lstrDescAux;
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);

				lobjAux = new AccountingData();
				lobjAux.mlngNumber = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYNUMBER);
				lobjAux.mdtDate = new Timestamp(pdtDate.getTimeInMillis());
				lobjAux.mdblAccount = new BigDecimal(lstrMedEff);
				lobjAux.mdblValue = ldblRetro.abs();
				lobjAux.mstrSign = (ldblRetro.signum() > 0 ? "C" : "D");
				lobjAux.mlngBook = 3;
				lobjAux.mstrSupportDoc = lobjReceipt.getLabel();
				lobjAux.mstrDesc = "Retrocessões" + lstrDescAux;
				lobjAux.midDocType = Constants.ObjID_Receipt;
				lobjAux.mlngYear = (Integer)lobjReceipt.getAt(Receipt.I.ENTRYYEAR);
				lobjAux.midFile = null;
				lobjAux.midCostCenter = lobjCenter.getKey();
				larrResult.add(lobjAux);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrResult.toArray(new AccountingData[larrResult.size()]);
	}
}
