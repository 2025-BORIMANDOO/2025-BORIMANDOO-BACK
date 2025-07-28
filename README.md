# 🐳 MEDIMO Backend

Spring Boot 기반 RESTful API 서버로, AWS RDS, S3, GitHub Actions, Docker, Nginx, HTTPS를 활용한 클라우드 환경에서 운영됩니다.
최종 배포 주소: https://medimo.site/

## 📁 프로젝트 구조

```
📦 com.example.borimandoo_back
├─ config # 각종 Configuration 세팅 
├─ global # ApiResponse, GlobalExceptionHandler
├─ controller
├─ service
├─ repository
├─ domain
└─ dto
```

## 🚀 기술 스택

- Java 21
- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- MySQL (AWS RDS)
- AWS EC2 (Amazon Linux 2023)
- AWS S3
- Docker
- Nginx + Certbot (HTTPS)
- GitHub Actions (CI/CD)

## 🛠️ 환경 변수 (.env or GitHub Secrets)

다음 값들을 EC2 환경 변수 또는 GitHub Secrets로 설정해야 합니다:

| 이름                      | 설명                       |
|---------------------------|----------------------------|
| `DB_URL`                 | RDS JDBC URL               |
| `DB_USERNAME`            | DB 사용자명                |
| `DB_PASSWORD`            | DB 비밀번호                |
| `AWS_ACCESS_KEY_ID`      | S3 접근용 AWS 키           |
| `AWS_SECRET_ACCESS_KEY`  | S3 접근용 AWS 시크릿 키    |
| `AWS_REGION`             | AWS 리전 (예: `ap-northeast-2`) |
| `S3_BUCKET`              | 버킷 이름                  |


## ⚙️ 배포 및 CI/CD

### GitHub Actions 기반 자동 배포 흐름

1. `main` 브랜치에 push 발생 시, GitHub Actions가 트리거됨
2. 프로젝트를 빌드하고, Docker 이미지 생성
3. ECR에 푸시한 후 EC2 인스턴스에 SSH로 접속하여:
   - 기존 컨테이너 제거
   - 환경 변수와 함께 새로운 컨테이너 실행

## 🔒 HTTPS 설정 요약 (Nginx + Certbot)

1. EC2 인스턴스에 Nginx 설치
2. 가비아에서 발급받은 도메인 A레코드 → EC2 IP 연결
3. certbot을 사용하여 Let's Encrypt SSL 인증서 발급
4. Nginx에서 HTTPS 설정 및 HTTP → HTTPS 리디렉션 추가


