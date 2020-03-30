

- 문제해결전략

Properties 파일을 읽어서 Dict 을 만든다.

차단목록 Dict 의 구조는 { CIDR: [start_ip_num] } 의 구조이다.

임의의 IP 가 들어오면 현재 차단 목록에 들어있는 CIDR 이 낮은 순서대로 IP 의 해당 CIDR 의 start_ipnum 를 구한다.

차단목록 Dict 에 start_ip_num 이 존재하면 차단을 시키고 아니면 통과시킨다.

[start_ip_num] 은 HashSet 을 사용하여 중복을 없애고 O(1) 로 찾을 수 있게 하였다.


- 빌드방법

프로젝트는 gradle 를 이용하여 빌드할 수 있다.

IntelliJ 를 이용하여 spring boot 를 실행시킬 수 있다.


- 웹어플리케이셔 부하테스트

nGrinder 를 이용하였다.

로컬에 Agent 와 서버, controller 를 모두 동작시켰다.
