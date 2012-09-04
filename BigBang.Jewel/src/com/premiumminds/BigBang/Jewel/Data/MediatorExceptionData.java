package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.MediatorException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;

public class MediatorExceptionData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midMediator;
	public UUID midClient;
	public UUID midPolicy;
	public BigDecimal mdblPercent;

	public MediatorExceptionData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midMediator = (UUID)      pobjSource.getAt(MediatorException.I.MEDIATOR);
		midClient   = (UUID)      pobjSource.getAt(MediatorException.I.CLIENT);
		midPolicy   = (UUID)      pobjSource.getAt(MediatorException.I.POLICY);
		mdblPercent = (BigDecimal)pobjSource.getAt(MediatorException.I.PERCENTAGE);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MediatorException.I.MEDIATOR,   midMediator);
			pobjDest.setAt(MediatorException.I.CLIENT,     midClient);
			pobjDest.setAt(MediatorException.I.POLICY,     midPolicy);
			pobjDest.setAt(MediatorException.I.PERCENTAGE, mdblPercent);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		Client lobjClient;
		Policy lobjPolicy;

		if ( midClient != null )
		{
			pstrBuilder.append("Cliente: ");
			try
			{
				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
				pstrBuilder.append(lobjClient.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o cliente.)");
			}
		}

		if ( midPolicy != null )
		{
			pstrBuilder.append("Apólice: ");
			try
			{
				lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);
				pstrBuilder.append(lobjPolicy.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter a apólice.)");
			}
		}

		pstrBuilder.append(": ").append(String.format("%,.2f", mdblPercent)).append(pstrLineBreak);
	}
}
