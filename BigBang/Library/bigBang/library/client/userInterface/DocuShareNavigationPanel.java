package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ScanHandle;
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
import bigBang.library.shared.ScanItem;

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

public class DocuShareNavigationPanel extends View implements HasValue<ScanItem> {

	protected NavigationPanel navigationPanel;
	protected DocuShareServiceAsync service;
	protected FileServiceAsync fileService;
	protected SelectionChangedEventHandler selectionHandler;
	private String ownerId;
	private String ownerTypeId;
	private DocumentNavigationList list;
	private String fileHandle;
	private String currentDirDesc;

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
				ValueSelectable<ScanItem> selectable = (ValueSelectable<ScanItem>) event.getFirstSelected();
				if(selectable != null){
					ScanItem item = selectable.getValue();
					if(item.directory){
						navigateToDirectoryList(item.handle, true);
					}else{
						fileHandle = item.handle;
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
		list = new DocumentNavigationList();
		list.setLocation(dirDesc);
		list.showFilterField(false);
		list.showSearchField(false);
		list.getElement().getStyle().setBackgroundColor("white");
		ListHeader header = new ListHeader();
		header.showRefreshButton();
		list.setHeaderWidget(header); 

		header.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				list.clear();
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

	public DocumentNavigationList getList() {
		return list;
	}

	protected void fetchListContent(final DocumentNavigationList list, final String dirDesc, final boolean showSubFolders){
		boolean changeContainer;
		
		if(currentDirDesc != null){
			changeContainer = !currentDirDesc.equalsIgnoreCase(dirDesc);
		}else{
			changeContainer = dirDesc != null;
		}

		currentDirDesc = dirDesc;

		if(dirDesc == null) {
			navigationPanel.setHomeWidget(list);
		}

		if(dirDesc == null && ownerId != null && ownerTypeId != null){
			list.showLoading(true);
			service.getContext(ownerId, ownerTypeId, new BigBangAsyncCallback<ScanItem[]>() {

				@Override
				public void onResponseSuccess(ScanItem[] result) {
					for(int i = 0; i < result.length; i++){
						list.addEntryForItem(result[i]);
					}
					list.showLoading(false);	
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					super.onResponseFailure(caught);
					showNoConnectionMessage();
					list.showLoading(false);
				}



			});
		}else{
			list.showLoading(true);
			service.getItems(dirDesc, showSubFolders, new BigBangAsyncCallback<ScanItem[]>() {

				@Override
				public void onResponseSuccess(ScanItem[] result) {
					for(int i = 0; i < result.length; i++){
						list.addEntryForItem(result[i]);
					}
					list.showLoading(false);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					super.onResponseFailure(caught);
					showNoConnectionMessage();
					list.showLoading(false);
				}
			});
		}

		if(changeContainer){
			navigationPanel.navigateTo(list);
		}
	}


	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ScanItem> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}


	public void setParameters(String ownerId, String ownerTypeId) {
		this.ownerId = ownerId;
		this.ownerTypeId = ownerTypeId;
		navigateToDirectoryList(null, true);
	}

	public ScanHandle getDocuShareHandle() {
		ScanHandle handle = new ScanHandle();
		handle.docushare = true;
		handle.locationHandle = getList().getLocation();
		handle.handle = getHandle();
		return handle;
	}

	private String getHandle() {
		return fileHandle;
	}

	@Override
	public ScanItem getValue() {
		return null;
	}

	@Override
	public void setValue(ScanItem value) {
		return;

	}

	@Override
	public void setValue(ScanItem value, boolean fireEvents) {
		return;
	}

}
