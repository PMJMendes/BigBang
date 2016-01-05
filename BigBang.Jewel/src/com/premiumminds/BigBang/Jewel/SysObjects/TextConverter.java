package com.premiumminds.BigBang.Jewel.SysObjects;

public class TextConverter
{
	public static String fromNumber(Long plngNumber)
	{
		String lstrAux1, lstrAux2;
		long llngAux1, llngAux2;

		String larrH[] = {"", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"};
		String larrD[] = {"", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"};
		String larrU[] = {"", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove",
					"dez", "onze", "doze", "treze", "catorze", "quinze", "dezasseis", "dezassete", "dezoito", "dezanove"};

		if (plngNumber == null)
			return "zero";

		if (plngNumber == 0)
			return "zero";

		if (plngNumber < 0)
			return "menos " + fromNumber(-plngNumber);

		if (plngNumber > 999999)
		{
			llngAux1 = plngNumber / 1000000;
			llngAux2 = plngNumber - 1000000 * llngAux1;
			lstrAux1 = fromNumber(llngAux1);
			if (llngAux1 > 1)
				lstrAux1 = lstrAux1 + " milhões";
			else
				lstrAux1 = lstrAux1 + " milhão";
			if (llngAux2 == 0)
				return lstrAux1;
			lstrAux2 = fromNumber(llngAux2);
			if ( (llngAux2 > 100) && (lstrAux2.indexOf(" e ") > 0) )
				return lstrAux1 + ", " + lstrAux2;
			return lstrAux1 + " e " + lstrAux2;
		}

		if (plngNumber > 999)
		{
			llngAux1 = plngNumber / 1000;
			llngAux2 = plngNumber - 1000 * llngAux1;
			if (llngAux1 > 1)
				lstrAux1 = fromNumber(llngAux1) + " mil";
			else
				lstrAux1 = "mil";
			if (llngAux2 == 0)
				return lstrAux1;
			lstrAux2 = fromNumber(llngAux2);
			if ( (llngAux2 > 100) && (lstrAux2.indexOf(" e ") > 0) )
				return lstrAux1 + ", " + lstrAux2;
			return lstrAux1 + " e " + lstrAux2;
		}

		if (plngNumber == 100)
			return "cem";

		if (plngNumber > 99)
		{
			llngAux1 = plngNumber / 100;
			llngAux2 = plngNumber - 100 * llngAux1;
			lstrAux1 = larrH[(int)llngAux1];
			if (llngAux2 == 0)
				return lstrAux1;
			return lstrAux1 + " e " + fromNumber(llngAux2);
		}

		if (plngNumber < 20)
			return larrU[(int)(long)plngNumber];

//		if (plngNumber > 9) // Se chegar aqui, é sempre maior que 9, e o Java queixa-se se os paths não devolverem todos um valor.
		{
			llngAux1 = plngNumber / 10;
			llngAux2 = plngNumber - 10 * llngAux1;
			lstrAux1 = larrD[(int)llngAux1 - 1];
			if (llngAux2 == 0)
				return lstrAux1;
			return lstrAux1 + " e " + larrU[(int)llngAux2];
		}
	}

	public static String fromCurrency(Double pdblAmount)
	{
		String lstrAux1, lstrAux2;
		long llngAux1, llngAux2, llngAux3, llngAux4;

		if (pdblAmount == null)
			return "zero " + Utils.getCurrTxt() + "s";

		if (Math.abs(pdblAmount) < 0.005)
			return "zero " + Utils.getCurrTxt() + "s";

		if (pdblAmount < 0)
			return "menos " + fromCurrency(-pdblAmount);

		llngAux1 = (long)(double)pdblAmount;
		llngAux2 = (long)((pdblAmount - ((double)llngAux1)) * 100.0 + 0.5);

		lstrAux1 = "";
		if (llngAux1 > 0)
		{
			lstrAux1 = fromNumber(llngAux1);
			if (llngAux1 > 999999)
			{
				llngAux3 = llngAux1 / 1000000;
				llngAux4 = llngAux1 - 1000000 * llngAux3;
				if (llngAux4 == 0)
					lstrAux1 = lstrAux1 + " de " + Utils.getCurrTxt() + "s";
				else
				{
					if (llngAux4 == 1)
						lstrAux1 = lstrAux1 + " " + Utils.getCurrTxt();
					else
						lstrAux1 = lstrAux1 + " " + Utils.getCurrTxt() + "s";
				}
			}
			else
			{
				if (llngAux1 == 1)
					lstrAux1 = lstrAux1 + " " + Utils.getCurrTxt();
				else
					lstrAux1 = lstrAux1 + " " + Utils.getCurrTxt() + "s";
			}
			if (llngAux2 == 0)
				return lstrAux1;
			lstrAux1 = lstrAux1 + " e ";
		}

		lstrAux2 = fromNumber(llngAux2);
		if (llngAux2 == 1)
			lstrAux2 = lstrAux2 + " cêntimo";
		else
			lstrAux2 = lstrAux2 + " cêntimos";

		return lstrAux1 + lstrAux2;
	}
}
