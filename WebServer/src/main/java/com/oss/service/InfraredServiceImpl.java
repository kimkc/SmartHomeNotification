package com.oss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oss.domain.Criteria;
import com.oss.domain.HourPlaceCountDTO;
import com.oss.domain.InfraredVO;
import com.oss.domain.TodayPlaceCountDTO;
import com.oss.mapper.InfraredMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
public class InfraredServiceImpl implements InfraredService {

	private InfraredMapper infmapper;
	
	@Override
	public InfraredVO get(Integer ino) {
		log.info("Infrared Service Read.....: "+ino);
		return infmapper.read(ino);
	}
	
	@Override
	public void register(String name) {
		log.info("Infrared Service Register.........");
		infmapper.insert(name);
	}
	
	@Override
	public List<InfraredVO> getListAll(){
		log.info("Infrared Service GetListAll....");
		return infmapper.getList();
	}
	
	@Override
	public boolean modify(InfraredVO infrared) {
		log.info("Infrared Service Modify......");
		return infmapper.update(infrared) == 1;
	}
	
	@Override
	public boolean delete(Integer ino) {
		log.info("Infrared Service Delete.....");
		return infmapper.delete(ino) == 1;
	}
	
	@Override
	public List<InfraredVO> getLatest(){
		log.info("Infrared Service getLatest 5 List....");
		return infmapper.getLatestList();
	}
	
	@Override
	public List<InfraredVO> listCriteria(Criteria cri){
		log.info("Infrared Service get listCriteria...");
		return infmapper.listPage(cri);
	}
	
	@Override
	public int listCountCriteria(Criteria cri) {
		log.info("infrared service get listpage");
		return infmapper.countPaging(cri);
	}
	
	@Override
	public List<TodayPlaceCountDTO> getTodayPlaceCount(){
		log.info("infrared Place count per today...");
		return infmapper.getTodayPlaceCount();
	}
	
	@Override
	public List<HourPlaceCountDTO> getHourPlaceCount(){
		log.info("infrared Place count per hour...");
		return infmapper.getHourPlaceCount();
	}
}
