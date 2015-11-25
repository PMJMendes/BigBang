package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

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
	public BigDecimal mdblBonusMalus;
	public Boolean mbIsMalus;
	public Timestamp mdtIssue;
	public Timestamp mdtMaturity;
	public Timestamp mdtEnd;
	public Timestamp mdtDue;
	public UUID midMediator;
	public String mstrNotes;
	public String mstrDescription;
	public Boolean mbInternal;
	public Integer mlngEntryNumber;
	public Integer mlngEntryYear;
	public UUID midStatus;
	public UUID midPolicy;
	public UUID midSubPolicy;
	public UUID midSubCasualty;

	public UUID midManager;
	public UUID midProcess;

	public ReceiptData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber =            (String)pobjSource.getAt(Receipt.I.NUMBER);
		midType =                 (UUID)pobjSource.getAt(Receipt.I.TYPE);
		midProcess =              (UUID)pobjSource.getAt(Receipt.I.PROCESS);
		mdblTotal =         (BigDecimal)pobjSource.getAt(Receipt.I.TOTALPREMIUM);
		mdblCommercial =    (BigDecimal)pobjSource.getAt(Receipt.I.COMMERCIALPREMIUM);
		mdblCommissions =   (BigDecimal)pobjSource.getAt(Receipt.I.COMMISSIONS);
		mdblRetrocessions = (BigDecimal)pobjSource.getAt(Receipt.I.RETROCESSIONS);
		mdblFAT =           (BigDecimal)pobjSource.getAt(Receipt.I.FAT);
		mdtIssue =           (Timestamp)pobjSource.getAt(Receipt.I.ISSUEDATE);
		mdtMaturity =        (Timestamp)pobjSource.getAt(Receipt.I.MATURITYDATE);
		mdtEnd =             (Timestamp)pobjSource.getAt(Receipt.I.ENDDATE);
		mdtDue =             (Timestamp)pobjSource.getAt(Receipt.I.DUEDATE);
		midMediator =             (UUID)pobjSource.getAt(Receipt.I.MEDIATOR);
		mstrNotes =             (String)pobjSource.getAt(Receipt.I.NOTES);
		mstrDescription =       (String)pobjSource.getAt(Receipt.I.DESCRIPTION);
		mdblBonusMalus =    (BigDecimal)pobjSource.getAt(Receipt.I.BONUSMALUS);
		mbIsMalus =            (Boolean)pobjSource.getAt(Receipt.I.ISMALUS);
		mbInternal =           (Boolean)pobjSource.getAt(Receipt.I.ISINTERNAL);
		mlngEntryNumber =      (Integer)pobjSource.getAt(Receipt.I.ENTRYNUMBER);
		mlngEntryYear =        (Integer)pobjSource.getAt(Receipt.I.ENTRYYEAR);
		midStatus =               (UUID)pobjSource.getAt(Receipt.I.STATUS);
		midPolicy =               (UUID)pobjSource.getAt(Receipt.I.POLICY);
		midSubPolicy =            (UUID)pobjSource.getAt(Receipt.I.SUBPOLICY);
		midSubCasualty =          (UUID)pobjSource.getAt(Receipt.I.SUBCASUALTY);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(Receipt.I.NUMBER,            mstrNumber);
			pobjDest.setAt(Receipt.I.TYPE,              midType);
			pobjDest.setAt(Receipt.I.PROCESS,           midProcess);
			pobjDest.setAt(Receipt.I.TOTALPREMIUM,      mdblTotal);
			pobjDest.setAt(Receipt.I.COMMERCIALPREMIUM, mdblCommercial);
			pobjDest.setAt(Receipt.I.COMMISSIONS,       mdblCommissions);
			pobjDest.setAt(Receipt.I.RETROCESSIONS,     mdblRetrocessions);
			pobjDest.setAt(Receipt.I.FAT,               mdblFAT);
			pobjDest.setAt(Receipt.I.ISSUEDATE,         mdtIssue);
			pobjDest.setAt(Receipt.I.MATURITYDATE,      mdtMaturity);
			pobjDest.setAt(Receipt.I.ENDDATE,           mdtEnd);
			pobjDest.setAt(Receipt.I.DUEDATE,           mdtDue);
			pobjDest.setAt(Receipt.I.MEDIATOR,          midMediator);
			pobjDest.setAt(Receipt.I.NOTES,             mstrNotes);
			pobjDest.setAt(Receipt.I.DESCRIPTION,       mstrDescription);
			pobjDest.setAt(Receipt.I.BONUSMALUS,        mdblBonusMalus);
			pobjDest.setAt(Receipt.I.ISMALUS,           mbIsMalus);
			pobjDest.setAt(Receipt.I.ISINTERNAL,        mbInternal);
			pobjDest.setAt(Receipt.I.ENTRYNUMBER,       mlngEntryNumber);
			pobjDest.setAt(Receipt.I.ENTRYYEAR,         mlngEntryYear);
			pobjDest.setAt(Receipt.I.STATUS,            midStatus);
			pobjDest.setAt(Receipt.I.POLICY,            midPolicy);
			pobjDest.setAt(Receipt.I.SUBPOLICY,         midSubPolicy);
			pobjDest.setAt(Receipt.I.SUBCASUALTY,       midSubCasualty);
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

		if ( midStatus != null )
		{
			pstrBuilder.append("Estado: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptStatus), midStatus);
				pstrBuilder.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o estado do recibo.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Prémio Total: ");
		pstrBuilder.append(mdblTotal == null ? "(não indicado)" : mdblTotal.toPlainString());
		pstrBuilder.append(" (Prémio Simples/Com: ");
		pstrBuilder.append(mdblCommercial == null ? "não indicado" : mdblCommercial.toPlainString());
		pstrBuilder.append(")");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Comissão: ");
		pstrBuilder.append(mdblCommissions == null ? "(não indicada)" : mdblCommissions.toPlainString());
		pstrBuilder.append(" (Retrocessão: ");
		pstrBuilder.append(mdblRetrocessions == null ? "não indicada" : mdblRetrocessions.toPlainString());
		pstrBuilder.append(")");
		pstrBuilder.append(pstrLineBreak);

		if ( mdblFAT != null )
		{
			pstrBuilder.append("FAT: ");
			pstrBuilder.append(mdblFAT.toPlainString());
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mdblBonusMalus != null )
		{
			pstrBuilder.append(((mbIsMalus != null) && (mbIsMalus)) ? "Malus: " : "Bonus: ");
			pstrBuilder.append(mdblBonusMalus.toPlainString());
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Data de Emissão: ");
		pstrBuilder.append(mdtIssue == null ? "(não indicada)" : mdtIssue.toString().substring(0, 10));
		pstrBuilder.append(" (Data Limite Pagamento: ");
		pstrBuilder.append(mdtDue == null ? "não indicada" : mdtDue.toString().substring(0, 10));
		pstrBuilder.append(")");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data de Vigência: ");
		pstrBuilder.append(mdtMaturity == null ? "(não indicada)" : mdtMaturity.toString().substring(0, 10));
		pstrBuilder.append(" (Até: ");
		pstrBuilder.append(mdtEnd == null ? "não indicada" : mdtEnd.toString().substring(0, 10));
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
