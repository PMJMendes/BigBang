package bigBang.module.loginModule.server;

import java.sql.*;
import java.util.*;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.*;
import Jewel.Engine.Constants.*;
import Jewel.Engine.DataAccess.*;
import Jewel.Engine.Extensions.*;
import Jewel.Engine.Implementation.*;
import Jewel.Engine.Interfaces.*;
import Jewel.Engine.Security.*;
import Jewel.Engine.SysObjects.*;
import bigBang.library.server.*;
import bigBang.library.shared.*;
import bigBang.module.loginModule.interfaces.*;
import bigBang.module.loginModule.shared.LoginResponse;

public class AuthenticationServiceImpl
	extends EngineImplementor
	implements AuthenticationService
{
	private static final long serialVersionUID = 1L;

	public LoginResponse login(String domain)
		throws BigBangException
	{
		String lstrUsername;
		UUID lidNSpace;
		IEntity lrefUser;
		MasterDB ldb;
		ResultSet lrs;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		User lobjUser;
		NameSpace lobjNSpace;
		LoginResponse lobjResult;

		lstrUsername = getThreadLocalRequest().getRemoteUser();
		if ( lstrUsername == null )
			return null;
		lobjUser = null;

		if ( domain.equals("AMartins") )
			lidNSpace = Constants.NSID_AMartins;
		else if ( domain.equals("CrediteEGS") )
			lidNSpace = Constants.NSID_CredEGS;
		else
			throw new BigBangException("Invalid login domain.");

		larrMembers = new int[1];
		larrMembers[0] = Miscellaneous.Username_In_User;
		larrParams = new java.lang.Object[1];
		larrParams[0] = "!" + lstrUsername;

		try
		{
			lrefUser = Entity.GetInstance(Engine.FindEntity(lidNSpace, ObjectGUIDs.O_User));

			ldb = new MasterDB();
			lrs = lrefUser.SelectByMembers(ldb, larrMembers, larrParams, null);

			if (lrs.next())
			{
				lobjUser = User.GetInstance(lidNSpace, lrs);
				if (lrs.next())
					throw new BigBangException("Unexpected: Username is not unique!");
			}

			lrs.close();
			ldb.Disconnect();

			if ( lobjUser == null )
				return null;

			getSession().setAttribute("UserID", lobjUser.getKey());
			getSession().setAttribute("UserNSpace", lidNSpace);

			lobjNSpace = NameSpace.GetInstance(lidNSpace);
			
			lobjNSpace.DoLogin(lobjUser.getKey(), false);

			lobjResult = new LoginResponse();
			lobjResult.userId = lobjUser.getKey().toString();
			lobjResult.userName = lobjUser.getDisplayName();
			lobjResult.domain = lobjNSpace.getLabel();
			lobjResult.isSU = Constants.ProfileID_Root.equals(lobjUser.getProfile().getKey());
			lobjResult.isAgent = Constants.ProfileID_Agent.equals(lobjUser.getProfile().getKey());
			return lobjResult;
		}
		catch (BigBangException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public LoginResponse login(String username, String password, String domain)
		throws BigBangException
	{
		UUID lidNSpace;
		IEntity lrefUser;
		MasterDB ldb;
		ResultSet lrs;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		User lobjUser;
		NameSpace lobjNSpace;
		LoginResponse lobjResult;
		boolean lbSave;

		lobjUser = null;

		if ( domain.equals("AMartins") )
			lidNSpace = Constants.NSID_AMartins;
		else if ( domain.equals("CrediteEGS") )
			lidNSpace = Constants.NSID_CredEGS;
		else
			throw new BigBangException("Invalid login domain.");

		larrMembers = new int[2];
		larrMembers[0] = Miscellaneous.Username_In_User;
		larrMembers[1] = Miscellaneous.Password_In_User;
		larrParams = new java.lang.Object[2];
		larrParams[0] = "!" + username;

		try
		{
			if ((password == null) || (password.equals("")))
				larrParams[1] = null;
			else
				larrParams[1] = new Password(password, false);

			lrefUser = Entity.GetInstance(Engine.FindEntity(lidNSpace, ObjectGUIDs.O_User));

			lobjUser = null;
			lbSave = false;

			ldb = new MasterDB();

			lrs = lrefUser.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
			if (lrs.next())
			{
				lobjUser = User.GetInstance(lidNSpace, lrs);
				if (lrs.next())
					throw new BigBangException("Unexpected: Username is not unique!");
			}
			lrs.close();

			if ( (lobjUser == null) && (larrParams[1] != null) )
			{
				((Password)larrParams[1]).setShort(password);
				lrs = lrefUser.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
				if (lrs.next())
				{
					lobjUser = (User)Engine.GetWorkInstance(Engine.FindEntity(lidNSpace, ObjectGUIDs.O_User), lrs);
					if (lrs.next())
						throw new BigBangException("Unexpected: Username is not unique!");
					lbSave = true;
				}
				lrs.close();
				if ( lbSave )
				{
					lobjUser.setAt(Jewel.Engine.Constants.Miscellaneous.Password_In_User, new Password(password, false));
					lobjUser.SaveToDb(ldb);
				}
			}

			ldb.Disconnect();

			if ( lobjUser == null )
				throw new BigBangException("Invalid Username or Password!");

			if ( larrParams[1] == null )
				throw new BigBangException("User restricted to integrated logon!");

			getSession().setAttribute("UserID", lobjUser.getKey());
			getSession().setAttribute("UserNSpace", lidNSpace);

			lobjNSpace = NameSpace.GetInstance(lidNSpace);
			
			lobjNSpace.DoLogin(lobjUser.getKey(), false);

			lobjResult = new LoginResponse();
			lobjResult.userId = lobjUser.getKey().toString();
			lobjResult.userName = lobjUser.getDisplayName();
			lobjResult.domain = lobjNSpace.getLabel();
			lobjResult.isSU = Constants.ProfileID_Root.equals(lobjUser.getProfile().getKey());
			lobjResult.isAgent = Constants.ProfileID_Agent.equals(lobjUser.getProfile().getKey());
			return lobjResult;
		}
		catch (BigBangException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
			}

	public String logout()
		throws BigBangException
	{
		DocuShareServiceImpl.LogOff();
		getSession().invalidate();
		return null;
	}

	public String changePassword(String oldPassword, String newPassword)
		throws BigBangException, SessionExpiredException
	{
		java.lang.Object[] larrParams;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( "".equals(oldPassword) )
			oldPassword = null;
		if ( "".equals(newPassword) )
			newPassword = null;

		try
		{
			larrParams = new java.lang.Object[3];

			larrParams[0] = (oldPassword == null ? null : new Password(oldPassword, false));
			larrParams[1] = (newPassword == null ? null : new Password(newPassword, false));
			larrParams[2] = larrParams[1];

			User_Manager.ChangePassword(Engine.getCurrentNameSpace(), larrParams);
		}
		catch (JewelEngineException e)
		{
			if ("Password changed.".equals(e.getMessage()))
				return "Password alterada com sucesso.";

			throw new BigBangException(e.getMessage(), e);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return null;
	}

	public LoginResponse getCurrentLoginData()
		throws BigBangException
	{
		UUID lidUser;
		User lobjUser;
		UUID lidNSpace;
		LoginResponse lobjResult;

		lidUser = Engine.getCurrentUser();
		if ( lidUser == null )
			return null;

		lidNSpace = Engine.getCurrentNameSpace();
		if ( lidNSpace == null )
			throw new BigBangException("Unexpected: No Name Space in session.");

		lobjResult = new LoginResponse();
		try
		{
			lobjUser = User.GetInstance(lidNSpace, lidUser);
			lobjResult.userId = lidUser.toString();
			lobjResult.userName = lobjUser.getDisplayName();
			lobjResult.domain = NameSpace.GetInstance(lidNSpace).getLabel();
			lobjResult.isSU = Constants.ProfileID_Root.equals(lobjUser.getProfile().getKey());
			lobjResult.isAgent = Constants.ProfileID_Agent.equals(lobjUser.getProfile().getKey());
		}
		catch(Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}
}
