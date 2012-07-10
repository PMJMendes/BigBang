package bigbang.tests.client;

import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.Tax;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestTaxEdit
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Line[]> callback = new AsyncCallback<Line[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Line[] result)
			{
				int i, j, k, l;

				if ( result == null )
					return;
				else
				{
					for ( i = 0; i < result.length; i++ )
					{
						if ( "CFBC796D-1F64-4646-A964-9EE9010DF63E".equalsIgnoreCase(result[i].id) )
						{
							for ( j = 0; j < result[i].subLines.length; j++ )
							{
								if ( "FE18F5ED-F87C-4595-B9AC-9EE90118D568".equalsIgnoreCase(result[i].subLines[j].id) )
								{
									for ( k = 0; k < result[i].subLines[j].coverages.length; k++ )
									{
										if ( "D0198709-B571-4F50-B1C0-9F9100E6F9A3".equalsIgnoreCase(result[i].subLines[j].coverages[k].id) )
										{
											for ( l = 0; l < result[i].subLines[j].coverages[k].taxes.length; l++ )
											{
												if ( "555D1EE3-3970-4842-88DE-9F96014074DE".equalsIgnoreCase(result[i].subLines[j].coverages[k].taxes[l].id) )
												{
													DoStep2(result[i].subLines[j].coverages[k].taxes[l]);
													break;
												}
											}
											break;
										}
									}
									break;
								}
							}
							break;
						}
					}
				}
			}
		};

		Services.coveragesService.getLines(callback);
	}

	private static void DoStep2(Tax tax)
	{
		AsyncCallback<Tax> callback = new AsyncCallback<Tax>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Tax result)
			{
				return;
			}
		};

		tax.name = tax.name + "s";
		Services.coveragesService.saveTax(tax, callback);
	}
}
