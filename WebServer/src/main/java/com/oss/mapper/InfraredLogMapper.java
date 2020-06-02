package com.oss.mapper;

import com.oss.domain.HourPlaceCountDTO;
import com.oss.domain.TodayPlaceCountDTO;

public interface InfraredLogMapper {

	public void hourInsert(HourPlaceCountDTO hourPlace);
	
	public void todayInsert(TodayPlaceCountDTO todayPlace);
}
