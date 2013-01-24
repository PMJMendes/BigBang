package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.TotalLoss;

public class TotalLossData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrReference;
	public UUID midSubCasualty;
	public BigDecimal mdblCapital;
	public BigDecimal mdblDeductible;
	public BigDecimal mdblSettlement;
	public BigDecimal mdblsalvageValue;
	public UUID midSalvageType;
	public String mstrSalvageBuyer;

	public UUID midManager;
	public UUID midProcess;

	public TotalLossData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrReference    = (String)    pobjSource.getAt(TotalLoss.I.REFERENCE);
		midSubCasualty   = (UUID)      pobjSource.getAt(TotalLoss.I.SUBCASUALTY);
		midProcess       = (UUID)      pobjSource.getAt(TotalLoss.I.PROCESS);
		mdblCapital      = (BigDecimal)pobjSource.getAt(TotalLoss.I.CAPITAL);
		mdblDeductible   = (BigDecimal)pobjSource.getAt(TotalLoss.I.DEDUCTIBLE);
		mdblSettlement   = (BigDecimal)pobjSource.getAt(TotalLoss.I.SETTLEMENT);
		mdblsalvageValue = (BigDecimal)pobjSource.getAt(TotalLoss.I.SALVAGEVALUE);
		midSalvageType   = (UUID)      pobjSource.getAt(TotalLoss.I.SALVAGETYPE);
		mstrSalvageBuyer = (String)    pobjSource.getAt(TotalLoss.I.SALVAGEBUYER);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(TotalLoss.I.REFERENCE,    mstrReference);
			pobjDest.setAt(TotalLoss.I.SUBCASUALTY,  midSubCasualty);
			pobjDest.setAt(TotalLoss.I.PROCESS,      midProcess);
			pobjDest.setAt(TotalLoss.I.CAPITAL,      mdblCapital);
			pobjDest.setAt(TotalLoss.I.DEDUCTIBLE,   mdblDeductible);
			pobjDest.setAt(TotalLoss.I.SETTLEMENT,   mdblSettlement);
			pobjDest.setAt(TotalLoss.I.SALVAGEVALUE, mdblsalvageValue);
			pobjDest.setAt(TotalLoss.I.SALVAGETYPE,  midSalvageType);
			pobjDest.setAt(TotalLoss.I.SALVAGEBUYER, mstrSalvageBuyer);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;

		pstrBuilder.append("Referência: ");
		pstrBuilder.append(mstrReference);
		pstrBuilder.append(pstrLineBreak);

		if ( mdblCapital != null )
		{
			pstrBuilder.append("Capital seguro: ");
			pstrBuilder.append(mdblCapital.toPlainString());
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mdblDeductible != null )
		{
			pstrBuilder.append("Franquia: ");
			pstrBuilder.append(mdblDeductible.toPlainString());
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mdblSettlement != null )
		{
			pstrBuilder.append("Indemnização proposta: ");
			pstrBuilder.append(mdblSettlement.toPlainString());
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mdblsalvageValue != null )
		{
			pstrBuilder.append("Valor de salvado proposto: ");
			pstrBuilder.append(mdblCapital.toPlainString());
			pstrBuilder.append(pstrLineBreak);
		}

		if ( midSalvageType != null )
		{
			pstrBuilder.append("Salvado entregue a: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SalvageType),
						midSalvageType);
				pstrBuilder.append(lobjAux.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(erro a obter o destino do salvado)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrSalvageBuyer != null )
		{
			pstrBuilder.append("Comprador do salvado: ");
			pstrBuilder.append(pstrLineBreak);
			pstrBuilder.append(mstrSalvageBuyer);
			pstrBuilder.append(pstrLineBreak);
		}
	}
}
