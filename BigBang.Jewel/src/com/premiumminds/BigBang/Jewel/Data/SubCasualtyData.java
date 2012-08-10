package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;

public class SubCasualtyData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrNumber;
	public UUID midPolicy;
	public UUID midSubPolicy;
	public String mstrInsurerProc;
	public String mstrDescription;
	public String mstrNotes;
	public boolean mbHasJudicial;
	public UUID midReviewer;
	public Timestamp mdtReviewDate;
	public UUID midPolicyObject;
	public UUID midSubPolicyObject;
	public String mstrGenericObject;

	public OutgoingMessageData mobjNotification;
	public Timestamp mdtLimitDate;

	public UUID midManager;
	public UUID midProcess;

	public SubCasualtyItemData[] marrItems;

	public SubCasualtyData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber         = (String)    pobjSource.getAt(SubCasualty.I.NUMBER);
		midProcess         = (UUID)      pobjSource.getAt(SubCasualty.I.PROCESS);
		midPolicy          = (UUID)      pobjSource.getAt(SubCasualty.I.POLICY);
		midSubPolicy       = (UUID)      pobjSource.getAt(SubCasualty.I.SUBPOLICY);
		mstrInsurerProc    = (String)    pobjSource.getAt(SubCasualty.I.INSURERPROCESS);
		mstrDescription    = (String)    pobjSource.getAt(SubCasualty.I.DESCRIPTION);
		mstrNotes          = (String)    pobjSource.getAt(SubCasualty.I.NOTES);
		mbHasJudicial      = (Boolean)   pobjSource.getAt(SubCasualty.I.HASJUDICIAL);
		midReviewer        = (UUID)      pobjSource.getAt(SubCasualty.I.REVIEWER);
		mdtReviewDate      = (Timestamp) pobjSource.getAt(SubCasualty.I.REVIEWDATE);
		midPolicyObject    = (UUID)      pobjSource.getAt(SubCasualty.I.POLICYOBJECT);
		midSubPolicyObject = (UUID)      pobjSource.getAt(SubCasualty.I.SUBPOLICYOBJECT);
		mstrGenericObject  = (String)    pobjSource.getAt(SubCasualty.I.GENERICOBJECT);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(SubCasualty.I.NUMBER,          mstrNumber);
			pobjDest.setAt(SubCasualty.I.PROCESS,         midProcess);
			pobjDest.setAt(SubCasualty.I.POLICY,          midPolicy);
			pobjDest.setAt(SubCasualty.I.SUBPOLICY,       midSubPolicy);
			pobjDest.setAt(SubCasualty.I.INSURERPROCESS,  mstrInsurerProc);
			pobjDest.setAt(SubCasualty.I.DESCRIPTION,     mstrDescription);
			pobjDest.setAt(SubCasualty.I.NOTES,           mstrNotes);
			pobjDest.setAt(SubCasualty.I.HASJUDICIAL,     mbHasJudicial);
			pobjDest.setAt(SubCasualty.I.REVIEWER,        midReviewer);
			pobjDest.setAt(SubCasualty.I.REVIEWDATE,      mdtReviewDate);
			pobjDest.setAt(SubCasualty.I.POLICYOBJECT,    midPolicyObject);
			pobjDest.setAt(SubCasualty.I.SUBPOLICYOBJECT, midSubPolicyObject);
			pobjDest.setAt(SubCasualty.I.GENERICOBJECT,   mstrGenericObject);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		PolicyObject lobjPObj;
		SubPolicyObject lobjSPObj;
		int i;

		pstrBuilder.append("Número do processo: ").append(mstrNumber).append(pstrLineBreak);

		if ( midPolicy != null )
		{
			pstrBuilder.append("Apólice activada: ");
			try
			{
				lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);
				pstrBuilder.append(lobjPolicy.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter a apólice activada.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( midSubPolicy != null )
		{
			pstrBuilder.append("Apólice adesão activada: ");
			try
			{
				lobjSubPolicy = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), midSubPolicy);
				pstrBuilder.append(lobjSubPolicy.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter a apólice adesão activada.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Número do processo na seguradora: ");
		if ( mstrInsurerProc != null )
			pstrBuilder.append(mstrInsurerProc);
		else
			pstrBuilder.append("Não indicado.");
		pstrBuilder.append(pstrLineBreak);

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

		if ( mstrDescription != null )
			pstrBuilder.append("Descrição:").append(pstrLineBreak).append(mstrDescription).append(pstrLineBreak);

		if ( mstrNotes != null )
			pstrBuilder.append("Notas internas:").append(pstrLineBreak).append(mstrNotes).append(pstrLineBreak);

		if ( (marrItems != null) && (marrItems.length > 0) )
		{
			pstrBuilder.append(pstrLineBreak).append("Resumo dos danos:").append(pstrLineBreak).append(pstrLineBreak);
			for ( i = 0; i < marrItems.length; i++ )
			{
				marrItems[i].Describe(pstrBuilder, pstrLineBreak);
				pstrBuilder.append(pstrLineBreak);
			}
		}
	}
}
