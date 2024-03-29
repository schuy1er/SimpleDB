package simpledb;

import java.io.IOException;

/**
 * The delete operator. Delete reads tuples from its child operator and removes
 * them from the table they belong to.
 */
public class Delete extends Operator {

    private static final long serialVersionUID = 1L;

    private TransactionId t;

    private OpIterator child;

    private int called;

    /**
     * Constructor specifying the transaction that this delete belongs to as
     * well as the child to read from.
     *
     * @param t
     *            The transaction this delete runs in
     * @param child
     *            The child operator from which to read tuples for deletion
     */
    public Delete(TransactionId t, OpIterator child) {
        // some code goes here
        this.t = t;
        this.child = child;
        called = 0;
    }

    public TupleDesc getTupleDesc() {
        // some code goes here
        Type[] typeAr = new Type[1];
        String[] fieldAr = new String[1];
        typeAr[0] = Type.INT_TYPE;
        fieldAr[0] = "aaa";
        TupleDesc td = new TupleDesc(typeAr, fieldAr);
        return td;
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
        super.open();
        child.open();
    }

    public void close() {
        // some code goes here
        super.close();
        child.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
        child.rewind();
    }

    /**
     * Deletes tuples as they are read from the child operator. Deletes are
     * processed via the buffer pool (which can be accessed via the
     * Database.getBufferPool() method.
     *
     * @return A 1-field tuple containing the number of deleted records.
     * @see Database#getBufferPool
     * @see BufferPool#deleteTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // some code goes here
        int num = 0;
        if(called == 1){
            return null;
        }
        called = 1;
        while(child.hasNext()){
            num++;
            try {
                Database.getBufferPool().deleteTuple(t, child.next());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Type[] typeAr = new Type[1];
        String[] fieldAr = new String[1];
        typeAr[0] = Type.INT_TYPE;
        fieldAr[0] = "";
        TupleDesc td = new TupleDesc(typeAr,fieldAr);
        Tuple tp = new Tuple(td);
        tp.setField(0, new IntField(num));
        return tp;
    }

    @Override
    public OpIterator[] getChildren() {
        // some code goes here
        OpIterator[] children = new OpIterator[1];
        children[0] = child;
        return children;
    }

    @Override
    public void setChildren(OpIterator[] children) {
        // some code goes here
        children[0] = child;
    }

}
