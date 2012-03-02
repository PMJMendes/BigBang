package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class QuoteRequestProcessToolBar extends BigBangOperationsToolBar {

	public QuoteRequestProcessToolBar(){

		//CREATE
		addItem(SUB_MENU.CREATE, new MenuItem("Negociação", new Command() {

			@Override
			public void execute() {
				onCreateNegotiationButtonPressed();
			}
		}));

		//DATA
		addItem(SUB_MENU.DATA, new MenuItem("Inserir Objecto Seguro", new Command() {

			@Override
			public void execute() {
				onInsertSecuredObjectButtonPressed();
			}
		}));

		//EXECUTE
		addItem(SUB_MENU.EXECUTE, new MenuItem("Enviar Resposta ao Cliente", new Command() {

			@Override
			public void execute() {
				onSendResponseToClientButtonPressed();
			}
		}));

		addItem(SUB_MENU.EXECUTE, new MenuItem("Pedir Informação ou Documento", new Command() {

			@Override
			public void execute() {
				onInfoOrDocumentRequestButtonPressed();
			}
		}));

		//ADMIN
		addItem(SUB_MENU.ADMIN, new MenuItem("Histórico", new Command() {

			@Override
			public void execute() {
				onHistoryButtonPressed();
			}
		}));

		addItem(SUB_MENU.ADMIN, new MenuItem("Fechar Processo", new Command() {

			@Override
			public void execute() {
				onCloseProcessButtonPressed();
			}
		}));

		addItem(SUB_MENU.ADMIN, new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDeleteButtonPressed();
			}
		}));	
	}

	public abstract void onCreateNegotiationButtonPressed();
	
	public abstract void onInsertSecuredObjectButtonPressed();
	
	public abstract void onSendResponseToClientButtonPressed();
	
	public abstract void onInfoOrDocumentRequestButtonPressed();
	
	public abstract void onHistoryButtonPressed();
	
	public abstract void onCloseProcessButtonPressed();
	
	public abstract void onDeleteButtonPressed();
}
