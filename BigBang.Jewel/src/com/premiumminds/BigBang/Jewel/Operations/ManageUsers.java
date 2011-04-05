package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Security.Password;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class ManageUsers
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public class UserData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrFullName;
		public String mstrUsername;
		public Password mobjPassword; //Filled only for the current user
		public UUID midProfile;
		public UUID midCostCenter;
		public String mstrEmail;
	}

	public UserData[] marrCreate;
	public UserData[] marrModify;
	public UserData[] marrDelete;

	public UUID[] marrNewIDs;

	public ManageUsers(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageUsers;
	}

	protected void Run()
		throws JewelPetriException
	{
		UUID lidUsers;
		MasterDB ldb;
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
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			if ( marrCreate != null )
			{
				marrNewIDs = new UUID[marrCreate.length];

				for ( i = 0; i < marrCreate.length; i++ )
				{
					if ( marrCreate[i].midProfile.equals(Constants.ProfID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode criar outros utilizadores Root.");

					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, (UUID)null);
					lobjAuxBase.setAt(0, marrCreate[i].mstrFullName);
					lobjAuxBase.setAt(1, marrCreate[i].mstrUsername);
					lobjAuxBase.setAt(2, marrCreate[i].mobjPassword);
					lobjAuxBase.setAt(3, marrCreate[i].midProfile);
					lobjAuxBase.SaveToDb(ldb);

					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAuxOuter.setAt(0, lobjAuxBase.getKey());
					lobjAuxOuter.setAt(1, marrCreate[i].mstrEmail);
					lobjAuxOuter.setAt(2, marrCreate[i].midCostCenter);
					lobjAuxOuter.SaveToDb(ldb);

					marrNewIDs[i] = lobjAuxOuter.getKey();
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					if ( marrModify[i].midProfile.equals(Constants.ProfID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode definir outros utilizadores Root.");
					if ( lobjAuxCurrent.getKey().equals(marrModify[i].mid) &&
							lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfID_Root) &&
							!marrModify[i].midProfile.equals(Constants.ProfID_Root) )
						throw new BigBangJewelException("Sem permissão: Não pode remover o seu próprio privilégio de Root.");

					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, (UUID)lobjAuxOuter.getAt(0));

					if ( lobjAuxBase.getProfile().getKey().equals(Constants.ProfID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode modificar outros utilizadores Root.");

					lobjAuxBase.setAt(0, marrModify[i].mstrFullName);
					lobjAuxBase.setAt(1, marrModify[i].mstrUsername);
					lobjAuxBase.setAt(2, marrModify[i].mobjPassword);
					lobjAuxBase.setAt(3, marrModify[i].midProfile);
					lobjAuxBase.SaveToDb(ldb);

					lobjAuxOuter.setAt(1, marrModify[i].mstrEmail);
					lobjAuxOuter.setAt(2, marrModify[i].midCostCenter);
					lobjAuxOuter.SaveToDb(ldb);
				}
			}

			if ( marrDelete != null )
			{
				lrefDecorations = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Decorations));
				lrefUsers = Entity.GetInstance(lidUsers);

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAuxOuter = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), marrDelete[i].mid);
					lobjAuxBase = (User)Engine.GetWorkInstance(lidUsers, (UUID)lobjAuxOuter.getAt(0));

					if ( lobjAuxBase.getKey().equals(Engine.getCurrentUser()) )
						throw new BigBangJewelException("Sem permissão: Não se pode apagar a si próprio.");
					if ( lobjAuxBase.getProfile().getKey().equals(Constants.ProfID_Root) &&
							!lobjAuxCurrent.getProfile().getKey().equals(Constants.ProfID_Root) )
						throw new BigBangJewelException("Sem permissão: Só um utilizador Root pode apagar outros utilizadores Root.");

					lrefDecorations.Delete(ldb, lobjAuxOuter.getKey());
					lrefUsers.Delete(ldb, lobjAuxBase.getKey());
				}
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
