package com.oss.mapper;

import java.util.List;

import com.oss.domain.Criteria;
import com.oss.domain.HourPlaceCountDTO;
import com.oss.domain.InfraredVO;
import com.oss.domain.TodayPlaceCountDTO;


public interface InfraredMapper {

	public List<InfraredVO> getLatestList();
	
	public List<InfraredVO> getList();
	
	public void insert(String name);
	
	public InfraredVO read(Integer ino);
	
	public int update(InfraredVO infrared);
	
	public int delete(Integer ino);
	
	public List<InfraredVO> listPage(Criteria cri);
	
	public int countPaging(Criteria cri);
	
	public List<TodayPlaceCountDTO> getTodayPlaceCount();
	
	public List<HourPlaceCountDTO> getHourPlaceCount();
	
}
