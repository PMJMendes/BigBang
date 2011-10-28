package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class ReceiptData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrNumber;
	public UUID midType;
	public BigDecimal mdblTotal;
	public BigDecimal mdblCommercial;
	public BigDecimal mdblCommissions;
	public BigDecimal mdblRetrocessions;
	public BigDecimal mdblFAT;
	public Timestamp mdtIssue;
	public Timestamp mdtMaturity;
	public Timestamp mdtEnd;
	public Timestamp mdtDue;
	public UUID midMediator;
	public String mstrNotes;
	public String mstrDescription;

	public UUID midManager;
	public UUID midProcess;
	
	public ReceiptData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber = (String)pobjSource.getAt(0);
		midType = (UUID)pobjSource.getAt(1);
		midProcess = (UUID)pobjSource.getAt(2);
		mdblTotal = (BigDecimal)pobjSource.getAt(3);
		mdblCommercial = (BigDecimal)pobjSource.getAt(4);
		mdblCommissions = (BigDecimal)pobjSource.getAt(5);
		mdblRetrocessions = (BigDecimal)pobjSource.getAt(6);
		mdblFAT = (BigDecimal)pobjSource.getAt(7);
		mdtIssue = (Timestamp)pobjSource.getAt(8);
		mdtMaturity = (Timestamp)pobjSource.getAt(9);
		mdtEnd = (Timestamp)pobjSource.getAt(10);
		mdtDue = (Timestamp)pobjSource.getAt(11);
		midMediator = (UUID)pobjSource.getAt(12);
		mstrNotes = (String)pobjSource.getAt(13);
		mstrDescription = (String)pobjSource.getAt(14);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt( 0, mstrNumber);
			pobjDest.setAt( 1, midType);
			pobjDest.setAt( 2, midProcess);
			pobjDest.setAt( 3, mdblTotal);
			pobjDest.setAt( 4, mdblCommercial);
			pobjDest.setAt( 5, mdblCommissions);
			pobjDest.setAt( 6, mdblRetrocessions);
			pobjDest.setAt( 7, mdblFAT);
			pobjDest.setAt( 8, mdtIssue);
			pobjDest.setAt( 9, mdtMaturity);
			pobjDest.setAt(10, mdtEnd);
			pobjDest.setAt(11, mdtDue);
			pobjDest.setAt(12, midMediator);
			pobjDest.setAt(13, mstrNotes);
			pobjDest.setAt(14, mstrDescription);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;

		pstrBuilder.append("Número: ");
		pstrBuilder.append(mstrNumber);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Tipo: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptType), midType);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o tipo de recibo.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Prémio Total: ");
		pstrBuilder.append(mdblTotal.toPlainString());
		pstrBuilder.append(" (Prémio Comercial: ");
		pstrBuilder.append(mdblCommercial.toPlainString());
		pstrBuilder.append(")");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Comissão: ");
		pstrBuilder.append(mdblCommissions.toPlainString());
		pstrBuilder.append(" (Retrocessão: ");
		pstrBuilder.append(mdblRetrocessions.toPlainString());
		pstrBuilder.append(")");
		pstrBuilder.append(pstrLineBreak);

		if ( mdblFAT != null )
		{
			pstrBuilder.append("FAT: ");
			pstrBuilder.append(mdblFAT.toPlainString());
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Data de Emissão: ");
		pstrBuilder.append(mdtIssue.toString().substring(0, 10));
		pstrBuilder.append(" (Data Limite Pagamento: ");
		pstrBuilder.append(mdtDue.toString().substring(0, 10));
		pstrBuilder.append(")");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data de Vencimento: ");
		pstrBuilder.append(mdtMaturity.toString().substring(0, 10));
		pstrBuilder.append(" (Vigência Até: ");
		pstrBuilder.append(mdtEnd.toString().substring(0, 10));
		pstrBuilder.append(")");
		pstrBuilder.append(pstrLineBreak);

		if ( midMediator != null )
		{
			pstrBuilder.append("Mediador: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Mediator),
						midMediator);
				pstrBuilder.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o mediador associado ao recibo.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrDescription != null )
		{
			pstrBuilder.append("Designação: ");
			pstrBuilder.append(mstrDescription);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrNotes != null )
		{
			pstrBuilder.append("Notas Internas: ");
			pstrBuilder.append(pstrLineBreak);
			pstrBuilder.append(mstrNotes);
			pstrBuilder.append(pstrLineBreak);
		}
	}
}
