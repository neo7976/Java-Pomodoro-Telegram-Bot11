package com.example.javapomodorotelegrambotspring.bot;

import java.time.Instant;

public class Timer {
    private Instant time;
    private TimerType timerType;

    public Timer(Instant time, TimerType timerType) {
        this.time = time;
        this.timerType = timerType;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public TimerType getTimerType() {
        return timerType;
    }

    public void setTimerType(TimerType timerType) {
        this.timerType = timerType;
    }
}
