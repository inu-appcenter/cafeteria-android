# cafeteria-android

[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/inu-appcenter/cafeteria-android/Android%20CI)](https://github.com/inu-appcenter/cafeteria-android/actions?query=workflow%3A%22Node.js+CI%22)
[![GitHub last commit](https://img.shields.io/github/last-commit/inu-appcenter/cafeteria-android)](https://github.com/inu-appcenter/cafeteria-android/commits)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/inu-appcenter/cafeteria-android)](https://github.com/inu-appcenter/cafeteria-android/releases/latest)
[![GitHub stars](https://img.shields.io/github/stars/inu-appcenter/cafeteria-android?style=shield)](https://github.com/inu-appcenter/cafeteria-android/stargazers)
[![GitHub issues](https://img.shields.io/github/issues/inu-appcenter/cafeteria-android)](https://github.com/inu-appcenter/cafeteria-android/issues)
![GitHub closed issues](https://img.shields.io/github/issues-closed/inu-appcenter/cafeteria-android)
[![GitHub license](https://img.shields.io/github/license/inu-appcenter/cafeteria-android)](https://github.com/inu-appcenter/cafeteria-android/blob/master/LICENSE)

**Cafeteria Android 앱**

> #### Cafeteria 관련 저장소 일람
>
> ##### 서비스
> - API 서버: [cafeteria-server](https://github.com/inu-appcenter/cafeteria-server)
> - **Android 앱**: [cafeteria-android](https://github.com/inu-appcenter/cafeteria-android)
>
> ##### 운영 관리
> - 콘솔 API 서버: [cafeteria-console-server](https://github.com/inu-appcenter/cafeteria-console-server)
> - 콘솔 웹 인터페이스: [cafeteria-console-web](https://github.com/inu-appcenter/cafeteria-console-web)
>
> ##### 배포 관리
> - API 서버 배포 스크립트: [cafeteria-server-deploy](https://github.com/inu-appcenter/cafeteria-server-deploy)

## 개요

다음 기능을 제공합니다:

- 식단 정보
- 번호 알림
- 할인 바코드
- 고객 지원

## 프로젝트 구조

이 프로젝트는 클린 아키텍쳐에서 영감을 받아 설계되었으며, 총 네 개의 모듈로 이루어져 있습니다:

> ### app
> Intrastructure 레이어에 해당합니다.
>
> 안드로이드 프리젠테이션 UI를 담당합니다. 액티비티와 프래그먼트, 뷰모델, 리소스와 위젯 등이 여기에 속합니다. `MVVM` 패턴을 적용하였습니다.

> ### domain
> Domain 레이어에 해당합니다.
> 
> 엔티티, 유스케이스, 저장소 정의, 서비스 정의 등이 여기에 속합니다.

> ### data
>Interfaces/Infrastructure 레이어에 해당합니다.
>
>저장소의 구현, Shared Preferences, Retrofit factory 등이 여기에 속합니다.

> ### common
>모든 레이어에서 참조할 수 있는 유틸리티 모음입니다.
>
>`UseCase`, `Repository` 원형에 대한 정의와 안드로이드/비 안드로이드 유틸리티, 설정 오브젝트가 여기에 속합니다.

## 업데이트 로그

### 2021.1.9 v4.2.0b6
- 할인 이용 안내 추가
- 서버 API 변경 대응
- 식단 상세보기 페이지 버그 수정
- 뒤로가기 키 동작 복구
- 오프라인 대응 개선
- 화면전환 대응 개선

### 2021.1.8 v4.2.0b5
- 인앱 리뷰 API 추가
- Remote Config 추가
- Crashlytics 추가
- Analytics 추가

### 2021.1.3 v4.2.0b4
- 공지 다이얼로그 배경 수정
- 주문 추가 버튼 아이콘 교체
- 문의와 답변 3줄까지만 표시

### 2021.1.3 v4.2.0b3
- 온보딩 힌트 개선

### 2021.1.2 v4.2.0b2
- 온보딩 힌트 추가
- 경고 및 안내 문구 조금 수정
- 카페테리아 순서 재설정 후 새로고침 안 되는 버그 해결

### 2020.12.31 v4.2.0b1
- 번호알림 기능 추가!!
- 멤버십 패스 레이아웃 사소한 수정

### 2020.12.6 v4.1.1
- 작성중인 문의 글자 수 표시
- 식단이 이상하게 표시되는 문제 해결

### 2020.12.5 v4.1.0
- 불필요한 권한 제거

### 2020.12.4 v4.1.0b6
- 테마 색상과 앱 로고 변경
- kotlin-android-extension 사용 중단

### 2020.12.4 v4.1.0b5
- 화면 회전에 따른 옵션 메뉴 실종 및 불필요한 새로고침 제거
- 문의 내역 새로고침 추가
- 네트워크 상태 불일치 문제 해결

### 2020.12.3 v4.1.0b4
- 문의 글자 수 제한 추가
- 문의 화면 UI 개선
- 카카오톡이 없으면 옵션 표시하지 않음

### 2020.12.2 v4.1.0b3
- 메인 테마 컬러 교체
- 공지 폰트 크기 변경

### 2020.12.1 v4.1.0-beta02
- 웹뷰 개선
- 문의시 기기 제조사와 모델명 수집

### 2020.11.30 v4.1.0-beta01
- 고객센터 신설
- 공지/서비스안내/FAQ/1:1문의 지원

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

## 라이센스

소스 코드에는 GPLv3 라이센스가 적용됩니다. 라이센스는 [이곳](/LICENSE)에서 확인하실 수 있습니다.
