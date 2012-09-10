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
import bigBang.definitions.shared.InsuredObject.Change;
import bigBang.library.client.DeepCopy;

public class WorkSpace {

	protected List<InsuredObject> alteredObjects;
	protected ExerciseData[] exercises;
	protected InsurancePolicy policy;

	protected int idCounter = 0;

	public WorkSpace(){
		this.alteredObjects = new ArrayList<InsuredObject>();
	}


	//INSURANCE POLICY

	public boolean isPolicyContext(String id){
		return this.policy != null && this.policy.id.equalsIgnoreCase(id);
	}

	public InsurancePolicy getWholePolicy() {
		//TODO MAJOR
		return null;
	}
	
	/**
	 * Creates the edition context
	 * @param policy
	 */
	public void editPolicy(InsurancePolicy policy) {
		this.policy = policy;
		this.exercises = policy == null ? null : policy.exerciseData;
		alteredObjects.clear();
	}

	public InsurancePolicy getPolicy(){
		if(this.policy == null) {
			return null; 
		}else{
			try {
				return DeepCopy.copy(this.policy);
			} catch (Exception e) {
				return null;
			}
		}
	}

	public InsurancePolicy updatePolicy(InsurancePolicy policy){
		if(this.policy != null && this.policy.id.equalsIgnoreCase(policy.id)) {
			this.policy = policy;
			return getPolicy();
		}
		return null;
	}


	//EXERCISES

	public ExerciseData getExercise(String id) {
		for(ExerciseData exercise : exercises) {
			if(exercise.id.equalsIgnoreCase(id)){
				return exercise;
			}
		}
		return null;
	}

	public ExerciseData[] getExercises(){
		return this.exercises;
	}

	public ExerciseData createExercise(ExerciseData exercise){
		ExerciseData newExercise = exercises[exercises.length - 1];
		boolean creationAvailable = exercises == null ? false : newExercise.id.equalsIgnoreCase("new") ? false : true;

		if(creationAvailable) {
			newExercise.id = "new";
			
			
			
			
			return newExercise;
		}else{
			return null;
		}
	}

	public ExerciseData updateExercise(ExerciseData alteredExercise) {
		for(int i = 0; i < exercises.length; i++) {
			ExerciseData exercise = exercises[i];
			if(exercise.id.equalsIgnoreCase(alteredExercise.id)) {
				exercises[i] = alteredExercise;
				return alteredExercise; //
			}
		}
		return null;
	}


	//INSURED OBJECTS

	public InsuredObject createObject() {
		InsuredObject newObject;
		try {
			newObject = DeepCopy.copy(policy.emptyObject);
		} catch (Exception e) {
			return null;
		}

		newObject.change = Change.CREATED;
		newObject.id = idCounter+"";
		idCounter++;

		alteredObjects.add(newObject);
		newObject.headerFields = (HeaderField[]) mergeArrays(new HeaderField[][] {policy.headerFields, newObject.headerFields});

		return newObject;
	}
	
