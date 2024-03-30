package org.example.api.webflux.domain.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableR2dbcRepositories // R2DBC를 사용 가능하게 해줌
@EnableR2dbcAuditing // DTO의 @CreatedDate과 같은 어노테이션을 사용 가능하게 해줌
public class R2dbcConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final DatabaseClient databaseClient;

    // 간단한 쿼리를 통해 클라이언트와의 커넥션 검증
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // reactor에 있는 publisher와 subscriber
        databaseClient.sql("SELECT 1").fetch().one()
            .subscribe(
                success -> { // 커넥션 성공 시
                    log.info("Initialize r2dbc database connection.");
                },
                error -> { // 커넥션 실패 시
                    log.error("Failed to initialize r2dbc database connection.");
                    SpringApplication.exit(event.getApplicationContext(), () -> -110); // 커넥션 오류 시 어플리케이션 종료 (선택 사항)
                }
            );

    }
}

