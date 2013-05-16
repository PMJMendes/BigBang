package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.OperationsToolBar;

public abstract class CreateQuoteRequestToolbar extends BigBangOperationsToolBar {

	//DATA
	protected MenuItem createObject, createPersonObject, createCompanyObject, createEquipmentObject, createLocationObject, createAnimalObject;

	public CreateQuoteRequestToolbar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.DATA, true);
		
		//DATA
		OperationsToolBar createObjectSubMenu = new OperationsToolBar(true);
		createObject =  new MenuItem("Criar Unidade de Risco", createObjectSubMenu);
		addItem(SUB_MENU.DATA, createObject);

		createPersonObject = new MenuItem("Pessoa", new Command() {

			@Override
			public void execute() {
				onCreatePersonObject();
			}
		});
		createObjectSubMenu.addItem(createPersonObject);

		createCompanyObject = new MenuItem("Empresa ou Grupo", new Command() {

			@Override
			public void execute() {
				onCreateCompanyObject();
			}
		});
		createObjectSubMenu.addItem(createCompanyObject);

		createEquipmentObject = new MenuItem("Objecto ou Equipamento", new Command() {

			@Override
			public void execute() {
				onCreateEquipmentObject();
			}
		});
		createObjectSubMenu.addItem(createEquipmentObject);

		createLocationObject = new MenuItem("Local", new Command() {

			@Override
			public void execute() {
				onCreateLocationObject();
			}
		});
		createObjectSubMenu.addItem(createLocationObject);

		createAnimalObject = new MenuItem("Animal", new Command() {

			@Override
			public void execute() {
				onCreateAnimalObject();
			}
		});
		createObjectSubMenu.addItem(createAnimalObject);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

	public abstract void onCreateInsuredObject();

	public abstract void onCreatePersonObject();

	public abstract void onCreateCompanyObject();

	public abstract void onCreateEquipmentObject();

	public abstract void onCreateLocationObject();

	public abstract void onCreateAnimalObject();

}
