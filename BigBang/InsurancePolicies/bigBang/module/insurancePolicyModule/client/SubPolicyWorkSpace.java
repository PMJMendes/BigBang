package bigBang.module.insurancePolicyModule.client;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.FieldContainer.ExtraField;
import bigBang.definitions.shared.FieldContainer.HeaderField;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.SubPolicy;

public class SubPolicyWorkSpace {
	private static String NEWID = "new";

	protected SubPolicy originalSubPolicy;

	protected SubPolicy subPolicy;
	protected List<InsuredObject> alteredObjects;
	protected int idCounter;

	public SubPolicyWorkSpace() {
		this.alteredObjects = new ArrayList<InsuredObject>();
	}

	private boolean isSubPolicyLoaded(String id) {
		return (this.subPolicy != null) && this.subPolicy.id.equalsIgnoreCase(id);
	}

	public SubPolicy loadSubPolicy(SubPolicy subPolicy) {
		originalSubPolicy = subPolicy;
		alteredObjects.clear();
		idCounter = 0;

		if ( subPolicy == null )
			this.subPolicy = null;
		else {
			try {
				if ( subPolicy.id == null )
					originalSubPolicy.id = NEWID;
				this.subPolicy = new SubPolicy(originalSubPolicy);
			} catch (Exception e) {
				this.subPolicy = null;
			}
		}

		return this.subPolicy;
	}

	public SubPolicy reset(String subPolicyId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		return loadSubPolicy(originalSubPolicy);
	}

	public SubPolicy getWholeSubPolicy(String subPolicyId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		try {
			originalSubPolicy = new SubPolicy(subPolicy);
		} catch (Exception e) {
			return null;
		}

		if ( NEWID.equals(originalSubPolicy.id) )
			originalSubPolicy.id = null;

		if ( originalSubPolicy.exerciseData != null )
		{
			for ( ExerciseData exercise : originalSubPolicy.exerciseData )
				if ( NEWID.equals(exercise.id) )
					exercise.id = null;
		}

		originalSubPolicy.changedObjects = alteredObjects.toArray(new InsuredObject[alteredObjects.size()]);
		for ( InsuredObject object : originalSubPolicy.changedObjects )
		{
			object.headerFields = splitArray(object.headerFields, originalSubPolicy.headerFields.length);
			if ( InsuredObjectStub.Change.CREATED.equals(object.change) )
				object.id = null;
		}

		return originalSubPolicy;
	}


	//SUBPOLICY

	public SubPolicy getSubPolicyHeader(String subPolicyId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		return subPolicy;
	}

	public SubPolicy updateSubPolicyHeader(SubPolicy policy) {
		if ( !isSubPolicyLoaded(policy.id) )
			return null;

		this.subPolicy = policy;
		return policy;
	}

	public void updateCoverages(StructuredFieldContainer.Coverage[] coverages) {
		int i;

		for ( i = 0; i < subPolicy.coverages.length; i++ )
			subPolicy.coverages[i].presentInPolicy = coverages[i].presentInPolicy;
	}


	//EXERCISES

	public ExerciseData[] getExercises(String subPolicyId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		return this.subPolicy.exerciseData;
	}


	//INSURED OBJECTS

	public InsuredObjectStub[] getLocalObjects(String subPolicyId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		InsuredObjectStub[] result;
		int i;

		result = new InsuredObjectStub[alteredObjects.size()];
		for ( i = 0; i < result.length; i++ )
			result[i] = new InsuredObjectStub(alteredObjects.get(i));

		return result;
	}

	public InsuredObject getObjectHeader(String subPolicyId, String objectId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		for ( InsuredObject object : alteredObjects )
			if(object.id.equalsIgnoreCase(objectId))
				return object;

		return null;
	}

	public InsuredObject loadExistingObject(String subPolicyId, InsuredObject object) {
		InsuredObject newObject;

		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		for ( InsuredObject oldObject : alteredObjects )
		{
			if(oldObject.id.equalsIgnoreCase(object.id)) {
				return object;
			}
		}

		newObject = new InsuredObject(object);
		newObject.change = InsuredObjectStub.Change.NONE;
		if ( subPolicy.exerciseData != null )
			newObject.exerciseData[0].isActive = subPolicy.exerciseData[0].isActive;

		try {
			newObject.headerFields = mergeHeaderArrays(new HeaderField[][] {subPolicy.headerFields, object.headerFields},
					new boolean[] {true, false});
		} catch (Exception e) {
			return null;
		}

		alteredObjects.add(newObject);

		return newObject;
	}

	public InsuredObject createLocalObject(String subPolicyId) {
		InsuredObject newObject;

		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		newObject = new InsuredObject(subPolicy.emptyObject);
		newObject.change = InsuredObjectStub.Change.CREATED;
		newObject.id = idCounter+"";
		idCounter++;

		try {
			newObject.headerFields = mergeHeaderArrays(new HeaderField[][] {subPolicy.headerFields, newObject.headerFields},
					new boolean[] {true, false});
		} catch (Exception e) {
			return null;
		}

		alteredObjects.add(newObject);

		return newObject;
	}

	public InsuredObject updateObject(String subPolicyId, InsuredObject alteredObject) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		ListIterator<InsuredObject> iterator = this.alteredObjects.listIterator();

		while(iterator.hasNext()) {
			InsuredObject object = iterator.next();
			if(object.id.equalsIgnoreCase(alteredObject.id)) {
				if (InsuredObjectStub.Change.NONE.equals(alteredObject.change))
					alteredObject.change = InsuredObjectStub.Change.MODIFIED;
				iterator.remove();
				iterator.add(alteredObject);
				return alteredObject;
			}
		}

