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
import bigBang.library.client.PermissionChecker;
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
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;



public class CoverageManagementOperationViewPresenter implements ViewPresenter {


	public enum Action{
		REFRESH, 
		NEW_LINE, SAVE_LINE, CANCEL_EDIT_LINE, EDIT_LINE, DELETE_LINE, DOUBLE_CLICK_LINE,
		NEW_SUB_LINE, EDIT_SUB_LINE, SAVE_SUB_LINE, CANCEL_EDIT_SUB_LINE, DELETE_SUB_LINE, DOUBLE_CLICK_SUB_LINE,
		EDIT_COVERAGE, NEW_COVERAGE, SAVE_COVERAGE, CANCEL_EDIT_COVERAGE, DELETE_COVERAGE, DOUBLE_CLICK_COVERAGE

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

		view.setReadOnly(!PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_LINES));
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
			((CoverageList)view.getCoverageList()).setReadOnly(true);
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
						((SubLineList)view.getSubLineList()).setReadOnly(true);
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

				case REFRESH:{
					broker.requireDataRefresh();

					broker.getLines(new ResponseHandler<Line[]>() {

						@Override
						public void onResponse(Line[] response) {

						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível actualizar a lista de ramos."), TYPE.ALERT_NOTIFICATION));
						}
					});
					break;
				}

