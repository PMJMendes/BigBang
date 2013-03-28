package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

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
	public Integer mlngMigrationID;
	public UUID midPolicy;

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

		mstrNumber =       (String)pobjSource.getAt(SubPolicy.I.NUMBER);
		midProcess =         (UUID)pobjSource.getAt(SubPolicy.I.PROCESS);
		midSubscriber =      (UUID)pobjSource.getAt(SubPolicy.I.SUBSCRIBER);
		mdtBeginDate =  (Timestamp)pobjSource.getAt(SubPolicy.I.BEGINDATE);
		mdtEndDate =    (Timestamp)pobjSource.getAt(SubPolicy.I.ENDDATE);
		midFractioning =     (UUID)pobjSource.getAt(SubPolicy.I.FRACTIONING);
		mstrNotes =        (String)pobjSource.getAt(SubPolicy.I.NOTES);
		midStatus =          (UUID)pobjSource.getAt(SubPolicy.I.STATUS);
		mdblPremium =  (BigDecimal)pobjSource.getAt(SubPolicy.I.PREMIUM);
		mstrDocuShare =    (String)pobjSource.getAt(SubPolicy.I.DOCUSHARE);
		mlngMigrationID = (Integer)pobjSource.getAt(SubPolicy.I.MIGRATIONID);
		midPolicy =          (UUID)pobjSource.getAt(SubPolicy.I.POLICY);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(SubPolicy.I.NUMBER, mstrNumber);
			pobjDest.setAt(SubPolicy.I.PROCESS, midProcess);
			pobjDest.setAt(SubPolicy.I.SUBSCRIBER, midSubscriber);
			pobjDest.setAt(SubPolicy.I.BEGINDATE, mdtBeginDate);
			pobjDest.setAt(SubPolicy.I.ENDDATE, mdtEndDate);
			pobjDest.setAt(SubPolicy.I.FRACTIONING, midFractioning);
			pobjDest.setAt(SubPolicy.I.NOTES, mstrNotes);
			pobjDest.setAt(SubPolicy.I.STATUS, midStatus);
			pobjDest.setAt(SubPolicy.I.PREMIUM, mdblPremium);
			pobjDest.setAt(SubPolicy.I.DOCUSHARE, mstrDocuShare);
			pobjDest.setAt(SubPolicy.I.MIGRATIONID, mlngMigrationID);
			pobjDest.setAt(SubPolicy.I.POLICY, midPolicy);
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
