package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.MedicalDetail;

public class MedicalDetailData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midFile;
	public UUID midDisabilityType;
	public Timestamp mdtStartDate;
	public String mstrPlace;
	public Integer mlngPercent;
	public Timestamp mdtEndDate;
	public BigDecimal mdblBenefits;

	public boolean mbNew;
	public boolean mbDeleted;

	public MedicalDetailData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midFile           =       (UUID)pobjSource.getAt(MedicalDetail.I.FILE);
		midDisabilityType =       (UUID)pobjSource.getAt(MedicalDetail.I.DISABILITYTYPE);
		mdtStartDate      =  (Timestamp)pobjSource.getAt(MedicalDetail.I.STARTDATE);
		mstrPlace         =     (String)pobjSource.getAt(MedicalDetail.I.PLACE);
		mlngPercent       =    (Integer)pobjSource.getAt(MedicalDetail.I.PERCENT);
		mdtEndDate        =  (Timestamp)pobjSource.getAt(MedicalDetail.I.ENDDATE);
		mdblBenefits      = (BigDecimal)pobjSource.getAt(MedicalDetail.I.BENEFITS);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MedicalDetail.I.FILE,           midFile);
			pobjDest.setAt(MedicalDetail.I.DISABILITYTYPE, midDisabilityType);
			pobjDest.setAt(MedicalDetail.I.STARTDATE,      mdtStartDate);
			pobjDest.setAt(MedicalDetail.I.PLACE,          mstrPlace);
			pobjDest.setAt(MedicalDetail.I.PERCENT,        mlngPercent);
			pobjDest.setAt(MedicalDetail.I.ENDDATE,        mdtEndDate);
			pobjDest.setAt(MedicalDetail.I.BENEFITS,       mdblBenefits);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;

		if ( mbNew )
			pstrBuilder.append("(Novo detalhe) ");
		else if ( mbDeleted )
			pstrBuilder.append("(Detalhe removido) ");

		pstrBuilder.append("Tipo de Incapacidade: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DisabilityType),
					midDisabilityType);
			pstrBuilder.append(lobjAux.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(erro a obter o tipo de incapacidade)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data de Início: ");
		pstrBuilder.append(mdtStartDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);

		if ( mstrPlace != null )
		{
			pstrBuilder.append("Sítio da Baixa ou Internamento: ");
			pstrBuilder.append(mstrPlace);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mlngPercent != null )
		{
			pstrBuilder.append("Percentagem de Invalidez: ");
			pstrBuilder.append(mlngPercent);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mdtEndDate != null )
		{
			pstrBuilder.append("Data de Fim: ");
			pstrBuilder.append(mdtEndDate.toString().substring(0, 10));
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mdblBenefits != null )
		{
			pstrBuilder.append("Valor da Indemnização (€): ");
			pstrBuilder.append(mdblBenefits);
			pstrBuilder.append(pstrLineBreak);
		}
	}
}
