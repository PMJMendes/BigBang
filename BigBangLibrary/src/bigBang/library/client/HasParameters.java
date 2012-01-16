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
		parameters.put(parameterId, value);
	}
	
	public void removeParameter(String parameterId){
		parameters.remove(parameterId);
	}
	
	public String getParameter(String parameterId){
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
	
}
