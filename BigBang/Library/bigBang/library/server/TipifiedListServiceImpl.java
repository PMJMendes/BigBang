package bigBang.library.server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.interfaces.TipifiedListService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class TipifiedListServiceImpl
	extends EngineImplementor
	implements TipifiedListService
{
	private static final long serialVersionUID = 1L;

	public TipifiedListItem[] getListItems(String listId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidListRef;
        MasterDB ldb;
        int[] larrSorts;
        ResultSet lrsItems;
		ArrayList<TipifiedListItem> larrAux;
//		ObjectBase lobjItem;
		TipifiedListItem lobjAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<TipifiedListItem>();
		larrSorts = new int[1];
		larrSorts[0] = 0;

		try
		{
			lidListRef = Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(listId));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsItems = Entity.GetInstance(lidListRef).SelectAllSort(ldb, larrSorts);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsItems.next())
	        {
//	        	lobjItem = Engine.GetWorkInstance(lidListRef, lrsItems);
	        	lobjAux = new TipifiedListItem();
//	        	lobjAux.id = lobjItem.getKey().toString();
//	        	lobjAux.value = (String) lobjItem.getAt(0);
	        	lobjAux.id = UUID.fromString(lrsItems.getString(1)).toString();
	        	lobjAux.value = lrsItems.getString(2);
	        	larrAux.add(lobjAux);
	        }
        }
        catch (Throwable e)
        {
			try { lrsItems.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsItems.close();
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

		return larrAux.toArray(new TipifiedListItem[larrAux.size()]);
	}

	public TipifiedListItem getSingleItem(String listId, String itemId)
		throws SessionExpiredException, BigBangException
	{
		ObjectBase lobjBase;
		TipifiedListItem lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjBase = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(listId)),
					UUID.fromString(itemId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new TipifiedListItem();
		lobjResult.id = lobjBase.getKey().toString();
		lobjResult.value = lobjBase.getLabel();

		return lobjResult;
	}

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidFilter;
		UUID lidListRef;
        MasterDB ldb;
        ResultSet lrsItems;
		ArrayList<TipifiedListItem> larrAux;
//		ObjectBase lobjItem;
		TipifiedListItem lobjAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lidFilter = UUID.fromString(filterId);
		}
		catch (Throwable e)
		{
			lidFilter = null;
		}

		larrAux = new ArrayList<TipifiedListItem>();

		try
		{
			lidListRef = Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(listId));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsItems = Entity.GetInstance(lidListRef).SelectByMembers(ldb,
	        		new int[] {1}, new java.lang.Object[] { (lidFilter == null ? filterId : lidFilter) }, new int[] {0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsItems.next())
	        {
//	        	lobjItem = Engine.GetWorkInstance(lidListRef, lrsItems);
	        	lobjAux = new TipifiedListItem();
//	        	lobjAux.id = lobjItem.getKey().toString();
//	        	lobjAux.value = (String) lobjItem.getAt(0);
	        	lobjAux.id = UUID.fromString(lrsItems.getString(1)).toString();
	        	lobjAux.value = lrsItems.getString(2);
	        	larrAux.add(lobjAux);
	        }
        }
        catch (Throwable e)
        {
			try { lrsItems.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsItems.close();
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

		return larrAux.toArray(new TipifiedListItem[larrAux.size()]);
	}

	public TipifiedListItem createListItem(String listId, TipifiedListItem item)
		throws SessionExpiredException, BigBangException
	{
		UUID lidListRef;
        MasterDB ldb;
		ObjectBase lobjItem;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lidListRef = Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(listId));
			lobjItem = Engine.GetWorkInstance(lidListRef, (UUID)null);
			lobjItem.setAt(0, item.value);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjItem.SaveToDb(ldb);
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

		item.id = lobjItem.getKey().toString();
		return item;
	}

	public TipifiedListItem createListItemFiltered(String listId, String filterId, TipifiedListItem item)
			throws SessionExpiredException, BigBangException
	{
		UUID lidFilter;
		UUID lidListRef;
        MasterDB ldb;
		ObjectBase lobjItem;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lidFilter = UUID.fromString(filterId);
		}
		catch (Throwable e)
		{
			lidFilter = null;
		}

		try
		{
			lidListRef = Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(listId));
			lobjItem = Engine.GetWorkInstance(lidListRef, (UUID)null);
			lobjItem.setAt(0, item.value);
			lobjItem.setAt(1, (lidFilter == null ? filterId : lidFilter));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjItem.SaveToDb(ldb);
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

		item.id = lobjItem.getKey().toString();
		return item;
	}

	public TipifiedListItem saveListItem(String listId, TipifiedListItem item)
		throws SessionExpiredException, BigBangException
	{
		UUID lidListRef;
        MasterDB ldb;
		ObjectBase lobjItem;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lidListRef = Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(listId));
			lobjItem = Engine.GetWorkInstance(lidListRef, UUID.fromString(item.id));
			lobjItem.setAt(0, item.value);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjItem.SaveToDb(ldb);
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

		return item;
	}

	public void deleteListItem(String listId, String itemId)
		throws SessionExpiredException, BigBangException
	{
		IEntity lrefList;
        MasterDB ldb;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lrefList = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(listId)));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrefList.Delete(ldb, UUID.fromString(itemId));
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
	}
}
