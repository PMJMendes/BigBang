package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.TaxList;
import bigBang.module.generalSystemModule.client.userInterface.view.TaxForm;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TaxManagementOperationViewPresenter implements ViewPresenter{

	public enum Action{
		DELETE_TAX, NEW_TAX, EDIT_TAX, SAVE_TAX, CANCEL_EDIT_TAX, LINE_LIST_ATTACH, SUB_LINE_LIST_ATTACH, COVERAGE_LIST_ATTACH, DOUBLE_CLICK_TAX;
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
		void updateTaxInList(Tax tax);		


		void setReadOnly(boolean readOnly);
		Widget asWidget();


		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		FilterableList<SubLine> getSubLineList();

		FilterableList<Coverage> getCoverageList();

		NavigationPanel getNavPanel();

		TaxList getTaxList();

		TaxForm getForm();

		FilterableList<Line> getLineList();

		boolean isReadOnly();

		void setCoverages(Coverage[] coverages);

		void setSubLines(SubLine[] subLines);

		void setTaxes(Tax[] taxes);

		String getClickedTax();

		BigBangOperationsToolBar getToolBar();
	}

	private String lineId; 
	private String subLineId;
	private String coverageId; 

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
		view.getTaxList().setReadOnly(false);
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
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<TaxManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(
					ActionInvokedEvent<bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter.Action> action) {

				switch(action.getAction()){
				case NEW_TAX: {
					view.getForm().setValue(new Tax());
					view.getForm().setCoverageId(coverageId);
					view.getForm().setReadOnly(false);
					view.getToolBar().setSaveModeEnabled(true);
					view.showForm(true);
					break;
				}
				case LINE_LIST_ATTACH:{
					ListHeader header = new ListHeader();
					header.setText("Ramos");
					header.setHeight("25px");
					view.getLineList().setHeaderWidget(header);
					view.getLineList().clearSelection();
					break;
				}
				case SUB_LINE_LIST_ATTACH:{
					ListHeader header = new ListHeader();
					header.setText("Modalidades");
					header.setHeight("25px");
					view.getSubLineList().setHeaderWidget(header);
					view.getLineList().clearSelection();
					break;
				}
				case COVERAGE_LIST_ATTACH:{
					ListHeader header = new ListHeader();
					header.setText("Coberturas");
					header.setHeight("25px");
					view.getCoverageList().setHeaderWidget(header);
					view.getCoverageList().clearSelection();
					if(!view.isReadOnly())
						view.getTaxList().getNewButton().setEnabled(true);
					break;
				}
				case DOUBLE_CLICK_TAX:{
					broker.getTax(lineId, subLineId, coverageId,  view.getClickedTax(), new ResponseHandler<Tax>() {

						@Override
						public void onResponse(Tax response) {
							view.getForm().setCoverageId(response.coverageId);
							view.getForm().setReadOnly(true);
							view.getToolBar().setSaveModeEnabled(false);
							view.showForm(true);
							view.getForm().setValue(response);

						}

						@Override
						public void onError(Collection<ResponseError> errors) {

							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o campo de cobertura."), TYPE.ALERT_NOTIFICATION));

						}
					});	
					break;
				}
				case CANCEL_EDIT_TAX:{
					if(view.getForm().getValue().id == null){
						view.showForm(false);
						return;
					}
					view.getForm().revert();
					view.getForm().setReadOnly(true);
					view.getToolBar().setSaveModeEnabled(false);
					break;
				}
				case EDIT_TAX:{
					view.getForm().setReadOnly(false);
					break;
				}

				case SAVE_TAX:{

					Tax newTax = view.getForm().getInfo();
					if(newTax.id == null){
						broker.addTax(lineId, subLineId, view.getForm().getInfo(), new ResponseHandler<Tax>(){

							@Override
							public void onResponse(Tax response) {
								view.showForm(false);
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Campo de cobertura criado com sucesso."), TYPE.TRAY_NOTIFICATION));
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o campo de cobertura."), TYPE.ALERT_NOTIFICATION));

							}


						});
					}
					else{
						broker.updateTax(lineId, subLineId, newTax, new ResponseHandler<Tax>(){

							@Override
							public void onResponse(Tax response) {
								view.showForm(false);
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Campo de cobertura gravado com sucesso."), TYPE.TRAY_NOTIFICATION));
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar o campo de cobertura."), TYPE.ALERT_NOTIFICATION));

							}


						});
					}
					break;
				}
				case DELETE_TAX:{
					if(view.getForm().getInfo().id != null){
						broker.removeTax(lineId,subLineId, coverageId, view.getClickedTax(), new ResponseHandler<Tax>(){

							@Override
							public void onResponse(Tax response) {
							
								view.showForm(false);
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Campo de cobertura eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
								
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o campo de cobertura."), TYPE.ALERT_NOTIFICATION));
							}
							
							
							
							
						});
					}
					else{
						view.showForm(false);
					}
				}
				}
			}


		});

		view.getSubLineList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					SubLine subLine = ((ValueSelectable<SubLine>) s).getValue();
					subLineId = subLine.id;
					view.setCoverages(subLine.coverages);
					view.getNavPanel().navigateTo(view.getCoverageList());
					view.getTaxList().setSubLineId(subLineId);
				}
			}
		});

		view.getLineList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					Line line = ((ValueSelectable<Line>) s).getValue();
					lineId = line.id;
					view.setSubLines(line.subLines);
					view.getNavPanel().navigateTo(view.getSubLineList());
					view.getTaxList().setLineId(lineId);
				}
			}
		});

		view.getCoverageList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					Coverage coverage = ((ValueSelectable<Coverage>) s).getValue();
					coverageId = coverage.id;
					view.setTaxes(coverage.taxes);
					view.getTaxList().setCoverageId(coverageId);
				}
			}
		});
		bound = true;
	}

}
