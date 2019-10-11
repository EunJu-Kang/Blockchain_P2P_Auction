도커명령어

- sudo docker ps      -> 현재 실행하고 있는 컨테이너 확인
- sudo docker ps -a  -> 내 모든 컨테이너 확인
- docker version -> 버전확인



도커설치

- sudo passwd root -> 초기 비번설정
- sudo apt update -> 업데이트 한번 하기
- --------------일반적인 설치
- sudo apt install apt-transport-https
- sudo apt install ca-certificates
- sudo apt install curl
- sudo apt install software-properties-common
- curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - 
- sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
- sudo apt update
- -----------------------------------------도커 설치
- apt-cache policy docker-ce
- sudo apt install docker-ce
- --------------------------------------도커 상태확인
- sudo systemctl status docker
- -------------------------------test 
- sudo docker run hello-world 
- //이미지 씀 없으면 다운받아와서 가져옴
- docker images  ->내가 가진 이미지 확인
- -----------------------------실행
- sudo docker run hello-world
- -------------------------어떤 컨테이너 삭제
- docerk rm 이미지이름
- -----------------------도커실행할때
- docker run -p 호스트port:컨테이너포트 레포지토리이름
- -------------------------컨테이너 모조리강제삭제
- docker rm -f `docker ps -a -q`
- ----------------------------마운트 시키는법
- docker run -p 호스트port:컨테이너포트 -v /home/ubuntu/경로/:/경로 레포지토리이름
- ------------------------------------mysql
- 도커이미지 다운로드
- docker pull 도커허브 이름
- docker pull emblockit/haribo-mysql
- -------------실행
- docker run -d -p 3307:3306 --name vilien-mysql -v /home/ubuntu/chainVilien/mysql:/var/lib/mysql emblockit/haribo-mysql 
- -----------------------------------mysql 접속
- docker exec -it 862c5f4b8701 /bin/bash
- -------------------그냥 밖에서 이거 입력하면 mysql 접속 ㄱ
-  mysql -u root -p --host 127.0.0.1 --port 3307 
- ------------------------컨테이너로 접속하면
- mysql -u root -p
- 패스워드입력  
- Id: haribo // pw : haribo
- root // hariboadmin
- -----------------------------컨테이너정보보기
- docker inspect 컨테이너아이디
- 