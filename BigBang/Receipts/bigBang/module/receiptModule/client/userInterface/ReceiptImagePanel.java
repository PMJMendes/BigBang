package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.ImageItem;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Session;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.ScanItemService;
import bigBang.library.interfaces.ScanItemServiceAsync;
import bigBang.library.shared.ScanItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiptImagePanel extends View implements HasAllFocusHandlers {

	protected Image imagePanel;
	ScanItem currentItem;
	protected NavigationPanel navigationPanel;
	protected ScanItemServiceAsync service;
	protected SelectionChangedEventHandler selectionHandler;
	protected FocusPanel focusWrapper;

	public ReceiptImagePanel(){
		this.service = Session.hasDocuShare() ? DocuShareService.Util.getInstance() : ScanItemService.Util.getInstance();

		currentItem = new ScanItem();

		focusWrapper = new FocusPanel();
		focusWrapper.setSize("100%", "100%");
		initWidget(focusWrapper);
		
		VerticalPanel wrapper = new VerticalPanel();
		focusWrapper.add(wrapper);
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
				ValueSelectable<ScanItem> selectable = (ValueSelectable<ScanItem>) event.getFirstSelected();
				if(selectable != null){
					ScanItem item = selectable.getValue();
					if(item.directory){
						navigateToDirectoryList(item.handle);
					}else{
						currentItem = item;
						goToFile(item.handle);
					}
				}
			}
		};

		navigateToDirectoryList(null, false);
	}

	public NavigationPanel getNavigationPanel() {
		return navigationPanel;
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

		Label label = new Label("Não foi possível aceder ao Repositório");

		widget.add(label);
		navigationPanel.setHomeWidget(widget);

	}

	protected void goToFile(String fileDesc){
		final ImageHandlerPanel panel = new ImageHandlerPanel();
		panel.setImageService(service);
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

	public void showNoImageReceipt() {
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		wrapper.add(new Label("A inserir recibos sem imagem"));
		navigationPanel.setHomeWidget(wrapper);
	}

	public void navigateToDirectoryList(String dirDesc){
		navigateToDirectoryList(dirDesc, true);
	}

	protected void navigateToDirectoryList(final String dirDesc, final boolean showSubFolders){
		final ReceiptNavigationList list = new ReceiptNavigationList();
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

	protected void fetchListContent(final ReceiptNavigationList list, final String dirDesc, final boolean showSubFolders){
		if(dirDesc == null){
			navigationPanel.setHomeWidget(list);
		}else{
			navigationPanel.navigateTo(list);
		}
		list.clear();
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

	public ScanItem getCurrentItem(){
		return currentItem;
	}

	public void removeSelected(String handler){
		Object o = null;
		
		while(o instanceof ReceiptNavigationList == false){
			o = navigationPanel.getPrevious();
		}
			
		
		ReceiptNavigationList list = (ReceiptNavigationList) o;
		navigationPanel.navigateToFirst();
		
		for(ListEntry<ScanItem> item : list){
			if(item.getValue().handle.equalsIgnoreCase(handler)){
				list.remove(item);
				break;
			}
		}
	}

	public void markReceipt(String handle) {
		
		Image hasProblems;
		Resources resources = GWT.create(Resources.class);
		hasProblems = new Image(resources.problem());
		
		Object o = null;
		
		while(o instanceof ReceiptNavigationList == false){
			o = navigationPanel.getPrevious();
		}
			
		
		ReceiptNavigationList list = (ReceiptNavigationList) o;

		navigationPanel.navigateToFirst();

		
		for(ListEntry<ScanItem> item : list){
			if(item.getValue().handle.equalsIgnoreCase(handle)){
				item.setLeftWidget(hasProblems);
				list.remove(item);
				list.add(list.size(), item);
				break;
			}
		}
		
			

	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return focusWrapper.addFocusHandler(handler);
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return focusWrapper.addBlurHandler(handler);
	}
	
}
