package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.MedicalFile;

public class MedicalFileData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrReference;
	public UUID midSubCasualty;
	public Timestamp mdtNextDate;
	public String mstrNotes;

	public UUID midManager;
	public UUID midProcess;

	public MedicalDetailData[] marrDetails;

	public MedicalAppointmentData[] marrAppts;
	
	public MedicalRelapseData[] marrRelps;

	public MedicalFileData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrReference  = (String)   pobjSource.getAt(MedicalFile.I.REFERENCE);
		midSubCasualty = (UUID)     pobjSource.getAt(MedicalFile.I.SUBCASUALTY);
		midProcess     = (UUID)     pobjSource.getAt(MedicalFile.I.PROCESS);
		mdtNextDate    = (Timestamp)pobjSource.getAt(MedicalFile.I.NEXTDATE);
		mstrNotes      = (String)   pobjSource.getAt(MedicalFile.I.NOTES);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MedicalFile.I.REFERENCE,   mstrReference);
			pobjDest.setAt(MedicalFile.I.SUBCASUALTY, midSubCasualty);
			pobjDest.setAt(MedicalFile.I.PROCESS,     midProcess);
			pobjDest.setAt(MedicalFile.I.NEXTDATE,    mdtNextDate);
			pobjDest.setAt(MedicalFile.I.NOTES,       mstrNotes);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		int i;

		pstrBuilder.append("Referência: ");
		pstrBuilder.append(mstrReference);
		pstrBuilder.append(pstrLineBreak);

		if ( mdtNextDate != null )
		{
			pstrBuilder.append("Data da Próxima Consulta: ");
			pstrBuilder.append(mdtNextDate.toString().substring(0, 10));
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrNotes != null )
		{
			pstrBuilder.append("Notas: ");
			pstrBuilder.append(pstrLineBreak);
			pstrBuilder.append(mstrNotes);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( (marrDetails != null) && (marrDetails.length > 0) )
		{
			pstrBuilder.append(pstrLineBreak).append("Detalhes da Baixa ou Internamento:").append(pstrLineBreak).append(pstrLineBreak);
			for ( i = 0; i < marrDetails.length; i++ )
			{
				if ( marrDetails[i] != null )
					marrDetails[i].Describe(pstrBuilder, pstrLineBreak);
				pstrBuilder.append(pstrLineBreak);
			}
		}

		if ( (marrAppts != null) && (marrAppts.length > 0) )
		{
			pstrBuilder.append(pstrLineBreak).append("Consultas:").append(pstrLineBreak).append(pstrLineBreak);
			for ( i = 0; i < marrAppts.length; i++ )
			{
				marrAppts[i].Describe(pstrBuilder, pstrLineBreak);
				pstrBuilder.append(pstrLineBreak);
			}
		}
		
		if ( (marrRelps != null) && (marrRelps.length > 0) )
		{
			pstrBuilder.append(pstrLineBreak).append("Recaídas:").append(pstrLineBreak).append(pstrLineBreak);
			for ( i = 0; i < marrRelps.length; i++ )
			{
				marrRelps[i].Describe(pstrBuilder, pstrLineBreak);
				pstrBuilder.append(pstrLineBreak);
			}
		}
	}
}
