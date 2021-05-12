package net.onest.timestoryprj.activity.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.book.BookDatailAdapter;
import net.onest.timestoryprj.entity.CertificateUserBookListVO;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private List<CertificateUserBookListVO> bookListVOS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Intent intent = getIntent();
        //接收图书ID

        initData();

        BookDatailAdapter bookDatailAdapter = new BookDatailAdapter(this,bookListVOS,R.layout.item_book);
        ListView bookList = findViewById(R.id.list_books);
        bookList.setAdapter(bookDatailAdapter);
    }

    private void initData() {
    }
}
