package org.krzysztofus.bifrost;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;

import static java.lang.String.format;

/**
 * Created by krzysztofus on 3/20/2019.
 */
public class Bifrost {

    private static final Logger logger = LogManager.getLogger(Bifrost.class);

    public static final String REMOTE_URL = "http://lawiny.topr.pl/viewpdf";

    public static final String USER_HOME = "user.home";
    public static final String DIR_NAME = "avalanche-dispatch";
    public static final String FILE_NAME = "%d.pdf";

    private final Now now = new Now();

    public void execute() throws IOException {
        final URL url = new URL(REMOTE_URL);

        final File file = getFile();
        prepareDirectory(file);

        final ReadableByteChannel channel = Channels.newChannel(url.openStream());
        final FileOutputStream fileOutputStream = new FileOutputStream(file);

        fileOutputStream.getChannel()
                .transferFrom(channel, 0, Long.MAX_VALUE);

    }

    private File getFile() {
        return Paths.get(
                System.getProperty(USER_HOME),
                DIR_NAME,
                now.getCurrentYear(),
                now.getCurrentMonth(),
                format(FILE_NAME, now.getCurrentDay())
        ).toFile();
    }

    private void prepareDirectory(File file) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                logger.error("Could not create a directory! " + file.toString(), e);
            }
        }
    }

    private static class Now {
        private final LocalDate now = LocalDate.now();

        private String getCurrentYear() {
            return String.valueOf(now.getYear());
        }

        private String getCurrentMonth() {
            final Month month = now.getMonth();
            return month.name();
        }

        private int getCurrentDay() {
            return now.getDayOfMonth();
        }

    }

}
