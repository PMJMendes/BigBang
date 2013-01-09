package bigBang.definitions.shared;

import java.io.Serializable;

public class MedicalFile
	extends MedicalFileStub
{
	private static final long serialVersionUID = 1L;

	public static class MedicalDetail
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id; //Campo auxiliar para ajudar os serviços, manter inalterado
		public String disabilityTypeId;
		public String startDate;
		public String place;
		public Integer percentDisability;
		public String endDate;
		public Double benefits;

		public boolean deleted;
	}

	public String subCasualtyId;

	public MedicalDetail[] details;
}
