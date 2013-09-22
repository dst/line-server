package com.stefanski.lineserver.server;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.mockito.Mockito;

import com.stefanski.lineserver.file.TextFile;
import com.stefanski.lineserver.file.TextFileException;
import com.stefanski.lineserver.server.cmd.Command;
import com.stefanski.lineserver.server.cmd.GetCommand;
import com.stefanski.lineserver.server.cmd.ShutdownCommand;
import com.stefanski.lineserver.server.comm.Communication;
import com.stefanski.lineserver.server.comm.CommunicationDetector;
import com.stefanski.lineserver.server.comm.CommunicationException;
import com.stefanski.lineserver.server.resp.GetResponse;
import com.stefanski.lineserver.server.resp.Response;
import com.stefanski.lineserver.util.StdLogger;

/**
 * @author Dariusz Stefanski
 * @date Sep 4, 2013
 */
public class BasicLineServerTest extends LineServerTest {

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
                if (resp instanceof GetResponse) {
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

            private GetCommand createSlowlyGetCommand() {
                sleep(100);
                return new GetCommand(123L);
            }

            private ShutdownCommand createSlowlyShutdownCommand() {
                sleep(150);
                return ShutdownCommand.getInstance();
            }

            private void sleep(long delay) {
                StdLogger.info("Waiting...");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    StdLogger.info("InterruptedException when sleeping: " + e);
                }
                StdLogger.info("Waiting finished");
            }
        }

        int simultaneousClientsLimit = 3;
        SlowCommunication communication = new SlowCommunication();
        serverThread = startServer(createServer(simultaneousClientsLimit, communication));
        serverThread.join();

        assertEquals("Not all command were executed", 2, communication.getResponseCount());
    }

    private LineServer createServer(int simultaneousClientsLimit, Communication communication)
            throws TextFileException, CommunicationException {
        CommunicationDetector detector = mock(CommunicationDetector.class);
        when(detector.acceptNextClient()).thenReturn(communication);

        TextFile textFile = mock(TextFile.class);
        when(textFile.isLineNrValid(Mockito.anyLong())).thenReturn(true);
        when(textFile.getLine(Mockito.anyLong())).thenReturn("line");

        return new LineServer(simultaneousClientsLimit, detector, textFile);
    }
}
