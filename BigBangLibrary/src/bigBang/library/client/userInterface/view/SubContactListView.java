package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;
import bigBang.library.client.userInterface.presenter.SubContactListViewPresenter;

public class SubContactListView extends View implements SubContactListViewPresenter.Display{
	
	VerticalPanel wrapper;
	List<Contact> subContacts;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public SubContactListView(){
		
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("410px", "500px");
		
		subContacts = new List<Contact>();
		wrapper.add(subContacts.getScrollable());
		
		
	}

	@Override
	public void setSubContacts(Contact[] contacts) {
	
		//TODO REMOVE THIS FROM HERE, TEST DATA
		contacts = criaContacto().subContacts;
		
		
		ListEntry<Contact> temp;
		Label tempLabel;
		for(int i = 0; i<contacts.length; i++){
			temp = new ListEntry<Contact>(contacts[i]);
			tempLabel = new Label(temp.getValue().name);
			temp.setWidget(tempLabel);
			subContacts.add(temp);
		}
		
		
		
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	//TODO REMOVE THIS FROM HERE, TEST DATA
Contact criaContacto(){
		
		Contact exemplo = new Contact();
		
		exemplo.id = "ID";
		exemplo.name = "Francisco Ventura";
		exemplo.ownerId = null;
		exemplo.address.street1 = "Rua do carril nº.15";
		exemplo.address.street2 = "Marmeleira - Souselas";
		exemplo.address.zipCode.city = "Coimbra";
		exemplo.address.zipCode.code = "3020-758";
		exemplo.address.zipCode.country = "Portugal";
		exemplo.address.zipCode.county = "Coimbra";
		exemplo.address.zipCode.district = "Coimbra";
		exemplo.typeId = "07367032-3a5d-499d-88bd-9eee013357c9";
		
		exemplo.info = new ContactInfo[2];
		exemplo.info[0] = new ContactInfo();
		exemplo.info[1] = new ContactInfo();
		
		exemplo.info[0].typeId = "96467849-6fe1-4113-928c-9edf00f40fb9"; //EMAIL
		exemplo.info[0].value = "franciscoreisventura@gmail.com";
		exemplo.info[1].typeId = "60414f28-49e7-43ad-acd9-9edf00f41e76"; //TELEMOVEL
		exemplo.info[1].value = "917114263";
		
		exemplo.subContacts = new Contact[2];
		exemplo.subContacts[0] = new Contact();
		exemplo.subContacts[1] = new Contact();
		
		
		exemplo.subContacts[0].id = "ID2";
		exemplo.subContacts[0].name = "Filho Ventura";
		exemplo.subContacts[0].ownerId = "ID";
		exemplo.subContacts[0].address.street1 = "Rua";
		exemplo.subContacts[0].address.street2 = "Aldeia";
		exemplo.subContacts[0].address.zipCode.city = "Coimbra";
		exemplo.subContacts[0].address.zipCode.county = "Coimbra";
		exemplo.subContacts[0].address.zipCode.district = "Coimbra";
		exemplo.subContacts[0].address.zipCode.code = "3020-111";
		exemplo.subContacts[0].address.zipCode.country = "Portugal";
		exemplo.subContacts[0].typeId = "Escravo";
		
		exemplo.subContacts[0].info = new ContactInfo[2];
		exemplo.subContacts[0].info[0] = new ContactInfo();
		exemplo.subContacts[0].info[1] = new ContactInfo();
		
		exemplo.subContacts[0].info[0].typeId = "172ec088-aa55-433b-bbc3-9edf00f42266"; //FAX
		exemplo.subContacts[0].info[0].value = "239911798";
		
		exemplo.subContacts[0].info[1].typeId = "E-Mail";
		exemplo.subContacts[0].info[1].value = "lfdasfdsa@fdsafdsaf.fdsa";
		
		exemplo.subContacts[1].id = "ID3";
		exemplo.subContacts[1].name = "Filho2 Ventura";
		exemplo.subContacts[1].ownerId = "ID";
		exemplo.subContacts[1].address.street1 = "Rua";
		exemplo.subContacts[1].address.street2 = "Aldeia";
		exemplo.subContacts[1].address.zipCode.city = "Coimbra";
		exemplo.subContacts[1].address.zipCode.county = "Coimbra";
		exemplo.subContacts[1].address.zipCode.district = "Coimbra";
		exemplo.subContacts[1].address.zipCode.code = "3020-111";
		exemplo.subContacts[1].address.zipCode.country = "Poirtugal";
		exemplo.subContacts[1].typeId = "Escravo";
		
		exemplo.subContacts[1].info = new ContactInfo[2];
		exemplo.subContacts[1].info[0] = new ContactInfo();
		exemplo.subContacts[1].info[1] = new ContactInfo();
		
		exemplo.subContacts[1].info[0].typeId = "01c8d0ca-074e-45aa-8a17-9edf00f41586"; //TELEFONE
		exemplo.subContacts[1].info[0].value = "239911798";
		
		exemplo.subContacts[1].info[1].typeId = "E-Mail";
		exemplo.subContacts[1].info[1].value = "lfdasfdsa@fdsafdsaf.fdsa";
		
		return exemplo;
		
	}

@Override
public List<Contact> getList() {
	return subContacts;
}

}
