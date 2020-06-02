package com.oss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oss.domain.CamDTO;
import com.oss.domain.Criteria;
import com.oss.domain.PageMaker;
import com.oss.domain.TodoListVO;
import com.oss.mapper.TodoListMapper;
import com.oss.service.ForeCastService;
import com.oss.service.InfraredService;
import com.oss.service.SocketService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class SafeHomeController {
	@Setter(onMethod_ = @Autowired)
	private SocketService socketService;
	
	@Setter(onMethod_ = @Autowired)
	private ForeCastService forecast;
	
	@Setter(onMethod_ = @Autowired)
	private TodoListMapper todoMapper;
	
	@Setter(onMethod_ = @Autowired)
	private InfraredService infservice;
	
	@GetMapping(value="/safehome")
	public String index(Model model) {
		
		log.info("Safe Home Index...");
		
		model.addAttribute("camState", todoMapper.getState());
		
		String[] foreCasts = forecast.post().split("/");
		model.addAttribute("basetime", foreCasts[1]);
		String[] pswtr = foreCasts[2].split(",");
		
		//forecast code value from openAPI
		for(int i = 0; i < pswtr.length; i=i+2 ) {
			if(pswtr[i].equals("pty")) {
				model.addAttribute("P", pswtr[i+1]);
			}else if(pswtr[i].equals("sky")) {
				model.addAttribute("S", pswtr[i+1]);
			}else if(pswtr[i].equals("wsd")) {
				model.addAttribute("W", pswtr[i+1]);
			}else if(pswtr[i].equals("t3h")) {
				model.addAttribute("T", pswtr[i+1]);
			}else if(pswtr[i].equals("reh")){
				model.addAttribute("R", pswtr[i+1]);
			}
		}
		
		//image recognition place or name
		model.addAttribute("TodayPlaceCount", infservice.getTodayPlaceCount());
		model.addAttribute("HourPlaceCount", infservice.getHourPlaceCount());
		
		
		return "oss/safehome";
	}
	
	@GetMapping(value="/tempNhumi")
	public ResponseEntity<String> tempNhumiSensor(String temp, String humi){

		log.info("temperature & Humidity Sensor received and register DB...");
		log.info("temperature: " + temp +"humidity: "+ humi + "with defaulted ino and regdate");

		HttpHeaders responseHeaders = new HttpHeaders(); //한글 처리 위해
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		float floatTemp = Float.parseFloat(temp);
		float floatHumi = Float.parseFloat(humi);
		
		String body = "0";
		
		//check temperature and humidity
		if (floatTemp <20 || floatTemp >30) {
			 body = "1";
		}
		if (floatHumi < 40 || floatHumi >80) {
			body = "2";
		}
		if ((floatTemp <20 || floatTemp >30) && (floatHumi < 40 || floatHumi >80)) {
			body = "3";
		}
		
		return new ResponseEntity<String>(body,responseHeaders,HttpStatus.OK); 
	}
	
	@PostMapping(value="/todolist")
	public String todoList(RedirectAttributes rttr, TodoListVO todoList) {
		log.info("register todoList....");
		boolean isSuccess = todoMapper.insertTodo(todoList);
		log.info("register: "+todoList.toString()+"in todoList");
		if( isSuccess) {
			rttr.addFlashAttribute("isTodoSuccess", "성공");
		}
		
		return "redirect:/safehome";
	}
	
	@GetMapping(value="/todolists")
	public void todolists(Model model, Criteria cri) {
		log.info("to do Lists...");
		model.addAttribute("todoList", todoMapper.listPage(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		
		pageMaker.setTotalCount(todoMapper.countPaging(cri));
		model.addAttribute("pageMaker", pageMaker);
		
	}
	
	@GetMapping(value="/infraredList")
	public String infraredList(Model model, Criteria cri) {
		log.info("Infrared Sensor List...");
		model.addAttribute("infraredList", infservice.listCriteria(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		
		pageMaker.setTotalCount(infservice.listCountCriteria(cri));
		model.addAttribute("pageMaker", pageMaker);
		
		return "/oss/infraredList";
	}
	
	@GetMapping(value="/infrared")
	public ResponseEntity<String> infraredSensor(String name) {
		log.info("Infrared Cam received and register DB...");
		
		log.info("infared register name: " + name + "with defaulted ino and regdate");

		infservice.register(name);

		HttpHeaders responseHeaders = new HttpHeaders(); 
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		String body = "OK";
		return new ResponseEntity<String>(body,responseHeaders,HttpStatus.OK); 
	}
	
	@GetMapping(value="/onNoff")
	public void onNoff(RedirectAttributes rttr, String state) {
		
		todoMapper.insertOnNOff(state);
	}
	
	@GetMapping(value="/camState")
	public ResponseEntity<String> checkState(){
		
		log.info("check cam state");
		HttpHeaders responseHeaders = new HttpHeaders(); //한글 처리 위해
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		CamDTO camState = todoMapper.getState();
		
		String body = camState.getState();
		return new ResponseEntity<String>(body,responseHeaders,HttpStatus.OK); 
	}
	
	@PostMapping(value="/TTS")  
	public String ttl(Model model,  RedirectAttributes rttr, String text) throws Exception{
		
		socketService.sendFile("183.100.114.246", 2123, text.getBytes());
		
		if(text.length() > 0) {
			rttr.addFlashAttribute("isSpeech", "성공");
		}
		
		return "redirect:/safehome";
	}
}
