package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;
import bigBang.module.generalSystemModule.client.userInterface.TaxList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter;

public class TaxManagementOperationView extends View implements TaxManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 300; //PX
	
	private FilterableList<Line> lineList;
	private FilterableList<SubLine> subLineList;
	private FilterableList<Coverage> coverageList;
	
	private TaxList taxList;
	
	private TaxForm form;
	
	public TaxManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		final NavigationPanel navPanel = new NavigationPanel();
		navPanel.setSize("100%", "100%");
		
		lineList = new FilterableList<Line>();
		lineList.setSize("100%", "100%");
		
		lineList.add(new ListEntry<Line>(null));
		
		subLineList = new FilterableList<SubLine>();
		subLineList.setSize("100%", "100%");
		
		subLineList.add(new ListEntry<SubLine>(null));
		
		subLineList.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				navPanel.navigateTo(coverageList);
			}
		});
		
		coverageList = new FilterableList<Coverage>();
		coverageList.setSize("100%", "100%");
		
		lineList.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				
				navPanel.navigateTo(subLineList);
			}
		});
		
		navPanel.setHomeWidget(lineList);
		wrapper.addWest(navPanel, LIST_WIDTH);
		
		form = new TaxForm();
		taxList = new TaxList();
		
		final VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		formWrapper.add(form.getNonScrollableContent());
		formWrapper.add(this.taxList);
		formWrapper.setCellHeight(this.taxList, "100%");
		
		form.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					formWrapper.setCellHeight(form, form.getOffsetHeight()+"px");
					GWT.log(form.getOffsetHeight() + "px");
				}
			}
		});
		
		wrapper.add(formWrapper);
		
		initWidget(wrapper);
	}


	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}
	
}
