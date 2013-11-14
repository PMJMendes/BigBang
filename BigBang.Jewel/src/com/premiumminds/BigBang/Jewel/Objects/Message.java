package com.premiumminds.BigBang.Jewel.Objects;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class Message
	extends ObjectBase
{
	public static class I
	{
		public static int SUBJECT   = 0;
		public static int OWNER     = 1;
		public static int NUMBER    = 2;
		public static int DIRECTION = 3;
		public static int ISEMAIL   = 4;
		public static int DATE      = 5;
		public static int TEXT      = 6;
		public static int EMAILID   = 7;
	}

    public static Message GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Message)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Message), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Message GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (Message)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Message), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private MessageAddress[] marrAddresses;
	private MessageAttachment[] marrAttachments;

	public void Initialize()
		throws JewelEngineException
	{
		MasterDB ldb;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}

		try
		{
			GetAddresses(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelEngineException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

	public MessageAddress[] GetAddresses()
		throws BigBangJewelException
	{
		MasterDB ldb;

		if ( marrAddresses == null )
		{
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
				GetAddresses(ldb);
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
		}

		return marrAddresses;
	}

	public MessageAddress[] GetAddresses(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		ArrayList<MessageAddress> larrAux;
		ResultSet lrs;

		lrs = null;
		if ( marrAddresses == null )
		{
			larrAux = new ArrayList<MessageAddress>();
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_MessageAddress));
				lrs = lrefAux.SelectByMembers(pdb, new int[] {MessageAddress.I.OWNER}, new java.lang.Object[] {getKey()}, new int[] {2, 0});
				while ( lrs.next() )
					larrAux.add(MessageAddress.GetInstance(getNameSpace(), lrs));
				lrs.close();
			}
			catch (Throwable e)
			{
				if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
			marrAddresses = larrAux.toArray(new MessageAddress[larrAux.size()]);
		}

		return marrAddresses;
	}

	public MessageAttachment[] GetAttachments()
		throws BigBangJewelException
	{
		MasterDB ldb;

		if ( marrAttachments == null )
		{
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
				GetAttachments(ldb);
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
		}

		return marrAttachments;
	}

	public MessageAttachment[] GetAttachments(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		ArrayList<MessageAttachment> larrAux;
		ResultSet lrs;

		lrs = null;
		if ( marrAttachments == null )
		{
			larrAux = new ArrayList<MessageAttachment>();
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_MessageAttachment));
				lrs = lrefAux.SelectByMembers(pdb, new int[] {MessageAttachment.I.OWNER}, new java.lang.Object[] {getKey()}, new int[0]);
				while ( lrs.next() )
					larrAux.add(MessageAttachment.GetInstance(getNameSpace(), lrs));
				lrs.close();
			}
			catch (Throwable e)
			{
				if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
			marrAttachments = larrAux.toArray(new MessageAttachment[larrAux.size()]);
		}

		return marrAttachments;
	}

	public void setText(String pstrText)
		throws BigBangJewelException
	{
		byte[] larrAux;
		FileXfer laux;

		try
		{
			if ( pstrText == null )
			{
				setAt(I.TEXT, (byte[])null);
				return;
			}

			larrAux = pstrText.getBytes();
			laux = new FileXfer(larrAux.length, "text/plain", "body.txt", new ByteArrayInputStream(larrAux));
			setAt(I.TEXT, laux.GetVarData());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public String getText()
	{
		FileXfer laux;

		if ( getAt(I.TEXT) == null )
			return null;

    	if ( getAt(I.TEXT) instanceof FileXfer )
    		laux = (FileXfer)getAt(I.TEXT);
    	else
        	laux = new FileXfer((byte[])getAt(I.TEXT));

    	return new String(laux.getData());
	}

	public void InitNew(UUID[] parrUserIDs, UUID[] parrContactInfoIDs, String[] parrCCs, String[] parrBCCs, SQLServer pdb)
		throws BigBangJewelException
	{
//		ArrayList<MessageAddress> larrAux;
//		MessageAddress lobjAux;
//		IEntity lrefDecos;
//		int i;
//        ResultSet lrs;
//		String lstrAddr;
//
//		GetAddresses(pdb);
//		if ( (marrAddresses != null) && (marrAddresses.length > 0) )
//			throw new BigBangJewelException("Erro: Não pode redefinir os endereços de pedidos pré-existentes.");
//		if ( getKey() == null )
//			throw new BigBangJewelException("Erro: Não pode definir o conjunto de endereços para um pedido antes de gravar.");
//		if ( parrUserIDs.length == 0 )
//			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de utilizadores para um novo pedido.");
//		if ( parrContactInfoIDs.length == 0 )
//			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de contactos para um novo pedido.");
//
//		larrAux = new ArrayList<MessageAddress>();
//
//		lrs = null;
//		try
//		{
//			lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
//			for ( i = 0; i < parrUserIDs.length; i++ )
//			{
//				lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {parrUserIDs[i]}, new int[0]);
//			    if (lrs.next())
//			    	lstrAddr = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrs).getAt(1);
//			    else
//			    	lstrAddr = null;
//			    lrs.close();
//
//				lobjAux = MessageAddress.GetInstance(getNameSpace(), (UUID)null);
//				lobjAux.setAt(0, lstrAddr);
//				lobjAux.setAt(1, getKey());
//				lobjAux.setAt(2, Constants.UsageID_ReplyTo);
//				lobjAux.setAt(3, parrUserIDs[i]);
//				lobjAux.setAt(4, (UUID)null);
//				lobjAux.SaveToDb(pdb);
//
//				larrAux.add(lobjAux);
//			}
//
//			for ( i = 0; i < parrContactInfoIDs.length; i++ )
//			{
//				lstrAddr = (String)ContactInfo.GetInstance(getNameSpace(), parrContactInfoIDs[i]).getAt(2);
//
//				lobjAux = MessageAddress.GetInstance(getNameSpace(), (UUID)null);
//				lobjAux.setAt(0, lstrAddr);
//				lobjAux.setAt(1, getKey());
//				lobjAux.setAt(2, Constants.UsageID_To);
//				lobjAux.setAt(3, (UUID)null);
//				lobjAux.setAt(4, parrContactInfoIDs[i]);
//				lobjAux.SaveToDb(pdb);
//
//				larrAux.add(lobjAux);
//			}
//
//			if ( parrCCs != null )
//			{
//				for ( i = 0; i < parrCCs.length; i++ )
//				{
//					lobjAux = MessageAddress.GetInstance(getNameSpace(), (UUID)null);
//					lobjAux.setAt(0, parrCCs[i]);
//					lobjAux.setAt(1, getKey());
//					lobjAux.setAt(2, Constants.UsageID_CC);
//					lobjAux.setAt(3, (UUID)null);
//					lobjAux.setAt(4, (UUID)null);
//					lobjAux.SaveToDb(pdb);
//
//					larrAux.add(lobjAux);
//				}
//			}
//
//			if ( parrBCCs != null )
//			{
//				for ( i = 0; i < parrBCCs.length; i++ )
//				{
//					lobjAux = MessageAddress.GetInstance(getNameSpace(), (UUID)null);
//					lobjAux.setAt(0, parrBCCs[i]);
//					lobjAux.setAt(1, getKey());
//					lobjAux.setAt(2, Constants.UsageID_BCC);
//					lobjAux.setAt(3, (UUID)null);
//					lobjAux.setAt(4, (UUID)null);
//					lobjAux.SaveToDb(pdb);
//
//					larrAux.add(lobjAux);
//				}
//			}
//
//			marrAddresses = larrAux.toArray(new MessageAddress[larrAux.size()]);
//		}
//		catch (Throwable e)
//		{
//			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
	}

	public void ClearData(SQLServer pdb)
		throws BigBangJewelException
	{
//		int i;
//
//		if ( getKey() == null )
//			throw new BigBangJewelException("Erro: Não pode limpar os dados de um item de agenda novo antes de gravar.");
//
//		try
//		{
//			GetAddresses(pdb);
//			for ( i = 0; i < marrAddresses.length; i++ )
//				marrAddresses[i].getDefinition().Delete(pdb, marrAddresses[i].getKey());
//		}
//		catch (Throwable e)
//		{
//			marrAddresses = null;
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		marrAddresses = null;
	}
}
