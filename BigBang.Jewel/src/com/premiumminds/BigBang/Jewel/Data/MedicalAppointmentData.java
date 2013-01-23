package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.MedicalAppointment;

public class MedicalAppointmentData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midFile;
	public String mstrLabel;
	public Timestamp mdtDate;

	public boolean mbNew;
	public boolean mbDeleted;

	public MedicalAppointmentData mobjPrevValues;


	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midFile   =      (UUID)pobjSource.getAt(MedicalAppointment.I.FILE);
		mstrLabel =    (String)pobjSource.getAt(MedicalAppointment.I.LABEL);
		mdtDate   = (Timestamp)pobjSource.getAt(MedicalAppointment.I.DATE);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MedicalAppointment.I.FILE,  midFile);
			pobjDest.setAt(MedicalAppointment.I.LABEL, mstrLabel);
			pobjDest.setAt(MedicalAppointment.I.DATE,  mdtDate);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		if ( mbNew )
			pstrBuilder.append("Nova consulta: ");
		else if ( mbDeleted )
			pstrBuilder.append("Consulta removida: ");
		else
			pstrBuilder.append("Consulta: ");
		pstrBuilder.append(mstrLabel);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data da consulta: ");
		pstrBuilder.append(mdtDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);
	}
}
