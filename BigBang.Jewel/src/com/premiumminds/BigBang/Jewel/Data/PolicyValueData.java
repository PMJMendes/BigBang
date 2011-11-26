package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public class PolicyValueData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrValue;
	public UUID midOwner;
	public UUID midField;
	public UUID midObject;
	public UUID midExercise;
	public int mlngObject;
	public int mlngExercise;

	public boolean mbNew;
	public boolean mbDeleted;

	public PolicyValueData mobjPrevValues;

	public void Clone(PolicyValueData pobjSource)
	{
		mid = pobjSource.mid;
		mstrValue = pobjSource.mstrValue;
		midOwner = pobjSource.midOwner;
		midField = pobjSource.midField;
		midObject = pobjSource.midObject;
		midExercise = pobjSource.midExercise;
		mlngObject = pobjSource.mlngObject;
		mlngExercise = pobjSource.mlngExercise;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrValue = (String)pobjSource.getAt(0);
		midOwner = (UUID)pobjSource.getAt(1);
		midField = (UUID)pobjSource.getAt(2);
		midObject = (UUID)pobjSource.getAt(3);
		midExercise = (UUID)pobjSource.getAt(4);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrValue);
			pobjDest.setAt(1, midOwner);
			pobjDest.setAt(2, midField);
			pobjDest.setAt(3, midObject);
			pobjDest.setAt(4, midExercise);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		Tax lobjTax;
		PolicyObject lobjObject;
		PolicyExercise lobjExercise;

		try
		{
			lobjTax = Tax.GetInstance(Engine.getCurrentNameSpace(), midField);
			pstrBuilder.append(lobjTax.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o nome do campo.)");
		}

		if ( midObject != null )
		{
			pstrBuilder.append(" [");
			try
			{
				lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), midObject);
				pstrBuilder.append(lobjObject.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("<Erro a obter o objecto correspondente.>");
			}
			if ( midExercise != null )
				pstrBuilder.append(", ");
			else
				pstrBuilder.append("]");
		}

		if ( midExercise != null )
		{
			if ( midObject == null )
				pstrBuilder.append("[");
			try
			{
				lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), midExercise);
				pstrBuilder.append(lobjExercise.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("<Erro a obter o exercício correspondente.>");
			}
			pstrBuilder.append("]");
		}

		pstrBuilder.append(": ").append(mstrValue).append(pstrLineBreak);
	}
}
