package bigBang.library.client.history;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import bigBang.library.client.HasParameters;

public class NavigationHistoryItem extends HasParameters{

	public static final String PARAMETER_DELIMITER = ";";
	public static final String VALUE_DELIMITER = "=";

	private Collection<String> tokens;

	public NavigationHistoryItem(){
		this(null);
	}

	public NavigationHistoryItem(String token){
		super();
		tokens = new ArrayList<String>();
		setToken(token);
	}

	public void setToken(String token){
		parameters.clear();
		tokens.clear();
		parse(token);
	}

	private void parse(String token){
		if(token != null){
			StringTokenizer parameterTokenizer = new StringTokenizer(token, PARAMETER_DELIMITER);
			while(parameterTokenizer.hasMoreTokens()){
				String parameterToken = parameterTokenizer.nextToken();
				StringTokenizer valueTokenizer = new StringTokenizer(parameterToken, VALUE_DELIMITER);
				while(valueTokenizer.hasMoreTokens()){
					String valueName = valueTokenizer.nextToken();
					String value = "";
					if(valueTokenizer.hasMoreTokens()){
						value = valueTokenizer.nextToken();
					}
					setParameter(valueName, value);
				}
			}
		}
	}

	@Override
	public void setParameter(String tokenParameterId, String value){
		tokenParameterId = tokenParameterId.toLowerCase();
		if(value != null){
			value = value.toLowerCase();
			if(value.isEmpty()){
				value = null;
			}
		}
		if(value == null){
			removeParameter(tokenParameterId);
		}else{
			super.setParameter(tokenParameterId, value);
			if(!tokens.contains(tokenParameterId)){
				tokens.add(tokenParameterId);
			}
		}
	}

	@Override
	public void removeParameter(String tokenParameterId){
		super.removeParameter(tokenParameterId);
		tokens.remove(tokenParameterId);
	}

	public String getToken(){
		return getParameter(null);
	}

	@Override
	public String getParameter(String tokenParameterId){
		String result = null;
		if(tokenParameterId == null){
			result = serializeToken();
		}else{
			result = super.getParameter(tokenParameterId.toLowerCase());
		}
		return result;
	}
	
	@Override
	public Collection<String> getAvailableParameters(){
		return this.tokens;
	}

	private String serializeToken(){
		StringBuilder builder = new StringBuilder();
		for(String token : tokens){
			String value = parameters.get(token);
			builder.append(token + VALUE_DELIMITER + value + PARAMETER_DELIMITER);
		}
		return builder.toString();
	}

	@Override
	public void setStackParameter(String parameterId) {
		parameterId = parameterId.toLowerCase();
		if(!tokens.contains(parameterId)){
			tokens.add(parameterId);
		}
		super.setStackParameter(parameterId);
	}
	
}
