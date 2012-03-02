package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.DocumentNavigationList;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.DocuShareServiceAsync;
import bigBang.library.interfaces.FileService;
import bigBang.library.interfaces.FileServiceAsync;
import bigBang.library.shared.DocuShareItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiptImagePanel extends View {

	protected Image imagePanel;
	protected NavigationPanel navigationPanel;
	protected DocuShareServiceAsync service;
	protected FileServiceAsync fileService;
	protected SelectionChangedEventHandler selectionHandler;

	public ReceiptImagePanel(){
		this.service = DocuShareService.Util.getInstance();
		this.fileService = FileService.Util.getInstance();

		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		navigationPanel = new NavigationPanel();
		navigationPanel.navBar.setText("Imagem do Recibo");
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
						navigateToDirectoryList(item.handle);
					}else{
						goToFile(item.handle);
					}
				}
			}
		};

		navigateToDirectoryList(null, false);
	}
	
	@Override
	protected void initializeView() {}

	protected void setImageSource(String src) {
		imagePanel.setUrl(src);
	}

	protected void showNoConnectionMessage() {
		VerticalPanel widget = new VerticalPanel();
		widget.setSize("100%", "100%");
		widget.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		widget.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Label label = new Label("Não foi possível aceder ao DocuShare");

		widget.add(label);
		navigationPanel.setHomeWidget(widget);
	}

	protected void goToFile(String fileDesc){
		final ImageHandlerPanel panel = new ImageHandlerPanel();
		panel.setSize("100%", "100%");
		panel.showLoading(true);
		navigationPanel.navigateTo(panel);

		service.getItemAsImage(fileDesc, new BigBangAsyncCallback<String>() {

			@Override
			public void onResponseSuccess(String result) {
				panel.setImage(GWT.getModuleBaseURL() + FileService.GET_PREFIX + result);
				panel.showLoading(false);
			}
		});
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
		}else{
			navigationPanel.navigateTo(list);
		}
		list.showLoading(true);
		service.getItems(dirDesc, showSubFolders, new BigBangAsyncCallback<DocuShareItem[]>() {

			@Override
			public void onResponseSuccess(DocuShareItem[] result) {
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

}
