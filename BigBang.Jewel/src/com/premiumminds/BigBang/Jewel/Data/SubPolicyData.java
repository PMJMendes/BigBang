package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;

public class SubPolicyData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;
	
	public String mstrNumber;
	public UUID midSubscriber;
	public Timestamp mdtBeginDate;
	public UUID midFractioning;
	public Timestamp mdtEndDate;
	public String mstrNotes;
	public UUID midStatus;
	public BigDecimal mdblPremium;
	public String mstrDocuShare;

	public UUID midManager;
	public UUID midProcess;

	public SubPolicyCoverageData[] marrCoverages;
	public SubPolicyObjectData[] marrObjects;
	public SubPolicyValueData[] marrValues;

	public boolean mbModified;

	public SubPolicyData mobjPrevValues;

	public void Clone(SubPolicyData pobjSource)
	{
		mid = pobjSource.mid;
		mstrNumber = pobjSource.mstrNumber;
		midSubscriber = pobjSource.midSubscriber;
		mdtBeginDate = pobjSource.mdtBeginDate;
		midFractioning = pobjSource.midFractioning;
		mdtEndDate = pobjSource.mdtEndDate;
		mstrNotes = pobjSource.mstrNotes;
		midManager = pobjSource.midManager;
		midProcess = pobjSource.midProcess;
		midStatus = pobjSource.midStatus;
		mdblPremium = pobjSource.mdblPremium;
		mstrDocuShare = pobjSource.mstrDocuShare;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber = (String)pobjSource.getAt(0);
		midProcess = (UUID)pobjSource.getAt(1);
		midSubscriber = (UUID)pobjSource.getAt(2);
		mdtBeginDate = (Timestamp)pobjSource.getAt(3);
		mdtEndDate = (Timestamp)pobjSource.getAt(4);
		midFractioning = (UUID)pobjSource.getAt(5);
		mstrNotes = (String)pobjSource.getAt(6);
		midStatus = (UUID)pobjSource.getAt(7);
		mdblPremium = (BigDecimal)pobjSource.getAt(8);
		mstrDocuShare = (String)pobjSource.getAt(9);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt( 0, mstrNumber);
			pobjDest.setAt( 1, midProcess);
			pobjDest.setAt( 2, midSubscriber);
			pobjDest.setAt( 3, mdtBeginDate);
			pobjDest.setAt( 4, mdtEndDate);
			pobjDest.setAt( 5, midFractioning);
			pobjDest.setAt( 6, mstrNotes);
			pobjDest.setAt( 7, midStatus);
			pobjDest.setAt( 8, mdblPremium);
//			pobjDest.setAt( 9, mstrDocuShare); JMMM: Nunca gravar por cima disto
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

		if ( midSubscriber != null )
		{
			pstrBuilder.append("Cliente Aderente: ");
			try
			{
				lobjAux = Client.GetInstance(Engine.getCurrentNameSpace(), midSubscriber);
				pstrBuilder.append((String)lobjAux.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o cliente aderente.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Data de Início: ");
		if ( mdtBeginDate != null )
			pstrBuilder.append(mdtBeginDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data de Fim: ");
		if ( mdtEndDate != null )
			pstrBuilder.append(mdtEndDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Fraccionamento: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Fractioning),
					midFractioning);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o fraccionamento.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Prémio Comercial: ");
		if ( mdblPremium != null )
			pstrBuilder.append(mdblPremium);
		else
			pstrBuilder.append("(não definido)");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Observações: ");
		if ( mstrNotes != null )
			pstrBuilder.append(mstrNotes);
		pstrBuilder.append(pstrLineBreak);
	}
}
