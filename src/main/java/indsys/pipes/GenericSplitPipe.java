package indsys.pipes;

import thirdparty.interfaces.Writable;
import thirdparty.pipes.BufferedSyncPipe;

import java.io.StreamCorruptedException;
import java.util.LinkedList;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public class GenericSplitPipe<T> extends BufferedSyncPipe<T> {
    private final Writable<T> _output;

    public GenericSplitPipe(int maxBufferSize, Writable<T> output) {
        super(maxBufferSize);
        _output = output;
    }

    @Override
    public synchronized T read() throws StreamCorruptedException {
        T t = super.read();

        //forwarding to observer pipe.
        _output.write(t);

        //forwarding to real destination.
        return t;
    }
}
