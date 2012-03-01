package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.LineList;
import bigBang.module.generalSystemModule.client.userInterface.TaxList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TaxManagementOperationViewPresenter implements ViewPresenter{
	
	public enum Action{
		DELETE_TAX, SELECTED_COVERAGE, SELECTED_LINE, SELECTED_SUBLINE, NEW_TAX, EDIT_TAX, SAVE_TAX, CANCEL_EDIT_TAX, DELETE_SUB_LINE;
	}
	
	public interface Display {
		void setLines(Line[] lines);
		
		//Buttons
		HasClickHandlers getNewButton();
	
		String getCurrentCoverageId();
	
		//Form
		HasValue<Tax> getTaxForm();
		void showForm(boolean show);
		void lockForm(boolean lock);
		
		//GENERAL
		void clear();
		
		//List
		void removeTaxFromList(Tax tax);
		void addTaxToList(Tax tax);
		void updateTaxInList(Tax tax);		
		
		
		
		void setReadOnly(boolean readOnly);
		Widget asWidget();

		TaxList getList();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
	}
	
	private String lineId; 
	private String subLineId;
	private String coverageId; 
	private String taxId;
	
	CoverageBroker broker;
	private Display view;
	private boolean bound = false;

	public TaxManagementOperationViewPresenter(Display view) {
		broker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		setView((UIObject) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		
		view.setReadOnly(false); //TODO ISTO TEM DE VIR DE FORA
		broker.getLines(new ResponseHandler<Line[]>() {
			
			@Override
			public void onResponse(Line[] response) {
				view.setLines(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de ramos."), TYPE.ALERT_NOTIFICATION));	
			}
		});
		
	}
	
	public void bind() {
		if(bound){return;
		}
		view.registerActionHandler(new ActionInvokedEventHandler<TaxManagementOperationViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(
					ActionInvokedEvent<bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter.Action> action) {

				switch(action.getAction()){
				case NEW_TAX: {
					view.getList().getForm().clearInfo();
					view.getList().getForm().setReadOnly(false);
					view.getList().getToolbar().setSaveModeEnabled(true);
					view.getList().showForm(true);
					break;
				}
				}
			}
		});
		
		bound = true;
	}

}
