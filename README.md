# Auto Store


> 상품 등록 자동화 시스템 및 서버 구현


## API 명세서

|   방식    | HTTP Method |    기능    | 주소                          | 
|:-------:|:-----------:|:--------:|:----------------------------| 
|   MVC   |     GET     |  상품 조회   | /mvc/product/find/{id}      |
|   MVC   |     GET     | 상품 전체 조회 | /mvc/product/find-all       |
|   MVC   |     PUT     | 상품 정보 수정 | /mvc/product/update         |
|   MVC   |    POST     |  상품 등록   | /mvc/product/create         |
|   MVC   |   DELETE    |  상품 삭제   | /mvc/product/delete/{id}    |
| WebFlux |     GET     |  상품 조회   | /web-flux/product/find/{id} |
|   WebFlux   |     GET     | 상품 전체 조회 | /web-flux/product/find-all       |
|   WebFlux   |     PUT     | 상품 정보 수정 | /web-flux/product/update         |
|   WebFlux   |    POST     |  상품 등록   | /web-flux/product/create         |
|   WebFlux   |   DELETE    |  상품 삭제   | /web-flux/product/delete/{id}    |

