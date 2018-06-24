package com.am.cs12.commu.core.innerCommand.help;

import com.am.cs12.commu.protocol.amLocal.* ;
import com.am.util.*;

public class HelpClock {

	public Clock clock(){
		Clock c = new Clock() ;
		c.setClock(DateTime.yyyy_MM_dd_HH_mm_ss()) ;
		c.setYear(Integer.parseInt(DateTime.yyyy())) ;
		c.setMonth(Integer.parseInt(DateTime.MM())) ;
		c.setDate(Integer.parseInt(DateTime.dd())) ;
		c.setHour(Integer.parseInt(DateTime.HH())) ;
		c.setMinute(Integer.parseInt(DateTime.mm())) ;
		c.setSecond(Integer.parseInt(DateTime.ss())) ;
		return c ;
	}
}
