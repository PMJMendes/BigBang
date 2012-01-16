package bigBang.library.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasWidgets;

import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public abstract class ViewPresenterController {

	protected NavigationHistoryManager navigationManager;
	protected Map<String, ViewPresenter> presenters;
	protected HasWidgets container;
	protected NavigationHistoryItem dependencies;
	private String currentPresenterId;
	private ViewPresenter currentPresenter;

	public ViewPresenterController(){
		this(null);
	}

	public ViewPresenterController(HasWidgets container){
		this(container, null);
	}

	public ViewPresenterController(HasWidgets container, NavigationHistoryItem dependencies) {
		this.navigationManager = NavigationHistoryManager.getInstance(); 
		this.presenters = new HashMap<String, ViewPresenter>();
		setContainer(container);
		setDependencies(dependencies);
		bindToEvents();
		onNavigationHistoryEvent(navigationManager.getCurrentState());
	}

	public void setContainer(HasWidgets container){
		this.container = container;
	}

	protected void bindToEvents(){
		navigationManager.addValueChangeHandler(new ValueChangeHandler<NavigationHistoryItem>() {

			@Override
			public void onValueChange(ValueChangeEvent<NavigationHistoryItem> event) {
				onNavigationHistoryEventBypass(event.getValue());
			}
		});
	}

	protected void clearPresentation(){
		this.container.clear();
		this.currentPresenterId = null;
	}

	protected ViewPresenter present(String presenterId, HasParameters parameters){
		return this.present(presenterId, parameters, this.container, true);
	}

	protected ViewPresenter present(String presenterId, HasParameters parameters, boolean keepState){
		return this.present(presenterId, parameters, this.container, keepState);
	}

	protected ViewPresenter present(final String presenterId, final HasParameters parameters, final HasWidgets container, final boolean keepState){
		if(container == null){
			throw new RuntimeException("Could not present the screen. There is no defined container.");
		}

		ViewPresenter result = null;
		
		if(!keepState || currentPresenterId == null || !currentPresenterId.equalsIgnoreCase(presenterId)){

			ViewPresenter presenter = null;
			if(keepState && presenters.containsKey(presenterId)){
				//Gets an already bound instance
				presenter = presenters.get(presenterId);			
			}else{
				//Gets a new instance
				presenter = ViewPresenterFactory.getInstance().getViewPresenter(presenterId);
				presenters.put(presenterId, presenter);
			}
			if(this.currentPresenter != presenter){
				presenter.go(container);
			}
			presenter.setParameters(parameters);
			currentPresenterId = presenterId;
			result = presenter;

		}else if(currentPresenterId != null && currentPresenterId.equalsIgnoreCase(presenterId)){
			ViewPresenter presenter = presenters.get(presenterId);
			presenter.setParameters(parameters);
			result = presenter;
		}
		this.currentPresenter = result;
		return result;
	}

	public void setDependencies(NavigationHistoryItem deps){
		this.dependencies = deps;
	}

	public NavigationHistoryItem getDependencies(){
		return this.dependencies;
	}

	private void onNavigationHistoryEventBypass(NavigationHistoryItem historyItem){
		if(NavigationHistoryManager.respectsNavigationHistoryDependencies(this.dependencies, historyItem)){
			onNavigationHistoryEvent(historyItem);
		}
	} 

	public abstract void onParameters(HasParameters parameters);
	
	protected abstract void onNavigationHistoryEvent(NavigationHistoryItem historyItem);

}
