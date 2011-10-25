package bigBang.module.clientModule.client.userInterface;

import java.util.Collection;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.BigBangDocumentsBroker;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

public class ClientChildrenLists {

	public static class ContactsList extends FilterableList<Contact> implements ContactsBrokerClient {

		public static class Entry extends ListEntry<Contact> {

			public Entry(Contact value) {
				super(value);
				setHeight("25px");
				titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			}

			public <I extends Object> void setInfo(I info) {
				Contact c = (Contact) info;
				this.setTitle(c.name);
			};
		}

		protected int contactsDataVersion;
		protected String ownerId;
		protected ContactsBroker broker;

		public ContactsList(){
			this.showFilterField(false);
			this.showSearchField(true);

			this.broker = BigBangContactsListBroker.Util.getInstance();

			this.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()) {
						setOwner(ownerId);
					}else{
						discardOwner();
					}
				}
			});
		}

		public void setOwner(String ownerId){
			discardOwner();
			if(this.isAttached() && ownerId != null){
				this.broker.registerClient(this, ownerId);
			}
			this.ownerId = ownerId;
		}

		public void discardOwner(){
			this.clear();
			if(ownerId != null){
				this.broker.unregisterClient(this, this.ownerId);
				this.ownerId = null;
			}
		}

		public void addEntry(Contact c){
			this.add(new Entry(c));
		}

		@Override
		public void setDataVersionNumber(String dataElementId, int number) {
			if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)) {
				this.contactsDataVersion = number;
			}
		}

		@Override
		public int getDataVersion(String dataElementId) {
			if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)){
				return contactsDataVersion;
			}
			return -1;
		}

		@Override
		public int getContactsDataVersionNumber(String ownerId) {
			return contactsDataVersion;
		}

		@Override
		public void setContactsDataVersionNumber(String ownerId, int number) {
			contactsDataVersion = number;
		}

		@Override
		public void setContacts(String ownerId, List<Contact> contacts) {
			this.clear();
			for(Contact c : contacts) {
				addEntry(c);
			}
		}

		@Override
		public void removeContact(String ownerId, Contact contact) {
			if(ownerId == null){
				for(ValueSelectable<Contact> s : this) {
					if(s.getValue().id.equalsIgnoreCase(contact.id)){
						remove(s);
					}
				}
			}
		}

		@Override
		public void addContact(String ownerId, Contact contact) {
			if(ownerId == null) {
				addEntry(contact);
			}
		}

		@Override
		public void updateContact(String ownerId, Contact contact) {
			for(ValueSelectable<Contact> s : this){
				if(s.getValue().id.equalsIgnoreCase(contact.id)){
					s.setValue(contact);
				}
			}
		}
	}

	public static class DocumentsList extends FilterableList<Document> implements DocumentsBrokerClient {

		public static class Entry extends ListEntry<Document> {

			public Entry(Document value) {
				super(value);
				setHeight("25px");
				titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			}

			public <I extends Object> void setInfo(I info) {
				Document c = (Document) info;
				this.setTitle(c.name);
			};
		}

		protected int documentsDataVersion;
		protected String ownerId;
		protected DocumentsBroker broker;

		public DocumentsList(){
			this.showFilterField(false);
			this.showSearchField(true);

			broker = BigBangDocumentsBroker.Util.getInstance();

			this.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()) {
						setOwner(ownerId);
					}else{
						discardOwner();
					}
				}
			});
		}

		public void setOwner(String ownerId){
			discardOwner();
			if(this.isAttached() && ownerId != null){
				this.broker.registerClient(this);
			}
			this.ownerId = ownerId;
		}

		public void discardOwner(){
			this.clear();
			if(ownerId != null) {
				broker.unregisterClient(this);
				this.ownerId = null;
			}
		}

		public void addEntry(Document c){
			this.add(new Entry(c));
		}

		@Override
		public void setDataVersionNumber(String dataElementId, int number) {
			if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.DOCUMENT)) {
				this.documentsDataVersion = number;
			}
		}

		@Override
		public int getDataVersion(String dataElementId) {
			if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.DOCUMENT)){
				return documentsDataVersion;
			}
			return -1;
		}

		@Override
		public void setDocumentsDataVersionNumber(String ownerId, int number) {
			documentsDataVersion = number;
		}

		@Override
		public void setDocuments(String ownerId, List<Document> documents) {
			this.clear();
			for(Document c : documents) {
				addEntry(c);
			}
		}

		@Override
		public void removeDocument(String ownerId, Document document) {
			if(ownerId == null){
				for(ValueSelectable<Document> s : this) {
					if(s.getValue().id.equalsIgnoreCase(document.id)){
						remove(s);
					}
				}
			}
		}

		@Override
		public void addDocument(String ownerId, Document document) {
			if(ownerId == null) {
				addEntry(document);
			}
		}

		@Override
		public void updateDocument(String ownerId, Document document) {
			for(ValueSelectable<Document> s : this){
				if(s.getValue().id.equalsIgnoreCase(document.id)){
					s.setValue(document);
				}
			}
		}

		@Override
		public int getDocumentsDataVersionNumber(String ownerId) {
			return this.documentsDataVersion;
		}

	}

	public static class InsurancePoliciesList extends FilterableList<InsurancePolicyStub> implements InsurancePolicyDataBrokerClient{

		public static class Entry extends ListEntry<InsurancePolicyStub> {

			public Entry(InsurancePolicyStub value) {
				super(value);
				setHeight("25px");
				titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			}

			public <I extends Object> void setInfo(I info) {
				InsurancePolicyStub p = (InsurancePolicyStub) info;
				setTitle(p.categoryName+"/"+p.lineName+"/"+p.subLineName);
			}
		}

		protected InsurancePolicyBroker broker;
		protected String ownerId;
		protected int dataVersion;

		public InsurancePoliciesList(){
			this.showFilterField(false);
			this.showSearchField(true);

			broker = (InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);

			this.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()){
						setOwner(ownerId);
					}else{
						discardOwner();
					}
				}
			});
		}

		public void setOwner(String ownerId){
			discardOwner();
			if(this.isAttached() && ownerId != null){
				this.broker.registerClient(this);
				broker.getClientPolicies(ownerId, new ResponseHandler<Collection<InsurancePolicyStub>>() {

					@Override
					public void onResponse(Collection<InsurancePolicyStub> response) {
						for(InsurancePolicyStub s : response){
							addEntry(s);
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
			this.ownerId = ownerId;
		}

		public void discardOwner(){
			this.clear();
			if(ownerId != null) {
				broker.unregisterClient(this);
				this.ownerId = null;
			}
		}

		public void addEntry(InsurancePolicyStub policy) {
			add(new Entry(policy));
		}

		@Override
		public void setDataVersionNumber(String dataElementId, int number) {
			if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
				dataVersion = number;
			}
		}

		@Override
		public int getDataVersion(String dataElementId) {
			if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
				return dataVersion;
			}
			return -1;
		}

		@Override
		public void addInsurancePolicy(InsurancePolicy policy) {
			if(this.ownerId != null){
				if(policy.clientId.equalsIgnoreCase(this.ownerId)){
					this.addEntry(policy);
				}
			}
		}

		@Override
		public void updateInsurancePolicy(InsurancePolicy policy) {
			for(ValueSelectable<InsurancePolicyStub> s : this){
				if(s.getValue().id.equalsIgnoreCase(policy.id)){
					s.setValue(policy);
					break;
				}
			}
		}

		@Override
		public void removeInsurancePolicy(String policyId) {
			for(ValueSelectable<InsurancePolicyStub> s : this) {
				if(s.getValue().id.equalsIgnoreCase(policyId)){
					remove(s);
					break;
				}
			}
		}
	}

	public static class InfoRequestList extends FilterableList<InfoOrDocumentRequest> {
		//TODO
		public InfoRequestList(){
			this.showFilterField(false);
			this.showSearchField(true);
		}

		public void setOwner(String ownerId) {
			//TODO
		}
	}

	public static class ManagerTransferList extends FilterableList<ManagerTransfer> {
		//TODO
		public ManagerTransferList(){
			this.showFilterField(false);
			this.showSearchField(true);
		}

		public void setOwner(String ownerId) {
			//TODO
		}
	}
}
