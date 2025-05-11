package com.example.hackathonback.config;

import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./") // 루트 디렉토리 기준일 경우
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dotenv.get("DB_URL"));
        dataSource.setUsername(dotenv.get("DB_USERNAME"));
        dataSource.setPassword(dotenv.get("DB_PASSWORD"));
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return dataSource;
    }
}
