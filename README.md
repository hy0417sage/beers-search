# beers-search
Punck API를 활용해 검색어에 해당되는 맥주를 검색하고 북마크하는 안드로이드 앱입니다.
~~~
https://punkapi.com/documentation/v2
~~~

## ◼️ Features
- UI는 activity 2개를 사용했습니다.
  1. 첫 번째 activity : 검색 결과
    - 앱을 시작하면 맥주 검색 화면이 시작됩니다. 검색어를 입력하고 검색 버튼을 누르면 punck API가 1~(최대 3페이지)를 호출합니다.
      - 데이터를 페이징해서 보여주고, 스크롤을 통해 다음 페이지를 호출할 수 있습니다.
      - 검색된 맥주들의 목록에는 맥주의 표지 사진과 제목, 북마크 여부가 표현됩니다.
      - 다시 검색하더라도 북마크 된 맥주라면 북마크 표시가 유지됩니다.
      - 북마크 된 맥주들은 앱 재시작 후에 재 검색해도 다시 확인할 수 있습니다.
      - 검색된 이미지를 클릭하면 상세 화면으로 이동합니다.
  2. 두 번째 activity : 상세 화면
     - 북마크 버튼을 누르면 해당 맥주를 북마크에 추가하거나 삭제할 수 있습니다.
     - 검색 화면으로 돌아갔을 때 해당 셀에 북마크가 반영됩니다.
     - 앱을 종료한 후 다시 실행 했을 때도 북마크는 유지됩니다.
       
|검색|
|:---------------------------------------------------------------------------|
| <img width="653" alt="스크린샷 2023-06-21 오전 10 27 03" src="https://github.com/hy0417sage/media-storage/assets/97173983/1b5db803-39c0-418b-9381-2527319622a7"> |
| **상세** |
| <img width="654" alt="스크린샷 2023-06-21 오전 10 27 49" src="https://github.com/hy0417sage/media-storage/assets/97173983/73694ed2-f883-4744-b6e1-109100a10803"> |
| **Error handling** |
| <img width="434" alt="스크린샷 2023-06-21 오전 10 28 17" src="https://github.com/hy0417sage/media-storage/assets/97173983/ab290e1d-a9d9-46e4-a4af-c00744fe247d"> |


## ◼️ Architecture & Modularization
- Clean Architecture를 참고하여 멀티모듈 앱을 설계 했습니다.
- 관심사 분리(UI, Domain, Data)를 통해 코드의 복잡성을 줄일 수 있었고, Hilt를 통한 의존성 주입 구현을 통해 클래스간 의존 관계를 분리해 유지보수가 쉬워졌습니다.
- 저수준 모듈인 UI와 Data가 고수준 모듈(추상적 모듈)인 Domain 에 의존하도록 구현해서 고수준 모듈이 저수준 모듈에 영행을 받지 않게 해 유지보수 및 확장에 용이하도록 설계했습니다.

<p align='center'>
<img width='600' src='https://github.com/hy0417sage/media-storage/assets/97173983/786952ac-6e64-4ec9-89a9-50203a99a998'>
</p>

- MVVM 아키텍처와 Repository 패턴을 베이스로 하여 구성했습니다.

<p align='center'>
<img width='600' src='https://user-images.githubusercontent.com/97173983/216803295-5ea485be-d9ff-429d-ac31-3b1d4e2e646f.png'>
</p>


## Description of each module
- app : DI, Application, UI 클래스와 레이아웃을 모아놓은 모듈입니다.
- core : 각 모듈에서 공통으로 필요한 클래스와 리소스를 모아놓은 모듈입니다.
- data : 서버로 부터 데이터를 받아 가공하는 비즈니스 로직이 포함됩니다.
- domain : Data 모듈과 UI 모듈의 의존성을 분리하고, 저수준 모듈(UI, Data)의 추상화 모듈인 고수준 모듈입니다.


## ◼️ Tech stack & Open-source libraries
### Android
- Minimum SDK level 26
- MVVM pattern

### *****Skill Set*****
| 구분 | Skill |
|:---|:---------------------------------------------------------------------------|
| Language | Kotlin |
| Networking | Retrofit, Okhttp, Moshi |
| Asynchronous | Coroutine, Flow |
| DI | Hilt |
| ETC |ViewBinding, Glide, Paging3, DiffUtil |
