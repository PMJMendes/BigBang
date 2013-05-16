package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PolicyCoInsurerData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midPolicy;
	public UUID midCompany;
	public BigDecimal mdblPercent;

	public boolean mbNew;
	public boolean mbDeleted;

	public PolicyCoInsurerData mobjPrevValues;

	public void Clone(PolicyCoInsurerData pobjSource)
	{
		mid = pobjSource.mid;
		midPolicy = pobjSource.midPolicy;
		midCompany = pobjSource.midCompany;
		mdblPercent = pobjSource.mdblPercent;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midPolicy = (UUID)pobjSource.getAt(0);
		midCompany = (UUID)pobjSource.getAt(1);
		mdblPercent = (BigDecimal)pobjSource.getAt(2);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, midPolicy);
			pobjDest.setAt(1, midCompany);
			pobjDest.setAt(2, mdblPercent);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjCompany;

		try
		{
			lobjCompany = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Company),
					midCompany);
			pstrBuilder.append((String)lobjCompany.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter a seguradora.)");
		}
		pstrBuilder.append(": ").append(mdblPercent);
	}
}
