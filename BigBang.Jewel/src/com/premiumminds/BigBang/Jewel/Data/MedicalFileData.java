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

	public UUID midManager;
	public UUID midProcess;

	public MedicalDetailData[] marrDetails;

	public MedicalFileData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrReference =  (String)pobjSource.getAt(MedicalFile.I.REFERENCE);
		midSubCasualty =   (UUID)pobjSource.getAt(MedicalFile.I.SUBCASUALTY);
		midProcess =       (UUID)pobjSource.getAt(MedicalFile.I.PROCESS);
		mdtNextDate = (Timestamp)pobjSource.getAt(MedicalFile.I.NEXTDATE);
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

		if ( (marrDetails != null) && (marrDetails.length > 0) )
		{
			pstrBuilder.append(pstrLineBreak).append("Detalhes da Baixa ou Internamento:").append(pstrLineBreak).append(pstrLineBreak);
			for ( i = 0; i < marrDetails.length; i++ )
			{
				marrDetails[i].Describe(pstrBuilder, pstrLineBreak);
				pstrBuilder.append(pstrLineBreak);
			}
		}
	}
}
