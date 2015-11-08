package com.stefanski.liner.server;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileFactory;
import com.stefanski.liner.server.command.Command;
import com.stefanski.liner.server.command.LineCommand;
import com.stefanski.liner.server.command.ShutdownCommand;
import com.stefanski.liner.server.communication.Communication;
import com.stefanski.liner.server.communication.CommunicationDetector;
import com.stefanski.liner.server.response.LineResponse;
import com.stefanski.liner.server.response.Response;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Dariusz Stefanski
 * @since Sep 4, 2013
 */
@Slf4j
public class BasicLinerTest extends LinerTest {

    private Thread serverThread;

    @Test
    public void shouldServerStopAfterReceivingShutdownMsg() throws Exception {
        Communication communication = mock(Communication.class);
        when(communication.receiveCommand()).thenReturn(ShutdownCommand.getInstance());

        int simultaneousClientsLimit = 1;
        LinerServer server = createServer(simultaneousClientsLimit, communication);
        serverThread = startServer(server, "filename.txt");
        serverThread.join();

        // Otherwise it will be still running and test will not finish
    }

    @Test
    public void shouldServerSupportMultipleSimultaneousClients() throws Exception {
        int simultaneousClientsLimit = 3;
        SlowCommunication communication = new SlowCommunication();
        LinerServer server = createServer(simultaneousClientsLimit, communication);
        serverThread = startServer(server, "filename.txt");
        serverThread.join();

        assertEquals("Not all command were executed", 2, communication.getResponseCount());
    }

    private LinerServer createServer(int simultaneousClientsLimit, Communication communication) {
        CommunicationDetector detector = mock(CommunicationDetector.class);
        when(detector.acceptNextClient()).thenReturn(communication);

        TextFile textFile = mock(TextFile.class);
        when(textFile.getLine(Mockito.anyLong())).thenReturn("line");

        TextFileFactory textFileFactory = mock(TextFileFactory.class);
        when(textFileFactory.createFromFile(anyString())).thenReturn(textFile);

        return new LinerServer(simultaneousClientsLimit, detector, textFileFactory);
    }

    class SlowCommunication implements Communication {

        private final AtomicInteger responseCounter = new AtomicInteger();
        private final AtomicInteger count = new AtomicInteger();

        @Override
        public void close() {
        }

        @Override
        public void sendResponse(Response resp) {
            if (resp instanceof LineResponse) {
                log.debug("Sending response {}", resp);
                responseCounter.incrementAndGet();
            }
        }

        @Override
        public Command receiveCommand() {
            // Line, Line, Shutdown
            int val = count.getAndIncrement();
            if (val < 2) {
                return createSlowlyLineCommand();
            } else {
                return createSlowlyShutdownCommand();
            }
        }

        public int getResponseCount() {
            return responseCounter.get();
        }

        private LineCommand createSlowlyLineCommand() {
            log.debug("Creating slowly line command");
            sleep(200);
            return new LineCommand(123L);
        }

        private ShutdownCommand createSlowlyShutdownCommand() {
            log.debug("Creating slowly shutdown command");
            sleep(300);
            return ShutdownCommand.getInstance();
        }

        private void sleep(long delay) {
            log.debug("Waiting...");
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                log.info("InterruptedException when sleeping: ", e);
            }
            log.debug("Waiting finished");
        }
    }
}
