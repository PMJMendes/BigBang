package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;

public class SubPolicyCoverageData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midOwner;
	public UUID midCoverage;
	public Boolean mbPresent;

	public boolean mbNew;
	public boolean mbDeleted;

	public SubPolicyCoverageData mobjPrevValues;

	public void Clone(SubPolicyCoverageData pobjSource)
	{
		mid = pobjSource.mid;
		midOwner = pobjSource.midOwner;
		midCoverage = pobjSource.midCoverage;
		mbPresent = pobjSource.mbPresent;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midOwner = (UUID)pobjSource.getAt(0);
		midCoverage = (UUID)pobjSource.getAt(1);
		mbPresent = (Boolean)pobjSource.getAt(2);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, midOwner);
			pobjDest.setAt(1, midCoverage);
			pobjDest.setAt(2, mbPresent);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		Coverage lobjCoverage;

		pstrBuilder.append("Cobertura: ");
		try
		{
			lobjCoverage = Coverage.GetInstance(Engine.getCurrentNameSpace(), midCoverage);
			pstrBuilder.append(lobjCoverage.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o nome da cobertura.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Presente na apólice: ");
		if ( mbPresent == null )
			pstrBuilder.append("(Não especificado.)");
		else if ( mbPresent.booleanValue() )
			pstrBuilder.append("Sim");
		else
			pstrBuilder.append("Não");
		pstrBuilder.append(pstrLineBreak);
	}
}
