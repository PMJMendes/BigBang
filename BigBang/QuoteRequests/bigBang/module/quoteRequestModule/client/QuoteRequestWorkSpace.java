package bigBang.module.quoteRequestModule.client;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.StructuredFieldContainer;

public class QuoteRequestWorkSpace {
	private static String NEWID = "new";

	public static class SubLineWorkSpace extends CompositeFieldContainer.SubLineFieldContainer {
		private static final long serialVersionUID = 1L;
		protected Map<String, InsuredObject> alteredObjects;
		CompositeFieldContainer.SubLineFieldContainer.Change prevChange;

		public SubLineWorkSpace() {
			alteredObjects = new HashMap<String, InsuredObject>();
			prevChange = CompositeFieldContainer.SubLineFieldContainer.Change.NONE;
		}

		public SubLineWorkSpace(CompositeFieldContainer.SubLineFieldContainer orig) {
			super(orig);

			alteredObjects = new HashMap<String, InsuredObject>();
			prevChange = orig.change;
		}
	}

	protected QuoteRequest originalRequest;

	protected QuoteRequest request;
	protected Map<String, SubLineWorkSpace> localSubLines;
	protected Map<String, QuoteRequestObject> alteredObjects;
	protected int idCounter;

	public QuoteRequestWorkSpace() {
		this.alteredObjects = new HashMap<String, QuoteRequestObject>();
		this.localSubLines = new HashMap<String, SubLineWorkSpace>();
	}

	private boolean isRequestLoaded(String id) {
		return (this.request != null) && this.request.id.equalsIgnoreCase(id);
	}

	public QuoteRequest loadRequest(QuoteRequest request) {
		SubLineWorkSpace subLine;
		int i;

		originalRequest = request;
		alteredObjects.clear();
		localSubLines.clear();
		idCounter = 0;

		if ( request == null )
		{
			this.request = null;
			return null;
		}

		try {
			if ( request.id == null )
				originalRequest.id = NEWID;
			this.request = new QuoteRequest(originalRequest);
			for ( i = 0; i < request.subLineData.length; i++ )
			{
				subLine = new SubLineWorkSpace(request.subLineData[i]);
				subLine.change = CompositeFieldContainer.SubLineFieldContainer.Change.NONE;
				subLine.prevChange = subLine.change;
				localSubLines.put(subLine.subLineId, subLine);
			}
			request.subLineData = null;
		} catch (Exception e) {
			this.request = null;
		}

		return this.request;
	}

	public QuoteRequest reset(String requestId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		return loadRequest(originalRequest);
	}

	public QuoteRequest getWholeRequest(String requestId) {
		InsuredObject objectAux;
		int i, j;

		if ( !isRequestLoaded(requestId) )
			return null;

		try {
			originalRequest = new QuoteRequest(request);
		} catch (Exception e) {
			return null;
		}

		if ( NEWID.equals(originalRequest.id) )
			originalRequest.id = null;

		originalRequest.subLineData = new CompositeFieldContainer.SubLineFieldContainer[localSubLines.size()];
		i = 0;
		for ( SubLineWorkSpace subLine : localSubLines.values() ) {
			originalRequest.subLineData[i] = new CompositeFieldContainer.SubLineFieldContainer(subLine);
			i++;
		}

		originalRequest.changedObjects = new QuoteRequestObject[alteredObjects.size()];
		i = 0;
		for ( QuoteRequestObject object : alteredObjects.values() ) {
			originalRequest.changedObjects[i] = new QuoteRequestObject(object);
			if ( InsuredObjectStub.Change.CREATED.equals(originalRequest.changedObjects[i].change) )
				originalRequest.changedObjects[i].id = null;
			originalRequest.changedObjects[i].subLineData = new CompositeFieldContainer.SubLineFieldContainer[localSubLines.size()];
			j = 0;
			for ( SubLineWorkSpace subLine : localSubLines.values() ) {
				objectAux = subLine.alteredObjects.get(object.id);
				originalRequest.changedObjects[i].subLineData[j] = ( objectAux == null ? null :
						new CompositeFieldContainer.SubLineFieldContainer(subLine, objectAux));
				j++;
			}
			i++;
		}

		return originalRequest;
	}


	//QUOTE REQUEST

	public QuoteRequest getRequestHeader(String requestId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		return request;
	}

