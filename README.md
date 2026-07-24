# SpringBoard Full Stack

Spring Boot + React 기반 JWT 인증 게시판 서비스 프로젝트

## 📌 Project Overview

Spring Boot REST API와 React를 활용하여 구현한 Full Stack 게시판 서비스 프로젝트입니다.

백엔드 API 구축부터 React 기반 사용자 인터페이스 구현,
JWT 인증 처리, AWS 배포 및 HTTPS 적용까지 실제 서비스 환경을 고려하여 개발했습니다.

---

## 🛠 Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Gradle

### Frontend
- React
- Vite
- Axios

### Infrastructure
- AWS EC2
- AWS RDS (MySQL)
- Nginx
- Let's Encrypt
- DuckDNS

---

## ✨ Features

### Authentication
- 회원가입 / 로그인
- JWT Access Token / Refresh Token 인증
- Axios Interceptor 기반 토큰 갱신
- 로그인 상태 관리

### Board
- 게시글 CRUD
- 게시글 검색
- 페이징 처리
- 파일 업로드

### Comment
- 댓글 CRUD
- 작성자 권한 검증

### Frontend
- React 컴포넌트 기반 UI 구성
- 게시글 목록 / 상세 / 작성 화면
- 로그인 사용자 UI 처리
- REST API 연동

### Deployment
- AWS EC2 배포
- AWS RDS(MySQL) 연동
- Nginx Reverse Proxy 구성
- HTTPS(Let's Encrypt) 적용
- DuckDNS 도메인 연결

---

## 📂 Project Structure
