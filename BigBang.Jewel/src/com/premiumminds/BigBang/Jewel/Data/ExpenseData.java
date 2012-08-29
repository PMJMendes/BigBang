package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Expense;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;

public class ExpenseData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrNumber;
	public Timestamp mdtDate;
	public UUID midPolicyObject;
	public UUID midSubPolicyObject;
	public UUID midPolicyCoverage;
	public UUID midSubPolicyCoverage;
	public BigDecimal mdblDamages;
	public BigDecimal mdblSettlement;
	public boolean mbIsManual;
	public String mstrNotes;
	public String mstrRejection;
	public String mstrGenericObject;

	public UUID midManager;
	public UUID midProcess;

	public ExpenseData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber           =     (String) pobjSource.getAt(Expense.I.NUMBER);
		midProcess           =       (UUID) pobjSource.getAt(Expense.I.PROCESS);
		mdtDate              =  (Timestamp) pobjSource.getAt(Expense.I.DATE);
		midPolicyObject      =       (UUID) pobjSource.getAt(Expense.I.POLICYOBJECT);
		midSubPolicyObject   =       (UUID) pobjSource.getAt(Expense.I.SUBPOLICYOBJECT);
		midPolicyCoverage    =       (UUID) pobjSource.getAt(Expense.I.POLICYCOVERAGE);
		midSubPolicyCoverage =       (UUID) pobjSource.getAt(Expense.I.SUBPOLICYCOVERAGE);
		mdblDamages          = (BigDecimal) pobjSource.getAt(Expense.I.DAMAGES);
		mdblSettlement       = (BigDecimal) pobjSource.getAt(Expense.I.SETTLEMENT);
		mbIsManual           =    (Boolean) pobjSource.getAt(Expense.I.MANUAL);
		mstrNotes            =     (String) pobjSource.getAt(Expense.I.NOTES);
		mstrRejection        =     (String) pobjSource.getAt(Expense.I.REJECTION);
		mstrGenericObject    =     (String) pobjSource.getAt(Expense.I.GENERICOBJECT);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(Expense.I.NUMBER,            mstrNumber);
			pobjDest.setAt(Expense.I.PROCESS,           midProcess);
			pobjDest.setAt(Expense.I.DATE,              mdtDate);
			pobjDest.setAt(Expense.I.POLICYOBJECT,      midPolicyObject);
			pobjDest.setAt(Expense.I.SUBPOLICYOBJECT,   midSubPolicyObject);
			pobjDest.setAt(Expense.I.POLICYCOVERAGE,    midPolicyCoverage);
			pobjDest.setAt(Expense.I.SUBPOLICYCOVERAGE, midSubPolicyCoverage);
			pobjDest.setAt(Expense.I.DAMAGES,           mdblDamages);
			pobjDest.setAt(Expense.I.SETTLEMENT,        mdblSettlement);
			pobjDest.setAt(Expense.I.MANUAL,            mbIsManual);
			pobjDest.setAt(Expense.I.NOTES,             mstrNotes);
			pobjDest.setAt(Expense.I.REJECTION,         mstrRejection);
			pobjDest.setAt(Expense.I.GENERICOBJECT,     mstrGenericObject);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		PolicyObject lobjPObj;
		SubPolicyObject lobjSPObj;
		PolicyCoverage lobjPCov;
		SubPolicyCoverage lobjSPCov;

		pstrBuilder.append("Número do Processo: ");
		pstrBuilder.append(mstrNumber);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data do Sinistro: ");
		pstrBuilder.append(mdtDate.toString().substring(0, 10));

		if ( midPolicyObject != null )
		{
			pstrBuilder.append("Objecto seguro: ");
			try
			{
				lobjPObj = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), midPolicyObject);
				pstrBuilder.append(lobjPObj.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o nome do objecto seguro.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( midSubPolicyObject != null )
		{
			pstrBuilder.append("Objecto seguro: ");
			try
			{
				lobjSPObj = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), midSubPolicyObject);
				pstrBuilder.append(lobjSPObj.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o nome do objecto seguro.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrGenericObject != null )
		{
			pstrBuilder.append("Objecto seguro: ");
			pstrBuilder.append(mstrGenericObject);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( midPolicyCoverage != null )
		{
			pstrBuilder.append("Cobertura activada: ");
			try
			{
				lobjPCov = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), midPolicyCoverage);
				pstrBuilder.append(lobjPCov.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o nome da cobertura activada.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( midSubPolicyCoverage != null )
		{
			pstrBuilder.append("Cobertura activada: ");
			try
			{
				lobjSPCov = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), midSubPolicyCoverage);
				pstrBuilder.append(lobjSPCov.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o nome da cobertura activada.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Valor dos danos: ");
		if ( mdblDamages != null )
			pstrBuilder.append(mdblDamages);
		else
			pstrBuilder.append("Não indicado.");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Valor da indemnização: ");
		if ( mdblSettlement != null )
		{
			pstrBuilder.append(mdblSettlement);
			if ( !mbIsManual )
				pstrBuilder.append(" (calculado automaticamente)");
		}
		else
			pstrBuilder.append("Não indicado.");
		pstrBuilder.append(pstrLineBreak);

		if ( mstrRejection != null )
			pstrBuilder.append("Motivo da rejeição:").append(pstrLineBreak).append(mstrRejection).append(pstrLineBreak);

		pstrBuilder.append("Notas Internas: ");
		if ( mstrNotes != null )
			pstrBuilder.append(mstrNotes);
		pstrBuilder.append(pstrLineBreak);
	}
}
