package com.stefanski.liner.server;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.liner.file.TextFile;
import com.stefanski.liner.file.TextFileException;
import com.stefanski.liner.server.cmd.Command;
import com.stefanski.liner.server.cmd.LineCommand;
import com.stefanski.liner.server.cmd.ShutdownCommand;
import com.stefanski.liner.server.comm.Communication;
import com.stefanski.liner.server.comm.CommunicationDetector;
import com.stefanski.liner.server.comm.CommunicationException;
import com.stefanski.liner.server.resp.LineResponse;
import com.stefanski.liner.server.resp.Response;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
@Slf4j
public class BasicLinerTest extends LinerTest {

    private Thread serverThread;

    @Test
    public void shouldServerStopAfterReceivingShutdownMsg() throws Exception {
        Communication communication = mock(Communication.class);
        when(communication.receiveCommand()).thenReturn(ShutdownCommand.getInstance());

        int simultaneousClientsLimit = 1;
        serverThread = startServer(createServer(simultaneousClientsLimit, communication));
        serverThread.join();

        // Otherwise it will be still running and test will not finish
    }

    @Test
    public void shouldServerSupportMultipleSimultaneousClients() throws Exception {
        class SlowCommunication implements Communication {
            private final AtomicInteger responseCounter = new AtomicInteger(0);

            private final AtomicInteger count = new AtomicInteger(0);

            @Override
            public void close() throws Exception {
            }

            @Override
            public void sendResponse(Response resp) throws CommunicationException {
                if (resp instanceof LineResponse) {
                    responseCounter.incrementAndGet();
                }
            }

            @Override
            public Command receiveCommand() {
                int val = count.incrementAndGet();
                if (val > 2) {
                    return createSlowlyShutdownCommand();
                } else {
                    return createSlowlyGetCommand();
                }
            }

            public int getResponseCount() {
                return responseCounter.get();
            }

            private LineCommand createSlowlyGetCommand() {
                sleep(100);
                return new LineCommand(123L);
            }

            private ShutdownCommand createSlowlyShutdownCommand() {
                sleep(150);
                return ShutdownCommand.getInstance();
            }

            private void sleep(long delay) {
                log.info("Waiting...");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    log.info("InterruptedException when sleeping: ", e);
                }
                log.info("Waiting finished");
            }
        }

        int simultaneousClientsLimit = 3;
        SlowCommunication communication = new SlowCommunication();
        serverThread = startServer(createServer(simultaneousClientsLimit, communication));
        serverThread.join();

        assertEquals("Not all command were executed", 2, communication.getResponseCount());
    }

    private LinerServer createServer(int simultaneousClientsLimit, Communication communication)
            throws TextFileException, CommunicationException {
        CommunicationDetector detector = mock(CommunicationDetector.class);
        when(detector.acceptNextClient()).thenReturn(communication);

        TextFile textFile = mock(TextFile.class);
        when(textFile.isLineNrValid(Mockito.anyLong())).thenReturn(true);
        when(textFile.getLine(Mockito.anyLong())).thenReturn("line");

        return new LinerServer(simultaneousClientsLimit, detector, textFile);
    }
}
