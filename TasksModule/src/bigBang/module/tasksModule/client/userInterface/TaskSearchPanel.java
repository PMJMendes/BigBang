package bigBang.module.tasksModule.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;
import bigBang.module.tasksModule.client.dataAccess.TasksDataBrokerClient;
import bigBang.module.tasksModule.client.resources.Resources;
import bigBang.module.tasksModule.shared.TaskSearchParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter.SortableField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;

public class TaskSearchPanel extends SearchPanel<TaskStub> implements TasksDataBrokerClient {

	public static class Entry extends ListEntry<TaskStub> {

		protected Image statusIndicator;
		protected String defaultBGColor;

		public Entry(TaskStub value) {
			super(value);
			setRightWidget(statusIndicator);
			defaultBGColor = this.getElement().getStyle().getBackgroundColor();
			setHeight("40px");
		}

		public <I extends Object> void setInfo(I info) {
			if(statusIndicator == null){
				statusIndicator = new Image();
			}
			Resources r = GWT.create(Resources.class);
			TaskStub t = (TaskStub) info;
			setTitle(t.description);
			setText(t.dueDate.substring(0, 10));
			statusIndicator.setVisible(true);
			this.getElement().getStyle().setBackgroundColor(this.defaultBGColor);
			switch(t.status){
			case VALID:
				statusIndicator.setVisible(false);
				break;
			case PENDING:
				statusIndicator.setResource(r.pendingSmallIcon());
				statusIndicator.setTitle("Pendente");
				break;
			case URGENT:
				this.getElement().getStyle().setBackgroundColor("#FFCC99");
				statusIndicator.setResource(r.urgentSmallIcon());
				statusIndicator.setTitle("Urgente");
				break;
			default:
				statusIndicator.setVisible(false);
				break;
			}
		};
	}

	protected static enum Filters {
		PROCESS,
		OPERATION
	} 

	protected int taskDataVersion;
	protected FiltersPanel filtersPanel;
	protected Collection<String> removedIds;
	protected TasksBroker broker;

	public TaskSearchPanel() {
		super(((TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK)).getSearchBroker());
		this.broker = ((TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK));
		broker.registerClient(this);

		ListHeader header = new ListHeader("Lista de Tarefas");
		this.setHeaderWidget(header);
		this.removedIds = new ArrayList<String>();

		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(TaskSortParameter.SortableField.STATUS, "Urgência");
		sortOptions.put(TaskSortParameter.SortableField.DUE_DATE, "Data limite");
		sortOptions.put(TaskSortParameter.SortableField.CREATION_DATE, "Data de criação");

		this.filtersPanel = new FiltersPanel(sortOptions);
		this.filtersPanel.addTypifiedListField(Filters.PROCESS, BigBangConstants.TypifiedListIds.PROCESS_TYPE, "Tipo de Processo");
		this.filtersPanel.addTypifiedListField(Filters.OPERATION, BigBangConstants.TypifiedListIds.OPERATION_TYPE, "Tipo de Operação", Filters.PROCESS);

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch();
			}
		});

		this.filtersContainer.clear();
		this.filtersContainer.add(filtersPanel);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.TASK)){
			taskDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.TASK)){
			return taskDataVersion;
		}
		return -1;
	}

	@Override
	public void addTask(Task task) {
		this.add(0, new Entry(task));
	}

	@Override
	public void updateTask(Task task) {
		for(ValueSelectable<TaskStub> e : this){
			if(e.getValue().id.equalsIgnoreCase(task.id)){
				e.setValue(task);
				break;
			}
		}
	}
	
	@Override
	public void removeTask(String id) {
		this.removedIds.add(id.toUpperCase());
		for(ValueSelectable<TaskStub> e : this){
			if(e.getValue().id.equalsIgnoreCase(id)){
				this.remove(e);
				break;
			}
		}
	}

	@Override
	public void doSearch() {
		if(this.workspaceId != null){
			this.broker.getSearchBroker().disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}
		this.removedIds.clear();

		TaskSearchParameter parameter = new TaskSearchParameter();
		parameter.freeText = new String();
		parameter.operationId = (String) filtersPanel.getFilterValue(Filters.OPERATION);
		parameter.processId = (String) filtersPanel.getFilterValue(Filters.PROCESS);

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};
		SortParameter[] sorts = new SortParameter[] {
				new TaskSortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
		};
		doSearch(parameters, sorts);
	}

	@Override
	public void onResults(Collection<TaskStub> results) {
		for(TaskStub s : results) {
			if(!this.removedIds.contains(s.id.toUpperCase())){
				addSearchResult(s);
			}
		}
	}

	public Entry addSearchResult(TaskStub r){
		Entry entry = new Entry(r);
		add(entry);
		return entry;
	}

}
