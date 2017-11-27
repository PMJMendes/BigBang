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
		public String disabilityTypeLabel;
		public String startDate;
		public String place;
		public Double percentDisability;
		public String endDate;
		public Double benefits;

		public boolean deleted;
	}

	public static class Appointment
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id; //Campo auxiliar para ajudar os serviços, manter inalterado
		public String label;
		public String date;

		public boolean deleted;
	}

	public String notes;

	public String subCasualtyId;
	public String subCasualtyNumber;

	public MedicalDetail[] details;
	public Appointment[] appointments;
}
