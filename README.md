# 스마트 홈 알리미

1. 프로젝트 설명

2. SW 아키텍처

3. 데모 및 시연 영상

4. 라이선스

## 1. 프로젝트 설명
스마트 홈 알리미는 `아두이노`, `라즈베리파이`, `웹`을 이용하여 제작되었다.
- `아두이노`는 집안 내 비정상 온/습도를 상태를 감지하며, 사적인 공간의 접근에 대한 알림을 해준다.
- `라즈베리 파이`는 얼굴 인식을 이용해 생활 패턴을 개선해 주고, 웹캠을 이용해 위험 상황을 실시간으로 탐지한다. 또한 외부에서 집 안에 있는 스피커를 통해 예약된 할 일 등을 알려줄 수 있다.
- `웹`에서 모든 것을 실시간으로 확인할 수 있고, 사용자가 쉽게 관리할 수 있도록 하였다.
이러한 Iot의 기능들을 구현하여 집안 보안을 강화하고, 생활의 질을 높여주는 **스마트 홈 알리미**를 제작하였다.

## 2. SW 아키텍처
<p align="center">
    <img src="/resources/image/Architecture.png", width="640">
</p>

- [라즈베리 파이](https://github.com/kimkc/SmartHomeNotification/RaspberryPi) 및 [카카오톡](https://github.com/kimkc/SmartHomeNotification/KakaoTalk)에 대한 구체적인 설명
- [웹 스트리밍](https://github.com/kimkc/SmartHomeNotification/WebStreaming) 및 [이미지 인식](https://github.com/kimkc/SmartHomeNotification/FaceRecognition)에 대한 구체적인 설명
- [서버 및 웹](https://github.com/kimkc/SmartHomeNotification/WebServer)에 대한 구체적인 설명
- [아두이노](https://github.com/kimkc/SmartHomeNotification/Arduino)에 대한 구체적인 설명

## 3. 데모 및 시연 영상
- 데모: [http://106.10.35.183:8080/safehome](http://106.10.35.183:8080/safehome)
- 시연 영상: [https://www.youtube.com/watch?v=sqL7J6I2UBc&t=3s](https://www.youtube.com/watch?v=sqL7J6I2UBc&t=3s)

## 4. 라이센스
This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0), also included in our repository in [the LICENSE file](https://github.com/khw5123/SmartHomeNotification/blob/master/LICENSE).
