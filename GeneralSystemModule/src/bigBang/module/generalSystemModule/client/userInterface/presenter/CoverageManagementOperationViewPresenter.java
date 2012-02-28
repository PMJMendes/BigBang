package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
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
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.LineList;
import bigBang.module.generalSystemModule.client.userInterface.SubLineList;
import bigBang.module.generalSystemModule.client.userInterface.CoverageList;
import bigBang.module.generalSystemModule.client.userInterface.LineList.Entry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;



public class CoverageManagementOperationViewPresenter implements ViewPresenter {


	public enum Action{
		DELETE_LINE,
		REFRESH, SAVE_LINE, CANCEL_EDIT_LINE, EDIT_LINE
	};

	private CoverageBroker broker;
	private String lineId; 
	private String subLineId;
	private String coverageId; 
	private boolean ignoreListeners;

	public interface Display {
		//Lists
		HasValueSelectables<Line> getLineList();
		HasValueSelectables<SubLine> getSubLineList();
		HasValueSelectables<Coverage> getCoverageList();		

		//General
		void setReadOnly(boolean readonly);
		void clear();
		Widget asWidget();
		void setLine(String lineId);
		void setSubLine(String lineId, String subLineId);
		void setCoverage(String lineId, String subLineId, String coverageId);
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionEventHandler);
	}

	private Display view;
	private boolean bound = false;

	public CoverageManagementOperationViewPresenter(Display view) {
		setView((View) view);
		broker = (CoverageBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		view.setReadOnly(false); //TODO ISTO TEM DE VIR DE FORA
		lineId = parameterHolder.getParameter("lineid");
		subLineId = parameterHolder.getParameter("sublineid");
		coverageId = parameterHolder.getParameter("coverageid");

		ignoreListeners = true;

		if(coverageId != null){
			view.setCoverage(lineId, subLineId, coverageId);
			broker.getCoverages(lineId, subLineId, new ResponseHandler<Coverage[]>() {

				@Override
				public void onResponse(Coverage[] response) {
					((LineList)view.getLineList()).setLineSelected(lineId);
					((CoverageList)view.getCoverageList()).setCoverages(response);

					broker.getSubLines(lineId, new ResponseHandler<SubLine[]>() {

						@Override
						public void onResponse(SubLine[] response) {
							((LineList)view.getLineList()).setLineSelected(lineId);
							((SubLineList)view.getSubLineList()).setSubLines(response);
							ignoreListeners = false;
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista pedida."), TYPE.ALERT_NOTIFICATION));
						}
					});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista pedida."), TYPE.ALERT_NOTIFICATION));
				}
			});



		}else if(subLineId != null){
			((CoverageList)view.getCoverageList()).clear();
			view.setSubLine(lineId, subLineId);
			broker.getSubLines(lineId, new ResponseHandler<SubLine[]>() {

				@Override
				public void onResponse(SubLine[] response) {
					((LineList)view.getLineList()).setLineSelected(lineId);
					((SubLineList)view.getSubLineList()).setSubLines(response);
					((CoverageList)view.getCoverageList()).setSubLines(response);
					ignoreListeners = false;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista pedida."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
		else{
			view.setLine(lineId);
			((CoverageList)view.getCoverageList()).clear();
			((SubLineList)view.getSubLineList()).clear();
			broker.getLines(new ResponseHandler<Line[]>() {

				@Override
				public void onResponse(Line[] response) {
					if(lineId != null){
						((LineList)view.getLineList()).setLineSelected(lineId);
						((SubLineList)view.getSubLineList()).setLines(response);
						ignoreListeners = false;
					}
					else{
						((LineList)view.getLineList()).clearSelection();
						ignoreListeners = false;
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista pedida."), TYPE.ALERT_NOTIFICATION));

				}
			});
		}

	}

	public void bind() {
		if(bound){return;}

		view.getLineList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0 || ignoreListeners){
					return;
				}
				for(Selectable s : selected) {
					ValueSelectable<Line> vs = (ValueSelectable<Line>)s;
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.setParameter("lineid", vs.getValue().id);
					navig.removeParameter("sublineid");
					navig.removeParameter("coverageid");
					NavigationHistoryManager.getInstance().go(navig);
					break;
				}
			}
		});
		view.getSubLineList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {

				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0 || ignoreListeners){		
					return;
				}

				for(Selectable s : selected) {
					ValueSelectable<SubLine> vs = (ValueSelectable<SubLine>)s;
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.setParameter("lineid", vs.getValue().lineId);
					navig.setParameter("sublineid", vs.getValue().id);
					navig.removeParameter("coverageid");
					NavigationHistoryManager.getInstance().go(navig);
					break;
				}
			}
		});
		view.getCoverageList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0 || ignoreListeners){
					return;
				}

				for(Selectable s : selected) {
					ValueSelectable<Coverage> vs = (ValueSelectable<Coverage>)s;
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.setParameter("lineid", lineId);
					navig.setParameter("sublineid", vs.getValue().subLineId);
					navig.setParameter("coverageid", vs.getValue().id);
					NavigationHistoryManager.getInstance().go(navig);
					break;
				}

			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<CoverageManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){

				case DELETE_LINE:{
					final String id = ((Entry)view.getLineList().getSelected().toArray()[0]).getValue().id;

					MessageBox.confirm("Eliminar Ramo", "Tem certeza que pretende eliminar o ramo?", new MessageBox.ConfirmationCallback() {

						@Override
						public void onResult(boolean result) {
							if(result){

								if(id != null){
									broker.removeLine(id, new ResponseHandler<Line>() {

										@Override
										public void onResponse(Line response) {

											EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ramo eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
											NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
											((LineList)view.getLineList()).closePopup();
											navig.removeParameter("lineid");
											navig.removeParameter("sublineid");
											navig.removeParameter("coverageid");
											NavigationHistoryManager.getInstance().go(navig);

										}

										@Override
										public void onError(Collection<ResponseError> errors) {
											EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o ramo."), TYPE.ALERT_NOTIFICATION));
										}				

									});
								}else{	
									((LineList)view.getLineList()).closePopup();
								}
							}
						}
					});

					break;

				}
				case SAVE_LINE:{
					Line newLine = ((LineList)view.getLineList()).getForm().getInfo();
					if(newLine.id != null){
						broker.updateLine(newLine, new ResponseHandler<Line>() {

							@Override
							public void onResponse(Line response) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ramo guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
								((LineList)view.getLineList()).closePopup();
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar o ramo."), TYPE.ALERT_NOTIFICATION));
							}
						});
					}
					else{
						broker.addLine(newLine, new ResponseHandler<Line>() {

							@Override
							public void onResponse(Line response) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ramo criado com sucesso."), TYPE.TRAY_NOTIFICATION));
								NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
								((LineList)view.getLineList()).closePopup();
								navig.setParameter("lineid", response.id);
								navig.removeParameter("sublineid");
								navig.removeParameter("coverageid");
								NavigationHistoryManager.getInstance().go(navig);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o ramo."), TYPE.ALERT_NOTIFICATION));
							}
						});
					}
					break;
				}
				case EDIT_LINE:{
					((LineList)view.getLineList()).getForm().setReadOnly(false);
					break;
				}
				case CANCEL_EDIT_LINE:{
					((LineList)view.getLineList()).getForm().revert();
					break;
				}

				}

			}
		});
		bound = true;
	}
}