	public InsuredObject manageObject(InsuredObject object) {
		InsuredObject newObject;
		try {
			newObject = DeepCopy.copy(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		alteredObjects.add(newObject);
		newObject.headerFields = (HeaderField[]) mergeArrays(new HeaderField[][] {policy.headerFields, newObject.headerFields});
		return newObject;
	}

	public InsuredObject updateObject(InsuredObject alteredObject) {
		ListIterator<InsuredObject> iterator = this.alteredObjects.listIterator();
		boolean contained = false;

		while(iterator.hasNext()) {
			InsuredObject object = iterator.next();
			if(object.id.equalsIgnoreCase(alteredObject.id)) {
				iterator.remove();
				alteredObject.change = Change.MODIFIED;
				iterator.add(alteredObject);
				contained = true;
				break;
			}
		}

		if(!contained) {
			return null;
		}

		return alteredObject;
	} 

	public void deleteObject(String objectId) {
		ListIterator<InsuredObject> iterator = this.alteredObjects.listIterator();

		while(iterator.hasNext()) {
			InsuredObject object = iterator.next();
			if(object.id.equalsIgnoreCase(objectId)) {
				object.change = Change.DELETED;
				break;
			}
		}
	}

	public InsuredObject getObject(String id) {
		ListIterator<InsuredObject> iterator = this.alteredObjects.listIterator();

		while(iterator.hasNext()) {
			InsuredObject object = iterator.next();
			if(object.id.equalsIgnoreCase(id)) {
				object.headerFields = (HeaderField[]) mergeArrays(new HeaderField[][] {policy.headerFields, object.headerFields});
				return object;
			}
		}
		return null;
	}

	protected InsuredObject[] getAlteredObjects(){
		if(policy == null) {
			return null;
		}else{
			InsuredObject[] result = new InsuredObject[this.alteredObjects.size()];
			for(int i = 0; i < result.length; i++) {
				try {
					result[i] = DeepCopy.copy(alteredObjects.get(i));
					if(result[i].change == Change.CREATED){
						result[i].id = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}
	}

	public FieldContainer getContextRelatedFields(String objectId, String exerciseId) {
		FieldContainer result = new FieldContainer();

		ExerciseData exercise = null;
		if(exerciseId != null) {
			for(ExerciseData e : exercises) {
				if(e.id.equalsIgnoreCase(exerciseId)){
					exercise = e;
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

		
		//HEADER FIELDS
		int policyHeaderFieldsCount = this.policy.headerFields.length;
		
		int exerciseHeaderFieldsCount = 0;
		if(exercise != null) {
			exerciseHeaderFieldsCount = exercise.headerFields.length;
		}

		int objectHeaderFieldsCount = 0;
		if(object != null) {
			objectHeaderFieldsCount = object.headerFields.length;
		}
		
		result.headerFields = new HeaderField[policyHeaderFieldsCount + exerciseHeaderFieldsCount + objectHeaderFieldsCount];
		for(int i = 0; i < this.policy.headerFields.length; i++) {
			result.headerFields[i] = this.policy.headerFields[i];
			result.headerFields[i].readOnly = false;
		}
		for(int i = 0; i < exerciseHeaderFieldsCount; i++) {
			int j = i + policyHeaderFieldsCount;
			result.headerFields[j] = exercise.headerFields[i];
//			result.headerFields[j].readOnly = true; //TODO
		}
		for(int i = 0; i < objectHeaderFieldsCount; i++) {
			int j = i + policyHeaderFieldsCount + exerciseHeaderFieldsCount;
			result.headerFields[j] = object.headerFields[i];
//			result.headerFields[j].readOnly = true; //TODO
		}

		
		//COLUMN FIELDS
		int policyColumnFieldsCount = this.policy.columnFields.length;
		
		int exerciseColumnFieldsCount = 0;
		if(exercise != null) {
			exerciseColumnFieldsCount = exercise.columnFields.length;
		}

		int objectColumnFieldsCount = 0;
		if(object != null) {
			objectColumnFieldsCount = object.columnFields.length;
		}
		
		result.columnFields = new ColumnField[policyColumnFieldsCount + exerciseColumnFieldsCount + objectColumnFieldsCount];
		for(int i = 0; i < this.policy.columnFields.length; i++) {
			result.columnFields[i] = this.policy.columnFields[i];
			result.columnFields[i].readOnly = false;
		}
		for(int i = 0; i < exerciseColumnFieldsCount; i++) {
			int j = i + policyColumnFieldsCount;
			result.columnFields[j] = exercise.columnFields[i];
//			result.columnFields[j].readOnly = true; //TODO
		}
		for(int i = 0; i < objectColumnFieldsCount; i++) {
			int j = i + policyColumnFieldsCount + exerciseColumnFieldsCount;
			result.columnFields[j] = object.columnFields[i];
//			result.columnFields[j].readOnly = true; //TODO
		}


		//EXTRA FIELDS
//		int policyColumnFieldsCount = this.policy.columnFields.length;
//		
//		int exerciseColumnFieldsCount = 0;
//		if(exercise != null) {
//			exerciseColumnFieldsCount = exercise.columnFields.length;
//		}
//
//		int objectColumnFieldsCount = 0;
//		if(object != null) {
//			objectColumnFieldsCount = object.columnFields.length;
//		}
		
		result.columnFields = new ColumnField[policyColumnFieldsCount + exerciseColumnFieldsCount + objectColumnFieldsCount];
		for(int i = 0; i < this.policy.columnFields.length; i++) {
			result.columnFields[i] = this.policy.columnFields[i];
			result.columnFields[i].readOnly = false;
		}
		for(int i = 0; i < exerciseColumnFieldsCount; i++) {
			int j = i + policyColumnFieldsCount;
			result.columnFields[j] = exercise.columnFields[i];
//			result.columnFields[j].readOnly = true; //TODO
		}
		for(int i = 0; i < objectColumnFieldsCount; i++) {
			int j = i + policyColumnFieldsCount + exerciseColumnFieldsCount;
			result.columnFields[j] = object.columnFields[i];
//			result.columnFields[j].readOnly = true; //TODO
		}

		return result;
	}

	private static HeaderField[] mergeArrays(HeaderField[][] parrSource)
	{
		HeaderField[] larrResult;
		int llngLen;
		int llngStart;
		int i, j;

		llngLen = 0;
		for ( i = 0; i < parrSource.length; i++ )
			llngLen += parrSource[i].length;

		larrResult = new HeaderField[llngLen];

		llngStart = 0;
		for ( i = 0; i < llngLen; i++ )
		{
			for ( j = 0; j < parrSource[i].length; j++ )
				larrResult[llngStart + j] = parrSource[i][j];
			llngStart += parrSource[i].length;
		}

		return larrResult;
	}
}
