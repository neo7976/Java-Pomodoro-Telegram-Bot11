package com.example.javapomodorotelegrambotspring.config;

import com.example.javapomodorotelegrambotspring.TimerRepository;
import com.example.javapomodorotelegrambotspring.bot.Pomodoro;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import javax.sql.DataSource;

@Configuration
public class BotConfiguration {

    @Bean
    public Pomodoro pomodoro(TimerRepository timerRepository) throws TelegramApiException {
        return new Pomodoro(timerRepository);
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://192.168.99.100:5432/postgres");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        return new HikariDataSource(hikariConfig);
    }
    @Bean
    public TimerRepository timerRepository(DataSource dataSource) {
        return new TimerRepository(dataSource);
    }
}
