// IBookManager.aidl
package com.wy.remote.remotemodel.book;
// Declare any non-default types here with import statements
import com.wy.remote.remotemodel.book.Book;
import com.wy.remote.remotemodel.book.IONNewBookArrivedListener;
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     List<Book> getBookList();
     void add( in Book book);
     void registerListener(IONNewBookArrivedListener listener);
     void unregisterListener(IONNewBookArrivedListener listener);
}
