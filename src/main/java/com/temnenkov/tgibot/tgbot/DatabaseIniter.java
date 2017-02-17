package com.temnenkov.tgibot.tgbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

import static com.temnenkov.IOUtils.readFullyAsString;

public class DatabaseIniter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIniter.class);

    private DataSource dataSource;

    @Required
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void setUp() throws SQLException, IOException {

        try (Connection conn = dataSource.getConnection()) {
            if (isNeedDbCreate(conn, "INT_MESSAGE")) {
                execStatement(conn, readFullyAsString(
                        getClass().getResourceAsStream("/org/springframework/integration/jdbc/schema-h2.sql"), "UTF-8"));
                LOGGER.info("create queue database");
            } else {
                LOGGER.info("queue database already exists");
            }
        }

    }

    private boolean isNeedDbCreate(Connection conn, String tableName) throws SQLException {
        try (PreparedStatement checkTable = conn.prepareStatement("SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?")) {
            checkTable.setString(1, tableName);
            try (ResultSet rs = checkTable.executeQuery()) {
                return !rs.first();
            }
        }
    }

    private void execStatement(Connection conn, String sql) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
    }

}

