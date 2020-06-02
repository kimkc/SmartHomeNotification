package com.oss.service;

import java.util.List;

import com.oss.domain.Criteria;
import com.oss.domain.HourPlaceCountDTO;
import com.oss.domain.InfraredVO;
import com.oss.domain.TodayPlaceCountDTO;

public interface InfraredService {

	public void register(String name);
	
	public List<InfraredVO> getListAll();
	
	public InfraredVO get(Integer ino);
	
	public boolean modify(InfraredVO infrared);
	
	public boolean delete(Integer ino);
	
	public List<InfraredVO> getLatest();
	
	public List<InfraredVO> listCriteria(Criteria cri);
	
	public int listCountCriteria(Criteria cri);
	
	public List<TodayPlaceCountDTO> getTodayPlaceCount();
	
	public List<HourPlaceCountDTO> getHourPlaceCount();
	
}
