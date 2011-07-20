package bigBang.module.complaintModule.client.userInterface.presenter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.EventBus;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.complaintModule.client.ComplaintSection;

public class ComplaintSectionViewPresenter implements SectionViewPresenter {

	public interface Display {
		HasValue <Object> getOperationNavigationPanel();
		HasWidgets getOperationViewContainer();
		void selectOperation(OperationViewPresenter p);
		
		void createOperationNavigationItem(OperationViewPresenter operationPresenter, boolean enabled);
		Widget asWidget();
	}
	
	private ComplaintSection complaintSection;
	
	private boolean hasRegisteredOperations = false;	
	@SuppressWarnings("unused")
	private EventBus eventBus;
	private Display view;
	
	public ComplaintSectionViewPresenter(EventBus eventBus, Service service, View view) {
		this.setEventBus(eventBus);
		this.setView(view);
	}
	
	public void registerOperation(OperationViewPresenter operationPresenter) {
		this.view.createOperationNavigationItem(operationPresenter, true);
		if(!hasRegisteredOperations) { //TO SHOW THE FIRST OPERATION BY DEFAULT
			this.view.selectOperation(operationPresenter);
			operationPresenter.go(this.view.getOperationViewContainer()); 
			hasRegisteredOperations = true;
		}
	}
	
	public void setService(Service service) {
		// TODO Auto-generated method stub
	}

	public void setEventBus(final EventBus eventBus) {
		this.eventBus = eventBus;
		if(eventBus != null)
			registerEventHandlers(eventBus);
	}

	public void setView(View view) {
		this.view = (Display) view;
	}
	
	public void setSection(MenuSection s) {
		this.complaintSection = (ComplaintSection) s;
		OperationViewPresenter[] operationPresenters = this.complaintSection.getOperationPresenters();
		for(int i = 0; i < operationPresenters.length; i++)
			registerOperation(operationPresenters[i]);
	}
	
	public MenuSection getSection() {
		return this.complaintSection;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	//TODO ERASE
	public void setTarget(String targetId) {
		
	}

	public void bind() {
		this.view.getOperationNavigationPanel().addValueChangeHandler(new ValueChangeHandler<Object>() {
			
			public void onValueChange(ValueChangeEvent<Object> event) {
				((ViewPresenter)event.getValue()).go(view.getOperationViewContainer());
			}
		});
	}
	
	public void registerEventHandlers(final EventBus eventBus){

	}


}
