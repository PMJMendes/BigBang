package bigBang.library.client.userInterface;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.DocumentNavigationList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.DocuShareServiceAsync;
import bigBang.library.interfaces.FileService;
import bigBang.library.interfaces.FileServiceAsync;
import bigBang.library.shared.DocuShareItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DocuShareNavigationPanel extends View implements HasValue<DocuShareItem> {

	protected NavigationPanel navigationPanel;
	protected DocuShareServiceAsync service;
	protected FileServiceAsync fileService;
	protected SelectionChangedEventHandler selectionHandler;
	private String ownerId;
	private String ownerTypeId;
	private String filename;

	public DocuShareNavigationPanel(){
		this.service = DocuShareService.Util.getInstance();
		this.fileService = FileService.Util.getInstance();

		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		navigationPanel = new NavigationPanel();
		navigationPanel.navBar.setText("Selecção do Ficheiro");
		navigationPanel.navBar.setHeight("30px");
		navigationPanel.setSize("100%", "100%");

		wrapper.add(navigationPanel);

		this.selectionHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<DocuShareItem> selectable = (ValueSelectable<DocuShareItem>) event.getFirstSelected();
				if(selectable != null){
					DocuShareItem item = selectable.getValue();
					if(item.directory){
						navigateToDirectoryList(item.handle, true);
					}else{
						ValueChangeEvent.fire(DocuShareNavigationPanel.this, item);
					}
				}
			}
		};
	}

	@Override
	protected void initializeView() {}

	protected void showNoConnectionMessage() {
		VerticalPanel widget = new VerticalPanel();
		widget.setSize("100%", "100%");
		widget.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		widget.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Label label = new Label("Não foi possível aceder ao DocuShare");

		widget.add(label);
		navigationPanel.setHomeWidget(widget);
	}

	protected void showFileCannotBeDisplayedMessage() {
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		wrapper.add(new Label("O ficheiro não pode ser apresentado"));
		navigationPanel.navigateTo(wrapper);
	}

	protected void navigateToDirectoryList(String dirDesc){
		navigateToDirectoryList(dirDesc, true);
	}

	protected void navigateToDirectoryList(final String dirDesc, final boolean showSubFolders){
		final DocumentNavigationList list = new DocumentNavigationList();
		ListHeader header = new ListHeader();
		header.showRefreshButton();
		list.setHeaderWidget(header); //TODO

		header.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fetchListContent(list, dirDesc, showSubFolders);
			}
		});

		list.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					list.clearSelection();
				}
			}
		});
		list.setSize("100%", "100%");
		list.addSelectionChangedEventHandler(this.selectionHandler);

		fetchListContent(list, dirDesc, showSubFolders);
	}

	protected void fetchListContent(final DocumentNavigationList list, final String dirDesc, final boolean showSubFolders){
		if(dirDesc == null){
			navigationPanel.setHomeWidget(list);
			list.showLoading(true);
			service.getContext(ownerId, ownerTypeId, new BigBangAsyncCallback<DocuShareItem[]>() {

				@Override
				public void onSuccess(DocuShareItem[] result) {
					for(int i = 0; i < result.length; i++){
						list.addEntryForItem(result[i]);
					}
					list.showLoading(false);	
				}

				@Override
				public void onFailure(Throwable caught) {
					super.onFailure(caught);
					showNoConnectionMessage();
					list.showLoading(false);
				}



			});
			navigationPanel.navigateTo(list);
		}else{
			navigationPanel.navigateTo(list);
			list.showLoading(true);
			service.getItems(dirDesc, showSubFolders, new BigBangAsyncCallback<DocuShareItem[]>() {

				@Override
				public void onSuccess(DocuShareItem[] result) {
					for(int i = 0; i < result.length; i++){
						list.addEntryForItem(result[i]);
					}
					list.showLoading(false);
				}

				@Override
				public void onFailure(Throwable caught) {
					super.onFailure(caught);
					showNoConnectionMessage();
					list.showLoading(false);
				}
			});
		}
	}


	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<DocuShareItem> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public DocuShareItem getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(DocuShareItem value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(DocuShareItem value, boolean fireEvents) {
		// TODO Auto-generated method stub

	}

	public void setParameters(String ownerId, String ownerTypeId) {
		this.ownerId = ownerId;
		this.ownerTypeId = ownerTypeId;
		navigateToDirectoryList(null, true);
	}

}