				case DELETE_LINE:{
					if(((LineList)view.getLineList()).getForm().getInfo().id == null){
						((LineList)view.getLineList()).showForm(false);
						return;
					}
					final String id = ((Entry)view.getLineList().getSelected().toArray()[0]).getValue().id;

					MessageBox.confirm("Eliminar ramo", "Tem certeza que pretende eliminar o ramo?", new MessageBox.ConfirmationCallback() {

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
					if(((LineList)view.getLineList()).getForm().getInfo().id == null){
						((LineList)view.getLineList()).showForm(false);
						return;
					}
					((LineList)view.getLineList()).getForm().revert();
					((LineList)view.getLineList()).getForm().setReadOnly(true);
					break;
				}
				case NEW_LINE:{
					((LineList)view.getLineList()).getForm().clearInfo();
					((LineList)view.getLineList()).getForm().setReadOnly(false);
					((LineList)view.getLineList()).getToolbar().setSaveModeEnabled(true);
					((LineList)view.getLineList()).showForm(true);
					break;
				}
				case DOUBLE_CLICK_LINE:{
					broker.getLine(((LineList)view.getLineList()).getClickedLine(), new ResponseHandler<Line>() {

						@Override
						public void onResponse(Line response) {
							((LineList)view.getLineList()).getForm().setValue(response);
							((LineList)view.getLineList()).showForm(true);
							((LineList)view.getLineList()).getForm().setReadOnly(true);
							((LineList)view.getLineList()).getToolbar().setSaveModeEnabled(false);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {

							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o ramo."), TYPE.ALERT_NOTIFICATION));

						}
					});

					break;
				}
				case NEW_SUB_LINE:{
					((SubLineList)view.getSubLineList()).getForm().clearInfo();
					((SubLineList)view.getSubLineList()).getForm().setReadOnly(false);
					((SubLineList)view.getSubLineList()).getToolBar().setSaveModeEnabled(true);
					((SubLineList)view.getSubLineList()).showForm(true);
					break;
				}
				case DOUBLE_CLICK_SUB_LINE:{

					broker.getSubLine(lineId, ((SubLineList)view.getSubLineList()).getClickedSubLine(), new ResponseHandler<SubLine>() {

						@Override
						public void onResponse(SubLine response) {

							((SubLineList)view.getSubLineList()).getForm().setValue(response);
							((SubLineList)view.getSubLineList()).showForm(true);
							((SubLineList)view.getSubLineList()).getForm().setReadOnly(true);
							((SubLineList)view.getSubLineList()).getToolBar().setSaveModeEnabled(false);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {

							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a modalidade."), TYPE.ALERT_NOTIFICATION));

						}
					});	
					break;
				}
				case EDIT_SUB_LINE:{
					((SubLineList)view.getSubLineList()).getForm().setReadOnly(false);
					break;
				}
				case SAVE_SUB_LINE:{
					SubLine newSubLine = ((SubLineList)view.getSubLineList()).getForm().getInfo();
					if(newSubLine.id != null){
						broker.updateSubLine(newSubLine, new ResponseHandler<SubLine>() {

							@Override
							public void onResponse(SubLine response) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Modalidade guardada com sucesso."), TYPE.TRAY_NOTIFICATION));
								((SubLineList)view.getSubLineList()).closePopup();
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a modalidade."), TYPE.ALERT_NOTIFICATION));
							}
						});
					}
					else{
						newSubLine.lineId = lineId;
						broker.addSubLine(newSubLine, new ResponseHandler<SubLine>() {

							@Override
							public void onResponse(SubLine response) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Modalidade criada com sucesso."), TYPE.TRAY_NOTIFICATION));
								NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
								((SubLineList)view.getSubLineList()).closePopup();
								navig.setParameter("lineid", response.lineId);
								navig.setParameter("sublineid", response.id);
								navig.removeParameter("coverageid");
								NavigationHistoryManager.getInstance().go(navig);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a modalidade."), TYPE.ALERT_NOTIFICATION));
							}
						});
					}
					break;

				}
				case CANCEL_EDIT_SUB_LINE:{
					if(((SubLineList)view.getSubLineList()).getForm().getInfo().id == null){
						((SubLineList)view.getSubLineList()).showForm(false);
						return;
					}
					((SubLineList)view.getSubLineList()).getForm().revert();
					((SubLineList)view.getSubLineList()).getForm().setReadOnly(true);
					break;
				}
				case DELETE_SUB_LINE:{
					if(((SubLineList)view.getSubLineList()).getForm().getInfo().id == null){
						((SubLineList)view.getSubLineList()).showForm(false);
						return;
					}
					final String id = ((SubLineList.Entry)view.getSubLineList().getSelected().toArray()[0]).getValue().id;

					MessageBox.confirm("Eliminar modalidade", "Tem certeza que pretende eliminar a modalidade?", new MessageBox.ConfirmationCallback() {

						@Override
						public void onResult(boolean result) {
							if(result){

								if(id != null){
									broker.removeSubLine(lineId, id, new ResponseHandler<SubLine>() {

										@Override
										public void onResponse(SubLine response) {

											EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Modalidade eliminada com sucesso."), TYPE.TRAY_NOTIFICATION));
											NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
											((SubLineList)view.getSubLineList()).closePopup();
											navig.removeParameter("sublineid");
											navig.removeParameter("coverageid");
											NavigationHistoryManager.getInstance().go(navig);

										}

										@Override
										public void onError(Collection<ResponseError> errors) {
											EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a modalidade."), TYPE.ALERT_NOTIFICATION));
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
				case CANCEL_EDIT_COVERAGE:{
					if(((CoverageList)view.getCoverageList()).getForm().getInfo().id == null){
						((CoverageList)view.getCoverageList()).showForm(false);
						return;
					}
					((CoverageList)view.getCoverageList()).getForm().revert();
					((CoverageList)view.getCoverageList()).getForm().setReadOnly(true);
					break;
				}
				case EDIT_COVERAGE:{
					((CoverageList)view.getCoverageList()).getForm().setReadOnly(false);
					break;
				}
				case NEW_COVERAGE:{
					((CoverageList)view.getCoverageList()).getForm().clearInfo();
					((CoverageList)view.getCoverageList()).getForm().setReadOnly(false);
					((CoverageList)view.getCoverageList()).getToolBar().setSaveModeEnabled(true);
					((CoverageList)view.getCoverageList()).showForm(true);
					break;
				}
				case SAVE_COVERAGE:{
					Coverage newCoverage = ((CoverageList)view.getCoverageList()).getForm().getInfo();
					if(newCoverage.id != null){
						broker.updateCoverage(lineId, newCoverage, new ResponseHandler<Coverage>() {

							@Override
							public void onResponse(Coverage response) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Cobertura guardada com sucesso."), TYPE.TRAY_NOTIFICATION));
								((CoverageList)view.getCoverageList()).closePopup();
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a cobertura."), TYPE.ALERT_NOTIFICATION));
							}
						});
					}
					else{
						newCoverage.subLineId = subLineId;
						broker.addCoverage(lineId, newCoverage, new ResponseHandler<Coverage>() {

							@Override
							public void onResponse(Coverage response) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Cobertura criada com sucesso."), TYPE.TRAY_NOTIFICATION));
								NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
								((CoverageList)view.getCoverageList()).closePopup();
								navig.setParameter("lineid", lineId);
								navig.setParameter("sublineid", response.subLineId);
								navig.setParameter("coverageid", response.id);				
								NavigationHistoryManager.getInstance().go(navig);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a cobertura."), TYPE.ALERT_NOTIFICATION));
							}
						});
					}
					break;
				}
				case DELETE_COVERAGE:{
					if(((CoverageList)view.getCoverageList()).getForm().getInfo().id == null){
						((CoverageList)view.getCoverageList()).showForm(false);
						return;
					}
					final String id = ((CoverageList.Entry)view.getCoverageList().getSelected().toArray()[0]).getValue().id;

					MessageBox.confirm("Eliminar cobertura", "Tem certeza que pretende eliminar a cobertura?", new MessageBox.ConfirmationCallback() {

						@Override
						public void onResult(boolean result) {
							if(result){

								if(id != null){
									broker.removeCoverage(lineId, subLineId, coverageId, new ResponseHandler<Coverage>() {

										@Override
										public void onResponse(Coverage response) {

											EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Cobertura eliminada com sucesso."), TYPE.TRAY_NOTIFICATION));
											NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
											((CoverageList)view.getCoverageList()).closePopup();
											navig.removeParameter("coverageid");
											NavigationHistoryManager.getInstance().go(navig);

										}

										@Override
										public void onError(Collection<ResponseError> errors) {
											EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a cobertura."), TYPE.ALERT_NOTIFICATION));
										}				

									});
								}else{	
									((CoverageList)view.getCoverageList()).closePopup();
								}
							}
						}
					});

					break;

					
				}
				case DOUBLE_CLICK_COVERAGE:{

					broker.getCoverage(lineId, subLineId, ((CoverageList)view.getCoverageList()).getClickedCoverage(), new ResponseHandler<Coverage>() {

						@Override
						public void onResponse(Coverage response) {

							((CoverageList)view.getCoverageList()).getForm().setValue(response);
							((CoverageList)view.getCoverageList()).showForm(true);
							((CoverageList)view.getCoverageList()).getForm().setReadOnly(true);
							((CoverageList)view.getCoverageList()).getToolBar().setSaveModeEnabled(false);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {

							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a cobertura."), TYPE.ALERT_NOTIFICATION));

						}
					});	
					break;
				}
				
				}

			}
		});
		bound = true;
	}
}
