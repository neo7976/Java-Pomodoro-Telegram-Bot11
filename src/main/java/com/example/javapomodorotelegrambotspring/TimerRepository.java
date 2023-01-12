package com.example.javapomodorotelegrambotspring;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TimerRepository {

    private final DataSource dataSource;
    private static final String INSERT_QUERY = "insert into sobinda.user_timer(user_id, timer_type) values (?, ?);";

    public TimerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(String userId, String timerType) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, timerType);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