	public QuoteRequest updateRequestHeader(QuoteRequest request) {
		if ( !isRequestLoaded(request.id) )
			return null;

		this.request = request;
		return request;
	}


	//SUBLINES

	public CompositeFieldContainer.SubLineFieldContainer[] getLocalSubLines(String requestId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		return localSubLines.values().toArray(new CompositeFieldContainer.SubLineFieldContainer[localSubLines.size()]);
	}

	public CompositeFieldContainer.SubLineFieldContainer loadSubLine(String requestId,
			CompositeFieldContainer.SubLineFieldContainer subLine) {
		SubLineWorkSpace subLineAux;

		if ( !isRequestLoaded(requestId) )
			return null;

		subLineAux = localSubLines.get(subLine.subLineId);
		if ( subLineAux != null )
		{
			if ( CompositeFieldContainer.SubLineFieldContainer.Change.DELETED.equals(subLineAux.change) )
				subLineAux.change = subLineAux.prevChange;
			return subLineAux;
		}

		subLineAux = new SubLineWorkSpace(subLine);
		subLineAux.change = CompositeFieldContainer.SubLineFieldContainer.Change.CREATED;
		subLineAux.prevChange = subLineAux.change;
		for ( QuoteRequestObject obj: alteredObjects.values() )
		{
			if ( obj.typeId.equalsIgnoreCase(subLineAux.objectTypeId) )
				subLineAux.alteredObjects.put(obj.id, new InsuredObject(obj, subLineAux.emptyObject));
		}
		localSubLines.put(subLineAux.subLineId, subLineAux);

		return subLineAux;
	}

	public void updateCoverages(String requestId, String subLineId, StructuredFieldContainer.Coverage[] coverages) {
		SubLineWorkSpace subLine;
		int i;

		if ( !isRequestLoaded(requestId) )
			return;

		subLine = localSubLines.get(subLineId);
		if ( subLine == null )
			return;
		if ( CompositeFieldContainer.SubLineFieldContainer.Change.DELETED.equals(subLine.change) )
			return;

		for ( i = 0; i < subLine.coverages.length; i++ )
			subLine.coverages[i].presentInPolicy = coverages[i].presentInPolicy;

		if ( CompositeFieldContainer.SubLineFieldContainer.Change.NONE.equals(subLine.change) )
		{
			subLine.change = CompositeFieldContainer.SubLineFieldContainer.Change.MODIFIED;
			subLine.prevChange = subLine.change;
		}
	}

	public CompositeFieldContainer.SubLineFieldContainer deleteSubLine(String requestId, String subLineId) {
		SubLineWorkSpace subLine;

		if ( !isRequestLoaded(requestId) )
			return null;

		subLine = localSubLines.get(subLineId);
		if ( subLine != null )
			subLine.change = CompositeFieldContainer.SubLineFieldContainer.Change.DELETED;

		return subLine;
	}


	//INSURED OBJECTS

	public QuoteRequestObjectStub[] getLocalObjects(String requestId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		QuoteRequestObjectStub[] result;
		int i;

		result = new QuoteRequestObjectStub[alteredObjects.size()];
		for ( i = 0; i < result.length; i++ )
			result[i] = new QuoteRequestObjectStub(alteredObjects.get(i), null);

		return result;
	}

	public QuoteRequestObject loadExistingObject(String requestId, QuoteRequestObject object) {
		QuoteRequestObject newObject;
		boolean bFound;
		int i;

		if ( !isRequestLoaded(requestId) )
			return null;

		newObject = alteredObjects.get(object.id);

		if ( newObject == null )
		{
			newObject = new QuoteRequestObject(object);
			newObject.change = InsuredObjectStub.Change.NONE;

			for ( SubLineWorkSpace subLine : localSubLines.values() )
			{
				if ( newObject.typeId.equalsIgnoreCase(subLine.objectTypeId) )
				{
					bFound = false;
					for ( i = 0; i < newObject.subLineData.length; i++ )
					{
						if ( subLine.subLineId.equalsIgnoreCase(newObject.subLineData[i].subLineId) )
						{
							subLine.alteredObjects.put(newObject.id, new InsuredObject(newObject, newObject.subLineData[i]));
							bFound = true;
							break;
						}
					}
					if ( !bFound )
						subLine.alteredObjects.put(newObject.id, new InsuredObject(newObject, subLine.emptyObject));
				}
			}
			newObject.subLineData = null;

			alteredObjects.put(newObject.id, newObject);
		}

		return newObject;
	}

