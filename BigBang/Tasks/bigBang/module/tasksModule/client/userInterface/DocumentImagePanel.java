package bigBang.module.tasksModule.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.ImageItem;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.DocumentNavigationList;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.DocuShareServiceAsync;
import bigBang.library.interfaces.FileService;
import bigBang.library.interfaces.FileServiceAsync;
import bigBang.library.shared.DocuShareItem;

public class DocumentImagePanel extends View{

	protected Image imagePanel;
	DocuShareItem currentItem;
	DocuShareItem locationItem;
	protected NavigationPanel navigationPanel;
	protected DocuShareServiceAsync service;
	protected FileServiceAsync fileService;
	protected SelectionChangedEventHandler selectionHandler;
	private boolean showSubFolders;

	public DocumentImagePanel(){
		this(true);
	}



	public DocumentImagePanel(boolean showSubFolders) {
		
		this.showSubFolders = showSubFolders;
		this.service = DocuShareService.Util.getInstance();
		this.fileService = FileService.Util.getInstance();

		currentItem = new DocuShareItem();

		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		navigationPanel = new NavigationPanel();
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
						locationItem = item;
						navigateToDirectoryList(item.handle);
					}else{
						currentItem = item;
						goToFile(item.handle);
					}
				}
			}
		};

		setHeaderText("Imagem do Documento");

		navigateToDirectoryList(null, showSubFolders);
	}



	@Override
	protected void initializeView() {
		return;
	}

	protected void showNoConnectionMessage(){
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
		panel.setImageService(DocuShareService.Util.getInstance());
		panel.setSize("100%", "100%");
		panel.showLoading(true);
		navigationPanel.navigateTo(panel);

		service.getItemAsImage(fileDesc, 0, new BigBangAsyncCallback<ImageItem>() {

			@Override
			public void onResponseSuccess(ImageItem result) {
				panel.handleImageItem(result);
				panel.showLoading(false);
			}

			@Override
			public void onResponseFailure(Throwable caught) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro. Tente novamente. Se persistir contacte o Suporte Técnico."), TYPE.ALERT_NOTIFICATION));				
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

	public void navigateToDirectoryList(String dirDesc){
		navigateToDirectoryList(dirDesc, showSubFolders);
	}

	protected void navigateToDirectoryList(final String dirDesc, final boolean showSubFolders){
		final DocumentNavigationList list = new DocumentNavigationList();
		ListHeader header = new ListHeader();
		header.showRefreshButton();
		list.setHeaderWidget(header);

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
		list.clear();
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

	public DocuShareItem getCurrentItem(){
		return currentItem;
	}

	public DocuShareItem getCurrentLocationItem() {
		return locationItem;
	}

	public void removeSelected(String handler){
		Object o = null;

		while(o instanceof DocumentNavigationList == false){
			o = navigationPanel.getPrevious();
		}


		DocumentNavigationList list = (DocumentNavigationList) o;
		navigationPanel.navigateToFirst();

		for(ListEntry<DocuShareItem> item : list){
			if(item.getValue().handle.equalsIgnoreCase(handler)){
				list.remove(item);
				break;
			}
		}

	}

	public void markDocument(String handle) {

		Image hasProblems;
		Resources resources = GWT.create(Resources.class);
		hasProblems = new Image(resources.problem());

		Object o = null;

		while(o instanceof DocumentNavigationList == false){
			o = navigationPanel.getPrevious();
		}


		DocumentNavigationList list = (DocumentNavigationList) o;

		navigationPanel.navigateToFirst();


		for(ListEntry<DocuShareItem> item : list){
			if(item.getValue().handle.equalsIgnoreCase(handle)){
				item.setLeftWidget(hasProblems);
				list.remove(item);
				list.add(list.size(), item);
				break;
			}
		}

	}

	public void setHeaderText(String text) {
		navigationPanel.navBar.setText(text);
	}

	public void showSubFolders(boolean show) {
		this.showSubFolders = show;
	}



	public NavigationPanel getPanel() {
		return navigationPanel;
	}


}
