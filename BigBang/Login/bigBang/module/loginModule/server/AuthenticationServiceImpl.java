package bigBang.module.loginModule.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.EntityGUIDs;
import Jewel.Engine.Constants.Miscellaneous;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Extensions.User_Manager;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.NameSpace;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.Interfaces.INameSpace;
import Jewel.Engine.Security.Password;
import Jewel.Engine.SysObjects.JewelEngineException;
import bigBang.library.server.DocuShareServiceImpl;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.shared.LoginDomain;
import bigBang.module.loginModule.shared.LoginResponse;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class AuthenticationServiceImpl
	extends EngineImplementor
	implements AuthenticationService
{
	private static final long serialVersionUID = 1L;

	public LoginDomain[] getDomains()
		throws BigBangException
	{
		IEntity lrefNameSpace;
		ArrayList<LoginDomain> larrNames;
        MasterDB ldb;
        ResultSet lrs;
        INameSpace lrefNSpace;
        LoginDomain lobjAux;

        try
        {
			lrefNameSpace = Entity.GetInstance(EntityGUIDs.E_NameSpace);

			larrNames = new ArrayList<LoginDomain>();

	        ldb = new MasterDB();
        }
        catch (Throwable e)
        {
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
	        lrs = lrefNameSpace.SelectByMembers(ldb, new int[] {3}, new java.lang.Object[] {Constants.NSID_BigBang}, new int[] {0});
        }
        catch (Throwable e)
        {
        	try { ldb.Disconnect(); } catch (SQLException e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
	        while (lrs.next())
	        {
	            lrefNSpace = NameSpace.GetInstance(lrs);
	            lobjAux = new LoginDomain();
	            lobjAux.domainName = lrefNSpace.getName();
	            lobjAux.domainId = lrefNSpace.getKey().toString();
	            larrNames.add(lobjAux);
	        }
        }
        catch (Throwable e)
        {
        	try { lrs.close(); } catch (SQLException e1) {}
        	try { ldb.Disconnect(); } catch (SQLException e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
	        lrs.close();
        }
        catch (Throwable e)
        {
        	try { ldb.Disconnect(); } catch (SQLException e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
			ldb.Disconnect();
		}
        catch (Throwable e)
        {
        	throw new BigBangException(e.getMessage(), e);
		}

        return larrNames.toArray(new LoginDomain[larrNames.size()]);
	}

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

		/*if ( domain.equals("AMartins") )
			lidNSpace = Constants.NSID_AMartins;
		else */if ( domain.equals("CrediteEGS") )
			lidNSpace = Constants.NSID_CredEGS;
		else if ( domain.equals("Leiria") )
			lidNSpace = Constants.NSID_Leiria;
		else if ( domain.equals("Angola") )
			lidNSpace = Constants.NSID_Angola;
		else if ( domain.equals("Rede Comercial") )
			lidNSpace = Constants.NSID_RedeCom;
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
			lobjResult.currency = Utils.getCurrency();
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


		/*if ( ("AMartins").equals(domain) )
			lidNSpace = Constants.NSID_AMartins;
		else */if ( ("Credite-EGS").equals(domain) )
			lidNSpace = Constants.NSID_CredEGS;
		else if ( domain.equals("Leiria") )
			lidNSpace = Constants.NSID_Leiria;
		else if ( domain.equals("Angola") )
			lidNSpace = Constants.NSID_Angola;
		else if ( domain.equals("Rede Comercial") )
			lidNSpace = Constants.NSID_RedeCom;
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
				((Password)larrParams[1]).setWrong(password);
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
			lobjResult.currency = Utils.getCurrency();
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
			lobjResult.currency = Utils.getCurrency();
		}
		catch(Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}
}
