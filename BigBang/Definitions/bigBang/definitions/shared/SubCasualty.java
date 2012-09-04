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
		public String coverageId; //Lista tipificada das coberturas da (sub)apólice
		public String damageTypeId; //Lista tipificada, campo obrigatório
		public Double damages; //Em €
		public Double settlement; //Em €
		public Double deductible; //Unidades desconhecidas
		public Double value; //Em €
		public boolean isManual;

		public boolean deleted;

	}

	public String insuredObjectId; //Lista tipificada dos objectos de apólice *ou* de sub-apólice, filtrada pelo ID da respectiva
	public String insuredObjectName;
	public SubCasualtyItem[] items; //Zero ou mais items
	public boolean hasJudicial;
	public String text;
	public String internalNotes;
}
