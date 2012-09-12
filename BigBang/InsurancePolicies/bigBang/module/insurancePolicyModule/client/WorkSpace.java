package bigBang.module.insurancePolicyModule.client;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.FieldContainer.ExtraField;
import bigBang.definitions.shared.FieldContainer.HeaderField;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;

public class WorkSpace {
	private static String NEWID = "new";

	protected InsurancePolicy originalPolicy;

	protected InsurancePolicy policy;
	protected List<InsuredObject> alteredObjects;
	protected int idCounter;

	public WorkSpace() {
		this.alteredObjects = new ArrayList<InsuredObject>();
	}

	private boolean isPolicyLoaded(String id) {
		return (this.policy != null) && this.policy.id.equalsIgnoreCase(id);
	}

	/**
	 * Creates the edition context
	 * @param policy 
	 */
	public InsurancePolicy loadPolicy(InsurancePolicy policy) {
		originalPolicy = policy;
		alteredObjects.clear();
		idCounter = 0;

		if ( policy == null )
			this.policy = null;
		else {
			try {
				this.policy = new InsurancePolicy(originalPolicy);
			} catch (Exception e) {
				policy = null;
			}
		}

		if ( policy.id == null )
			policy.id = NEWID;

		return policy;
	}

	public InsurancePolicy reset(String policyId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		return loadPolicy(originalPolicy);
	}

	public InsurancePolicy getWholePolicy(String policyId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		try {
			originalPolicy = new InsurancePolicy(policy);
		} catch (Exception e) {
			return null;
		}

		if ( NEWID.equals(originalPolicy.id) )
			originalPolicy.id = null;

		if ( originalPolicy.exerciseData != null )
		{
			for ( ExerciseData exercise : originalPolicy.exerciseData )
				if ( NEWID.equals(exercise.id) )
					exercise.id = null;
		}

		originalPolicy.changedObjects = alteredObjects.toArray(new InsuredObject[alteredObjects.size()]);
		for ( InsuredObject object : originalPolicy.changedObjects )
		{
			object.headerFields = splitArray(object.headerFields, originalPolicy.headerFields.length);
			if ( InsuredObject.Change.CREATED.equals(object.change) )
				object.id = null;
		}

		return originalPolicy;
	}


	//INSURANCE POLICY

	public InsurancePolicy getPolicyHeader(String policyId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		return policy;
	}

	public InsurancePolicy updatePolicyHeader(InsurancePolicy policy) {
		if ( !isPolicyLoaded(policy.id) )
			return null;

		this.policy = policy;
		return policy;
	}


	//EXERCISES

	public ExerciseData[] getExercises(String policyId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		return this.policy.exerciseData;
	}

	public ExerciseData createExercise(String policyId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		if((policy.exerciseData != null) && (policy.exerciseData.length > 0) && !policy.exerciseData[0].isActive) {
			policy.exerciseData[0].isActive = true;
			policy.exerciseData[0].id = NEWID;

			for ( InsuredObject object : alteredObjects )
				object.exerciseData[0].isActive = true;
			policy.emptyObject.exerciseData[0].isActive = true;

			return policy.exerciseData[0];
		} else {
			return null;
		}
	}

