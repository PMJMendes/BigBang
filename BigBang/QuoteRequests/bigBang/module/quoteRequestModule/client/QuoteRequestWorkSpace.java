package bigBang.module.quoteRequestModule.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.CompositeObject;
import bigBang.definitions.shared.CompositeObjectStub;
import bigBang.definitions.shared.QuoteRequest;

public class QuoteRequestWorkSpace {
	private static String NEWID = "new";

	protected QuoteRequest originalRequest;

	protected QuoteRequest request;
	protected List<CompositeObject> alteredObjects;
	protected int idCounter;

	public QuoteRequestWorkSpace() {
		this.alteredObjects = new ArrayList<CompositeObject>();
	}

	private boolean isRequestLoaded(String id) {
		return (this.request != null) && this.request.id.equalsIgnoreCase(id);
	}

	public QuoteRequest loadRequest(QuoteRequest request) {
		originalRequest = request;
		alteredObjects.clear();
		idCounter = 0;

		if ( request == null )
			this.request = null;
		else {
			try {
				if ( request.id == null )
					originalRequest.id = NEWID;
				this.request = new QuoteRequest(originalRequest);
			} catch (Exception e) {
				this.request = null;
			}
		}

		return this.request;
	}

	public QuoteRequest reset(String requestId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		return loadRequest(originalRequest);
	}

