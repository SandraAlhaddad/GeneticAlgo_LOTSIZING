package io;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

/**
 * An iterator through all the files contained in a directory. Can recurse into
 * subdirectories.
 * 
 * @author 
 * 
 */
public class DirectoryWalker implements Iterator<File>, Iterable<File> {

    private final Stack<Iterator<File>> stack;
    private final boolean recursive;
    private File last;

    /**
     * 
     */
    public DirectoryWalker(File root, boolean recursive) {
        this.recursive = recursive;
        this.stack = new Stack<Iterator<File>>();
        pushIteratorForDirectory(root);
    }

    /**
     * @param root
     */
    private void pushIteratorForDirectory(File root) {
        this.stack.push(Arrays.asList(root.listFiles()).iterator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (stack.isEmpty()) {
            return false;
        }
        if (!stack.peek().hasNext()) {
            stack.pop();
            return hasNext();
        } else {
            last = stack.peek().next();
            if(last.isDirectory()){
                if(recursive){
                    pushIteratorForDirectory(last);
                }
                last = null;
                return hasNext();
            }
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    @Override
    public File next() {
        if(last == null){
            throw new IllegalStateException("Eh");
        }
        File toReturn = last;
        last = null;
        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<File> iterator() {
        return this;
    }

}
