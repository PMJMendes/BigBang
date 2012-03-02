package bigBang.library.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class ViewPresenterFactory {

	public static class Util {
		private static ViewPresenterFactory instance;

		public static ViewPresenterFactory getInstance(){
			if(instance == null){
				instance = new ViewPresenterFactory();
			}
			return instance;
		}
	}

	public Map<String, ViewPresenterInstantiator> presenterInstantiators;

	public ViewPresenterFactory(){
		this.presenterInstantiators = new HashMap<String, ViewPresenterInstantiator>();
	}

	public void registerViewPresenterInstantiator(String presenterId, ViewPresenterInstantiator instantiator){
		boolean contains = this.presenterInstantiators.containsKey(presenterId);
		presenterInstantiators.put(presenterId, instantiator);
		if(contains){
			GWT.log("A view presenter instantiator was replaced in ViewPresenterFactory. Presenter id : " + presenterId);
		}
	}

	public ViewPresenter getViewPresenter(String presenterId){
		ViewPresenter result = null;
		if(this.presenterInstantiators.containsKey(presenterId)){
			result = presenterInstantiators.get(presenterId).getInstance();
		}else{
			throw new RuntimeException("The ViewPresenterInstantiator for id " + presenterId + " was not found");
		}
		return result;
	}

	public static ViewPresenterFactory getInstance(){
		return ViewPresenterFactory.Util.getInstance();
	}

}
