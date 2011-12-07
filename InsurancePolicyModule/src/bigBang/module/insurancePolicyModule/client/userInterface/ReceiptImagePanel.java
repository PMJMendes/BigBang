package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.DocumentNavigationList;
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

		navigateToDirectoryList(null);
	}

	protected void setImageSource(String src) {
		imagePanel.setUrl(src);
	}

	protected void showNoConnectionMessage() {

	}

	protected void goToFile(String fileDesc){
		service.getItem(fileDesc, new BigBangAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + FileService.GET_PREFIX + result);
				try {
					builder.sendRequest(null, new RequestCallback() {
						@Override 
						public void onResponseReceived(Request request,
								Response response) {
							showMessage("Ficheiro Recebido");
						}

						@Override
						public void onError(Request request, Throwable exception) {
							showMessage("Erro ao receber o ficheiro");
						}
					});
				} catch (RequestException e) {
					e.printStackTrace();
				}
			}
		});
	}	

	protected void navigateToDirectoryList(final String dirDesc){
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
		this.service.getSubFolders(dirDesc, new BigBangAsyncCallback<DocuShareItem[]>() {

			@Override
			public void onSuccess(DocuShareItem[] result) {
				for(int i = 0; i < result.length; i++){
					list.addEntryForItem(result[i]);
				}
				service.getItems(dirDesc, new BigBangAsyncCallback<DocuShareItem[]>() {

					@Override
					public void onSuccess(DocuShareItem[] result) {
						for(int i = 0; i < result.length; i++){
							list.addEntryForItem(result[i]);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						showNoConnectionMessage();
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				showNoConnectionMessage();
			}
		});
	}

}
