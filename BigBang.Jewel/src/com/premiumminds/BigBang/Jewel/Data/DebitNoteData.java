package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.sun.jmx.snmp.Timestamp;

public class DebitNoteData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrNumber;
	public UUID midProcess;
	public BigDecimal mdblValue;
	public Timestamp mdtMaturity;
	public Timestamp mdtIssue;
	public Timestamp mdtHandleDate;
	public UUID midReceipt;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber = (String)pobjSource.getAt(0);
		midProcess = (UUID)pobjSource.getAt(1);
		mdblValue = (BigDecimal)pobjSource.getAt(2);
		mdtMaturity = (Timestamp)pobjSource.getAt(3);
		mdtIssue = (Timestamp)pobjSource.getAt(4);
		mdtHandleDate = (Timestamp)pobjSource.getAt(5);
		midReceipt = (UUID)pobjSource.getAt(6);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrNumber);
			pobjDest.setAt(1, midProcess);
			pobjDest.setAt(2, mdblValue);
			pobjDest.setAt(3, mdtMaturity);
			pobjDest.setAt(4, mdtIssue);
			pobjDest.setAt(5, mdtHandleDate);
			pobjDest.setAt(6, midReceipt);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		pstrBuilder.append("Número: ").append(mstrNumber).append(pstrLineBreak);
		pstrBuilder.append("Valor: ").append(mdblValue).append(pstrLineBreak);
		pstrBuilder.append("Data de Vencimento: ").append(mdtMaturity).append(pstrLineBreak);
		pstrBuilder.append("Data de Emissão: ").append(mdtIssue).append(pstrLineBreak);
		if ( mdtHandleDate != null )
			pstrBuilder.append("Reconciliada em: ").append(mdtHandleDate).append(pstrLineBreak);
		if ( midReceipt != null )
		{
			pstrBuilder.append("Com o recibo n. ");
			try
			{
				pstrBuilder.append(Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt).getLabel());
			}
			catch (BigBangJewelException e)
			{
				pstrBuilder.append("(erro a obter o número do recibo)");
			}
			pstrBuilder.append(pstrLineBreak);
		}
	}
}
