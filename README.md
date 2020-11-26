# cafeteria-android

**Cafeteria Android 앱**

> #### Cafeteria 관련 저장소 일람
>
> ##### 서비스
> - API 서버: [cafeteria-server](https://github.com/inu-appcenter/cafeteria-server)
> - **Android 앱**: [cafeteria-android](https://github.com/inu-appcenter/cafeteria-android)
>
> ##### 운영 관리
> - 관리용 API 서버: [cafeteria-management-server](https://github.com/inu-appcenter/cafeteria-management-server)
> - 웹 콘솔: [cafeteria-management-web](https://github.com/inu-appcenter/cafeteria-management-web)
>
> ##### 배포 관리
> - API 서버 배포 스크립트: [cafeteria-server-deploy](https://github.com/inu-appcenter/cafeteria-server-deploy)


## 개요

인천대학교 교내 학식당을 포함한 식당의 메뉴를 보여주며 생협 학생 할인을 위한 바코드를 제공합니다.


## 사용자이신가요


### 주요 기능
- 학식, 카페테리아, 기숙사 식당 등을 교내 식당의 식단 정보를 보기 좋게 제공.
- 기숙사 식당에서 조식 할인, 사범대 식당에서 점심 저녁 할인을 받을 수 있는 생협 학생 할인 바코드 내장.


### 사용법

1. [스토어](https://play.google.com/store/apps/details?id=com.inu.cafeteria&hl=en_US)에서 애플리케이션을 설치한다.
2. 실행 후 로그인한다. (선택사항)
3. 단 한번의 클릭으로 오늘 어떤 밥이 나오는지 파악한다.
4. 밥 먹으러 가면서 바코드를 열고 할인받는다.


## 개발자이신가요
개발 문서는 [여기](docs/TECH.md)로


## 업데이트 로그

### 2020.11.26 v4.0.0
- 완전히 새로운 디자인!
- 서버 이전에 맞춰 개선

### 2020.11.3 v4.0.0-beta02
- 식단 카드뷰 간격 축소.
- 아이콘과 앱 이름 변경.

### 2020.11.1 v4.0.0-beta01
- 새로운 디자인.
- 새로운 서버.

### 2019.11.26 v3.0.0
- Android CI 적용.
- 3.0.0 업데이트 배포.

### 2019.11.26 v3.0.0-beta06
- 바코드 표시될 때 화면 밝아짐.
- 공지 표시됨.

### 2019.11.23 v3.0.0-beta05
- 스와이프 제스처 추가.
- 바코드 로드 중에 취소하면 앱이 죽는 버그 수정.

### 2019.9.11 v3.0.0-beta04
- 로그인 상태에서 back button을 눌러 액티비티를 종료시 다시 로그인해야 하는 문제 해결.
- 저장된 학번, 토큰과 바코드 암호화.
- 비회원으로 진입시 옵션 메뉴에 로그인 항목 추가.

### 2019.9.10 v3.0.0-beta03
- 네트워크 오류 발생시 다시 시도 선택지 제시
- 자동로그인 중 로딩화면 표시
- 로드 중에는 다른 뷰와 상호작용 중단

### 2019.9.8 v3.0.0-beta02
- 바코드 기능 인터페이스 변경
- Android 9.0 네트워크 오류 해결

### 2019.9.7 v3.0.0-beta01

- 디자인 변경
- 소스 다시 작성


## 스페셜 땡스 투

- [훌륭한 레퍼런스](https://fernandocejas.com/2018/05/07/architecting-android-reloaded/) 제공해준 [Fernando Cejas](https://fernandocejas.com)
- [안드로이드 학습하는 데에 큰 도움이 된 앱](https://github.com/moezbhatti/qksms)을 만들고 관리하는 [Moez Bhatti](https://github.com/moezbhatti)
- 먼저 왔다 가신 1기, 2기 개발팀
- 그리고 우리 팀
