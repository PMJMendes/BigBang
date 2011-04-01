package bigBang.module.tasksModule.client.userInterface.view;

import java.util.ArrayList;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.tasksModule.client.userInterface.TaskList;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter;
import bigBang.module.tasksModule.shared.Task;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TasksSectionView extends View implements TasksSectionViewPresenter.Display {
	
	private final int LIST_WIDTH = 400; //px
	
	private HasWidgets container;
	private TaskList tasksList;
	
	public TasksSectionView() {
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		SplitLayoutPanel splitPanel = new SplitLayoutPanel();
		splitPanel.setSize("100%", "100%");

		SimplePanel tasksListWrapper = new SimplePanel();
		tasksListWrapper.setSize("100%", "100%");

		tasksList = new TaskList();
		tasksList.setSize("100%", "100%");
		tasksListWrapper.setWidget(tasksList);
		
		splitPanel.addWest(tasksListWrapper, LIST_WIDTH);
		splitPanel.setWidgetMinSize(tasksListWrapper, LIST_WIDTH);
		
		SimplePanel previewPanel = new SimplePanel();
		previewPanel.setSize("100%", "100%");
		splitPanel.add(previewPanel);

		previewPanel.setStyleName("emptyContainer");
		container = previewPanel;
		
		wrapper.add(splitPanel);
		wrapper.setCellHeight(splitPanel, "100%");
		
		initWidget(wrapper);
	}

	public HasWidgets getOperationViewContainer() {
		return container;
	}

	public void setListEntries(ArrayList<Task> tasks) {
		//tasksList.setEntries(tasks);
	}

}
