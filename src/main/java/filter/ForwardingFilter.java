package filter;

import interfaces.Readable;
import interfaces.Writeable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;


public class ForwardingFilter<T> extends AbstractFilter <T,T> {

    public ForwardingFilter(Readable<T> input, Writeable<T> output) throws InvalidParameterException {
        super(input, output);

    }

    public ForwardingFilter(Readable<T> input) throws InvalidParameterException {
        super(input);

    }

    public ForwardingFilter(Writeable<T> output) throws InvalidParameterException {
        super(output);
    }

    public T read() throws StreamCorruptedException {
        return readInput();
    }

    public void write(T value) throws StreamCorruptedException {
        writeOutput(value);
    }
    


    public void run() {
        T input = null;
        try {
            do {
    
                input = readInput();
                if (input != null) {
                    writeOutput(input);
                }
                
            }while(input != null);
            sendEndSignal();
        } catch (StreamCorruptedException e) {
            // TODO Automatisch erstellter Catch-Block
            e.printStackTrace();
        }
    }
}
