package com.premiumminds.BigBang.Jewel.Detailed;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class Vida_Risco_Individual
	extends DetailedBase
{
	public Vida_Risco_Individual(Policy pobjPolicy, SubPolicy pobjSubPolicy)
	{
		super(pobjPolicy, pobjSubPolicy);
	}

	protected void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
		int i;
		PolicyValue lobjPERCEM;
		ObjectBase lobjAux;

		if ( marrObjects == null )
		{
			pstrBuilder.append("Esta modalidade de apólice obriga a ter objectos seguros, consoante o indicado no campo 'Nº de Cabeças'.\n");
			return;
		}

		i = FindValue(FindFieldID("H", "NCAB"), null, null, 0);
		if ( i < -1 )
		{
			pstrBuilder.append("O campo 'Nº de Cabeças'")
					.append(" é de preenchimento obrigatório, mas não está presente na apólice.\n");
			return;
		}
		lobjPERCEM = marrValues[i];

		if ( (lobjPERCEM.GetValue() == null) || !CheckFormat(pstrBuilder, lobjPERCEM, false) )
			return;

		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_FieldValues),
					UUID.fromString(lobjPERCEM.GetValue()));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( "Uma Cabeça".equals(lobjAux.getLabel()) && (marrObjects.length != 1) )
			pstrBuilder.append("Esta modalidade de apólice obriga a ter um objecto seguro, de acordo com o indicado no campo 'Nº de Cabeças'.\n");

		if ( "Duas Cabeças".equals(lobjAux.getLabel()) && (marrObjects.length != 2) )
			pstrBuilder.append("Esta modalidade de apólice obriga a ter dois objectos seguros, de acordo com o indicado no campo 'Nº de Cabeças'.\n");
	}

	protected void InnerSubValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
	}

	protected String InnerDoCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return null;
	}

	protected String InnerDoSubCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return null;
	}
}