	public QuoteRequestObject createLocalObject(String requestId, String typeId) {
		QuoteRequestObject newObject;

		if ( !isRequestLoaded(requestId) )
			return null;

		newObject = new QuoteRequestObject();
		newObject.typeId = typeId;
		newObject.change = InsuredObjectStub.Change.CREATED;
		newObject.id = idCounter+"";
		idCounter++;

		for ( SubLineWorkSpace subLine : localSubLines.values() )
		{
			if ( newObject.typeId.equalsIgnoreCase(subLine.objectTypeId) )
				subLine.alteredObjects.put(newObject.id, new InsuredObject(newObject, subLine.emptyObject));
		}

		alteredObjects.put(newObject.id, newObject);

		return newObject;
	}

	public QuoteRequestObject getObjectHeader(String requestId, String objectId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		return new QuoteRequestObject(alteredObjects.get(objectId), null);
	}

	public QuoteRequestObject updateObjectHeader(String requestId, QuoteRequestObject alteredObject) {
		QuoteRequestObject object;

		if ( !isRequestLoaded(requestId) )
			return null;

		object = alteredObjects.get(alteredObject.id);

		if ( object != null )
		{
			if ( InsuredObjectStub.Change.DELETED.equals(object.change) )
				return null;

			if ( InsuredObjectStub.Change.NONE.equals(alteredObject.change) )
				alteredObject.change = InsuredObjectStub.Change.MODIFIED;
			object = new QuoteRequestObject(alteredObject, object);
			alteredObjects.put(object.id, object);
		}

		return alteredObject;
	}

	public QuoteRequestObjectStub deleteObject(String requestId, String objectId) {
		QuoteRequestObject object;

		if ( !isRequestLoaded(requestId) )
			return null;

		object = alteredObjects.get(objectId);
		if ( object == null )
			return null;

		object.change = InsuredObjectStub.Change.DELETED;

		return new QuoteRequestObject(object, null);
	}


	//CONTEXT

	public FieldContainer getContext(String requestId, String subLineId, String objectId) {
		FieldContainer result;
		SubLineWorkSpace subLine;
		InsuredObject object;

		if ( !isRequestLoaded(requestId) )
			return null;

		subLine = localSubLines.get(subLineId);
		if ( subLine == null )
			return null;

		object = ( objectId == null ? null : subLine.alteredObjects.get(objectId) );

		result = new FieldContainer();

		try
		{
			result.headerFields = mergeHeaderArrays(new FieldContainer.HeaderField[][] {
						subLine.headerFields,
						( object == null ? null : object.headerFields )
					}, new boolean[] {object != null, false});

			result.columnFields = mergeColumnArrays(new FieldContainer.ColumnField[][] {
						subLine.columnFields,
						( object == null ? null : object.columnFields )
					}, new boolean[] {object != null, false});

			result.extraFields = mergeExtraArrays(new FieldContainer.ExtraField[][] {
						subLine.extraFields,
						( object == null ? null : object.extraFields )
					}, new boolean[] {object != null, false});
		} catch (Exception e) {
			return null;
		}

		return result;
	}

	public void updateContext(String requestId, String subLineId, String objectId, FieldContainer contents) {
		SubLineWorkSpace subLine;
		InsuredObject object;

		if ( !isRequestLoaded(requestId) )
			return;

		subLine = localSubLines.get(subLineId);
		if ( subLine == null )
			return;

		object = ( objectId == null ? null : subLine.alteredObjects.get(objectId) );

		readMergedHeaderArray(contents.headerFields, new FieldContainer.HeaderField[][] {
					subLine.headerFields,
					( object == null ? null : object.headerFields )
				}, new boolean[] {object != null, false});

		readMergedColumnArray(contents.columnFields, new FieldContainer.ColumnField[][] {
					subLine.columnFields,
					( object == null ? null : object.columnFields )
				}, new boolean[] {object != null, false});

		readMergedExtraArray(contents.extraFields, new FieldContainer.ExtraField[][] {
					subLine.extraFields,
					( object == null ? null : object.extraFields )
				}, new boolean[] {object != null, false});

		if ( CompositeFieldContainer.SubLineFieldContainer.Change.NONE.equals(subLine.change) )
		{
			subLine.change = CompositeFieldContainer.SubLineFieldContainer.Change.MODIFIED;
			subLine.prevChange = subLine.change;
		}

		if ( (object != null) && InsuredObjectStub.Change.NONE.equals(object.change) )
			object.change = InsuredObjectStub.Change.MODIFIED;
	}


