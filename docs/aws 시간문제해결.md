## 시간 문제

문제점 : 입력받은 시간과 서버의 시간이 다르게 찍힘

해결 : aws timezone과 mysql timezone을 kst로 바꿔줌

##### AWS timezone 바꾸기

```shell
sudo date
>> 현재 date 확인
sudo cat /ect/localtime
>> timezone 확인 : UTC0
sudo rm /etc/localtime
>> timezone 지우기
sudo ln -s /usr/share/zoneinfo/Aisa/Seoul /etc/localtime
>> kst로 localtime 바꿔주기
sudo date
>> 바꾼시간 확인
```

##### mysql timezone바꾸기

- application.properties에서 jdbc url 설정 바꾸기
  - serverTimezone=Asia/Seoul 추가

```java
jdbc:mysql://13.125.178.26:3307/haribo?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul
```
