package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class Conversation
	extends ProcessData
{
	public static class I
	{
		public static int SUBJECT          = 0;
		public static int TYPE             = 1;
		public static int PROCESS          = 2;
		public static int STARTDIRECTION   = 3;
		public static int PENDINGDIRECTION = 4;
		public static int DUEDATE          = 5;
	}

    public static Conversation GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Conversation)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Conversation), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
	}

	public UUID GetProcessID()
	{
		return (UUID)getAt(I.PROCESS);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(I.PROCESS, pidProcess);
	}

	public Message[] GetCurrentMessages()
		throws BigBangJewelException
	{
		MasterDB ldb;
		Message[] larrResult;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			larrResult = GetCurrentMessages(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrResult;
	}

	public Message[] GetCurrentMessages(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		ArrayList<Message> larrAux;
		ResultSet lrs;

		lrs = null;
		larrAux = new ArrayList<Message>();
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_Message));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {Message.I.OWNER}, new java.lang.Object[] {getKey()},
					new int[] {Message.I.NUMBER});
			while ( lrs.next() )
				larrAux.add(Message.GetInstance(getNameSpace(), lrs));
			lrs.close();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new Message[larrAux.size()]);
	}

	public UUID[] GetUsers()
		throws BigBangJewelException
	{
		MasterDB ldb;
		UUID[] larrResult;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			larrResult = GetUsers(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrResult;
	}

	public UUID[] GetUsers(SQLServer pdb)
		throws BigBangJewelException
	{
		Message[] larrMessages;
		HashSet<UUID> larrResult;
		MessageAddress[] larrAdds;
		int i, j;
		UUID lidUser;

		larrMessages = GetCurrentMessages();

		if ( larrMessages == null )
			return new UUID[] {Engine.getCurrentUser()};

		larrResult = new HashSet<UUID>();
		for ( i = larrMessages.length - 1; (larrResult.size() == 0) && (i >= 0) ; i-- )
		{
			larrAdds = larrMessages[i].GetAddresses(pdb);
			for ( j = 0; j < larrAdds.length; j++ )
			{
				if ( Constants.UsageID_ReplyTo.equals(larrAdds[j].getAt(MessageAddress.I.USAGE)) )
				{
					lidUser = (UUID)larrAdds[j].getAt(MessageAddress.I.USER);
					if ( lidUser != null )
						larrResult.add(lidUser);
				}
			}
		}

		if ( larrResult.size() == 0 )
			return new UUID[] {Engine.getCurrentUser()};

		return larrResult.toArray(new UUID[larrResult.size()]);
	}
}
