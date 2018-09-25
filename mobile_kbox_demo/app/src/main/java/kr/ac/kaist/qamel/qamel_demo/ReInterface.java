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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ReInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_interface);

        // --

        setTitle("모바일 K-Box 증강");

        // --

        Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText);

                String textUrl = et.getText().toString();

                // --

                et = (EditText) findViewById(R.id.editText2);

                String date = et.getText().toString();

                // --

                et = (EditText) findViewById(R.id.editText3);

                Double cThreshold = Double.parseDouble(et.getText().toString());

                // --

                URL url = null;

                try {
                    url = new URL(textUrl);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                ArrayList<ArrayList<String>> result = GlobalVariable.kbox.populateDb(url, date, cThreshold);

                ((TextView) findViewById(R.id.textView4)).setText(String.valueOf(result.size()) + "개 트리플을 추출 하였음\n(밝게 표시된 부분은 기존 DB에 없는 내용)");

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
                    String s = values.get(0);
                    String p = values.get(1);
                    String o = values.get(2);
                    String confidence = values.get(3);
                    String provenance = values.get(4);

                    String[] s_tokens = s.split("[/#]");
                    String s_lastToken = s_tokens[s_tokens.length - 1];

                    String[] p_tokens = p.split("[/#]");
                    String p_lastToken = p_tokens[p_tokens.length - 1];

                    String[] o_tokens = o.split("[/#]");
                    String o_lastToken = o_tokens[o_tokens.length - 1];

                    String content = "\n";
                    content += "s: " + s_lastToken + "\n";
                    content += "p: " + p_lastToken + "\n";
                    content += "o: " + o_lastToken + "\n";
                    content += "\n";
                    content += "[confidence]:  " + confidence + "\n";
                    content += "[provenance]:  " + provenance + "\n";

                    if (values.get(5) == "not-contained") {
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
