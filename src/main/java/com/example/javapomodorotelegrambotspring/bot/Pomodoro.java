package com.example.javapomodorotelegrambotspring.bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

public class Pomodoro {
    private static final ConcurrentHashMap<Timer, Long> userTimer = new ConcurrentHashMap<>();

    public Pomodoro() throws TelegramApiException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        PomodoroBot bot = new PomodoroBot();
        telegramBotsApi.registerBot(bot);

        //начинается написание многопоточности
        new Thread(() -> {
            try {
                bot.checkTimer();
            } catch (InterruptedException e) {
                System.out.println("Упс");
            }
        }).run();
        //заканчивается
    }

    static class PomodoroBot extends TelegramLongPollingBot {

        @Override
        public String getBotUsername() {
            return "Pomodoro timer";
        }

        @Override
        public String getBotToken() {
            return "5476484674:AAE3HNfDSLqkp63ygIKrKo3CtPN2jrJgBs8";

        }

        @Override
        public void onUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Long chatId = update.getMessage().getChatId();
                if (update.getMessage().getText().equals("/start")) {
                    sendMsg("Ehi pomodoro. Напиши пожалуйста команду\n" +
                            "Задай мне время работы, отдыха и количество повторов через пробел. Например: " +
                            "'1 1 1'.\nP.s. Я пока работаю в минутах ", chatId.toString());
                } else {
                    var args = update.getMessage().getText().split(" ");
                    if (args.length >= 1) {
                        var workTime = Instant.now().plus(Long.parseLong(args[0]), ChronoUnit.MINUTES);
                        userTimer.put(new Timer(workTime, TimerType.WORK), chatId);
                        sendMsg("Давай работай!", chatId.toString());

                        if (args.length >= 2) {
                            var breakTime = workTime.plus(Long.parseLong(args[1]), ChronoUnit.MINUTES);
                            userTimer.put(new Timer(breakTime, TimerType.BREAK), chatId);

                            if (args.length >= 3) {
                                long cycle = Long.parseLong(args[2]) - 1;
                                while (cycle != 0) {
                                    workTime = breakTime.plus(Long.parseLong(args[0]), ChronoUnit.MINUTES);
                                    userTimer.put(new Timer(workTime, TimerType.WORK), chatId);
                                    breakTime = workTime.plus(Long.parseLong(args[1]), ChronoUnit.MINUTES);
                                    userTimer.put(new Timer(breakTime, TimerType.BREAK), chatId);
                                    cycle--;
                                }
                            }
                        }
                    }
                    sendMsg("test server_time = " + Instant.now().toString(), chatId.toString());
                }
            }
        }


        public void checkTimer() throws InterruptedException {
            while (true) {
                System.out.println("Количество таймеров пользователей " + userTimer.size());
                userTimer.forEach((timer, userId) -> {
                    System.out.printf("Проверка userId = %d, server_time = %s, user_timer = %s\n",
                            userId, Instant.now().toString(), timer.getTime().toString());
                    if (Instant.now().isAfter(timer.getTime())) {
                        userTimer.remove(timer);
                        if (!userTimer.containsValue(userId)) {
                            sendMsg("Цикл работы пользователя истек.", userId.toString());
                        } else {
                            switch (timer.getTimerType()) {
                                case WORK:
                                    sendMsg("Пора отдыхать!!!", userId.toString());
                                    break;
                                case BREAK:
                                    sendMsg("Пора работать!!!", userId.toString());
                                    break;
                            }
                        }
                    }
                });
                Thread.sleep(1000);
            }
        }


        private void sendMsg(String text, String chatId) {
            SendMessage msg = new SendMessage();
            // пользователь чата
            msg.setChatId(chatId);
            msg.setProtectContent(true);
            msg.setText(text);

            try {
                execute(msg);
            } catch (TelegramApiException e) {
                System.out.println("УПС!!!");
            }
        }
    }
}

