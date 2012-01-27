package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Security.Password;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.CostCenter;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class ManageUsers
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public class UserData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public UUID midDecorations;
		public String mstrFullName;
		public String mstrUsername;
		public Password mobjPassword;
		public UUID midProfile;
		public String mstrEmail;
		public UUID midCostCenter;
		public UserData mobjPrevValues;
	}

	public UserData[] marrCreate;
	public UserData[] marrModify;
	public UserData[] marrDelete;

	public ManageUsers(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_ManageUsers;
	}

	public String ShortDesc()
	{
		return "Gestão de Utilizadores"; 
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				lstrResult.append("Foi criado 1 utilizador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram criados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" utilizadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Utilizador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrCreate[i], pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				lstrResult.append("Foi modificado 1 utilizador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram modificados ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" utilizadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Utilizador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrModify[i], pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				lstrResult.append("Foi apagado 1 utilizador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" utilizadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Utilizador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
				}
			}
		}

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		UUID lidUsers;
		int i;
		UserDecoration lobjAuxOuter;
		User lobjAuxBase;
		User lobjAuxCurrent;
		Entity lrefDecorations;
		Entity lrefUsers;

		try
		{
        	lidUsers = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_User);
			lobjAuxCurrent = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());

			if ( marrCreate != null )
			{
				for ( i = 0; i < marrCreate.length; i++ )
				{
					if ( marrCreate[i].midProfile.equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode criar outros utilizadores Root.");

					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, (UUID)null);
					lobjAuxBase.setAt(0, marrCreate[i].mstrFullName);
					lobjAuxBase.setAt(1, marrCreate[i].mstrUsername);
					lobjAuxBase.setAt(2, marrCreate[i].mobjPassword);
					lobjAuxBase.setAt(3, marrCreate[i].midProfile);
					lobjAuxBase.SaveToDb(pdb);

					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAuxOuter.setAt(0, lobjAuxBase.getKey());
					lobjAuxOuter.setAt(1, marrCreate[i].mstrEmail);
					lobjAuxOuter.setAt(2, marrCreate[i].midCostCenter);
					lobjAuxOuter.SaveToDb(pdb);

					marrCreate[i].mid = lobjAuxBase.getKey();
					marrCreate[i].midDecorations = lobjAuxOuter.getKey();
					marrCreate[i].mobjPassword = null;
					marrCreate[i].mobjPrevValues = null;
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					if ( marrModify[i].midProfile.equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode definir outros utilizadores Root.");
					if ( lobjAuxCurrent.getKey().equals(marrModify[i].mid) &&
							lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) &&
							!marrModify[i].midProfile.equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Não pode remover o seu próprio privilégio de Root.");

					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].midDecorations);
					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, marrModify[i].mid);

					if ( lobjAuxBase.getProfile().getKey().equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode modificar outros utilizadores Root.");

					marrModify[i].mobjPrevValues = new UserData();
					marrModify[i].mobjPrevValues.mid = lobjAuxBase.getKey();
					marrModify[i].mobjPrevValues.midDecorations = lobjAuxOuter.getKey();
					marrModify[i].mobjPrevValues.mstrFullName = (String)lobjAuxBase.getAt(0);
					marrModify[i].mobjPrevValues.mstrUsername = (String)lobjAuxBase.getAt(1);
					marrModify[i].mobjPrevValues.mobjPassword = null;
					marrModify[i].mobjPrevValues.midProfile = (UUID)lobjAuxBase.getAt(3);
					marrModify[i].mobjPrevValues.mstrEmail = (String)lobjAuxOuter.getAt(1);
					marrModify[i].mobjPrevValues.midCostCenter = (UUID)lobjAuxOuter.getAt(2);
					marrModify[i].mobjPrevValues.mobjPrevValues = null;

					lobjAuxBase.setAt(0, marrModify[i].mstrFullName);
					lobjAuxBase.setAt(1, marrModify[i].mstrUsername);
					lobjAuxBase.setAt(2, marrModify[i].mobjPassword);
					lobjAuxBase.setAt(3, marrModify[i].midProfile);
					lobjAuxBase.SaveToDb(pdb);

					lobjAuxOuter.setAt(1, marrModify[i].mstrEmail);
					lobjAuxOuter.setAt(2, marrModify[i].midCostCenter);
					lobjAuxOuter.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				lrefDecorations = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Decorations));
				lrefUsers = Entity.GetInstance(lidUsers);

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), marrDelete[i].midDecorations);
					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, marrDelete[i].mid);

					if ( lobjAuxBase.getKey().equals(Engine.getCurrentUser()) )
						throw new BigBangJewelException("Sem permissão: Não se pode apagar a si próprio.");
					if ( lobjAuxBase.getProfile().getKey().equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode apagar outros utilizadores Root.");

					marrDelete[i].mstrFullName = (String)lobjAuxBase.getAt(0);
					marrDelete[i].mstrUsername = (String)lobjAuxBase.getAt(1);
					marrDelete[i].mobjPassword = null;
					marrDelete[i].midProfile = (UUID)lobjAuxBase.getAt(3);
					marrDelete[i].mstrEmail = (String)lobjAuxOuter.getAt(1);
					marrDelete[i].midCostCenter = (UUID)lobjAuxOuter.getAt(2);
					marrDelete[i].mobjPrevValues = null;

					lrefDecorations.Delete(pdb, lobjAuxOuter.getKey());
					lrefUsers.Delete(pdb, lobjAuxBase.getKey());
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
				lstrResult.append("O utilizador criado será apagado.");
			else
				lstrResult.append("Os utilizadores criados serão apagados.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			lstrResult.append("Serão repostos os valores anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModify.length == 1 )
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Utilizador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrModify[i].mobjPrevValues, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
				lstrResult.append("O utilizador apagado será reposto.");
			else
				lstrResult.append("Os utilizadores apagados serão repostos.");
			lstrResult.append(pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				lstrResult.append("Foi apagado 1 utilizador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" utilizadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Utilizador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrCreate[i], pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				lstrResult.append("Foram repostos os valores de 1 utilizador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos os valores de ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" utilizadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Utilizador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrModify[i], pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				lstrResult.append("Foi reposto 1 utilizador:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" utilizadores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Utilizador ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
				}
			}
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		UUID lidUsers;
		int i;
		UserDecoration lobjAuxOuter;
		User lobjAuxBase;
		User lobjAuxCurrent;
		Entity lrefDecorations;
		Entity lrefUsers;

		try
		{
        	lidUsers = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_User);
			lobjAuxCurrent = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());

			if ( marrCreate != null )
			{
				lrefDecorations = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Decorations));
				lrefUsers = Entity.GetInstance(lidUsers);

				for ( i = 0; i < marrCreate.length; i++ )
				{
					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), marrCreate[i].mid);
					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, (UUID)lobjAuxOuter.getAt(0));

					if ( lobjAuxBase.getKey().equals(Engine.getCurrentUser()) )
						throw new BigBangJewelException("Sem permissão: Não pode desfazer a sua própria criação.");
					if ( lobjAuxBase.getProfile().getKey().equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode desfazer a criação de outros utilizadores Root.");

					lrefDecorations.Delete(pdb, lobjAuxOuter.getKey());
					lrefUsers.Delete(pdb, lobjAuxBase.getKey());
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					if ( marrModify[i].mobjPrevValues.midProfile.equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode repôr privilégio de Root a outros utilizadores.");
					if ( lobjAuxCurrent.getKey().equals(marrModify[i].mid) &&
							lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) &&
							!marrModify[i].mobjPrevValues.midProfile.equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Não pode desfazer alterações que lhe deram o seu próprio privilégio de Root.");

					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, (UUID)lobjAuxOuter.getAt(0));

					if ( lobjAuxBase.getProfile().getKey().equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode desfazer alterações a outros utilizadores Root.");

					lobjAuxBase.setAt(0, marrModify[i].mobjPrevValues.mstrFullName);
					lobjAuxBase.setAt(1, marrModify[i].mobjPrevValues.mstrUsername);
					lobjAuxBase.setAt(2, marrModify[i].mobjPrevValues.mobjPassword);
					lobjAuxBase.setAt(3, marrModify[i].mobjPrevValues.midProfile);
					lobjAuxBase.SaveToDb(pdb);

					lobjAuxOuter.setAt(1, marrModify[i].mobjPrevValues.mstrEmail);
					lobjAuxOuter.setAt(2, marrModify[i].mobjPrevValues.midCostCenter);
					lobjAuxOuter.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				for ( i = 0; i < marrDelete.length; i++ )
				{
					if ( marrDelete[i].midProfile.equals(Constants.ProfileID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfileID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode repôr outros utilizadores Root.");

					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, (UUID)null);
					lobjAuxBase.setAt(0, marrDelete[i].mstrFullName);
					lobjAuxBase.setAt(1, marrDelete[i].mstrUsername);
					lobjAuxBase.setAt(2, marrDelete[i].mobjPassword);
					lobjAuxBase.setAt(3, marrDelete[i].midProfile);
					lobjAuxBase.SaveToDb(pdb);

					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAuxOuter.setAt(0, lobjAuxBase.getKey());
					lobjAuxOuter.setAt(1, marrDelete[i].mstrEmail);
					lobjAuxOuter.setAt(2, marrDelete[i].midCostCenter);
					lobjAuxOuter.SaveToDb(pdb);
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		int llngCreates, llngModifies, llngDeletes;
		int i;

		llngCreates = ( marrCreate == null ? 0 : marrCreate.length );
		llngModifies = ( marrModify == null ? 0 : marrModify.length );
		llngDeletes = ( marrDelete == null ? 0 : marrDelete.length );

		if ( llngCreates + llngModifies + llngDeletes == 0 )
			return new UndoSet[0];

		larrResult = new UndoSet[1];
		larrResult[0] = new UndoSet();
		larrResult[0].midType = ObjectGUIDs.O_User;

		larrResult[0].marrDeleted = new UUID[llngCreates];
		for ( i = 0; i < llngCreates; i ++ )
			larrResult[0].marrDeleted[i] = marrCreate[i].mid;

		larrResult[0].marrChanged = new UUID[llngModifies];
		for ( i = 0; i < llngModifies; i ++ )
			larrResult[0].marrChanged[i] = marrModify[i].mid;

		larrResult[0].marrCreated = new UUID[llngDeletes];
		for ( i = 0; i < llngDeletes; i ++ )
			larrResult[0].marrCreated[i] = marrDelete[i].mid;

		return larrResult;
	}

	private void Describe(StringBuilder pstrString, UserData pobjData, String pstrLineBreak)
	{
		ObjectBase lobjProfile;
		CostCenter lobjCostCenter;

		pstrString.append("Nome: ");
		pstrString.append(pobjData.mstrFullName);
		pstrString.append(pstrLineBreak);
		pstrString.append("Username: ");
		pstrString.append(pobjData.mstrUsername);
		pstrString.append(pstrLineBreak);
		pstrString.append("Password: *****");
		pstrString.append(pstrLineBreak);
		pstrString.append("Perfil: ");

		try
		{
			lobjProfile = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_Profile), pobjData.midProfile);
			pstrString.append((String)lobjProfile.getAt(0));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o perfil.)");
		}
		pstrString.append(pstrLineBreak);

		pstrString.append("Email: ");
		pstrString.append(pobjData.mstrEmail);
		pstrString.append(pstrLineBreak);
		pstrString.append("Centro de Custo: ");

		try
		{
			lobjCostCenter = CostCenter.GetInstance(Engine.getCurrentNameSpace(), pobjData.midCostCenter);
			pstrString.append((String)lobjCostCenter.getAt(1));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o centro de custo.)");
		}
		pstrString.append(pstrLineBreak);
	}
}
