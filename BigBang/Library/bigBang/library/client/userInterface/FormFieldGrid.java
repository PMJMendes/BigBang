package bigBang.library.client.userInterface;

import bigBang.library.client.userInterface.view.View;

public class FormFieldGrid extends View{
	

	class Field extends GenericFormField{

		private String id;
		
		public Field(TYPE type) {
			super(type);
		}
		public Field(TYPE type, String id){
			super(type);
			this.id = id;
		}
		
		public void setId(String id){
			this.id = id;
		}
		
		public String getId(){
			return id;
		}
	}
	
	protected Field[][] fields;
	protected String[] rows;
	protected String[] columns;

	
	public FormFieldGrid() {
	
		
		
	}


	
	@Override
	protected void initializeView() {
		// TODO Auto-generated method stub
		
	}
	
}
