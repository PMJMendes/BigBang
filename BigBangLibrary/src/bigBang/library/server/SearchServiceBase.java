package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SessionExpiredException;
import bigBang.library.shared.SortParameter;

public abstract class SearchServiceBase
	extends EngineImplementor
	implements SearchService
{
	private static final long serialVersionUID = 1L;

	protected class ColumnDef
	{
		public String mstrAlias;
		public String mstrCriteria;

		public ColumnDef() {}
	}

	private class SearchWSpace
	{
        public class Row
        {
        	private UUID midKey;
        	private java.lang.Object[] marrValues;

        	public Row(UUID pidKey, java.lang.Object[] parrValues)
        	{
        		midKey = pidKey;
        		marrValues = parrValues;
        	}

        	public UUID getKey()
        	{
        		return midKey;
        	}

        	java.lang.Object[] GetValues()
        	{
        		return marrValues;
        	}
        }

		private UUID mid;
        private UUID midObject;
		private UUID midOp;
        private String[] marrColumns;
        private ArrayList<Row> marrData;
        private int mlngColCount;

		public SearchWSpace(UUID pidObject, UUID pidOp, String[] parrColumns)
			throws BigBangException
		{
			mid = UUID.randomUUID();
			midObject = pidObject;
			midOp = pidOp;
			marrColumns = parrColumns;
		}
		
		public UUID GetID()
		{
			return mid;
		}

		public UUID GetOpID()
		{
			return midOp;
		}

		public void OpenSearch(String pstrCriteria, String pstrSort)
			throws BigBangException
		{
	        IEntity lrefClients;
	        MasterDB ldb;
	        StringBuilder lstrSQLB;
	        String lstrSQL;
	        ResultSet lrsRows;
            UUID lidKey;
            java.lang.Object[] larrRow;
            int i;
            ResultSetMetaData lrsMetaData;
            String lstrAux;

			try
			{
		    	lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), midObject));
		        ldb = new MasterDB();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

	        try
	        {
	        	lstrSQLB = new StringBuilder("SELECT [PK]");
	        	for ( i = 0; i < marrColumns.length; i++ )
	        		lstrSQLB.append(", ").append(marrColumns[i]);
	        	lstrSQLB.append(" FROM (").append(lrefClients.SQLForSelectMulti()).append(") [Aux] WHERE ").append(pstrCriteria)
	        			.append(" ORDER BY ").append(pstrSort);
	        	lstrSQL = lstrSQLB.toString();
	        	lrsRows = ldb.OpenRecordset(lstrSQL);
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}
			try
			{
	            lrsMetaData = lrsRows.getMetaData();
	            mlngColCount = lrsMetaData.getColumnCount();

	            marrData = new ArrayList<Row>();
		        while (lrsRows.next())
		        {
					lidKey = UUID.fromString(lrsRows.getString(1));
	                larrRow = new java.lang.Object[mlngColCount - 1]; 
	                for (i = 1; i < mlngColCount; i++)
	                	if ( lrsMetaData.getColumnTypeName(i + 1).equals("uniqueidentifier") )
	                	{
	                		lstrAux = lrsRows.getString(i + 1);
	                		if ( lstrAux == null )
	                    		larrRow[i - 1] = null;
	                		else
	                			larrRow[i - 1] = UUID.fromString(lstrAux);
	                	}
	                	else
	                		larrRow[i - 1] = lrsRows.getObject(i + 1);
					marrData.add(new Row(lidKey, larrRow));
		        }
	        }
	        catch (Throwable e)
	        {
				try { lrsRows.close(); } catch (Throwable e1) {}
				try { ldb.Disconnect(); } catch (Throwable e1) {}
	        	throw new BigBangException(e.getMessage(), e);
	        }

	        try
	        {
	        	lrsRows.close();
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
		
		int GetRowCount()
		{
			return marrData.size();
		}

		Row GetRow(int plngRow)
		{
			return marrData.get(plngRow);
		}
	}

	@SuppressWarnings("unchecked")
	public static Hashtable<UUID, SearchWSpace> GetSearchWSStorage()
	{
		Hashtable<UUID, SearchWSpace> larrAux;

        if (getSession() == null)
            return null;

        larrAux = (Hashtable<UUID, SearchWSpace>)getSession().getAttribute("BigBang_Search_Storage");
        if (larrAux == null)
        {
        	larrAux = new Hashtable<UUID, SearchWSpace>();
            getSession().setAttribute("BigBang_Search_Storage", larrAux);
        }

        return larrAux;
	}

	public NewSearchResult openSearch(SearchParameter[] parameters, SortParameter[] sorts, int size)
		throws SessionExpiredException, BigBangException
	{
		return openForOperation(null, parameters, sorts, size);
	}

	public NewSearchResult openForOperation(String opId, SearchParameter[] parameters, SortParameter[] sorts, int size)
		throws SessionExpiredException, BigBangException
	{
		SearchWSpace lrefWSpace;
		UUID lidAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefWSpace = new SearchWSpace(getObjectID(), (opId == null ? null : UUID.fromString(opId)), getColumns());
		lidAux = lrefWSpace.GetID();
		GetSearchWSStorage().put(lidAux, lrefWSpace);

		return doOpenSearch(lrefWSpace, parameters, sorts, size);
	}

	public NewSearchResult search(String workspaceId, SearchParameter[] parameters, SortParameter[] sorts, int size)
		throws SessionExpiredException, BigBangException
	{
		SearchWSpace lrefWSpace;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefWSpace = GetSearchWSStorage().get(UUID.fromString(workspaceId));
		if ( lrefWSpace == null )
			throw new BigBangException("Unexpected: non-existant query workspace.");

		return doOpenSearch(lrefWSpace, parameters, sorts, size);
	}

	public SearchResult[] getResults(String workspaceId, int from, int size)
		throws SessionExpiredException, BigBangException
	{
		SearchWSpace lrefWSpace;
		int llngCount;
		SearchResult[] larrResult;
		int i;
		SearchWSpace.Row lobjAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefWSpace = GetSearchWSStorage().get(UUID.fromString(workspaceId));
		if ( lrefWSpace == null )
			throw new BigBangException("Unexpected: non-existant query workspace.");

		llngCount = lrefWSpace.GetRowCount();
		if ( llngCount < from + size )
			size = llngCount - from;
		larrResult = new SearchResult[size];
		for ( i = 0; i < size; i++ )
		{
			lobjAux = lrefWSpace.GetRow(i + from);
			larrResult[i] = buildResult(lobjAux.getKey(), lobjAux.GetValues());
		}

		return larrResult;
	}

	public void closeSearch(String workspaceId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		GetSearchWSStorage().remove(UUID.fromString(workspaceId));
	}

	private NewSearchResult doOpenSearch(SearchWSpace prefWSpace, SearchParameter[] parameters, SortParameter[] sorts, int size)
		throws BigBangException
	{
		StringBuilder lstrBuffer;
		String lstrCriteria, lstrSort;
		int[] larrMembers;
		java.lang.Object[] larrValues;
		IEntity lrefSteps;
		NewSearchResult lobjResult;
		SearchWSpace.Row lobjAux;
		int llngCount;
		int i;

		lstrBuffer = new StringBuilder("(");

		if ( (parameters == null) || (parameters.length == 0) )
			lstrBuffer.append("1 = 1");
		else
		{
			for ( i = 0; i < parameters.length; i++ )
			{
				if ( i > 0 )
					lstrBuffer.append(") OR (");

				if ( prefWSpace.GetOpID() == null )
				{
					lstrBuffer.append("1 = 1");
				}
				else
				{
					lstrBuffer.append("[PK] IN (SELECT [:Process:Data] FROM (");
					larrMembers = new int[1];
					larrMembers[0] = 1;
					larrValues = new java.lang.Object[1];
					larrValues[0] = prefWSpace.GetOpID();
					try
					{
						lrefSteps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
								Jewel.Petri.Constants.ObjID_PNStep));
						lstrBuffer.append(lrefSteps.SQLForSelectByMembers(larrMembers, larrValues, null));
					}
					catch (Throwable e)
					{
			    		throw new BigBangException(e.getMessage(), e);
					}
					lstrBuffer.append(") [AuxSteps0])");
				}

				buildFilter(lstrBuffer, parameters[i]);
			}
		}
		lstrBuffer.append(")");
		lstrCriteria = lstrBuffer.toString();

		lstrBuffer = new StringBuilder();
		if ( (sorts != null) && (sorts.length > 0) )
		{
			for ( i = 0; i < sorts.length; i++ )
			{
				buildSort(lstrBuffer, sorts[i], parameters);
				lstrBuffer.append(", ");
			}
		}
		lstrBuffer.append("[PK] ASC");
		lstrSort = lstrBuffer.toString();

		prefWSpace.OpenSearch(lstrCriteria, lstrSort);

		lobjResult = new NewSearchResult();
		lobjResult.workspaceId = prefWSpace.GetID().toString();
		lobjResult.totalCount = prefWSpace.GetRowCount();
		llngCount = prefWSpace.GetRowCount();
		if ( llngCount < size )
			size = llngCount;
		lobjResult.results = new SearchResult[size];
		for ( i = 0; i < size; i++ )
		{
			lobjAux = prefWSpace.GetRow(i);
			lobjResult.results[i] = buildResult(lobjAux.getKey(), lobjAux.GetValues());
		}

		return lobjResult;
	}

	protected abstract UUID getObjectID();
	protected abstract String[] getColumns();
	protected abstract void buildFilter(StringBuilder pstrBuffer, SearchParameter pParam) throws BigBangException;
	protected abstract void buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams);
	protected abstract SearchResult buildResult(UUID pid, java.lang.Object[] parrValues);
}
