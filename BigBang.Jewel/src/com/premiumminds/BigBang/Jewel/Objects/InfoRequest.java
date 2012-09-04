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
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class InfoRequest
	extends ProcessData
{
    public static InfoRequest GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InfoRequest)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InfoRequest), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private RequestAddress[] marrAddresses;

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

    public String getLabel()
    {
    	return (String)getAt(4);
    }

	public UUID GetProcessID()
	{
		return (UUID)getAt(0);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(0, pidProcess);
	}

	public RequestAddress[] GetAddresses()
		throws JewelEngineException
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

		return marrAddresses;
	}

	public RequestAddress[] GetAddresses(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		ArrayList<RequestAddress> larrAux;
		ResultSet lrs;

		lrs = null;
		if ( marrAddresses == null )
		{
			larrAux = new ArrayList<RequestAddress>();
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_RequestAddress));
				lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {getKey()}, new int[] {2, 0});
				while ( lrs.next() )
					larrAux.add(RequestAddress.GetInstance(getNameSpace(), lrs));
				lrs.close();
			}
			catch (Throwable e)
			{
				if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
			marrAddresses = larrAux.toArray(new RequestAddress[larrAux.size()]);
		}

		return marrAddresses;
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
				setAt(2, (byte[])null);
				return;
			}

			larrAux = pstrText.getBytes();
			laux = new FileXfer(larrAux.length, "text/plain", "body.txt", new ByteArrayInputStream(larrAux));
			setAt(2, laux.GetVarData());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public String getText()
	{
		FileXfer laux;

		if ( getAt(2) == null )
			return null;

    	if ( getAt(2) instanceof FileXfer )
    		laux = (FileXfer)getAt(2);
    	else
        	laux = new FileXfer((byte[])getAt(2));

    	return new String(laux.getData());
	}

	public void InitNew(UUID[] parrUserIDs, UUID[] parrContactInfoIDs, String[] parrCCs, String[] parrBCCs, SQLServer pdb)
		throws BigBangJewelException
	{
		ArrayList<RequestAddress> larrAux;
		RequestAddress lobjAux;
		IEntity lrefDecos;
		int i;
        ResultSet lrs;
		String lstrAddr;

		GetAddresses(pdb);
		if ( (marrAddresses != null) && (marrAddresses.length > 0) )
			throw new BigBangJewelException("Erro: Não pode redefinir os endereços de pedidos pré-existentes.");
		if ( getKey() == null )
			throw new BigBangJewelException("Erro: Não pode definir o conjunto de endereços para um pedido antes de gravar.");
		if ( parrUserIDs.length == 0 )
			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de utilizadores para um novo pedido.");
		if ( parrContactInfoIDs.length == 0 )
			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de contactos para um novo pedido.");

		larrAux = new ArrayList<RequestAddress>();

		lrs = null;
		try
		{
			lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
			for ( i = 0; i < parrUserIDs.length; i++ )
			{
				lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {parrUserIDs[i]}, new int[0]);
			    if (lrs.next())
			    	lstrAddr = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrs).getAt(1);
			    else
			    	lstrAddr = null;
			    lrs.close();

				lobjAux = RequestAddress.GetInstance(getNameSpace(), (UUID)null);
				lobjAux.setAt(0, lstrAddr);
				lobjAux.setAt(1, getKey());
				lobjAux.setAt(2, Constants.UsageID_ReplyTo);
				lobjAux.setAt(3, parrUserIDs[i]);
				lobjAux.setAt(4, (UUID)null);
				lobjAux.SaveToDb(pdb);

				larrAux.add(lobjAux);
			}

			for ( i = 0; i < parrContactInfoIDs.length; i++ )
			{
				lstrAddr = (String)ContactInfo.GetInstance(getNameSpace(), parrContactInfoIDs[i]).getAt(2);

				lobjAux = RequestAddress.GetInstance(getNameSpace(), (UUID)null);
				lobjAux.setAt(0, lstrAddr);
				lobjAux.setAt(1, getKey());
				lobjAux.setAt(2, Constants.UsageID_To);
				lobjAux.setAt(3, (UUID)null);
				lobjAux.setAt(4, parrContactInfoIDs[i]);
				lobjAux.SaveToDb(pdb);

				larrAux.add(lobjAux);
			}

			if ( parrCCs != null )
			{
				for ( i = 0; i < parrCCs.length; i++ )
				{
					lobjAux = RequestAddress.GetInstance(getNameSpace(), (UUID)null);
					lobjAux.setAt(0, parrCCs[i]);
					lobjAux.setAt(1, getKey());
					lobjAux.setAt(2, Constants.UsageID_CC);
					lobjAux.setAt(3, (UUID)null);
					lobjAux.setAt(4, (UUID)null);
					lobjAux.SaveToDb(pdb);

					larrAux.add(lobjAux);
				}
			}

			if ( parrBCCs != null )
			{
				for ( i = 0; i < parrBCCs.length; i++ )
				{
					lobjAux = RequestAddress.GetInstance(getNameSpace(), (UUID)null);
					lobjAux.setAt(0, parrBCCs[i]);
					lobjAux.setAt(1, getKey());
					lobjAux.setAt(2, Constants.UsageID_BCC);
					lobjAux.setAt(3, (UUID)null);
					lobjAux.setAt(4, (UUID)null);
					lobjAux.SaveToDb(pdb);

					larrAux.add(lobjAux);
				}
			}

			marrAddresses = larrAux.toArray(new RequestAddress[larrAux.size()]);
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void ClearData(SQLServer pdb)
		throws BigBangJewelException
	{
		int i;

		if ( getKey() == null )
			throw new BigBangJewelException("Erro: Não pode limpar os dados de um item de agenda novo antes de gravar.");

		try
		{
			GetAddresses(pdb);
			for ( i = 0; i < marrAddresses.length; i++ )
				marrAddresses[i].getDefinition().Delete(pdb, marrAddresses[i].getKey());
		}
		catch (Throwable e)
		{
			marrAddresses = null;
			throw new BigBangJewelException(e.getMessage(), e);
		}

		marrAddresses = null;
	}
}
