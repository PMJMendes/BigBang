package bigBang.library.client.userInterface.view;

import bigBang.library.client.userInterface.ManagerTransferToolBar;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter.BarStatus;

public class ManagerTransferWithToolbarView extends ManagerTransferView implements ManagerTransferViewPresenter.Display{

	ManagerTransferToolBar bar = new ManagerTransferToolBar(false);

	public ManagerTransferWithToolbarView(){

		super();
		wrapper.insert(bar, 0);
		bar.setSize("100%", "100%");
		//super.getForm().currentSection.addWidget(bar);

	}

	@Override
	public void setToolBarState(BarStatus status) {


		if(status == BarStatus.ACCEPT_REJECT){


			bar.accept.setVisible(true);
			bar.reject.setVisible(true);
			bar.cancel.setVisible(false);
			bar.setVisible(true);
		}

			else if (status == BarStatus.CANCEL){

				bar.accept.setVisible(false);
				bar.reject.setVisible(false);
				bar.cancel.setVisible(true);
				bar.setVisible(true);

			}else {
				
				bar.accept.setVisible(false);
				bar.reject.setVisible(false);
				bar.cancel.setVisible(false);
				bar.setVisible(false);
				
			}



		}
	}