		return null;
	} 

	public InsuredObjectStub deleteObject(String subPolicyId, String objectId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		for( InsuredObject object : alteredObjects ) {
			if(object.id.equalsIgnoreCase(objectId)) {
				object.change = InsuredObjectStub.Change.DELETED;
				return object;
			}
		}

		return null;
	}


	//CONTEXT

	public FieldContainer getContext(String subPolicyId, String objectId, String exerciseId) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return null;

		FieldContainer result;
		int exerciseIndex = -1;
		int i;

		if(exerciseId != null) {
			for( i = 0; i < subPolicy.exerciseData.length; i++ ) {
				if(exerciseId.equalsIgnoreCase(subPolicy.exerciseData[i].id)) {
					exerciseIndex = i;
					break;
				}
			}
		}

		InsuredObject object = null;
		if(objectId != null){
			for(InsuredObject o : alteredObjects) {
				if(o.id.equalsIgnoreCase(objectId)){
					object = o;
					break;
				}
			}
		}

		result = new FieldContainer();

		try
		{
			result.headerFields = mergeHeaderArrays(new HeaderField[][] {
						( exerciseIndex < 0 ? null : subPolicy.exerciseData[exerciseIndex].headerFields ),
						( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].headerFields )
					}, new boolean[] {object != null, false});

			result.columnFields = mergeColumnArrays(new ColumnField[][] {
						subPolicy.columnFields,
						( object == null ? null : object.columnFields ),
						( exerciseIndex < 0 ? null : subPolicy.exerciseData[exerciseIndex].columnFields ),
						( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].columnFields)
					}, new boolean[] {object != null, false, object != null, false});

			result.extraFields = mergeExtraArrays(new ExtraField[][] {
						subPolicy.extraFields,
						( object == null ? null : object.extraFields ),
						( exerciseIndex < 0 ? null : subPolicy.exerciseData[exerciseIndex].extraFields ),
						( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].extraFields)
					}, new boolean[] {object != null, false, object != null, false});
		} catch (Exception e) {
			return null;
		}

		return result;
	}

	public void updateContext(String subPolicyId, String objectId, String exerciseId, FieldContainer contents) {
		if ( !isSubPolicyLoaded(subPolicyId) )
			return;

		int exerciseIndex = -1;
		int i;

		if(exerciseId != null) {
			for( i = 0; i < subPolicy.exerciseData.length; i++ ) {
				if(exerciseId.equalsIgnoreCase(subPolicy.exerciseData[i].id)) {
					exerciseIndex = i;
					break;
				}
			}
		}

		InsuredObject object = null;
		if(objectId != null){
			for(InsuredObject o : alteredObjects) {
				if(o.id.equalsIgnoreCase(objectId)){
					object = o;
					break;
				}
			}
		}

		readMergedHeaderArray(contents.headerFields, new HeaderField[][] {
					( exerciseIndex < 0 ? null : subPolicy.exerciseData[exerciseIndex].headerFields ),
					( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].headerFields )
				}, new boolean[] {object != null, false});

		readMergedColumnArray(contents.columnFields, new ColumnField[][] {
					subPolicy.columnFields,
					( object == null ? null : object.columnFields ),
					( exerciseIndex < 0 ? null : subPolicy.exerciseData[exerciseIndex].columnFields ),
					( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].columnFields)
				}, new boolean[] {object != null, false, object != null, false});

		readMergedExtraArray(contents.extraFields, new ExtraField[][] {
					subPolicy.extraFields,
					( object == null ? null : object.extraFields ),
					( exerciseIndex < 0 ? null : subPolicy.exerciseData[exerciseIndex].extraFields ),
					( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].extraFields)
				}, new boolean[] {object != null, false, object != null, false});
	}

	private static HeaderField[] mergeHeaderArrays(HeaderField[][] source, boolean[] readOnly) throws Exception
	{
		HeaderField[] result;
		int len;
		int start;
		int i, j;

		len = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
				len += source[i].length;
		}

		result = new HeaderField[len];

		start = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
			{
				for ( j = 0; j < source[i].length; j++ )
				{
					result[start + j] = new HeaderField(source[i][j]);
					result[start + j].readOnly = readOnly[i];
				}
				start += source[i].length;
			}
		}

		return result;
	}

	private static ColumnField[] mergeColumnArrays(ColumnField[][] source, boolean[] readOnly) throws Exception
	{
		ColumnField[] result;
		int len;
		int start;
		int i, j;

		len = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
				len += source[i].length;
		}

		result = new ColumnField[len];

		start = 0;
		for ( i = 0; i < source.length; i++ )
		{
			if ( source[i] != null )
			{
				for ( j = 0; j < source[i].length; j++ )
				{
					result[start + j] = new ColumnField(source[i][j]);
					result[start + j].readOnly = readOnly[i];
				}
				start += source[i].length;
			}
		}

		return result;
	}

	private static ExtraField[] mergeExtraArrays(ExtraField[][] source, boolean[] readOnly) throws Exception
	{
		ExtraField[] result;
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

		result = new ExtraField[len];

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
						result[i] = new ExtraField(source[j][at[j]]);
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

	private static HeaderField[] splitArray(HeaderField[] source, int start)
	{
		HeaderField[] result;
		int i;

		result = new HeaderField[source.length - start];

		for ( i = start; i < source.length; i++ )
			result[i - start] = source[i];

		return result;
	}

	private static void readMergedHeaderArray(HeaderField[] source, HeaderField[][] dest, boolean[] ignore)
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

	private static void readMergedColumnArray(ColumnField[] source, ColumnField[][] dest, boolean[] ignore)
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

	private static void readMergedExtraArray(ExtraField[] source, ExtraField[][] dest, boolean[] ignore)
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
