package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class ClientData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrName;
	public int mlngNumber;
	public String mstrAddress1;
	public String mstrAddress2;
	public UUID midZipCode;
	public String mstrFiscal;
	public UUID midType;
	public UUID midSubtype;
	public UUID midMediator;
	public UUID midProfile;
	public UUID midGroup;
	public String mstrBankingID;
	public Timestamp mdtDateOfBirth;
	public UUID midSex;
	public UUID midMarital;
	public UUID midProfession;
	public UUID midCAE;
	public String mstrCAENotes;
	public UUID midSize;
	public UUID midSales;
	public String mstrNotes;

	public UUID midManager;
	public UUID midProcess;
	
	public ClientData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrName = (String)pobjSource.getAt(0);
		mlngNumber = (Integer)pobjSource.getAt(1);
		mstrAddress1 = (String)pobjSource.getAt(2);
		mstrAddress2 = (String)pobjSource.getAt(3);
		midZipCode = (UUID)pobjSource.getAt(4);
		mstrFiscal = (String)pobjSource.getAt(5);
		midType = (UUID)pobjSource.getAt(6);
		midSubtype = (UUID)pobjSource.getAt(7);
		midMediator = (UUID)pobjSource.getAt(8);
		midProfile = (UUID)pobjSource.getAt(9);
		midGroup = (UUID)pobjSource.getAt(10);
		mstrBankingID = (String)pobjSource.getAt(11);
		mdtDateOfBirth = (Timestamp)pobjSource.getAt(12);
		midSex = (UUID)pobjSource.getAt(13);
		midMarital = (UUID)pobjSource.getAt(14);
		midProfession = (UUID)pobjSource.getAt(15);
		midCAE = (UUID)pobjSource.getAt(16);
		mstrCAENotes = (String)pobjSource.getAt(17);
		midSize = (UUID)pobjSource.getAt(18);
		midSales = (UUID)pobjSource.getAt(19);
		mstrNotes = (String)pobjSource.getAt(20);
		midProcess = (UUID)pobjSource.getAt(21);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt( 0, mstrName);
			pobjDest.setAt( 1, mlngNumber);
			pobjDest.setAt( 2, mstrAddress1);
			pobjDest.setAt( 3, mstrAddress2);
			pobjDest.setAt( 4, midZipCode);
			pobjDest.setAt( 5, mstrFiscal);
			pobjDest.setAt( 6, midType);
			pobjDest.setAt( 7, midSubtype);
			pobjDest.setAt( 8, midMediator);
			pobjDest.setAt( 9, midProfile);
			pobjDest.setAt(10, midGroup);
			pobjDest.setAt(11, mstrBankingID);
			pobjDest.setAt(12, mdtDateOfBirth);
			pobjDest.setAt(13, midSex);
			pobjDest.setAt(14, midMarital);
			pobjDest.setAt(15, midProfession);
			pobjDest.setAt(16, midCAE);
			pobjDest.setAt(17, mstrCAENotes);
			pobjDest.setAt(18, midSize);
			pobjDest.setAt(19, midSales);
			pobjDest.setAt(20, mstrNotes);
			pobjDest.setAt(21, midProcess);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;

		pstrBuilder.append("Número: ");
		pstrBuilder.append(mlngNumber);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("Nome: ");
		pstrBuilder.append(mstrName);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("Morada:");
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");
		if ( mstrAddress1 != null )
			pstrBuilder.append(mstrAddress1);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");
		if ( mstrAddress2 != null )
			pstrBuilder.append(mstrAddress2);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("- ");
		if ( midZipCode != null )
		{
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						midZipCode);
				pstrBuilder.append((String)lobjAux.getAt(0));
				pstrBuilder.append(" ");
				pstrBuilder.append((String)lobjAux.getAt(1));
				pstrBuilder.append(pstrLineBreak);
				pstrBuilder.append("- ");
	        	pstrBuilder.append((String)lobjAux.getAt(4));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o código postal.)");
			}
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("NIF/NIPC: ");
		if ( mstrFiscal != null )
			pstrBuilder.append(mstrFiscal);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Tipo: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientType),
					midType);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o tipo de cliente.)");
		}
		pstrBuilder.append(pstrLineBreak);

		if ( Constants.TypeID_Other.equals(midType) )
		{
			pstrBuilder.append("Sub-Tipo: ");
			if ( midSubtype != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientSubtype),
							midSubtype);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o sub-tipo de cliente.)");
				}
			}
			pstrBuilder.append(pstrLineBreak);
		}

//		pstrBuilder.append("Gestor: ");
//		try
//		{
//			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_User),
//					midManager);
//			pstrBuilder.append((String)lobjAux.getAt(0));
//		}
//		catch (Throwable e)
//		{
//			pstrBuilder.append("(Erro a obter o gestor de cliente.)");
//		}
//		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Mediador: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Mediator),
					midMediator);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o mediador do cliente.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Perfil Operacional: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OpProfile),
					midProfile);
			pstrBuilder.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o perfil operacional do cliente.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Grupo: ");
		if ( midGroup != null )
		{
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup),
						midGroup);
				pstrBuilder.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o grupo do cliente.)");
			}
		}
		else
			pstrBuilder.append("(nenhum)");
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("NIB: ");
		if ( mstrBankingID != null )
			pstrBuilder.append(mstrBankingID);
		pstrBuilder.append(pstrLineBreak);

		if ( Constants.TypeID_Individual.equals(midType) )
		{
			pstrBuilder.append("Data de Nascimento: ");
			if ( mdtDateOfBirth != null )
				pstrBuilder.append(mdtDateOfBirth.toString().substring(0, 10));
			pstrBuilder.append(pstrLineBreak);

			pstrBuilder.append("Sexo: ");
			if ( midSex != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Sex),
							midSex);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o sexo do cliente.)");
				}
			}
			pstrBuilder.append(pstrLineBreak);

			pstrBuilder.append("Estado Civil: ");
			if ( midMarital != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MaritalStatus),
							midMarital);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o estado civil do cliente.)");
				}
			}
			pstrBuilder.append(pstrLineBreak);

			pstrBuilder.append("Profissão: ");
			if ( midProfession != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Profession),
							midProfession);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter a profissão do cliente.)");
				}
			}
			pstrBuilder.append(pstrLineBreak);
		}

		if ( Constants.TypeID_Company.equals(midType) )
		{
			pstrBuilder.append("CAE: ");
			if ( midCAE != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CAE),
							midCAE);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o CAE do cliente.)");
				}
			}
			pstrBuilder.append(pstrLineBreak);

			pstrBuilder.append("Obs. Actividade: ");
			if ( mstrCAENotes != null )
				pstrBuilder.append(mstrCAENotes);
			pstrBuilder.append(pstrLineBreak);

			pstrBuilder.append("N. Trabalhadores: ");
			if ( midSize != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Size),
							midSize);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter o número de trabalhadores do cliente.)");
				}
			}
			pstrBuilder.append(pstrLineBreak);

			pstrBuilder.append("Facturação: ");
			if ( midSales != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SalesVolume),
							midSales);
					pstrBuilder.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					pstrBuilder.append("(Erro a obter a facturação do cliente.)");
				}
			}
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Observações: ");
		if ( mstrNotes != null )
			pstrBuilder.append(mstrNotes);
		pstrBuilder.append(pstrLineBreak);
	}
}
