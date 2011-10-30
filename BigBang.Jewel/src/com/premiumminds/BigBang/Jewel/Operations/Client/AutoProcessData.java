package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class AutoProcessData
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public String mstrPostal;
	public String mstrFiscal;

	public AutoProcessData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_AutoProcessData;
	}

	public String ShortDesc()
	{
		return "Análise de Dados (Automática)";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("Resultados da Análise:").append(pstrLineBreak);
		lstrBuffer.append("- Código Postal: ").append(mstrPostal).append(pstrLineBreak);
		lstrBuffer.append("- Número de Contribuinte: ").append(mstrFiscal).append(pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ObjectBase lobjData;
		Client lobjClient;
		boolean b;

		lobjData = GetProcess().GetData();
		if ( lobjData == null )
			throw new JewelPetriException("Inesperado: Dados do cliente não definidos.");
		if ( !(lobjData instanceof Client) )
			throw new JewelPetriException("Inesperado: Dados do cliente do tipo errado.");
		lobjClient = (Client)lobjData;

		b = true;

		if ( lobjClient.getAt(4) == null )
		{
			b = false;
			mstrPostal = "Ausente";
		}
		else
		{
			mstrPostal = "Presente";
		}

		if ( lobjClient.getAt(5) == null )
		{
			b = false;
			mstrFiscal = "Ausente";
		}
		else
		{
			if ( !IsValidNIF((String)lobjClient.getAt(5), (UUID)lobjClient.getAt(6)) )
			{
				b = false;
				mstrFiscal = "Inválido";
			}
			else
			{
				mstrFiscal = "Válido";
			}
		}

		if ( !b )
			TriggerOp(new TriggerDisallowPolicies(this.GetProcess().getKey()));
	}

	private static boolean IsValidNIF(String pstrNIF, UUID pidType)
	{
		char c;
		int checkDigit;
		int i;

		if (pstrNIF != null)
		{
			if (pstrNIF.matches("([0-9]*)") && pstrNIF.length() == 9)
			{
				c = pstrNIF.charAt(0);
				if ( ((c == '1' || c == '2') && Constants.TypeID_Individual.equals(pidType)) ||
						((c == '5' || c == '6') && !Constants.TypeID_Individual.equals(pidType)) ) 
				{
					checkDigit = Character.getNumericValue(c) * 9;
					for ( i = 2; i <= 8; i++ )
						checkDigit += (Character.getNumericValue(pstrNIF.charAt(i - 1)) * (10 - i));
					checkDigit = 11 - (checkDigit % 11);
					if ( checkDigit >= 10 )
						checkDigit = 0;
					if ( checkDigit == Character.getNumericValue(pstrNIF.charAt(8)) )
						return true;
				}
			}
		}

		return false;
	}
}
