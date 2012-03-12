package bigBang.library.client.userInterface.presenter;

import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ExternalRequestViewPresenter<T> implements ViewPresenter{
	
	public static enum Action{
		
	}
	
	public static interface Display{
		Widget asWidget();
		
	}
	
	protected Display view;
	protected boolean bound = false;
	private ExchangeServiceAsync service;
	
	
	public ExternalRequestViewPresenter(Display view){
		setView((UIObject)view);
		service = ExchangeService.Util.getInstance();
	}
	
	@Override
	public void setView(UIObject view){
		this.view = (Display)view;
	}
	@Override
	public void go(HasWidgets container){
		bind();
		container.clear();
		container.add(view.asWidget());
	}
	
	private void bind(){
		if(bound){
			return;
		}
		//TODO
	}
}
