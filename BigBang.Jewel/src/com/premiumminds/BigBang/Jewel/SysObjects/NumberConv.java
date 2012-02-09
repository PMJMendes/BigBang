package com.premiumminds.BigBang.Jewel.SysObjects;

public class NumberConv
{
	public static String getAsText(long plngNumber)
	{
		String lstrAux1, lstrAux2;
		long llngAux1, llngAux2;

		String larrH[] = new String[]{"cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos",
				"oitocentos", "novecentos"};
		String larrD[] = new String[]{"vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"};
		String larrU[] = new String[]{"um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze",
				"doze", "treze", "catorze", "quinze", "dezasseis", "dezassete", "dezoito", "dezanove"};

		if (plngNumber == 0)
			return "zero";

		if (plngNumber < 0)
			return "menos " + getAsText(-plngNumber);

		if (plngNumber > 999999)
		{
			llngAux1 = plngNumber / 1000000;
			llngAux2 = plngNumber - 1000000 * llngAux1;
			lstrAux1 = getAsText(llngAux1);
			if (llngAux1 > 1)
				lstrAux1 = lstrAux1 + " milhões";
			else
				lstrAux1 = lstrAux1 + " milhão";
			if (llngAux2 == 0)
				return lstrAux1;
			lstrAux2 = getAsText(llngAux2);
			if ((llngAux2 > 100) && (lstrAux2.indexOf(" e ") > 0))
				return lstrAux1 + ", " + lstrAux2;
			return lstrAux1 + " e " + lstrAux2;
		}

		if (plngNumber > 999)
		{
			llngAux1 = plngNumber / 1000;
			llngAux2 = plngNumber - 1000 * llngAux1;
			if (llngAux1 > 1)
				lstrAux1 = getAsText(llngAux1) + " mil";
			else
				lstrAux1 = "mil";
			if (llngAux2 == 0)
				return lstrAux1;
			lstrAux2 = getAsText(llngAux2);
			if ((llngAux2 > 100) && (lstrAux2.indexOf(" e ") > 0))
				return lstrAux1 + ", " + lstrAux2;
			return lstrAux1 + " e " + lstrAux2;
		}

		if (plngNumber == 100)
			return "cem";
		if (plngNumber > 99)
		{
			llngAux1 = plngNumber / 100;
			llngAux2 = plngNumber - 100 * llngAux1;
			lstrAux1 = larrH[(int)llngAux1 - 1];
			if (llngAux2 == 0) 
				return lstrAux1;
			return lstrAux1 + " e " + getAsText(llngAux2);
		}

		if (plngNumber < 20)
			return larrU[(int)plngNumber - 1];
		llngAux1 = plngNumber / 10;
		llngAux2 = plngNumber - 10 * llngAux1;
		lstrAux1 = larrD[(int)llngAux1 - 2];
		if (llngAux2 == 0)
			return lstrAux1;
		return lstrAux1 + " e " + larrU[(int)llngAux2 - 1];
	}

	public static String getAsEuroText(double pdblAmount)
	{
		long llngAux1, llngAux2, llngAux3, llngAux4;
		String lstrAux1, lstrAux2;

		if ( Math.abs(pdblAmount) < 0.005 )
			return "zero euros";

		if ( pdblAmount < 0 )
			return "menos " + getAsEuroText(-pdblAmount);

		llngAux1 = (long)pdblAmount;
		llngAux2 = (long)((pdblAmount - llngAux1) * 100.0 + 0.5);

		lstrAux1 = "";
		if (llngAux1 > 0)
		{
			lstrAux1 = getAsText(llngAux1);
			if (llngAux1 > 999999)
			{
				llngAux3 = llngAux1 / 1000000;
				llngAux4 = llngAux1 - 1000000 * llngAux3;
				if (llngAux4 == 0)
					lstrAux1 = lstrAux1 + " de euros";
				else
				{
					if (llngAux4 == 1)
						lstrAux1 = lstrAux1 + " euro";
					else
						lstrAux1 = lstrAux1 + " euros";
				}
			}
			else
			{
				if (llngAux1 == 1)
					lstrAux1 = lstrAux1 + " euro";
				else
					lstrAux1 = lstrAux1 + " euros";
			}
			if (llngAux2 == 0)
				return lstrAux1;
			lstrAux1 = lstrAux1 + " e ";
		}

		lstrAux2 = getAsText(llngAux2);
		if (llngAux2 == 1)
			lstrAux2 = lstrAux2 + " cêntimo";
		else
			lstrAux2 = lstrAux2 + " cêntimos";

		return lstrAux1 + lstrAux2;
	}
}
