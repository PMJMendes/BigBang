package bigBang.library.client.userInterface;

import bigBang.definitions.shared.Document;
import bigBang.library.client.DocumentManager;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class DocumentsPreviewList extends List<Document> {
	protected class DocumentPreviewPanel extends NavigationPanel {

		protected Label nameLabel;
		protected Label valueLabel;
		protected ExpandableListBoxFormField type;
		protected DocumentManager documentManager;

		public DocumentPreviewPanel(DocumentManager documentManager){
			this();
			setDocumentManager(documentManager);
		}

		public DocumentPreviewPanel(){
			super();
			setSize("100%", "100%");
			navBar.setText("Documento");
		}

		public void setDocumentManager(DocumentManager documentManager) {
			this.documentManager = documentManager;
		}

		public void setDocument(Document document) {
			
		}
	}

	protected Resources resources;
	protected PopupPanel documentPopupPanel;
	protected SelectedStateChangedEventHandler documentSelectedStateChangedHandler;
	protected DocumentManager manager;
	protected HandlerRegistration managerHandlerRegistration;
	protected Button newButton;

	public DocumentsPreviewList(){
		super();

		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		resources = GWT.create(Resources.class);
		ListHeader header = new ListHeader();
		header.setText("Documentos");
		header.setHeight("25px");
		header.setLeftWidget(new Image(resources.documentsIcon()));

		setHeaderWidget(header);

		setSize("100%", "145px");

		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		documentPopupPanel = new PopupPanel();
		documentPopupPanel.setAutoHideEnabled(true);
		documentPopupPanel.setSize("350px", "400px");

		/*documentSelectedStateChangedHandler = new SelectedStateChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectedStateChanged(final SelectedStateChangedEvent event) {
				if(!event.getSelected()){
					documentPopupPanel.hide();
					((ListEntry<?>) event.getSource()).setSelected(false);
				}else{
					DocumentPreviewPanel documentPreviewPanel = new DocumentPreviewPanel(DocumentsPreviewList.this.manager);
					documentPreviewPanel.setDocument(((ValueSelectable<Document>)event.getSource()).getValue());
					documentPopupPanel.setWidget(documentPreviewPanel);
					documentPopupPanel.setPopupPositionAndShow(new PositionCallback() {

						@Override
						public void setPosition(int offsetWidth, int offsetHeight) {
							documentPopupPanel.setPopupPosition(((UIObject) event.getSource()).getAbsoluteLeft() - offsetWidth,
									((UIObject) event.getSource()).getAbsoluteTop() + 10);
						}
					});
				}
			}
		};

		documentPopupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				for(ListEntry<?> e : entries){
					if(e.isSelected())
						e.setSelected(false);
				}
			}
		});*/

		newButton = new Button("Novo");
		newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				/*bigBang.library.client.userInterface.view.PopupPanel popup = new bigBang.library.client.userInterface.view.PopupPanel();
				DocumentPreviewPanel documentPreviewPanel = new DocumentPreviewPanel(DocumentsPreviewList.this.manager);
				documentPreviewPanel.setDocument(new Document());
				popup.add(documentPreviewPanel);
				popup.setSize("600px", "600px");
				popup.center();*/
			}
		});
		header.setRightWidget(newButton);
	}

	public DocumentsPreviewList(DocumentManager documentManager) {
		this();
		setManager(documentManager);
	}

	public void setManager(DocumentManager manager) {
		this.manager = manager;
		//if(this.managerHandlerRegistration != null)
		//	this.managerHandlerRegistration = this.manager.
		refresh();
	}

	public void refresh(){
		for(Document c : this.manager.getDocuments()){
			addEntryForDocument(c);
		}
	}

	protected void addEntryForDocument(Document c){
		ListEntry<Document> e = new ListEntry<Document>(c) {
			@Override
			public void setSelected(boolean selected, boolean fireEvents) {
				super.setSelected(selected, fireEvents);
				if(selected) {
					//setLeftWidget(new Image(resources.phoneSmallIconWhite()));
				}else{
					//setLeftWidget(new Image(resources.phoneSmallIconBlack()));
				}
			}
		};
		//e.setLeftWidget(new Image(resources.phoneSmallIconBlack()));
		e.setText(c.name);
		e.setToggleable(true);
		e.addSelectedStateChangedEventHandler(documentSelectedStateChangedHandler);
		e.setHeight("25px");
		add(e);
	}

	public void setReadOnly(boolean readOnly) {
		this.newButton.setEnabled(!readOnly);
	}
}
