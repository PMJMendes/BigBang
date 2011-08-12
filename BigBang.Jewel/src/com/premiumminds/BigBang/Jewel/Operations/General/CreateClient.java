package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class CreateClient
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public class ClientData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public int mlngNumber;
		public String mstrName;
		public String mstrAddress1;
		public String mstrAddress2;
		public UUID midZipCode;
		public String mstrFiscal;
		public UUID midType;
		public UUID midSubtype;
		public UUID midManager;
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
		public UUID midProcess;
		public ContactOps mobjContactOps;
		public DocOps mobjDocOps;
	}

	public ClientData mobjData;

	public CreateClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Criação de Cliente";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		ObjectBase lobjAux;

		lstrResult = new StringBuilder();

		lstrResult.append("Número: ");
		lstrResult.append(mobjData.mlngNumber);
		lstrResult.append(pstrLineBreak);
		lstrResult.append("Nome: ");
		lstrResult.append(mobjData.mstrName);
		lstrResult.append(pstrLineBreak);
		lstrResult.append("Morada:");
		lstrResult.append(pstrLineBreak);
		lstrResult.append("- ");
		if ( mobjData.mstrAddress1 != null )
			lstrResult.append(mobjData.mstrAddress1);
		lstrResult.append(pstrLineBreak);
		lstrResult.append("- ");
		if ( mobjData.mstrAddress2 != null )
			lstrResult.append(mobjData.mstrAddress2);
		lstrResult.append(pstrLineBreak);

		lstrResult.append("- ");
		if ( mobjData.midZipCode != null )
		{
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						mobjData.midZipCode);
				lstrResult.append((String)lobjAux.getAt(0));
				lstrResult.append(" ");
				lstrResult.append((String)lobjAux.getAt(1));
				lstrResult.append(pstrLineBreak);
				lstrResult.append("- ");
	        	lstrResult.append((String)lobjAux.getAt(4));
			}
			catch (Throwable e)
			{
				lstrResult.append("(Erro a obter o código postal.)");
			}
		}
		lstrResult.append(pstrLineBreak);

		lstrResult.append("NIF/NIPC: ");
		if ( mobjData.mstrFiscal != null )
			lstrResult.append(mobjData.mstrFiscal);
		lstrResult.append(pstrLineBreak);

		lstrResult.append("Tipo: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientType),
					mobjData.midType);
			lstrResult.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			lstrResult.append("(Erro a obter o tipo de cliente.)");
		}
		lstrResult.append(pstrLineBreak);

		if ( Constants.TypeID_Other.equals(mobjData.midType) )
		{
			lstrResult.append("Sub-Tipo: ");
			if ( mobjData.midSubtype != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientSubtype),
							mobjData.midSubtype);
					lstrResult.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					lstrResult.append("(Erro a obter o sub-tipo de cliente.)");
				}
			}
			lstrResult.append(pstrLineBreak);
		}

		lstrResult.append("Gestor: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_User),
					mobjData.midManager);
			lstrResult.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			lstrResult.append("(Erro a obter o gestor de cliente.)");
		}
		lstrResult.append(pstrLineBreak);

		lstrResult.append("Mediador: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Mediator),
					mobjData.midMediator);
			lstrResult.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			lstrResult.append("(Erro a obter o mediador do cliente.)");
		}
		lstrResult.append(pstrLineBreak);

		lstrResult.append("Perfil Operacional: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OpProfile),
					mobjData.midProfile);
			lstrResult.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			lstrResult.append("(Erro a obter o perfil operacional do cliente.)");
		}
		lstrResult.append(pstrLineBreak);

		lstrResult.append("Grupo: ");
		if ( mobjData.midGroup != null )
		{
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup),
						mobjData.midGroup);
				lstrResult.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				lstrResult.append("(Erro a obter o grupo do cliente.)");
			}
		}
		else
			lstrResult.append("(nenhum)");
		lstrResult.append(pstrLineBreak);

		lstrResult.append("NIB: ");
		if ( mobjData.mstrBankingID != null )
			lstrResult.append(mobjData.mstrBankingID);
		lstrResult.append(pstrLineBreak);

		if ( Constants.TypeID_Individual.equals(mobjData.midType) )
		{
			lstrResult.append("Data de Nascimento: ");
			if ( mobjData.mdtDateOfBirth != null )
				lstrResult.append(mobjData.mdtDateOfBirth.toString().substring(0, 10));
			lstrResult.append(pstrLineBreak);

			lstrResult.append("Sexo: ");
			if ( mobjData.midSex != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Sex),
							mobjData.midSex);
					lstrResult.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					lstrResult.append("(Erro a obter o sexo do cliente.)");
				}
			}
			lstrResult.append(pstrLineBreak);

			lstrResult.append("Estado Civil: ");
			if ( mobjData.midMarital != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MaritalStatus),
							mobjData.midMarital);
					lstrResult.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					lstrResult.append("(Erro a obter o estado civil do cliente.)");
				}
			}
			lstrResult.append(pstrLineBreak);

			lstrResult.append("Profissão: ");
			if ( mobjData.midProfession != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Profession),
							mobjData.midProfession);
					lstrResult.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					lstrResult.append("(Erro a obter a profissão do cliente.)");
				}
			}
			lstrResult.append(pstrLineBreak);
		}

		if ( Constants.TypeID_Company.equals(mobjData.midType) )
		{
			lstrResult.append("CAE: ");
			if ( mobjData.midCAE != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CAE),
							mobjData.midCAE);
					lstrResult.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					lstrResult.append("(Erro a obter o CAE do cliente.)");
				}
			}
			lstrResult.append(pstrLineBreak);

			lstrResult.append("Obs. Actividade: ");
			if ( mobjData.mstrCAENotes != null )
				lstrResult.append(mobjData.mstrCAENotes);
			lstrResult.append(pstrLineBreak);

			lstrResult.append("N. Trabalhadores: ");
			if ( mobjData.midSize != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Size),
							mobjData.midSize);
					lstrResult.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					lstrResult.append("(Erro a obter o número de trabalhadores do cliente.)");
				}
			}
			lstrResult.append(pstrLineBreak);

			lstrResult.append("Facturação: ");
			if ( mobjData.midSales != null )
			{
				try
				{
					lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SalesVolume),
							mobjData.midSales);
					lstrResult.append((String)lobjAux.getAt(0));
				}
				catch (Throwable e)
				{
					lstrResult.append("(Erro a obter a facturação do cliente.)");
				}
			}
			lstrResult.append(pstrLineBreak);
		}

		lstrResult.append("Obervações: ");
		if ( mobjData.mstrNotes != null )
			lstrResult.append(mobjData.mstrNotes);
		lstrResult.append(pstrLineBreak);

		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.LongDesc(lstrResult, pstrLineBreak);
		if ( mobjData.mobjDocOps != null )
			mobjData.mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "N/A";
	}

	protected UUID OpID()
	{
		return Constants.OPID_CreateClient;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Client lobjAux;
		PNProcess lobjProcess;

		try
		{
			lobjAux = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjAux.setAt( 0, mobjData.mlngNumber);
			lobjAux.setAt( 1, mobjData.mstrName);
			lobjAux.setAt( 2, mobjData.mstrAddress1);
			lobjAux.setAt( 3, mobjData.mstrAddress2);
			lobjAux.setAt( 4, mobjData.midZipCode);
			lobjAux.setAt( 5, mobjData.mstrFiscal);
			lobjAux.setAt( 6, mobjData.midType);
			lobjAux.setAt( 7, mobjData.midSubtype);
			lobjAux.setAt( 8, mobjData.midManager);
			lobjAux.setAt( 9, mobjData.midMediator);
			lobjAux.setAt(10, mobjData.midProfile);
			lobjAux.setAt(11, mobjData.midGroup);
			lobjAux.setAt(12, mobjData.mstrBankingID);
			lobjAux.setAt(13, mobjData.mdtDateOfBirth);
			lobjAux.setAt(14, mobjData.midSex);
			lobjAux.setAt(15, mobjData.midMarital);
			lobjAux.setAt(16, mobjData.midProfession);
			lobjAux.setAt(17, mobjData.midCAE);
			lobjAux.setAt(18, mobjData.mstrCAENotes);
			lobjAux.setAt(19, mobjData.midSize);
			lobjAux.setAt(20, mobjData.midSales);
			lobjAux.setAt(21, mobjData.mstrNotes);
			lobjAux.SaveToDb(pdb);

			lobjProcess = (PNProcess)Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Jewel.Petri.Constants.ObjID_PNProcess), (UUID)null);
			lobjProcess.setAt(0, Constants.ProcID_Client);
			lobjProcess.setAt(1, lobjAux.getKey());
			lobjProcess.setAt(2, mobjData.midManager);
			lobjProcess.setAt(4, true);
			lobjProcess.SaveToDb(pdb);

			lobjAux.setAt(22, lobjProcess.getKey());
			lobjAux.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
