package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.DocumentNavigationList;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.DocuShareServiceAsync;
import bigBang.library.interfaces.FileService;
import bigBang.library.interfaces.FileServiceAsync;
import bigBang.library.shared.DocuShareItem;

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
		wrapper.setSize("100%", "100%");

		navigationPanel = new NavigationPanel();
		navigationPanel.navBar.setText("Imagem do Recibo");
		navigationPanel.navBar.setHeight("30px");
		navigationPanel.setSize("100%", "100%");

		wrapper.add(navigationPanel);

		initWidget(wrapper);

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
			public void onSuccess(String result) {
				panel.setImage(GWT.getModuleBaseURL() + FileService.GET_PREFIX + result);
				panel.showLoading(false);
				
//				final ScrollPanel wrapper = new ScrollPanel();
//				wrapper.setSize("100%", "100%");
//				wrapper.getElement().getStyle().setBackgroundColor("#000");
//
//				final Image image = new Image();
//				image.addAttachHandler(new AttachEvent.Handler() {
//					
//					@Override
//					public void onAttachOrDetach(AttachEvent event) {
//						if(image.getOffsetHeight() > image.getOffsetWidth()){
//							image.setHeight(wrapper.getOffsetHeight() - 25 + "px");
//						}else{
//							image.setWidth("100%");
//						}
//					}
//				});
//				
//				MouseWheelHandler mouseWheelHandler = new MouseWheelHandler() {
//
//					@Override
//					public void onMouseWheel(MouseWheelEvent event) {
//						event.preventDefault();
//						double scale = event.getDeltaY() * 0.05;
//						
//						int originX = wrapper.getOffsetWidth();
//						int originY = wrapper.getOffsetHeight();
//						int originZ = 1;
//
//						double newWidth = (image.getOffsetWidth() - (image.getOffsetWidth() * scale));
//						double newHeight = (image.getOffsetHeight() - (image.getOffsetHeight() * scale));
//
//						if(newWidth < wrapper.getOffsetWidth() || newHeight < wrapper.getOffsetHeight()){
//							fitImage(wrapper, image);
//							return;
//						}else{
//							image.setSize((image.getOffsetWidth() - (image.getOffsetWidth() * scale)) + "px", (image.getOffsetHeight() - (image.getOffsetHeight() * scale)) + "px");
//						}
//						
//						int viewportOriginX = -image.getElement().getOffsetLeft();
//						int viewportOriginY = -image.getElement().getOffsetTop();
//						
//						int newViewportOriginX = (int) (scale * viewportOriginX);
//						int newViewportOriginY = (int) (scale * viewportOriginY);
//						
//						GWT.log(newViewportOriginX + "-" + newViewportOriginY);
//						
//						if(newViewportOriginX < 0){
//							newViewportOriginX = 0;
//						}
//						if(newViewportOriginY < 0){
//							newViewportOriginY = 0;
//						}
//						
//						wrapper.setHorizontalScrollPosition(newViewportOriginX);
//						wrapper.setVerticalScrollPosition(newViewportOriginY);
//					}
//				};
//				
//				wrapper.addHandler(mouseWheelHandler, MouseWheelEvent.getType());
//				image.addMouseWheelHandler(mouseWheelHandler);
//				
//				VerticalPanel imageWrapper = new VerticalPanel();
//				imageWrapper.setSize("100%", "100%");
//				imageWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//				imageWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//				imageWrapper.add(image);
//				
//				wrapper.add(imageWrapper);
//				
//				image.setUrl(GWT.getModuleBaseURL() + FileService.GET_PREFIX + result);
//				navigationPanel.navigateTo(wrapper);

				//				image.addLoadHandler(new LoadHandler() {
				//					
				//					@Override
				//					public void onLoad(LoadEvent event) {
				//						navigationPanel.navigateTo((Widget) event.getSource());
				//					}
				//				});
				//				image.addErrorHandler(new ErrorHandler() {
				//					
				//					@Override
				//					public void onError(ErrorEvent event) {
				//						showFileCannotBeDisplayedMessage();
				//					}
				//				});

				//TODO FAZER O DISCARD

				//				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + FileService.GET_PREFIX + result);
				//				try {
				//					builder.sendRequest(null, new RequestCallback() {
				//						@Override 
				//						public void onResponseReceived(Request request,
				//								Response response) {
				//							showMessage("Ficheiro Recebido");
				//						}
				//
				//						@Override
				//						public void onError(Request request, Throwable exception) {
				//							showMessage("Erro ao receber o ficheiro");
				//						}
				//					});
				//				} catch (RequestException e) {
				//					e.printStackTrace();
				//				}
			}
		});
	}	

	private void fitImage(Widget canvas, Widget image){
		double imageWidth = image.getOffsetWidth();
		double imageHeight = image.getOffsetHeight();
		double canvasWidth = canvas.getOffsetWidth();
		double canvasHeight = canvas.getOffsetHeight();
		
		if(imageWidth > imageHeight){
			image.setWidth(canvasWidth + "px");
			image.setHeight("");
		}else{
			image.setWidth("");
			image.setHeight(canvasHeight + "px");
		}
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

	protected void navigateToDirectoryList(final String dirDesc, boolean showSubFolders){
		final DocumentNavigationList list = new DocumentNavigationList();
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

		if(dirDesc == null){
			navigationPanel.setHomeWidget(list);
		}else{
			navigationPanel.navigateTo(list);
		}
		list.showLoading(true);
		this.service.getItems(dirDesc, showSubFolders, new BigBangAsyncCallback<DocuShareItem[]>() {

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
