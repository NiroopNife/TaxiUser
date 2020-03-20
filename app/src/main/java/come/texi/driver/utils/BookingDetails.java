package come.texi.driver.utils;

import java.io.Serializable;

public class BookingDetails implements Serializable {

    String bookid;

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }
}
