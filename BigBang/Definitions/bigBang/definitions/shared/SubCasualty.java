package bigBang.definitions.shared;

import java.io.Serializable;

public class SubCasualty
	extends SubCasualtyStub
{
	private static final long serialVersionUID = 1L;

	public static class SubCasualtyItem
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id; //Campo auxiliar para ajudar os serviços, manter inalterado
		public String insuredObjectId; //Lista tipificada dos objectos de apólice *ou* de sub-apólice, filtrada pelo ID da respectiva
		public String coverageId; //Lista tipificada das coberturas da (sub)apólice
		public String damageTypeId; //Lista tipificada, campo obrigatório
		public String damages; //Em €
		public String settlement; //Em €
	}

	public SubCasualtyItem[] items; //Zero ou mais items
	public boolean hasJudicial;
	public String text;
	public String internalNotes;
}