	//INTERNALS

	private static FieldContainer.HeaderField[] mergeHeaderArrays(FieldContainer.HeaderField[][] source, boolean[] readOnly) throws Exception
	{
		FieldContainer.HeaderField[] result;
		int len;
		int start;
		int i, j;

		len = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
				len += source[i].length;
		}

		result = new FieldContainer.HeaderField[len];

		start = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
			{
				for ( j = 0; j < source[i].length; j++ )
				{
					result[start + j] = new FieldContainer.HeaderField(source[i][j]);
					result[start + j].readOnly = readOnly[i];
				}
				start += source[i].length;
			}
		}

		return result;
	}

	private static FieldContainer.ColumnField[] mergeColumnArrays(FieldContainer.ColumnField[][] source, boolean[] readOnly) throws Exception
	{
		FieldContainer.ColumnField[] result;
		int len;
		int start;
		int i, j;

		len = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
				len += source[i].length;
		}

		result = new FieldContainer.ColumnField[len];

		start = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
			{
				for ( j = 0; j < source[i].length; j++ )
				{
					result[start + j] = new FieldContainer.ColumnField(source[i][j]);
					result[start + j].readOnly = readOnly[i];
				}
				start += source[i].length;
			}
		}

		return result;
	}

	private static FieldContainer.ExtraField[] mergeExtraArrays(FieldContainer.ExtraField[][] source, boolean[] readOnly) throws Exception
	{
		FieldContainer.ExtraField[] result;
		int len;
		int[] at;
		int currentCoverage;
		int i, j;

		at = new int[source.length];
		len = 0;
		for ( i = 0; i < source.length; i++ )
		{
			at[i] = 0;
			if ( source[i] != null )
				len += source[i].length;
		}

		result = new FieldContainer.ExtraField[len];

		currentCoverage = 0;
		i = 0;
		while ( i < len )
		{
			for ( j = 0; j < source.length; j++ )
			{
				if ( source[j] != null )
				{
					while ( (at[j] < source[j].length) && (source[j][at[j]].coverageIndex == currentCoverage) )
					{
						result[i] = new FieldContainer.ExtraField(source[j][at[j]]);
						result[i].readOnly = readOnly[j];
						at[j]++;
						i++;
					}
				}
			}
			currentCoverage++;
		}

		return result;
	}

	private static void readMergedHeaderArray(FieldContainer.HeaderField[] source, FieldContainer.HeaderField[][] dest, boolean[] ignore)
	{
		int start;
		int i, j;

		start = 0;
		for ( i = 0; i < dest.length; i++ )
		{
			if ( dest[i] != null )
			{
				if ( !ignore[i] )
				{
					for ( j = 0; j < dest[i].length; j++ )
						dest[i][j].value = source[start + j].value;
				}
				start += dest[i].length;
			}
		}
	}

	private static void readMergedColumnArray(FieldContainer.ColumnField[] source, FieldContainer.ColumnField[][] dest, boolean[] ignore)
	{
		int start;
		int i, j;

		start = 0;
		for ( i = 0; i < dest.length; i++ )
		{
			if ( dest[i] != null )
			{
				if ( !ignore[i] )
				{
					for ( j = 0; j < dest[i].length; j++ )
						dest[i][j].value = source[start + j].value;
				}
				start += dest[i].length;
			}
		}
	}

	private static void readMergedExtraArray(FieldContainer.ExtraField[] source, FieldContainer.ExtraField[][] dest, boolean[] ignore)
	{
		int[] at;
		int currentCoverage;
		int i, j;

		at = new int[dest.length];
		for ( i = 0; i < dest.length; i++ )
			at[i] = 0;

		currentCoverage = 0;
		i = 0;
		while ( i < source.length )
		{
			for ( j = 0; j < dest.length; j++ )
			{
				if ( (dest[j] != null) )
				{
					while ( (at[j] < dest[j].length) && (dest[j][at[j]].coverageIndex == currentCoverage) )
					{
						if ( !ignore[j] )
							dest[j][at[j]].value = source[i].value;
						at[j]++;
						i++;
					}
				}
			}
			currentCoverage++;
		}
	}
}
