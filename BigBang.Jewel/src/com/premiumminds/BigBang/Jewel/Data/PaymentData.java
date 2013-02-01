package com.premiumminds.BigBang.Jewel.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

public class PaymentData
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public UUID midPaymentType;
	public BigDecimal mdblValue;
	public UUID midBank;
	public String mstrCheque;
	public UUID midReceipt;
	public boolean mbCreateCounter;
	public UUID midLog;

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;
		
		pstrBuilder.append("Tipo: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PaymentType), midPaymentType);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o tipo de pagamento.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Valor: ");
		pstrBuilder.append(mdblValue);
		pstrBuilder.append(pstrLineBreak);

		if ( midBank != null )
		{
			pstrBuilder.append("Banco: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Bank), midBank);
				pstrBuilder.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o banco.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrCheque != null )
		{
			pstrBuilder.append("Número: ");
			pstrBuilder.append(mstrCheque);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( midReceipt != null )
		{
			pstrBuilder.append("Recibo Compensado: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt), midReceipt);
				pstrBuilder.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o banco.)");
			}
			pstrBuilder.append(pstrLineBreak);

			if ( mbCreateCounter )
				pstrBuilder.append("Este recibo também foi marcado como pago.").append(pstrLineBreak);
		}
	}
}
