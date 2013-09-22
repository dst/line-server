package com.stefanski.lineserver.util;

import static java.nio.file.StandardOpenOption.READ;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

/**
 * A wrapper for SeekableByteChannel for a more convenient use
 * 
 * @author Dariusz Stefanski
 * @date Sep 19, 2013
 */
public class SeekableByteChannelReader implements AutoCloseable {

    private final SeekableByteChannel channel;

    public static SeekableByteChannelReader fromFilePath(Path path) throws IOException {
        FileChannel channel = FileChannel.open(path, READ);
        return new SeekableByteChannelReader(channel);
    }

    public SeekableByteChannelReader(SeekableByteChannel channel) {
        this.channel = channel;
    }

    public ByteBuffer read(long startPos, int length) throws IOException {
        seek(startPos);
        ByteBuffer buf = ByteBuffer.allocate(length);
        fillBuffer(buf);
        return buf;
    }

    public void close() throws IOException {
        channel.close();
    }

    private void seek(long pos) throws IOException {
        channel.position(pos);
    }

    private void fillBuffer(ByteBuffer buf) throws IOException {
        int readCount;
        do {
            readCount = channel.read(buf);
        } while (readCount != -1 && buf.hasRemaining());

        if (buf.hasRemaining()) {
            throw new IOException("Not enough data in channel");
        }
    }
}
