package bigBang.library.client.userInterface.form;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Attachment;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.view.DocumentView;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.PopupPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConversationForm extends FormView<Conversation> implements DocumentsBrokerClient{

	protected ExpandableListBoxFormField requestType;
	protected TextBoxFormField subject;
	private TextBoxFormField pendingAction;
	protected NumericTextBoxFormField replyLimit;
	private RichTextAreaFormField text;
	private String directionText = "";
	private TextBoxFormField messageSubject;
	public FormViewSection messageSection;
	private Button printButton;
	private HorizontalPanel emailAndAttachmentsWrapper;
	private PopupPanel popup;
	private DocumentViewPresenter documentViewPresenter;
	private DocumentsBroker documentBroker;
	protected bigBang.library.client.userInterface.List<Document> attachments;


	public ConversationForm() {
		addSection("Troca de Mensagens");
		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Pedido");
		subject = new TextBoxFormField("Tópico");
		pendingAction = new TextBoxFormField("Acção Pendente");
		pendingAction.setFieldWidth("150px");
		pendingAction.setEditable(false);
		replyLimit = new NumericTextBoxFormField("Prazo", false);
		replyLimit.setUnitsLabel("dias");
		replyLimit.setFieldWidth("50px");
		printButton = new Button("Imprimir Troca de Mensagens");

		HorizontalPanel panel = new HorizontalPanel();

		panel.add(subject);
		panel.add(printButton);

		panel.setCellWidth(printButton, "100%");
		printButton.getElement().getStyle().setRight(10, Unit.PX);
		panel.setCellHorizontalAlignment(printButton, HasHorizontalAlignment.ALIGN_CENTER);

		registerFormField(subject);

		addWidget(panel);
		addFormField(requestType, false);
		addLineBreak();
		addFormField(pendingAction, true);
		addFormField(replyLimit, true);

		messageSection = new FormViewSection("");
		addSection(messageSection);

		VerticalPanel innerWrapper = new VerticalPanel();

		messageSubject = new TextBoxFormField("Assunto da Mensagem");
		messageSubject.setEditable(false);
		text = new RichTextAreaFormField();
		text.setEditable(false);
		text.getNativeField().setSize("98%", "600px");
		
		emailAndAttachmentsWrapper = new HorizontalPanel();
		attachments = new List<Document>();

		documentViewPresenter = new DocumentViewPresenter(new DocumentView());

		attachments.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(event.getFirstSelected() != null){

					Document doc = ((DocumentsList.Entry)event.getFirstSelected()).getValue();

					HasParameters params = new HasParameters();
					params.setParameter("ownerid", doc.ownerId);
					params.setParameter("ownertypeid",doc.ownerTypeId);
					params.setParameter("documentid", doc.id);
					params.setParameter("emailId", doc.emailId);
					params.setParameter("folderId", doc.emailFolderId);
					params.setParameter("attId", doc.emailAttId);
					popup = new PopupPanel();

					documentViewPresenter.setParameters(params);
					documentViewPresenter.go(popup);					
					popup.center();				
					documentViewPresenter.allowEdit(false);

				}
			}
		});

		ListHeader firstHeader = new ListHeader("Anexos");		
		attachments.setHeaderWidget(firstHeader);

		innerWrapper.add(messageSubject);
		innerWrapper.add(text);
		innerWrapper.setSize("100%", "100%");

		emailAndAttachmentsWrapper.add(innerWrapper);
		emailAndAttachmentsWrapper.add(attachments);

		emailAndAttachmentsWrapper.setSize("100%", "100%");

		messageSection.addWidget(emailAndAttachmentsWrapper);

		documentBroker = (DocumentsBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DOCUMENT);

		setValidator(new ConversationFormValidator(this));

	}

	@Override
	public Conversation getInfo() {
		Conversation conv = value;

		conv.subject = subject.getValue();
		conv.requestTypeId = requestType.getValue();
		try{
			conv.replylimit = replyLimit.getValue().intValue();
		}catch(Exception e){
			conv.replylimit = null;
		}		
		return conv;
	}

	@Override
	public void setInfo(Conversation info) {

		subject.setValue(info.subject);
		requestType.setValue(info.requestTypeId);
		replyLimit.setValue(info.replylimit != null ? info.replylimit.doubleValue() : null);
		pendingAction.setValue(ConversationStub.Direction.INCOMING.equals(info.pendingDir) ? "Receber Mensagem" : "Enviar Mensagem");

		setAttachments(info.messages[0].attachments, info.messages[0].folderId);
	}

	private void setAttachments(Attachment[] info, String folderId) {
		boolean b = true;
		
		attachments.clear();
		
		if(info.length > 0){

			for(Message.Attachment att : info){
				if ( att.ownerId != null ){
					if ( b ){
						documentBroker.registerClient(this, att.ownerId);
						b = false;
					}

					if ( att.docId != null ){
						documentBroker.getDocument(att.ownerId, att.docId, new ResponseHandler<Document>() {
		
							@Override
							public void onResponse(Document response) {
								addAttachment(new DocumentsList.Entry(response));
							}
		
							@Override
							public void onError(Collection<ResponseError> errors) {
								return;
							}
						});
					}
				}else{
					Document doc = new Document();
					doc.name = att.name;
					doc.docTypeLabel = "";
					doc.creationDate = att.date;
					doc.emailAttId = att.attachmentId;
					doc.emailId = att.emailId;
					doc.emailFolderId = folderId;
					addAttachment(new DocumentsList.Entry(doc));
				}
			}
		}		
	}

	protected void addAttachment(DocumentsList.Entry entry) {
		if(entry.getValue().id != null){ 
			for(ListEntry<Document>  document: attachments){
				if(document.getValue().id.equalsIgnoreCase(entry.getValue().id)){
					return;
				}
			}
		}

		attachments.add(entry);		
	}

	public void setCurrentMessage(Message info){
		directionText = ConversationStub.Direction.INCOMING.equals(info.direction) ? " recebida" : " enviada";
		messageSection.setHeaderText("Mensagem nº. " + (info.order+1) + directionText + " a: " + info.date);
		text.setValue(info.text);
		messageSubject.setValue(info.subject);
		messageSection.setVisible(true);
		setAttachments(info.attachments, info.folderId);
	}

	public Message getMessage() {
		Message message = new Message();

		return message;
	}

	public HasClickHandlers getPrintButton() {
		return printButton;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return -1;
	}

	@Override
	public int getDocumentsDataVersionNumber(String ownerId) {
		return 0;
	}

	@Override
	public void setDocumentsDataVersionNumber(String ownerId, int number) {
		return;
	}

	@Override
	public void setDocuments(String ownerId, java.util.List<Document> documents) {
		return;
	}

	@Override
	public void removeDocument(String ownerId, Document document) {
		return;
	}

	@Override
	public void addDocument(String ownerId, Document document) {
		return;
	}

	@Override
	public void updateDocument(String ownerId, Document document) {
		return;
	}



}
