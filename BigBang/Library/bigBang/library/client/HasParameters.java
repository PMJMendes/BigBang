package bigBang.library.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HasParameters {

	protected Map<String, String> parameters;

	public HasParameters(){
		this.parameters = new HashMap<String, String>();
	}

	public void setParameter(String parameterId, String value){
		if(isStackParameter(parameterId)){
			throw new RuntimeException("The parameter " + parameterId + " is a stack parameter");
		}
		parameters.put(parameterId, value);
	}

	public void removeParameter(String parameterId){
		parameters.remove(parameterId);
	}

	public String getParameter(String parameterId){
		if(isStackParameter(parameterId)){
			throw new RuntimeException("The parameter " + parameterId + " is a stack parameter");
		}
		return parameters.get(parameterId);
	}

	public Collection<String> getAvailableParameters(){
		return parameters.keySet();
	}

	public HasParameters getParameters(String[] parameterIds){
		HasParameters result = new HasParameters();
		for(int i = 0; i < parameterIds.length; i++) {
			String parameterId = parameterIds[i];
			String parameterValue = parameters.get(parameterId);
			result.setParameter(parameterId, parameterValue);
		}
		return result;
	}
	
	public boolean hasSetParameter(String parameterId) {
		return this.parameters.containsKey(parameterId);
	}

	//PARAMETER STACKS

	public void setStackParameter(String parameterId){
		parameters.put(parameterId, "s:");
	}

	public boolean isStackParameter(String parameterId){
		String parameterValue = parameters.get(parameterId);
		return parameterValue != null && parameterValue.startsWith("s:");
	}

	public void pushIntoStackParameter(String parameterId, String value){
		if(value.contains("s:")){
			throw new RuntimeException("The string 's:' cannot be contained in a value used for a stack parameter");
		}else if(value.contains(".")){
			throw new RuntimeException("The character '.' cannot be used as a value in a stack parameter");
		}else if(!isStackParameter(parameterId)){
			throw new RuntimeException("The parameter " + parameterId + " is not a stack parameter");
		}else{
			String serializedValue = parameters.get(parameterId);
			serializedValue += value + ".";
			parameters.put(parameterId, serializedValue);
		}
	}

	public String peekInStackParameter(String parameterId){
		if(isStackParameter(parameterId)){
			String serializedValue = parameters.get(parameterId);
			if(serializedValue != null) {
				String values = serializedValue.substring(2);
				String[] stackEntries = values.split("\\.");
				String result = stackEntries.length > 0 ? stackEntries[stackEntries.length - 1] : null;
				return result;
			}
		}
		return null;
	}
	
	public String popFromStackParameter(String parameterId){
		if(!isStackParameter(parameterId)){
			throw new RuntimeException("The parameter " + parameterId + " is not a stack parameter");
		}else{
			String serializedValue = parameters.get(parameterId);
			if(serializedValue != null) {
				String[] stackEntries = serializedValue.substring(2).split("\\.");
				String newStack = new String("s:");
				for(int i = 0; i < stackEntries.length - 1; i++) {
					newStack += stackEntries[i] + ".";
				}
				parameters.put(parameterId, newStack);
				String result = stackEntries.length > 0 ? stackEntries[stackEntries.length - 1] : null;
				return result;
			}
		}
		return null;
	}

}
