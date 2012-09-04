package bigBang.library.client.history;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.ProcessNavigationMapper;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;

public class NavigationHistoryManager implements HasValueChangeHandlers<NavigationHistoryItem>{

	public static class Util {
		private static NavigationHistoryManager instance;

		public static NavigationHistoryManager getInstance() {
			if(instance == null){
				instance = new NavigationHistoryManager();
			}
			return instance;
		}
	}

	private HandlerManager handlerManager;
	private ProcessNavigationMapper mapper;

	private NavigationHistoryManager(){
		this.handlerManager = new HandlerManager(this);
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				ValueChangeEvent.fire(NavigationHistoryManager.this, new NavigationHistoryItem(event.getValue()));
			}
		});
	}

	public void fireCurrentState(){
		History.fireCurrentHistoryState();
	}

	public NavigationHistoryItem getCurrentState() {
		return new NavigationHistoryItem(History.getToken());
	} 

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<NavigationHistoryItem> handler) {
		return this.handlerManager.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		this.handlerManager.fireEvent(event);
	}

	public void go(NavigationHistoryItem item){
		String token = item.getToken();
		if(token.equals(History.getToken())){
			reload();
		}else{
			History.newItem(item.getToken(), true);
		}
	}
	
	public void reload(){
		History.fireCurrentHistoryState();
	}

	public static NavigationHistoryManager getInstance(){
		return NavigationHistoryManager.Util.getInstance();
	}

	public static boolean respectsNavigationHistoryDependencies(
			NavigationHistoryItem dependencies,
			NavigationHistoryItem historyItem) {
		if(dependencies != null && historyItem != null){
			Collection<String> dependencyParameters = dependencies.getAvailableParameters();
			for(String parameterName : dependencyParameters){
				String parameterValue = historyItem.getParameter(parameterName);
				if(parameterValue == null || !parameterValue.equalsIgnoreCase(dependencies.getParameter(parameterName))){
					return false;
				}
			}
		}
		return true;
	}
	
	public void setProcessMapper(ProcessNavigationMapper mapper) {
		this.mapper = mapper;
	}
	
	public void NavigateToProcess(String typeId, String instanceId) {
		if(this.mapper != null) {
			this.mapper.getProcessNavigationItem(typeId, instanceId, new ResponseHandler<NavigationHistoryItem>() {
				
				@Override
				public void onResponse(NavigationHistoryItem response) {
					go(response);
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}

}
