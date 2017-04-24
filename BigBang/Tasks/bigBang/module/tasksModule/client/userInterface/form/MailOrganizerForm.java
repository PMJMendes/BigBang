package bigBang.module.tasksModule.client.userInterface.form;

import java.util.Iterator;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MailOrganizerForm extends FormView<Document>{

	public class DocumentDetailEntry extends ListEntry<DocInfo>{

		protected TextBoxFormField info;
		protected TextBoxFormField infoValue;
		private Button remove;
		private CheckBoxFormField displayAtPortal;
		public DocumentDetailEntry(DocInfo docInfo) {
			super(docInfo);
			this.setHeight("40px");
			this.setSelectable(false);
		}


		@Override
		public void setValue(DocInfo docInfo) {
			if(docInfo == null){
				this.setLeftWidget(add);
				super.setValue(docInfo);
				return;	
			}

			setInfo(new TextBoxFormField());
			setInfoValue(new TextBoxFormField());
			setDisplayAtPortal(new CheckBoxFormField());

			getInfo().setValue(docInfo.name);
			getInfoValue().setValue(docInfo.value);
			getDisplayAtPortal().setValue(docInfo.displayAtPortal);

			remove = new Button("X");
			remove.addClickHandler( new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onRemoveListEntry(DocumentDetailEntry.this);
				}
			});

			info.getNativeField().addKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					event.stopPropagation();
				}
			});

			infoValue.getNativeField().addKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					event.stopPropagation();
				}
			});

			this.info.setFieldWidth("");
			this.infoValue.setFieldWidth("100%");
			this.remove.setWidth("20px");
			super.setValue(docInfo);
			this.setLeftWidget(getInfo());
			this.setWidget(getInfoValue());
			this.setRightWidget(remove);
		}

		public void setEditable(boolean editable){

			if(getInfo() == null){
				this.setVisible(editable);
				return;
			}
			getInfo().setReadOnly(!editable);
			getInfoValue().setReadOnly(!editable);
			getDisplayAtPortal().setReadOnly(!editable);
			remove.setVisible(editable);
			add.setVisible(editable);
		}


		@Override
		public DocInfo getValue() {
			DocInfo docInfo = new DocInfo();
			docInfo.name = info.getValue();
			docInfo.value = infoValue.getValue();
			docInfo.displayAtPortal = displayAtPortal.getValue();
			return docInfo;
		}

		public TextBoxFormField getInfo() {
			return info;
		}

		public void setInfo(TextBoxFormField info) {
			this.info = info;
		}

		public TextBoxFormField getInfoValue() {
			return infoValue;
		}

		public void setInfoValue(TextBoxFormField infoValue) {
			this.infoValue = infoValue;
		}
		
		public CheckBoxFormField getDisplayAtPortal() {
			return displayAtPortal;
		}

		public void setDisplayAtPortal(CheckBoxFormField displayAtPortal) {
			this.displayAtPortal = displayAtPortal;
		}

	}

	protected ListBoxFormField referenceType;
	@SuppressWarnings("rawtypes")
	protected FormField[] references;
	private HorizontalPanel referenceWrapper;
	protected TextBoxFormField name;
	protected ExpandableListBoxFormField docType;
	protected CheckBoxFormField displayAtPortal;
	List<DocInfo> details;
	DocInfo[] docInfo;
	private Button add;

	public MailOrganizerForm() {

		referenceType = new ListBoxFormField("");
		referenceType.addItem("Seguradora", BigBangConstants.EntityIds.INSURANCE_AGENCY);
		referenceType.addItem("Mediador", BigBangConstants.EntityIds.MEDIATOR);
		referenceType.addItem("Cliente", BigBangConstants.EntityIds.CLIENT);
		referenceType.addItem("Apólice nº.", BigBangConstants.EntityIds.INSURANCE_POLICY);
		referenceType.addItem("Apólice Adesão nº.", BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		referenceType.addItem("Recibo nº.", BigBangConstants.EntityIds.RECEIPT);
		referenceType.addItem("Sinistro nº.", BigBangConstants.EntityIds.CASUALTY);
		referenceType.addItem("Sub-Sinistro nº.", BigBangConstants.EntityIds.SUB_CASUALTY);
		referenceType.addItem("Despesa de Saúde nº.", BigBangConstants.EntityIds.EXPENSE);

		addSection("Referência");

		references = new FormField[9];
		referenceWrapper = new HorizontalPanel();
		referenceWrapper.add(referenceType);
		setReferences();

		referenceType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				changeReference(event.getValue());
			}
		});

		addWidget(referenceWrapper);
		registerFormField(referenceType);		
		addSection("Informação Geral");

		name = new TextBoxFormField("Nome");
		docType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, "Tipo");
		docType.allowEdition(false);
		name.setWidth("390px");
		name.setFieldWidth("390px");
		
		displayAtPortal = new CheckBoxFormField("Mostrar no Portal");
		
		addFormField(name);
		addFormField(docType);
		addFormField(displayAtPortal);
		
		addSection("Detalhes");

		details = new List<DocInfo>();
		details.setSelectableEntries(false);
		details.setSize("400px", "300px");

		add = new Button("Adicionar Detalhe");
		add.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onAddDetail();				
			}
		});

		addWidget(details);
		details.add(new DocumentDetailEntry(null));

		setValidator(new MailOrganizerFormValidator(this));
	}

	protected void changeReference(String value) {

		for(int i = 0; i<references.length; i++){
			references[i].setVisible(false);
			references[i].clear();
		}
		
		if(value == null){
			return;
		}
		
		if(value.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_AGENCY)){
			references[0].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.MEDIATOR)){
			references[1].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			references[2].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
			references[3].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			references[4].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
			references[5].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
			references[6].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
			references[7].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
			references[8].setVisible(true);
		}
	}

	@SuppressWarnings("unchecked")
	private void setReferences() {
		//AGENCY
		references[0] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, new HasParameters());
		referenceWrapper.add(references[0]);
		registerFormField(references[0]);
		//MEDIATOR
		references[1] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.MEDIATOR, new HasParameters());
		referenceWrapper.add(references[1]);
		registerFormField(references[1]);
		//CLIENT
		references[2] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.CLIENT, new HasParameters());
		referenceWrapper.add(references[2]);
		registerFormField(references[2]);
		//POLICY 
		references[3] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, new HasParameters());
		referenceWrapper.add(references[3]);
		registerFormField(references[3]);
		//SUB POLICY 
		references[4] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, new HasParameters());
		referenceWrapper.add(references[4]);
		registerFormField(references[4]);
		//RECEIPT
		references[5] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.RECEIPT, new HasParameters());
		referenceWrapper.add(references[5]);
		registerFormField(references[5]);
		//CASUALTY
		references[6] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.CASUALTY, new HasParameters());
		referenceWrapper.add(references[6]);
		registerFormField(references[6]);
		//SUB_CASUALTY
		references[7] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.SUB_CASUALTY, new HasParameters());
		referenceWrapper.add(references[7]);
		registerFormField(references[7]);
		//EXPENSE
		references[8] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.EXPENSE, new HasParameters());
		referenceWrapper.add(references[8]);
		registerFormField(references[8]);

		for(int i = 1; i<references.length; i++){
			references[i].setVisible(false);
		}
	}

	@Override
	public Document getInfo() {
		Document newDoc = new Document();
		
		newDoc.ownerId = (String)references[getVisibleReference()].getValue();
		newDoc.ownerTypeId = referenceType.getValue();
		
		newDoc.name = name.getValue();

		newDoc.docTypeId = docType.getValue();
		newDoc.hasFile = true;
		
		newDoc.displayAtPortal = displayAtPortal.getValue();
		
		Iterator<ListEntry<DocInfo>> iterator = details.iterator();
		ListEntry<DocInfo> temp;

		while(iterator.hasNext()){
			temp = iterator.next();
			if(iterator.hasNext()){
				if(temp.getValue().name == null && temp.getValue().value == null){
					iterator.remove();
				}
			}
		}

		newDoc.parameters = new DocInfo[details.size()-1];
		for(int i = 0; i<newDoc.parameters.length; i++){
			newDoc.parameters[i] = details.get(i).getValue();
		}
		
		return newDoc;
	}

	private int getVisibleReference() {
		for(int i = 0; i<references.length; i++){
			if(references[i].isVisible()){
				return i;
			}
		}
		return 0;
	}

	@Override
	public void setInfo(Document info) {
		return;
	}

	protected void onAddDetail() {

		DocInfo emptyd = new DocInfo();
		emptyd.name = "";
		emptyd.value = "";

		details.add(details.size()-1,new DocumentDetailEntry(emptyd));


	}

	protected void onRemoveListEntry(DocumentDetailEntry documentDetailEntry) {

		details.remove(documentDetailEntry);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(details != null && details.size() > 0){
			for(int i = 0; i < details.size()-1; i++){
				((DocumentDetailEntry) details.get(i)).setEditable(!readOnly);
			}
			details.get(details.size()-1).setVisible(!readOnly);
			add.setVisible(!readOnly);
		}
		super.setReadOnly(readOnly);
	}

	@Override
	public void clearInfo() {
		details.clear();
		details.add(new DocumentDetailEntry(null));
		super.clearInfo();
	}
}
