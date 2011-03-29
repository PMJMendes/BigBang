package bigBang.module.loginModule.server;

import java.sql.*;
import java.util.*;

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

public class AuthenticationServiceImpl
	extends EngineImplementor
	implements AuthenticationService
{
	private static final long serialVersionUID = 1L;

	public String login(String domain)
		throws BigBangException
	{
		String lstrUsername;
		UUID lidNSpace;
        IEntity lrefUser;
        MasterDB ldb;
        ResultSet lrs;
        int[] larrMembers;
        java.lang.Object[] larrParams;
        UUID lidUser;

        lstrUsername = getThreadLocalRequest().getRemoteUser();
        if ( lstrUsername == null )
        	return null;
        lidUser = null;

        if ( domain.equals("AMartins") )
        	lidNSpace = Constants.WSpace_AMartins;
        else if ( domain.equals("CrediteEGS") )
        	lidNSpace = Constants.WSpace_CredEGS;
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
	        lrs = lrefUser.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);

	        if (lrs.next())
	        {
	            lidUser = UUID.fromString(lrs.getString(1));
	            if (lrs.next())
	                throw new BigBangException("Unexpected: Username is not unique!");
	        }

	        lrs.close();
	        ldb.Disconnect();

	        if ( lidUser == null )
	        	return null;

	        getSession().setAttribute("UserID", lidUser);
	        getSession().setAttribute("UserNSpace", lidNSpace);

	        NameSpace.GetInstance(lidNSpace).DoLogin(lidUser);

	        return User.GetInstance(lidNSpace, lidUser).getDisplayName();
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

	public String login(String username, String password, String domain)
		throws BigBangException
	{
		UUID lidNSpace;
        IEntity lrefUser;
        MasterDB ldb;
        ResultSet lrs;
        int[] larrMembers;
        java.lang.Object[] larrParams;
        UUID lidUser;

        lidUser = null;

        if ( domain.equals("AMartins") )
        	lidNSpace = Constants.WSpace_AMartins;
        else if ( domain.equals("CrediteEGS") )
        	lidNSpace = Constants.WSpace_CredEGS;
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

	        ldb = new MasterDB();
	        lrs = lrefUser.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);

	        if (lrs.next())
	        {
	            lidUser = UUID.fromString(lrs.getString(1));
	            if (lrs.next())
	                throw new BigBangException("Unexpected: Username is not unique!");
	        }
	        else
	            throw new BigBangException("Invalid Username or Password!");

	        lrs.close();
	        ldb.Disconnect();

	        if ( larrParams[1] == null )
	            throw new BigBangException("User restricted to integrated logon!");

	        getSession().setAttribute("UserID", lidUser);
	        getSession().setAttribute("UserNSpace", lidNSpace);

	        NameSpace.GetInstance(lidNSpace).DoLogin(lidUser);

	        return User.GetInstance(lidNSpace, lidUser).getDisplayName();
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
		getSession().invalidate();
		return null;
	}

	public String changePassword(String oldPassword, String newPassword)
		throws BigBangException, SessionExpiredException
	{
		java.lang.Object[] larrParams;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrParams = new java.lang.Object[3];

			larrParams[0] = new Password(oldPassword, false);
			larrParams[1] = new Password(newPassword, false);
			larrParams[2] = larrParams[1];

			User_Manager.ChangePassword(Engine.getCurrentNameSpace(), larrParams);
		}
		catch (JewelEngineException e)
		{
			if (e.getMessage().equals("Password changed."))
				return e.getMessage();

        	throw new BigBangException(e.getMessage(), e);
		}
        catch (Throwable e)
        {
        	throw new BigBangException(e.getMessage(), e);
        }

		return null;
	}
}
