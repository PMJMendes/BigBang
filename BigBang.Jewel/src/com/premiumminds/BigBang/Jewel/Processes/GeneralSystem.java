package com.premiumminds.BigBang.Jewel.Processes;

import java.util.UUID;

import Jewel.Petri.SysObjects.Process;

import com.premiumminds.BigBang.Jewel.Constants;

public class GeneralSystem
	extends Process
{
	protected UUID ProcID()
	{
		return Constants.ProcID_GenSys;
	}
}
