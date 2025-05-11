package com.example.hackathonback.config;

import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration // [한 줄 요약] Spring에서 데이터베이스 연결 설정을 위한 설정 클래스
// 이 클래스는 HikariCP 커넥션 풀을 사용하여 데이터베이스 연결 정보를 구성합니다.
// .env 파일에서 DB 관련 설정을 읽어와 HikariDataSource를 생성합니다.
public class DataSourceConfig {

    @Bean // [한 줄 요약] DataSource 빈을 생성하여 Spring에 등록
    public DataSource dataSource() {
        // [한 줄 요약] .env 파일을 현재 프로젝트 루트에서 불러옴
        Dotenv dotenv = Dotenv.configure()
                .directory("./")             // .env 파일이 위치한 디렉토리 설정 (현재 디렉토리 기준)
                .ignoreIfMalformed()         // 잘못된 형식이 있어도 무시하고 로딩 계속
                .ignoreIfMissing()           // .env 파일이 없더라도 예외 발생 없이 무시
                .load();                     // 설정 적용 및 환경 변수 로드

        HikariDataSource dataSource = new HikariDataSource();

        // [한 줄 요약] .env에서 불러온 DB 설정값 적용
        dataSource.setJdbcUrl(dotenv.get("DB_URL"));                  // JDBC 연결 URL
        dataSource.setUsername(dotenv.get("DB_USERNAME"));            // DB 사용자 이름
        dataSource.setPassword(dotenv.get("DB_PASSWORD"));            // DB 비밀번호
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");    // MySQL 드라이버 클래스명

        return dataSource; // [한 줄 요약] 구성된 HikariDataSource 반환
    }
}
