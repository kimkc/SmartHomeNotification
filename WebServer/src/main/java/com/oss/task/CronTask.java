package com.oss.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oss.domain.HourPlaceCountDTO;
import com.oss.domain.TodayPlaceCountDTO;
import com.oss.domain.TodoListVO;
import com.oss.mapper.InfraredLogMapper;
import com.oss.mapper.InfraredMapper;
import com.oss.mapper.TodoListMapper;
import com.oss.service.ForeCastService;
import com.oss.service.InfraredService;
import com.oss.service.SocketService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class CronTask {

	@Setter(onMethod_={ @Autowired })
	private InfraredMapper infMapper;
	
	@Setter(onMethod_={ @Autowired })
	private InfraredLogMapper infLogMapper;
	
	@Setter(onMethod_={@Autowired})
	private InfraredService infservice;
	
	@Setter(onMethod_={ @Autowired })
	private TodoListMapper todoMapper;
	
	private SocketService socket = new SocketService();
	
	private ForeCastService forecast = new ForeCastService();
	
	@Scheduled(cron="58 59 23 * * *")
	public void makeTodayInfraredLog() throws Exception {
		
		log.warn("infrared Today Task run.................");
		
		List<TodayPlaceCountDTO> todayPlaces = infMapper.getTodayPlaceCount();
		
		
		log.info(new Date());
		
		for(TodayPlaceCountDTO item : todayPlaces) {
			
			log.info(item.toString());
			infLogMapper.todayInsert(item);
		}
	}
	
	@Scheduled(cron="57 59 * * * *")
	public void makeHourInfraredLog() throws Exception {
		
		log.warn("infrared Hour Task run.................");
		List<HourPlaceCountDTO> hourPlaces = infservice.getHourPlaceCount();
		
		log.info(new Date());
		for(int k = 0; k <hourPlaces.size(); k++) {
			
			log.info(hourPlaces.get(k).toString());
			infLogMapper.hourInsert(hourPlaces.get(k));
		}		
	}
	
	@Scheduled(cron="00 00 7 * * *")
	public void alarmForecast() throws Exception {
		
		log.warn("ForeCast Task run.................");
		String[] foreCasts = forecast.post().split("/");
		socket.sendFile("183.100.114.246", 2123, foreCasts[0].getBytes());
		
	}
	
	@Scheduled(cron="0 0-59 * * * *")
	public void sendTTSTodoList() throws Exception{
		
		Date scheduledTime = new Date();
		SimpleDateFormat doFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		List<TodoListVO> todoLists = todoMapper.getThisTime();
		
		for (TodoListVO item: todoLists) {
			
			if(item.getRegdate().equals(doFormat.format(scheduledTime))) {
			
				socket.sendFile("183.100.114.246", 2123, item.getTodo().getBytes());
			}
		}
	}
	
}
