package bigBang.module.tasksModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.tasksModule.client.userInterface.TaskSearchPanel;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TasksSectionView extends View implements TasksSectionViewPresenter.Display {
	
	private final int LIST_WIDTH = 400; //px
	
	protected HasWidgets container;
	protected TaskSearchPanel searchPanel;
	protected ListHeader containerHeader;
	
	public TasksSectionView() {
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		SplitLayoutPanel splitPanel = new SplitLayoutPanel();
		splitPanel.setSize("100%", "100%");

		SimplePanel tasksListWrapper = new SimplePanel();
		tasksListWrapper.setSize("100%", "100%");

		searchPanel = new TaskSearchPanel();
		searchPanel.setSize("100%", "100%");
		tasksListWrapper.setWidget(searchPanel);
		
		splitPanel.addWest(tasksListWrapper, LIST_WIDTH);
		splitPanel.setWidgetMinSize(tasksListWrapper, LIST_WIDTH);
		
		VerticalPanel contentWrapper = new VerticalPanel();
		contentWrapper.setSize("100%", "100%");

		containerHeader = new ListHeader();

		contentWrapper.add(this.containerHeader);
		
		SimplePanel previewPanel = new SimplePanel();
		previewPanel.setSize("100%", "100%");
		contentWrapper.add(previewPanel);
		contentWrapper.setCellHeight(previewPanel, "100%");

		previewPanel.setStyleName("emptyContainer");
		container = previewPanel;
		
		splitPanel.add(contentWrapper);
		
		wrapper.add(splitPanel);
		wrapper.setCellHeight(splitPanel, "100%");
		
		this.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					searchPanel.doSearch();
				}
			}
		});

		initWidget(wrapper);
	}

	@Override
	public HasWidgets getOperationViewContainer() {
		return container;
	}

	@Override
	public HasValueSelectables<TaskStub> getTaskList() {
		return this.searchPanel;
	}

	@Override
	public void clear() {
		this.container.clear();
		this.containerHeader.setText("");
	}

	@Override
	public void presentTaskScreen(Task task, ViewPresenter presenter) {
		this.containerHeader.setText(task.description);
		presenter.go(this.container);
	}

	@Override
	public void showTaskDone() {
		MessageBox.info("", "Tarefa completa");
	}

}
