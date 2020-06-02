package com.oss.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ForeCastService {

	public String post() {
		try {
			HttpClient client = HttpClientBuilder.create().build(); // HttpClient ����
			
			SimpleDateFormat forecastDatefmt = new SimpleDateFormat ( "yyyyMMdd");
			SimpleDateFormat forecastTime = new SimpleDateFormat ("HH");
			
			Calendar time = Calendar.getInstance();   
			String forecastToday = forecastDatefmt.format(time.getTime());
			String strbaseTime = forecastTime.format(time.getTime());
			int basetime=Integer.parseInt(strbaseTime);
			
			if( basetime >= 0 && basetime < 2){
				strbaseTime = "2300";
			}else if( basetime >= 2 && basetime < 5){
				strbaseTime = "0200";
			}else if( basetime >= 5 && basetime < 8){
				strbaseTime = "0500";
			}else if( basetime >= 8 && basetime < 11) {
				strbaseTime = "0800";
			}else if( basetime >= 11 && basetime < 14) {
				strbaseTime = "1100";
			}else if( basetime >= 14 && basetime < 17) {
				strbaseTime = "1400";
			}else if( basetime >= 17 && basetime < 20) {
				strbaseTime = "1700";
			}else if( basetime >= 20 && basetime < 23) {
				strbaseTime = "2000";
			}else if( basetime >= 23) {
				strbaseTime = "2300";
			}
			
			HttpGet postRequest = new HttpGet("http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?ServiceKey=4d6GyBtQk8gbfA1JcWcIgSnu1gsNntespsPOAqRlv3l%2B9kp2HVnIsnDpLeFIXPVbwFC6%2BxdKBnX1YMSEi%2BUVoQ%3D%3D&base_date="+forecastToday+"&base_time="+strbaseTime+"&nx=62&ny=110&_type=json"); //POST �޼ҵ� URL ���� 
			
			HttpResponse response = client.execute(postRequest);

			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				
				String body = handler.handleResponse(response); 
				
				int itemStart = body.indexOf("[");
				int itemEnd = body.indexOf("]");
				body = body.substring(itemStart+1,itemEnd);
				
				HashMap<String, Float> map = new HashMap<String, Float>();
				String[] items = body.split("},");
				
				for (int k = 0; k < items.length; k++) {
					
					String[] item = items[k].split(",");
					String category = item[2].substring(12,15);
					float fcstVal = Float.parseFloat(item[5].substring(12));
					
					map.put(category,fcstVal);
				}
				
				String forecast="오늘의 아침 날씨는";
				String foreJson ="/";
				
				if(map.containsKey("PTY")) {
					if( map.get("PTY") == 1) {
						forecast= forecast+"비가오니 우산을 챙겨주세요.";
						foreJson += "pty,1,";
					}else if( map.get("PTY") == 2 ) {
						forecast= forecast+"비와 눈이 오니 우산을 챙겨주세요.";
						foreJson += "pty,2,";
					}else if( map.get("PTY") == 3 ) {
						forecast= forecast+"눈이 오니 우산을 챙겨주세요.";
						foreJson += "pty,3,";
					}else{
						forecast= forecast+"소나기가 오니 우산을 챙겨주세요.";
						foreJson += "pty,4,";
					}
				}
				
				if(map.containsKey("SKY")) {
					if( map.get("SKY") == 1) {
						forecast= forecast+" 하늘의 상태는 맑고";
						foreJson += "sky,1,";
					}else if( map.get("SKY") == 3 ) {
						forecast= forecast+" 하늘의 상태는 구름이 많고";
						foreJson += "sky,3,";
					}else{
						forecast= forecast+" 하늘의 상태는 흐리고";
						foreJson += "sky,5,";
					}
				}
				
				if(map.containsKey("WSD")) {
					if( map.get("WSD") < 4) {
						forecast= forecast+" 바람은 약하겠습니다.";
						foreJson += "wsd,1,";
					}else if( map.get("WSD") >=4 || map.get("WSD") <9 ) {
						forecast= forecast+" 바람은 감촉이 느껴질 정도입니다.";
						foreJson += "wsd,2,";
					}else if( map.get("WSD") >=9 || map.get("WSD") <14 ) {
						forecast= forecast+" 바람은 나뭇가지와 깃발이 흔들릴 정도로 약간 강하겠습니다.";
						foreJson += "wsd,3,";
					}else{
						forecast= forecast+" 바람은 매우 강하게 불 예정이니 이점 유의하여 주세요.";
						foreJson += "wsd,4,";
					}
				}
				
				if(map.containsKey("T3H")) {
					forecast += " 앞으로 3시간의 기온은 "+map.get("T3H")+"도 이고 ";
					foreJson += "t3h,"+map.get("T3H")+",";
				}
				
				if(map.containsKey("REH")) {
					forecast += "습도는 "+map.get("REH")+"퍼센트 입니다.";
					foreJson += "reh,"+map.get("REH");				
				}

				return forecast+"/"+strbaseTime+foreJson;
				
			} else {
				System.out.println("response is error : " + response.getStatusLine().getStatusCode());
				return "response is error : " +response.getStatusLine().getStatusCode();
			}
		} catch (Exception e){
			System.err.println(e.toString());
			return "Exception!: "+e.toString();
		}
	}
}
