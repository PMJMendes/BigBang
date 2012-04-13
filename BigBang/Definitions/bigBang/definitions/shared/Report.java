package bigBang.definitions.shared;

import java.io.Serializable;

public class Report
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static class Section
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public static class Verb
			implements Serializable
		{
			private static final long serialVersionUID = 1L;

			public String label;
			public String argument;
		}

		public String htmlContent;
		public Verb[] verbs;
	}

	public Section[] sections;
}
