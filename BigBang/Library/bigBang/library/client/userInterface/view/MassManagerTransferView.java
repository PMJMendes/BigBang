package bigBang.library.client.userInterface.view;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.CheckableSearchPanel;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.SelectedProcessesList;
import bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter.Action;

public abstract class MassManagerTransferView<T extends ProcessBase, T2 extends T> extends View implements MassManagerTransferViewPresenter.Display<T, T2> {

	protected static class ReceivingManagerForm extends FormView<String> {

		protected ExpandableListBoxFormField manager;

		public ReceivingManagerForm(){
			manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER);
			setReadOnly(true);
		}

		@Override
		public String getInfo() {
			return manager.getValue();
		}

		@Override
		public void setInfo(String info) {
			manager.setValue(info);
		}

	} 

	protected ActionInvokedEventHandler<Action> actionHandler;
	protected CheckableSearchPanel<T> searchPanel;
	protected SelectedProcessesList<T> selectedList; 
	protected ExpandableListBoxFormField managerSelection;
	protected HasEditableValue<T2> selectedProcessForm;
	protected BigBangOperationsToolBar toolbar;
	protected Button transferButton, clearButton;

	public MassManagerTransferView(HasEditableValue<T2> selectedProcessForm, CheckableSearchPanel<T> searchPanel, SelectedProcessesList<T> selectedList){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		this.searchPanel = searchPanel;
		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");
		searchPanelWrapper.add(new ListHeader("Lista de Processos"));
		searchPanelWrapper.add(searchPanel);
		searchPanelWrapper.setCellHeight(searchPanel, "100%");

		Button selectAllButton = new Button("Seleccionar Todos", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MassManagerTransferViewPresenter.Action>(Action.SELECT_ALL_PROCESSES));
			}
		});
		selectAllButton.setWidth("100%");
		searchPanelWrapper.add(selectAllButton);
		wrapper.addWest(searchPanelWrapper, 400);

		this.managerSelection = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Novo Gestor");
		this.managerSelection.setMandatory(true);
		((ExpandableListBoxFormField) this.managerSelection).allowEdition(false);
		this.transferButton = new Button("Transferir");

		HorizontalPanel managerSelectionWrapper = new HorizontalPanel();
		managerSelectionWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		managerSelectionWrapper.add((Widget) this.managerSelection);
		managerSelectionWrapper.add(this.transferButton);
		this.transferButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MassManagerTransferViewPresenter.Action>(Action.TRANSFER));
			}
		});

		FormView<Void> managerSelectionForm = new FormView<Void>() {

			@Override
			public void setInfo(Void info) {
				return;
			}

			@Override
			public Void getInfo() {
				return null;
			}
		};
		managerSelectionForm.addSection("Escolha de Gestor");
		managerSelectionForm.addWidget(managerSelectionWrapper);

		VerticalPanel selectedListWrapper = new VerticalPanel();
		selectedListWrapper.add(new ListHeader("Criar Transferência"));
		selectedListWrapper.setSize("100%", "100%");
		selectedListWrapper.add(managerSelectionForm.getNonScrollableContent());
		this.selectedList = selectedList;
		this.clearButton = new Button("Limpar");
		managerSelectionWrapper.add(clearButton);
		managerSelectionWrapper.setSpacing(5);
		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<MassManagerTransferViewPresenter.Action>(Action.CLEAR_SELECTED_PROCESSES));
			}
		});
		selectedListWrapper.add(this.selectedList);
		selectedListWrapper.setCellHeight(this.selectedList, "100%");
		wrapper.addEast(selectedListWrapper, 400);

		this.selectedProcessForm = selectedProcessForm;
		selectedProcessForm.setReadOnly(true);
		VerticalPanel selectedProcessFormWrapper = new VerticalPanel();
		selectedProcessFormWrapper.setSize("100%", "100%");
		selectedProcessFormWrapper.add(new ListHeader("Ficha de Processo"));
		selectedProcessFormWrapper.add((Widget) selectedProcessForm);
		selectedProcessFormWrapper.setCellHeight((Widget) selectedProcessForm, "100%");
		wrapper.add(selectedProcessFormWrapper);
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setOperationFilter(String operationId) {
		this.searchPanel.setOperationId(operationId);
	}

	@Override
	public HasValue<String> getNewManagerForm() {
		return this.managerSelection;
	}

	@Override
	public HasEditableValue<T2> getSelectedProcessForm() {
		return this.selectedProcessForm;
	}

	@Override
	public HasValueSelectables<T> getMainList() {
		return searchPanel;
	}

	@Override
	public HasValueSelectables<T> getSelectedList() {
		return selectedList;
	}

	@Override
	public HasCheckables getCheckableMainList() {
		return searchPanel;
	}

	@Override
	public void markForCheck(String id) {
		this.searchPanel.markForCheck(id);
	}

	@Override
	public void markForUncheck(String id) {
		this.searchPanel.markForUncheck(id);
	}

	@Override
	public void allowTransfer(boolean allow) {
		this.transferButton.setEnabled(allow);
		this.clearButton.setEnabled(allow);
		this.managerSelection.setReadOnlyInternal(!allow);
		this.searchPanel.setCheckable(allow);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void removeAllProcessesFromTransfer() {
		while(!this.selectedList.isEmpty()){
			this.selectedList.get(0).setChecked(false, true);
		}
	};

	@Override
	public void markAllForCheck() {
		String workspaceId = this.searchPanel.workspaceId;
		if(workspaceId != null){
			SearchDataBroker<T> broker = this.searchPanel.getSearchBroker();
			broker.getResults(workspaceId, 0, -1, new ResponseHandler<Search<T>>() {

				@Override
				public void onResponse(Search<T> response) {
					Collection<T> results = response.getResults();
					for(T result : results){
						if(!searchPanel.isMarkedForCheck(result.id)){
							searchPanel.markForCheck(result.id);
							selectedList.addEntry(result);
						}
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}

	@Override
	public void refreshMainList() {
		searchPanel.doSearch();
	}
	
}
