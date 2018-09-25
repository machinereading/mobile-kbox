package kr.ac.kaist.qamel.qamel_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyncInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_interface);

        // --

        setTitle("모바일 K-Box 동기화");

        // --

        Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText);

                String kbUrl = et.getText().toString();

                // --

                et = (EditText) findViewById(R.id.editText2);

                String labelUri = et.getText().toString();

                // --

                et = (EditText) findViewById(R.id.editText3);

                String keyword = et.getText().toString();

                // --

                ArrayList<ArrayList<String>> result = GlobalVariable.kbox.syncDb(kbUrl, labelUri, keyword);

                ((TextView) findViewById(R.id.textView4)).setText(String.valueOf(result.size()) + "개 트리플을 동기화 하였음\n(밝게 표시된 부분은 기존 DB에 없는 내용)");

                // --

                RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);

                rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

                rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));

                // --

                ArrayList<RecyclerItem> items = new ArrayList<>();

                RecyclerView.Adapter adapter = new RecyclerAdapter(items);

                rv.setAdapter(adapter);

                // --

                items.clear();

                // --

                for (ArrayList<String> values : result) {
                    String content = "";

                    for (int i=0; i<values.size(); i++) {
                        if (i == 0) {
                            content += "\n";
                        }

                        // --

                        String value = values.get(i);

                        // --

                        String[] tokens = value.split("[/#]");

                        String lastToken = tokens[tokens.length - 1];

                        // --

                        if (i == 0) {
                            content += "s:  " + lastToken;
                        }
                        else if (i == 1) {
                            content += "p:  " + lastToken;
                        }
                        else if (i == 2) {
                            content += "o:  " + lastToken;
                        }

                        // --

                        if (i < values.size() - 1) {
                            content += "\n";
                        }
                    }

                    if (values.get(3) == "not-contained") {
                        items.add(new RecyclerItem(content, true));
                    } else {
                        items.add(new RecyclerItem(content, false));
                    }
                }

                // --

                adapter.notifyDataSetChanged();
            }
        });
    }
}
