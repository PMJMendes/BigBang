package bigBang.module.generalSystemModule.server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Security.Password;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.UserService;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.Operations.ManageUsers;

public class UserServiceImpl
	extends EngineImplementor
	implements UserService
{
	private static final long serialVersionUID = 1L;

	public User[] getUsers()
		throws SessionExpiredException, BigBangException
	{
		UUID lidUsers;
        MasterDB ldb;
        ResultSet lrsUsers;
		ArrayList<User> larrAux;
		UserDecoration lobjUser;
		User lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<User>();

		try
		{
        	lidUsers = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsUsers = Entity.GetInstance(lidUsers).SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsUsers.next())
	        {
	        	lobjUser = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrsUsers);
	        	lobjTmp = new User();
	        	lobjTmp.id = lobjUser.getKey().toString();
	        	lobjTmp.name = lobjUser.getBaseUser().getDisplayName();
	        	lobjTmp.username = lobjUser.getBaseUser().getUserName();
	        	lobjTmp.password = null; //JMMM: No way!
	        	lobjTmp.profileId = lobjUser.getBaseUser().getProfile().getKey().toString();
	        	lobjTmp.costCenterId = ((UUID)lobjUser.getAt(2)).toString();
	        	lobjTmp.email = (String)lobjUser.getAt(3);
	        	larrAux.add(lobjTmp);
	        }
        }
        catch (Throwable e)
        {
			try { lrsUsers.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsUsers.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return larrAux.toArray(new User[larrAux.size()]);
	}

//	public User getUser(String id)
//		throws SessionExpiredException, BigBangException
//	{
//		UUID lidUsers;
//		UserDecoration lobjUser;
//		User lobjTmp;

//		if ( Engine.getCurrentUser() == null )
//			throw new SessionExpiredException();

//		try
//		{
//			lidUsers = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations);
//			lobjUser = (UserDecoration)Engine.GetWorkInstance(lidUsers, UUID.fromString(id));
//			lobjTmp = new User();
//			lobjTmp.id = lobjUser.getKey().toString();
//			lobjTmp.name = lobjUser.getBaseUser().getDisplayName();
//			lobjTmp.username = lobjUser.getBaseUser().getUserName();
//			lobjTmp.password = null; //JMMM: No way!
//			lobjTmp.profileId = lobjUser.getBaseUser().getProfile().getKey().toString();
//			lobjTmp.costCenterId = ((UUID)lobjUser.getAt(2)).toString();
//			lobjTmp.email = (String)lobjUser.getAt(3);
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangException(e.getMessage(), e);
//		}

//		return lobjTmp;
//	}

	public User saveUser(User user)
		throws SessionExpiredException, BigBangException
	{
		ManageUsers lopMU;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMU = new ManageUsers(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMU.marrModify = new ManageUsers.UserData[1];
			lopMU.marrModify[0] = lopMU.new UserData();
			lopMU.marrModify[0].mid = UUID.fromString(user.id);
			lopMU.marrModify[0].mstrFullName = user.name;
			lopMU.marrModify[0].mstrUsername = user.username;
			lopMU.marrModify[0].mobjPassword = new Password(user.password, false);
			lopMU.marrModify[0].midProfile = UUID.fromString(user.profileId);
			lopMU.marrModify[0].midCostCenter = UUID.fromString(user.costCenterId);
			lopMU.marrModify[0].mstrEmail = user.email;
			lopMU.marrCreate = null;
			lopMU.marrDelete = null;
			lopMU.marrNewIDs = null;

			lopMU.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		user.id = lopMU.marrModify[0].mid.toString();
		user.name = lopMU.marrModify[0].mstrFullName;
		user.username = lopMU.marrModify[0].mstrUsername;
		user.password = null;
		user.profileId = lopMU.marrModify[0].midProfile.toString();
		user.costCenterId = lopMU.marrModify[0].midCostCenter.toString();
		user.email = lopMU.marrModify[0].mstrEmail;

		return user;
	}

	public User addUser(User user)
		throws SessionExpiredException, BigBangException
	{
		ManageUsers lopMU;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMU = new ManageUsers(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMU.marrCreate = new ManageUsers.UserData[1];
			lopMU.marrCreate[0] = lopMU.new UserData();
			lopMU.marrCreate[0].mid = null;
			lopMU.marrCreate[0].mstrFullName = user.name;
			lopMU.marrCreate[0].mstrUsername = user.username;
			lopMU.marrCreate[0].mobjPassword = new Password(user.password, false);
			lopMU.marrCreate[0].midProfile = UUID.fromString(user.profileId);
			lopMU.marrCreate[0].midCostCenter = UUID.fromString(user.costCenterId);
			lopMU.marrCreate[0].mstrEmail = user.email;
			lopMU.marrModify = null;
			lopMU.marrDelete = null;
			lopMU.marrNewIDs = null;

			lopMU.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		user.id = lopMU.marrNewIDs[0].toString();
		user.name = lopMU.marrCreate[0].mstrFullName;
		user.username = lopMU.marrCreate[0].mstrUsername;
		user.password = null;
		user.profileId = lopMU.marrCreate[0].midProfile.toString();
		user.costCenterId = lopMU.marrCreate[0].midCostCenter.toString();
		user.email = lopMU.marrCreate[0].mstrEmail;

		return user;
	}

	public void deleteUser(String id)
		throws SessionExpiredException, BigBangException
	{
		ManageUsers lopMU;

			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

			try
			{
				lopMU = new ManageUsers(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
				lopMU.marrDelete = new ManageUsers.UserData[1];
				lopMU.marrDelete[0] = lopMU.new UserData();
				lopMU.marrDelete[0].mid = UUID.fromString(id);
				lopMU.marrDelete[0].mstrFullName = null;
				lopMU.marrDelete[0].mstrUsername = null;
				lopMU.marrDelete[0].mobjPassword = null;
				lopMU.marrDelete[0].midProfile = null;
				lopMU.marrDelete[0].midCostCenter = null;
				lopMU.marrDelete[0].mstrEmail = null;
				lopMU.marrCreate = null;
				lopMU.marrModify = null;
				lopMU.marrNewIDs = null;

				lopMU.Execute();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
	}

//	public User[] getUsersForCostCenterAssignment()
//		throws SessionExpiredException, BigBangException
//	{
//		return new User[0];
//	}

	public UserProfile[] getUserProfiles()
		throws SessionExpiredException, BigBangException
	{
		UUID lidProfiles;
        MasterDB ldb;
        ResultSet lrsProfiles;
		ArrayList<UserProfile> larrAux;
		ObjectBase lobjAux;
		UserProfile lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<UserProfile>();

		try
		{
        	lidProfiles = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_Profile);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsProfiles = Entity.GetInstance(lidProfiles).SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsProfiles.next())
	        {
	        	lobjAux = Engine.GetWorkInstance(lidProfiles, lrsProfiles);
	        	lobjTmp = new UserProfile();
	        	lobjTmp.id = lobjAux.getKey().toString();
	        	lobjTmp.name = (String)lobjAux.getAt(0);
	        	larrAux.add(lobjTmp);
	        }

        }
        catch (Throwable e)
        {
			try { lrsProfiles.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsProfiles.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return larrAux.toArray(new UserProfile[larrAux.size()]);
	}
}
