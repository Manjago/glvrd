package com.temnenkov.statstore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.temnenkov.tgibot.tgapi.dto.Update;
import com.temnenkov.tgibot.tgapi.method.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.temnenkov.IOUtils.readFullyAsString;

public class JdbcStatStore implements StatStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcStatStore.class);

    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private volatile JdbcOperations jdbcTemplate;
    private DataSource dataSource;

    @Override
    public void storeUpdate(Update update) {
        if (update == null) {
            return;
        }

        final String content = gson.toJson(update);

        LOGGER.debug("store {}",  content);

        store(update.getMessage().getChat().getId(), content, UpdateType.UPDATE);
    }

    @Override
    public void storeSendMessage(SendMessage sendMessage) {
        if (sendMessage == null) {
            return;
        }

        final String content = gson.toJson(sendMessage);

        LOGGER.debug("store {}",  content);

        store(sendMessage.getChatId(), content, UpdateType.SENDMESSAGE);
    }

    private void store(Long id, String content, UpdateType updateType) {
        jdbcTemplate.update(Query.STORE.getSql(), ps -> {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setLong(2, id);
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setString(4, content);
            ps.setInt(5, updateType.getType());
        });

    }

    void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            setUp();
        } catch (SQLException | IOException e) {
            throw new BeanInitializationException("fail init statstore", e);
        }
    }

    private void setUp() throws SQLException, IOException {

        List<Integer> res= jdbcTemplate.queryForList(Query.IS_NEED_DB_CREATE.getSql(), Integer.class, "STOREDEVENT");

        if (res.isEmpty()) {
            jdbcTemplate.execute(readFullyAsString(
                    getClass().getResourceAsStream("/statstore.sql"), "UTF-8"));

            LOGGER.info("create stat database");
        } else {
            LOGGER.info("stat database already exists");
        }

    }

    @Required
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private enum UpdateType {
        UPDATE(0), SENDMESSAGE(1);

        private int type;

        UpdateType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private enum Query {
        IS_NEED_DB_CREATE("SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?"),
        STORE("INSERT into STOREDEVENT " +
                "(EVENT_ID, CHAT_ID, CREATED, EVENT_BODY, EVENT_TYPE)"
                + " values (?, ?, ?, ?, ?)");

        private final String sql;

        Query(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }

    }

}
