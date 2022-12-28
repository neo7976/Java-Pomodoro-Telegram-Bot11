package com.example.javapomodorotelegrambotspring.config;

import com.example.javapomodorotelegrambotspring.bot.Pomodoro;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class BotConfiguration {

    @Bean
    public Pomodoro pomodoro() throws TelegramApiException {
        return new Pomodoro();
    }
}
