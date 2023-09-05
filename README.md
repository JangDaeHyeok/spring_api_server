# spring_api_server
Spring API 암/복호화 통신 - 서버

## Spring API 암/복호화 통신 로직
![image](https://github.com/JangDaeHyeok/spring_api_client/assets/92128277/f584b265-58c3-43b6-89d6-4cba8fbb709d)

- 서버에서 실행된는 암/복호화 로직은 Filter에서 실행

## Test 결과
- api 성능 테스트 결과 암호화 전 문자열 데이터 길이 최대 37만개 까지 가능 (환경에 따라 다를 수 있음)
![api_test](https://user-images.githubusercontent.com/92128277/150636597-23098e7c-9ec7-40a4-808c-4c9b4f13bd72.png)
