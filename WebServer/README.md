# WebServer

- 서버는 아두이노, 라즈베리 파이와 웹을 통해 **통신과 제어 역할**을 합니다.
- 할 일 예약, 이미지 인식 횟수 등을 서버의 DB에 저장하고, 일기예보나 TTS의 메시지 부분을 라즈베리 파이로 보냅니다. `현재 보안상 보안 카메라 스트리밍은 실행하지 않고 있습니다.`

## 1. 시스템 구조
![Web Server Architecture](/resources/image/webserverArchitecture.jpg)

## 2. 사용 환경
- Server: Naver Cloud platform, CentOS 7.2 (64-bit), Apache-tomcat-8.5.43
- DB: MySQL 5.6.45 Linux (x86_64)
- Front-End: HTML, CSS, JS, JQuery, BootStrap, Ajax
- Back-End: **Spring 5.0.7.RELEASE** (Lombok 1.18.0, HikariCP 2.7.8, Mybatis 3.4.6, jackson 2.9.5, quartz 2.3.0, httpclient 4.4), **Java 1.8(jdk1.8.0_202)**

## 3. 라이선스
- This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0), also included in our repository in [the LICENSE file](https://github.com/kimkc/SmartHomeNotification/blob/master/LICENSE).

## 4. 사용 시나리오
### 4-1. 일기 예보
![Web Server Forecast](/resources/image/serverForecast.jpg)
- openAPI로부터 3시간마다 기상정보를 HTTP GET 방식의 Json으로 받아 서버에서 처리 후 웹으로 보여줍니다. 
-**아침 7시마다** 집 안의 라즈베리 파이가 **기상 예보**를 스피커로 알려줄 수 있게 Socket 통신으로 기상 정보를 보내줍니다.
***

### 4-2. 온/습도 
![Web Server temperature and humidity](/resources/image/webTempHum.jpg)
- 집 안의 아두이노가 보낸 온/습도, 적외선 값으로 만들어진 thingspeak의 그래프를 웹에서 `<iframe>` 태그를 사용하여 보여줍니다. 
- 온/습도 모듈은 HTTP GET 방식으로 웹에 값을 보내며, 온도는 **20부터 30도, 습도는 40부터 80%의 범위**를 매번 확인하며, 범위가 벗어날 시 그에 대한 응답을 아두이노에게 해줍니다.
***

### 4-3. 라즈베리 파이 보안 카메라 동작 On/Off
![Web Server Cam Security](/resources/image/serverSecurityOnOff.jpg)
- 외출이나 귀가 시 웹을 통해 라즈베리 파이에 연결된 **보안 카메라를 동작 설정, 해제**를 합니다. 
- 웹에서는 현재 서버의 데이터베이스에 저장되어 있는 동작인식 모드의 상태를 나타납니다. 예를 들어, 동작 인식 모드가 꺼져있을 때는 데이터베이스에 0이 저장되어있는 값을 통해  웹에서는 `off`를 보여줍니다. `on`을 하고 싶을 때, `off`를 클릭하면 `on`으로 변하며 DB에는 1이 저장됩니다. 
- 라즈베리 파이는 http GET 방식으로 계속 서버의 현재 DB 값을 확인하여 동작하게 됩니다.
***

### 4-4. 이미지 인식을 통한 생활 습관 개선
![Web Server life Style](/resources/image/serverLifeStyle.jpg)
- 라즈베리 파이와 연결된 이미지 인식 카메라가 인식한 결과(이름)을 HTTP GET 방식을 통해 웹으로 보내주면, 웹은 해당 이름을 DB에 저장합니다. 
- DB에 저장된 값들을 통해 웹에서는 `현재 시간의 사용자가 인식된 횟수`, `오늘 하루 동안 사용자가 인식된 총 횟수`를 **웹을 통해 보여주며 생활 습관**을 알 수 있습니다. 
- 등록된 시간 정보들을 보려면 웹 내비게이션 바의 **습관 일지**에 들어가 그동안 등록된 내용을 확인하실 수 있습니다. 
***

### 4-5. 실시간 Text To Speech
![Web Server life Style](/resources/image/webTTS.jpg)
- **실시간**으로 웹의 `TTS 버튼`을 눌러 **메시지**를 작성하고 `변환하기`를 클릭하면 Socket 통신을 통해 라즈베리 파이의 **스피커**에게 내용을 **전송**하여 음성으로 출력할 수 있게 해줍니다.
***

### 4-6. 할 일 예약 및 알림
![Web Server life Style](/resources/image/webTodo.jpg)
- 웹의 **할 일 등록**에서 예약할 날짜와 시간을 정하고 할 일을 작성한 후 `등록`버튼을 클릭하면 DB에 등록되고, 예약된 날짜와 시간에 서버에서 소켓을 통해 라즈베리 파이에게 보내어 스피커로 출력할 수 있게 해줍니다.
- 웹 내비게이션의 **할 일 목록**을 클릭하면 할 일 목록을 보실 수 있습니다.
***

### 4-7. 실시간 웹 스트리밍 
![Web Server Streaming](/resources/image/serverStreaming.jpg)
- 라즈베리 파이 보안 카메라가 자신의 서버로 스트리밍을 출력하는 것을 `<iframe>`태그를 이용하여 **실시간으로 집안 내부**를 **웹**으로 **확인** 할 수 있습니다.
***