	public ExerciseData updateExerciseHeader(String policyId, ExerciseData alteredExercise) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		for(int i = 0; i < policy.exerciseData.length; i++) {
			if(policy.exerciseData[i].id.equalsIgnoreCase(alteredExercise.id)) {
				policy.exerciseData[i] = alteredExercise;
				return alteredExercise;
			}
		}
		return null;
	}


	//INSURED OBJECTS

	public InsuredObject getObjectHeader(String policyId, String objectId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		for ( InsuredObject object : alteredObjects )
			if(object.id.equalsIgnoreCase(objectId))
				return object;

		return null;
	}

	public InsuredObject loadExistingObject(String policyId, InsuredObject object) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		for ( InsuredObject oldObject : alteredObjects )
		{
			if(oldObject.id.equalsIgnoreCase(object.id)) {
				return object;
			}
		}

		InsuredObject newObject;

		try {
			newObject = new InsuredObject(object);
			newObject.headerFields = mergeHeaderArrays(new HeaderField[][] {policy.headerFields, object.headerFields},
					new boolean[] {true, false});
		} catch (Exception e) {
			return null;
		}

		newObject.change = InsuredObject.Change.MODIFIED;
		if ( policy.exerciseData != null )
			newObject.exerciseData[0].isActive = policy.exerciseData[0].isActive;
		alteredObjects.add(newObject);

		return newObject;
	}

	public InsuredObject createLocalObject(String policyId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		InsuredObject newObject;

		try {
			newObject = new InsuredObject(policy.emptyObject);
			newObject.headerFields = mergeHeaderArrays(new HeaderField[][] {policy.headerFields, newObject.headerFields},
					new boolean[] {true, false});
		} catch (Exception e) {
			return null;
		}

		newObject.change = InsuredObject.Change.CREATED;
		newObject.id = idCounter+"";
		idCounter++;
		alteredObjects.add(newObject);

		return newObject;
	}

	public InsuredObject updateObject(String policyId, InsuredObject alteredObject) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		ListIterator<InsuredObject> iterator = this.alteredObjects.listIterator();

		while(iterator.hasNext()) {
			InsuredObject object = iterator.next();
			if(object.id.equalsIgnoreCase(alteredObject.id)) {
				iterator.remove();
				alteredObject.change = InsuredObject.Change.MODIFIED;
				iterator.add(alteredObject);
				return alteredObject;
			}
		}

		return null;
	} 

	public void deleteObject(String policyId, String objectId) {
		if ( !isPolicyLoaded(policyId) )
			return;

		for( InsuredObject object : alteredObjects ) {
			if(object.id.equalsIgnoreCase(objectId)) {
				object.change = InsuredObject.Change.DELETED;
				break;
			}
		}
	}


	//CONTEXT

	public FieldContainer getContext(String policyId, String objectId, String exerciseId) {
		if ( !isPolicyLoaded(policyId) )
			return null;

		FieldContainer result;
		int exerciseIndex = -1;
		int i;

		if(exerciseId != null) {
			for( i = 0; i < policy.exerciseData.length; i++ ) {
				if(exerciseId.equalsIgnoreCase(policy.exerciseData[i].id)) {
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
						( exerciseIndex < 0 ? null : policy.exerciseData[exerciseIndex].headerFields ),
						( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].headerFields )
					}, new boolean[] {true, false});

			result.columnFields = mergeColumnArrays(new ColumnField[][] {
						policy.columnFields,
						( object == null ? null : object.columnFields ),
						( exerciseIndex < 0 ? null : policy.exerciseData[exerciseIndex].columnFields ),
						( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].columnFields)
					}, new boolean[] {true, false, true, false});

			result.extraFields = mergeExtraArrays(new ExtraField[][] {
						policy.extraFields,
						( object == null ? null : object.extraFields ),
						( exerciseIndex < 0 ? null : policy.exerciseData[exerciseIndex].extraFields ),
						( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].extraFields)
					}, new boolean[] {true, false, true, false});
		} catch (Exception e) {
			return null;
		}

		return result;
	}

	public void updateContext(String policyId, String objectId, String exerciseId, FieldContainer contents) {
		if ( !isPolicyLoaded(policyId) )
			return;

		int exerciseIndex = -1;
		int i;

		if(exerciseId != null) {
			for( i = 0; i < policy.exerciseData.length; i++ ) {
				if(exerciseId.equalsIgnoreCase(policy.exerciseData[i].id)) {
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
					( exerciseIndex < 0 ? null : policy.exerciseData[exerciseIndex].headerFields ),
					( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].headerFields )
				}, new boolean[] {true, false});

		readMergedColumnArray(contents.columnFields, new ColumnField[][] {
					policy.columnFields,
					( object == null ? null : object.columnFields ),
					( exerciseIndex < 0 ? null : policy.exerciseData[exerciseIndex].columnFields ),
					( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].columnFields)
				}, new boolean[] {true, false, true, false});

		readMergedExtraArray(contents.extraFields, new ExtraField[][] {
					policy.extraFields,
					( object == null ? null : object.extraFields ),
					( exerciseIndex < 0 ? null : policy.exerciseData[exerciseIndex].extraFields ),
					( (exerciseIndex < 0) || (object == null) ? null : object.exerciseData[exerciseIndex].extraFields)
				}, new boolean[] {true, false, true, false});
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
		for ( i = 0; i < len; i++ )
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
