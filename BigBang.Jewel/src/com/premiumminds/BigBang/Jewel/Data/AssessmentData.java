package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Assessment;

public class AssessmentData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrReference;
	public UUID midSubCasualty;
	public Timestamp mdtScheduledDate;
	public Timestamp mdtEffectiveDate;
	public Boolean mbConditional;
	public Boolean mbTotalLoss;
	public String mstrNotes;
	public Timestamp mdtLimitDate;

	public UUID midManager;
	public UUID midProcess;

	public AssessmentData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrReference =       (String)pobjSource.getAt(Assessment.I.REFERENCE);
		midSubCasualty =        (UUID)pobjSource.getAt(Assessment.I.SUBCASUALTY);
		midProcess =            (UUID)pobjSource.getAt(Assessment.I.PROCESS);
		mdtScheduledDate = (Timestamp)pobjSource.getAt(Assessment.I.SCHEDULEDDATE);
		mdtEffectiveDate = (Timestamp)pobjSource.getAt(Assessment.I.EFFECTIVEDATE);
		mbConditional =      (Boolean)pobjSource.getAt(Assessment.I.ISCONDITIONAL);
		mbTotalLoss =        (Boolean)pobjSource.getAt(Assessment.I.ISTOTALLOSS);
		mstrNotes =           (String)pobjSource.getAt(Assessment.I.NOTES);
		mdtLimitDate =     (Timestamp)pobjSource.getAt(Assessment.I.LIMITDATE);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(Assessment.I.REFERENCE,     mstrReference);
			pobjDest.setAt(Assessment.I.SUBCASUALTY,   midSubCasualty);
			pobjDest.setAt(Assessment.I.PROCESS,       midProcess);
			pobjDest.setAt(Assessment.I.SCHEDULEDDATE, mdtScheduledDate);
			pobjDest.setAt(Assessment.I.EFFECTIVEDATE, mdtEffectiveDate);
			pobjDest.setAt(Assessment.I.ISCONDITIONAL, mbConditional);
			pobjDest.setAt(Assessment.I.ISTOTALLOSS,   mbTotalLoss);
			pobjDest.setAt(Assessment.I.NOTES,         mstrNotes);
			pobjDest.setAt(Assessment.I.LIMITDATE,     mdtLimitDate);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		pstrBuilder.append("Referência: ");
		pstrBuilder.append(mstrReference);
		pstrBuilder.append(pstrLineBreak);

		if ( mdtEffectiveDate == null )
		{
			pstrBuilder.append("Data agendada: ");
			pstrBuilder.append(mdtScheduledDate == null ? "(por agendar)" : mdtScheduledDate.toString().substring(0, 10));
			pstrBuilder.append(pstrLineBreak);
		}
		else
		{
			pstrBuilder.append("Data efectuada: ");
			pstrBuilder.append(mdtScheduledDate.toString().substring(0, 10));
			pstrBuilder.append(pstrLineBreak);
			if ( mdtScheduledDate != null )
			{
				pstrBuilder.append("Próxima data agendada: ");
				pstrBuilder.append(mdtScheduledDate.toString().substring(0, 10));
				pstrBuilder.append(pstrLineBreak);
			}
		}

		if ( mbConditional != null )
		{
			pstrBuilder.append("Resultado: ");
			pstrBuilder.append( mbConditional ? "Condicional" : "Definitiva" );
			pstrBuilder.append(pstrLineBreak);
		}

		if ( (mbTotalLoss != null) && mbTotalLoss )
		{
			pstrBuilder.append("Perda Total!");
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrNotes != null )
		{
			pstrBuilder.append("Notas: ");
			pstrBuilder.append(mstrNotes);
			pstrBuilder.append(pstrLineBreak);
		}
	}
}
