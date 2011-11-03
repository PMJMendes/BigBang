package bigBang.module.tasksModule.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.definitions.shared.TaskStub.Status;
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
			setText(t.dueDate);
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

	public TaskSearchPanel() {
		super(((TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK)).getSearchBroker());
		ListHeader header = new ListHeader("Lista de Tarefas");
		this.setHeaderWidget(header);
		this.removedIds = new ArrayList<String>();

		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(TaskSortParameter.SortableField.STATUS, "Urgência");
		sortOptions.put(TaskSortParameter.SortableField.DUE_DATE, "Data limite");
		sortOptions.put(TaskSortParameter.SortableField.CREATION_DATE, "Data de criação");
		sortOptions.put(TaskSortParameter.SortableField.OPERATION, "Tipo de Operação");
		sortOptions.put(TaskSortParameter.SortableField.PROCESS, "Tipo de Processo");

		this.filtersPanel = new FiltersPanel(sortOptions);
		this.filtersPanel.addTypifiedListField(Filters.OPERATION, "" /*TODO FJVC*/, "Tipo de Operação");
		this.filtersPanel.addTypifiedListField(Filters.PROCESS, "" /*TODO FJVC*/, "Tipo de Processo");

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

		TaskStub s3 = new TaskStub();
		s3.description = "DESC 1";
		s3.dueDate = "2011-09-31";
		s3.timeStamp = "2011-09-05";
		s3.status = Status.PENDING;
		addSearchResult(s3);

		TaskStub s4 = new TaskStub();
		s4.description = "DESC 1";
		s4.dueDate = "2011-09-31";
		s4.timeStamp = "2011-09-05";
		s4.status = Status.URGENT;
		addSearchResult(s4);

		TaskStub s5 = new TaskStub();
		s5.description = "DESC 1";
		s5.dueDate = "2011-09-31";
		s5.timeStamp = "2011-09-05";
		s5.status = Status.VALID;
		addSearchResult(s5);
	}

	public Entry addSearchResult(TaskStub r){
		Entry entry = new Entry(r);
		add(entry);
		return entry;
	}

}
