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

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

public class QueryInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_interface);

        // --

        setTitle("모바일 K-Box 검색");

        // --

        Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText);

                et.setText("SELECT ?s ?p ?o WHERE { ?s ?p ?o . }");
            }
        });

        // --

        b = (Button) findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText);

                et.setText("SELECT DISTINCT ?s WHERE { ?s ?p ?o . }");
            }
        });

        // --

        b = (Button) findViewById(R.id.button3);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText);

                et.setText("SELECT DISTINCT ?p WHERE { ?s ?p ?o . }");
            }
        });

        // --

        b = (Button) findViewById(R.id.button4);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText);

                et.setText("PREFIX kbox-p: <http://kbox.kaist.ac.kr/property/> \nPREFIX dbo: <http://dbpedia.org/ontology/> \nSELECT DISTINCT ?event ?agent ?rank \nWHERE { ?dummy kbox-p:event ?event . \n?dummy kbox-p:agent ?agent . \n?dummy dbo:rank ?rank . }");
            }
        });

        // --

        b = (Button) findViewById(R.id.button5);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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

                EditText et = (EditText) findViewById(R.id.editText);

                // --

                String queryString = et.getText().toString();

                ArrayList<ArrayList<String>> result = GlobalVariable.kbox.selectQuery(queryString);

                if (result.size() <= 0) {
                    ((TextView) findViewById(R.id.textView2)).setText(String.valueOf(result.size()) + "개의 검색 결과");

                } else {
                    ArrayList<String> names = result.remove(0);

                    ((TextView) findViewById(R.id.textView2)).setText(String.valueOf(result.size()) + "개의 검색 결과");

                    // --

                    for (ArrayList<String> values : result) {
                        String content = "";

                        for (int i=0; i<values.size(); i++) {
                            if (i == 0) {
                                content += "\n";
                            }

                            // --

                            String name = names.get(i);

                            String value = values.get(i);

                            // --

                            String[] tokens = value.split("[/#]");

                            String lastToken = tokens[tokens.length - 1];

                            // --

                            content += name + ":  " + lastToken;

                            // --

                            if (i < values.size()) {
                                content += "\n";
                            }
                        }

                        items.add(new RecyclerItem(content, false));
                    }

                    // --

                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
