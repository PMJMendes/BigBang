package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.MedicalRelapse;

public class MedicalRelapseData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midFile;
	public String mstrLabel;
	public Timestamp mdtDate;

	public boolean mbNew;
	public boolean mbDeleted;

	public MedicalRelapseData mobjPrevValues;


	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midFile   =      (UUID)pobjSource.getAt(MedicalRelapse.I.FILE);
		mstrLabel =    (String)pobjSource.getAt(MedicalRelapse.I.LABEL);
		mdtDate   = (Timestamp)pobjSource.getAt(MedicalRelapse.I.DATE);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MedicalRelapse.I.FILE,  midFile);
			pobjDest.setAt(MedicalRelapse.I.LABEL, mstrLabel);
			pobjDest.setAt(MedicalRelapse.I.DATE,  mdtDate);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		if ( mbNew )
			pstrBuilder.append("Nova recaída: ");
		else if ( mbDeleted )
			pstrBuilder.append("Recaída removida: ");
		else
			pstrBuilder.append("Recaída: ");
		pstrBuilder.append(mstrLabel);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data da recaída: ");
		pstrBuilder.append(mdtDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);
	}
}