	public QuoteRequest getWholeRequest(String requestId) {
		int i;

		if ( !isRequestLoaded(requestId) )
			return null;

		try {
			originalRequest = new QuoteRequest(request);
		} catch (Exception e) {
			return null;
		}

		if ( NEWID.equals(originalRequest.id) )
			originalRequest.id = null;

		originalRequest.changedObjects = new CompositeObject[alteredObjects.size()];
		i = 0;
		for ( CompositeObject object : alteredObjects ) {
			originalRequest.changedObjects[i] = new CompositeObject(object);
//			originalRequest.changedObjects[i].headerFields = splitArray(originalRequest.changedObjects[i].headerFields, originalRequest.headerFields.length);
			if ( CompositeObjectStub.Change.CREATED.equals(originalRequest.changedObjects[i].change) )
				originalRequest.changedObjects[i].id = null;
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
		CompositeFieldContainer.SubLineFieldContainer[] result;
		int i;

		if ( !isRequestLoaded(requestId) )
			return null;

		result = new CompositeFieldContainer.SubLineFieldContainer[request.subLineData.length];
		for ( i = 0; i < result.length; i++ )
			result[i] = new CompositeFieldContainer.SubLineFieldContainer(request.subLineData[i]);

		return result;
	}

	public CompositeFieldContainer.SubLineFieldContainer getSubLineHeader(String requestId, String subLineId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		if ( request.subLineData == null )
			return null;

		for ( CompositeFieldContainer.SubLineFieldContainer subLine : request.subLineData )
			if(subLine.subLineId.equalsIgnoreCase(subLineId))
				return subLine;

		return null;
	}

	public CompositeFieldContainer.SubLineFieldContainer loadSubLine(String requestId,
			CompositeFieldContainer.SubLineFieldContainer subLine) {
		CompositeFieldContainer.SubLineFieldContainer[] larrAux;
		int i;

		if ( !isRequestLoaded(requestId) )
			return null;

		if ( request.subLineData == null ) {
			larrAux = new CompositeFieldContainer.SubLineFieldContainer[1];
			larrAux[0] = new CompositeFieldContainer.SubLineFieldContainer(subLine);
			return larrAux[0];
		}

		for ( i = 0; i < request.subLineData.length; i++ ) {
			if(request.subLineData[i].subLineId.equalsIgnoreCase(subLine.subLineId)) {
				request.subLineData[i] = new CompositeFieldContainer.SubLineFieldContainer(subLine);
				return request.subLineData[i];
			}
		}

		i = request.subLineData.length;
		larrAux = Arrays.copyOf(request.subLineData, i + 1);
		larrAux[i] = new CompositeFieldContainer.SubLineFieldContainer(subLine);
		request.subLineData = larrAux;
		return request.subLineData[i];
	}

	public CompositeFieldContainer.SubLineFieldContainer updateSubLine(String requestId,
			CompositeFieldContainer.SubLineFieldContainer subLine) {
		int i;

		if ( !isRequestLoaded(requestId) )
			return null;

		if ( request.subLineData == null )
			return null;

		for ( i = 0; i < request.subLineData.length; i++ ) {
			if(request.subLineData[i].subLineId.equalsIgnoreCase(subLine.subLineId)) {
				request.subLineData[i] = subLine;
				return subLine;
			}
		}

		return null;
	}

	public CompositeFieldContainer.SubLineFieldContainer deleteSubLine(String requestId, String subLineId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		for( CompositeFieldContainer.SubLineFieldContainer subLine : request.subLineData ) {
			if(subLine.subLineId.equalsIgnoreCase(subLineId)) {
				subLine.change = CompositeFieldContainer.SubLineFieldContainer.Change.DELETED;
				return subLine;
			}
		}

		return null;
	}


	//INSURED OBJECTS

	public CompositeObjectStub[] getLocalObjects(String requestId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		CompositeObjectStub[] result;
		int i;

		result = new CompositeObjectStub[alteredObjects.size()];
		for ( i = 0; i < result.length; i++ )
			result[i] = new CompositeObjectStub(alteredObjects.get(i));

		return result;
	}

	public CompositeObject getObjectHeader(String requestId, String objectId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		for ( CompositeObject object : alteredObjects )
			if(object.id.equalsIgnoreCase(objectId))
				return object;

		return null;
	}

	public CompositeObject loadExistingObject(String requestId, CompositeObject object) {
		CompositeObject newObject;

		if ( !isRequestLoaded(requestId) )
			return null;

		for ( CompositeObject oldObject : alteredObjects )
		{
			if(oldObject.id.equalsIgnoreCase(object.id)) {
				return object;
			}
		}

		newObject = new CompositeObject(object);
		newObject.change = CompositeObjectStub.Change.NONE;

		try {
//			newObject.headerFields = mergeHeaderArrays(new FieldContainer.HeaderField[][] {request.headerFields, object.headerFields},
//					new boolean[] {true, false});
		} catch (Exception e) {
			return null;
		}

		alteredObjects.add(newObject);

		return newObject;
	}

	public CompositeObject createLocalObject(String requestId) {
		CompositeObject newObject;

		if ( !isRequestLoaded(requestId) )
			return null;

		newObject = new CompositeObject(request.emptyObject);
		newObject.change = CompositeObjectStub.Change.CREATED;
		newObject.id = idCounter+"";
		idCounter++;

		try {
//			newObject.headerFields = mergeHeaderArrays(new FieldContainer.HeaderField[][] {request.headerFields, newObject.headerFields},
//					new boolean[] {true, false});
		} catch (Exception e) {
			return null;
		}

		alteredObjects.add(newObject);

		return newObject;
	}

	public CompositeObject updateObject(String requestId, CompositeObject alteredObject) {
		if ( !isRequestLoaded(requestId) )
			return null;

		ListIterator<CompositeObject> iterator = this.alteredObjects.listIterator();

		while(iterator.hasNext()) {
			CompositeObject object = iterator.next();
			if(object.id.equalsIgnoreCase(alteredObject.id)) {
				if (CompositeObjectStub.Change.NONE.equals(alteredObject.change))
					alteredObject.change = CompositeObjectStub.Change.MODIFIED;
				iterator.remove();
				iterator.add(alteredObject);
				return alteredObject;
			}
		}

		return null;
	}

	public CompositeObjectStub deleteObject(String requestId, String objectId) {
		if ( !isRequestLoaded(requestId) )
			return null;

		for( CompositeObject object : alteredObjects ) {
			if(object.id.equalsIgnoreCase(objectId)) {
				object.change = CompositeObjectStub.Change.DELETED;
				return object;
			}
		}

		return null;
	}
}
