package com.temnenkov.tgibot.tgbot;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> LOGGER.info("Shutdown")));

        AppParams pars = new AppParams();
        try {
            new JCommander(pars, args);
            new Main().process(pars);
        } catch (ParameterException e) {
            LOGGER.warn("bad parameters", e);
            new JCommander(pars).usage();
            return;
        } catch (Exception e) {
            LOGGER.error("fail", e);
        }


        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            LOGGER.warn("Main thread was interrupted", e);
            Thread.currentThread().interrupt();
        }


    }

    private void process(AppParams appParams) throws IOException, SQLException {

        try {
            updateSystemProperties(appParams.getConfig());
        } catch (IOException e) {
            LOGGER.error("fail read config file {}", appParams.getConfig(), e);
            return;
        }

        dbInit();

        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("integration-context-core.xml", "integration-context.xml");
        applicationContext.registerShutdownHook();
        LOGGER.info("Started");
    }

    private void dbInit() throws IOException, SQLException {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("integration-context-prestart.xml", "integration-context-core.xml");
        DatabaseIniter databaseIniter = applicationContext.getBean(DatabaseIniter.class);
        databaseIniter.setUp();

        applicationContext.close();
    }

    private void updateSystemProperties(String filepath) throws IOException {
        try (InputStream propFile = new FileInputStream(filepath)) {
            Properties p =
                    new Properties(System.getProperties());
            p.load(propFile);

            System.setProperties(p);
        }
    }

}
