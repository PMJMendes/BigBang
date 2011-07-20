package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.resources.Resources;
import bigBang.module.clientModule.client.userInterface.ClientVerticalSearchPreview;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeOperationViewPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

public class ClientMergeOperationView extends View implements ClientMergeOperationViewPresenter.Display {

	public ClientMergeOperationView(){
		Resources resources = GWT.create(Resources.class);
		
		HorizontalPanel mainWrapper = new HorizontalPanel();
		mainWrapper.setSize("100%", "100%");
		
		ClientVerticalSearchPreview previewPanelOriginal = new ClientVerticalSearchPreview();
		previewPanelOriginal.setSize("100%", "100%");
		
		SimplePanel barrier = new SimplePanel();
		barrier.setSize("100%", "100%");
		
		Image barrierBackground = new Image(resources.verticalColumnBackground1());
		barrierBackground.setSize("100%", "100%");
		barrier.add(barrierBackground);
		
		ClientVerticalSearchPreview previewPanelDuplicate = new ClientVerticalSearchPreview();
		previewPanelDuplicate.setSize("100%", "100%");
		
		mainWrapper.add(previewPanelOriginal);
		mainWrapper.add(barrier);
		mainWrapper.add(previewPanelDuplicate);
		
		mainWrapper.setCellWidth(barrier, "30px");
				
		initWidget(mainWrapper);
	}

	public View getInstance() {
		return new ClientMergeOperationView();
	}

}
