package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;

public class SubCasualtyItemData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midSubCasualty;
	public UUID midPolicyCoverage;
	public UUID midSubPolicyCoverage;
	public UUID midType;
	public BigDecimal mdblDamages;
	public BigDecimal mdblSettlement;
	public boolean mbIsManual;

	public boolean mbNew;
	public boolean mbDeleted;

	public SubCasualtyItemData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midSubCasualty       =       (UUID) pobjSource.getAt(SubCasualtyItem.I.SUBCASUALTY);
		midPolicyCoverage    =       (UUID) pobjSource.getAt(SubCasualtyItem.I.POLICYCOVERAGE);
		midSubPolicyCoverage =       (UUID) pobjSource.getAt(SubCasualtyItem.I.SUBOPOLICYCOVERAGE);
		midType              =       (UUID) pobjSource.getAt(SubCasualtyItem.I.TYPE);
		mdblDamages          = (BigDecimal) pobjSource.getAt(SubCasualtyItem.I.DAMAGES);
		mdblSettlement       = (BigDecimal) pobjSource.getAt(SubCasualtyItem.I.SETTLEMENT);
		mbIsManual           =    (Boolean) pobjSource.getAt(SubCasualtyItem.I.MANUAL);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(SubCasualtyItem.I.SUBCASUALTY,        midSubCasualty);
			pobjDest.setAt(SubCasualtyItem.I.POLICYCOVERAGE,     midPolicyCoverage);
			pobjDest.setAt(SubCasualtyItem.I.SUBOPOLICYCOVERAGE, midSubPolicyCoverage);
			pobjDest.setAt(SubCasualtyItem.I.TYPE,               midType);
			pobjDest.setAt(SubCasualtyItem.I.DAMAGES,            mdblDamages);
			pobjDest.setAt(SubCasualtyItem.I.SETTLEMENT,         mdblSettlement);
			pobjDest.setAt(SubCasualtyItem.I.MANUAL,             mbIsManual);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		PolicyCoverage lobjPCov;
		SubPolicyCoverage lobjSPCov;
		ObjectBase lobjType;

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

		pstrBuilder.append("Tipo de danos: ");
		if ( midType != null )
		{
			try
			{
				lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DamageType), midType);
				pstrBuilder.append(lobjType.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o tipo de danos.)");
			}
		}
		else
			pstrBuilder.append("Não indicado.");
		pstrBuilder.append(pstrLineBreak);

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
	}
}
