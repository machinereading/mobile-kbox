package kr.ac.kaist.qamel.mobile_kbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kbox {
    private Repository db;

    private Context context;

    // --

    public Kbox(Context mainContext) {
        db = new SailRepository(new MemoryStore());

        db.initialize();

        // --

        this.context = mainContext;

        // --

        loadDb();
    }

    // --

    public void loadDb() {
        try {
            InputStream is = context.openFileInput("kbox.ttl");

            try (RepositoryConnection conn = db.getConnection()) {
                try {
                    conn.add(is, "", RDFFormat.TURTLE);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                // --

                try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                    while (result.hasNext()) {
                        Statement st = result.next();
                        System.out.println("DB loaded: " + st);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            try (RepositoryConnection conn = db.getConnection()) {
                try (InputStream is = context.getResources().getAssets().open("olympic-triples.ttl")) {
                    conn.add(is, "", RDFFormat.TURTLE);
                }

                catch (IOException e2) {
                    e2.printStackTrace();
                }

                // --

                try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                    while (result.hasNext()) {
                        Statement st = result.next();
                        System.out.println("DB loaded: " + st);
                    }
                }
            }

        }
    }

    public boolean saveDb() {
        try (RepositoryConnection conn = db.getConnection()) {
            try {
                FileOutputStream os = context.openFileOutput("kbox.ttl", Context.MODE_PRIVATE);

                try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                    while (result.hasNext()) {
                        Statement st = result.next();

                        Rio.write(st, os, RDFFormat.TURTLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    return false;
                }

                os.close();
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            }
        }

        catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    // --

    public ArrayList<ArrayList<String>> syncDb(String kbUrl, String labelUri, String keyword, int depth) {
        ArrayList<ArrayList<String>> arrayListResult = new ArrayList<ArrayList<String>>();

        // --

        if (kbUrl == null) {
            kbUrl = "http://ko.dbpedia.org/sparql";
        }

        if (labelUri == null) {
            labelUri = "http://www.w3.org/2000/01/rdf-schema#label";
        }

        // --

        String q1 = "SELECT DISTINCT ";
        String q2 = "";
        String q3 = "WHERE { ";
        String q4 = "";
        String q5 = "?e1 <" + labelUri + "> ?n . FILTER(CONTAINS(LCASE(STR(?n)), \"" + keyword + "\")) . }";

        // --

        for (int i = 1; i <= depth; i++) {
            q2 = "?e" + Integer.toString(i) + " ?p" + Integer.toString(i) + " ?e" + Integer.toString(i+1) + " ";
            q4 = q2 + ". " + q4;

            String queryString = q1 + q2 + q3 + q4 + q5;

            // --

            String params = "";

            try {
                params = params + "query=" + URLEncoder.encode(queryString, "UTF-8").toString();
            } catch (UnsupportedEncodingException e) {
            }

            // params = params + "&format=text%2Ftab-separated-values";
            params = params + "&format=text%2Fturtle";
            params = params + "&default-graph-uri=";
            params = params + "&timeout=0";
            params = params + "&debug=on";

            // --

            String response = HttpRequest.get(kbUrl + "?" + params);

            // --

            InputStream is = new ByteArrayInputStream(response.getBytes());

            try {
                Model model = Rio.parse(is, "", RDFFormat.TURTLE);

                // --

                Repository dump = new SailRepository(new MemoryStore());

                dump.initialize();

                // --

                try (RepositoryConnection dbConn = db.getConnection()) {
                    try (RepositoryConnection dumpConn = dump.getConnection()) {
                        dumpConn.add(model);

                        // --

                        String queryString2 = "SELECT ?s ?p ?o WHERE { ?a <http://www.w3.org/2005/sparql-results#variable> \"e" + Integer.toString(i) + "\" . ?a <http://www.w3.org/2005/sparql-results#value> ?s . ?b  <http://www.w3.org/2005/sparql-results#variable> \"p" + Integer.toString(i) + "\" . ?b <http://www.w3.org/2005/sparql-results#value> ?p . ?c  <http://www.w3.org/2005/sparql-results#variable> \"e" + Integer.toString(i+1) + "\" . ?c <http://www.w3.org/2005/sparql-results#value> ?o . ?d <http://www.w3.org/2005/sparql-results#binding> ?a . ?d <http://www.w3.org/2005/sparql-results#binding> ?b . ?d <http://www.w3.org/2005/sparql-results#binding> ?c . }";

                        TupleQuery query = dumpConn.prepareTupleQuery(queryString2);

                        try (TupleQueryResult result = query.evaluate()) {
                            while (result.hasNext()) {
                                BindingSet solution = result.next();

                                // --

                                String s = solution.getValue("s").toString();
                                String p = solution.getValue("p").toString();
                                String o = solution.getValue("o").toString();

                                // --

                                ArrayList<String> values = new ArrayList<String>();

                                values.add(s);
                                values.add(p);
                                values.add(o);

                                if (dbConn.hasStatement((Resource) solution.getValue("s"), (IRI) solution.getValue("p"), solution.getValue("o"), false)) {
                                    values.add("contained"); // already contained in K-Box
                                } else {
                                    values.add("not-contained"); // not yet contained in K-Box
                                }

                                arrayListResult.add(values);

                                // --

                                System.out.println("DB synced: " + s + " " + p + " " + o);
                                System.out.println("query: " + queryString);
                                System.out.println(Integer.toString(i) + " " + Integer.toString(depth));

                                // --

                                dbConn.add((Resource) solution.getValue("s"), (IRI) solution.getValue("p"), solution.getValue("o"));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arrayListResult;
    }

    public ArrayList<ArrayList<String>> populateDb(URL url, String date, Double cThreshold) {
        String text = HttpRequest.get(url);

        // --

        return populateDb(text, date, cThreshold);
    }

    public ArrayList<ArrayList<String>> populateDb(String text, String date, Double cThreshold) {
        ArrayList<ArrayList<String>> arrayListResult = new ArrayList<ArrayList<String>>();

        // --

        if (date == null) {
            date = "0000-00-00";
        }

        // --

        String sInput = "{\"date\": \"" + date + "\", \"content\": \"" + text + "\"}";

        String sOutput = HttpRequest.post("http://qamel.kaist.ac.kr:60003/service", sInput);

        JSONArray jOutput = null;

        try {
            jOutput = new JSONArray(sOutput);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // --

        try (RepositoryConnection conn = db.getConnection()) {
            for (int i = 0; i < jOutput.length(); i++) {
                try {
                    JSONArray iValues = (JSONArray) jOutput.get(i);

                    String s = iValues.get(0).toString();
                    String p = iValues.get(1).toString();
                    String o = iValues.get(2).toString();
                    String c = iValues.get(3).toString();
                    String provenance = iValues.get(4).toString();

                    // --

                    ArrayList<String> oValues = new ArrayList<String>();

                    oValues.add(s);
                    oValues.add(p);
                    oValues.add(o);
                    oValues.add(c);
                    oValues.add(provenance);

                    // --

                    if (cThreshold == null) {
                        cThreshold = 0.8;

                    }

                    if (Double.parseDouble(c) < cThreshold) {
                        continue;
                    }

                    // --

                    String oType = getLiteralType(o);

                    Value oValue = null;

                    // --

                    ValueFactory vf = SimpleValueFactory.getInstance();

                    if (oType == "float") {
                        oValue = vf.createLiteral(o, vf.createIRI("http://www.w3.org/2001/XMLSchema#float"));
                    }
                    else if (oType == "integer") {
                        oValue = vf.createLiteral(o, vf.createIRI("http://www.w3.org/2001/XMLSchema#integer"));
                    }
                    else if (oType == "date") {
                        oValue = vf.createLiteral(o, vf.createIRI("http://www.w3.org/2001/XMLSchema#date"));
                    }
                    else if (oType == "time") {
                        oValue = vf.createLiteral(o);
                    }
                    else if (oType == "resource") {
                        oValue = vf.createIRI(o);
                    }

                    // --

                    if (conn.hasStatement((Resource) vf.createIRI(s), (IRI) vf.createIRI(p), oValue, false)) {
                        oValues.add("contained"); // already contained in K-Box
                    } else {
                        oValues.add("not-contained"); // not yet contained in K-Box
                    }

                    // --

                    arrayListResult.add(oValues);

                    // --

                    // --

                    System.out.println("DB populated: " + s + " " + p + " " + o);

                    // --

                    conn.add((Resource) vf.createIRI(s), (IRI) vf.createIRI(p), oValue);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // --

        return arrayListResult;
    }

    // --

    public ArrayList<ArrayList<String>> selectQuery(String queryString) {
        try (RepositoryConnection conn = db.getConnection()) {
            TupleQuery query = conn.prepareTupleQuery(queryString);

            try (TupleQueryResult result = query.evaluate()) {
                ArrayList<ArrayList<String>> arrayListResult = new ArrayList<ArrayList<String>>();

                // --

                ArrayList<String> names = new ArrayList<String>();

                for (String name : result.getBindingNames()) {
                    names.add(name);
                }

                arrayListResult.add(names);

                // --

                while (result.hasNext()) {
                    BindingSet solution = result.next();

                    // --

                    ArrayList<String> values = new ArrayList<String>();

                    for (String name : names) {
                        String value = solution.getValue(name).toString();

                        values.add(value);
                    }

                    arrayListResult.add(values);
                }

                return arrayListResult;
            }
        }
    }

    // --

    public int size() {
        int size = 0;

        try (RepositoryConnection conn = db.getConnection()) {
            RepositoryResult<Statement> result = conn.getStatements(null, null, null);

            while (result.hasNext()) {
                size += 1;

                result.next();
            }

        }

        return size;
    }

    // --

    public String getLiteralType(String value) {
        Pattern p = Pattern.compile("^[0-9]+[.][0-9]+$");

        Matcher m = p.matcher(value);

        if (m.find()) {
            return "float";
        }

        // --

        p = Pattern.compile("^[0-9]+$");

        m = p.matcher(value);

        if (m.find()) {
            return "integer";
        }

        // --

        p = Pattern.compile("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$");

        m = p.matcher(value);

        if (m.find()) {
            return "date";
        }

        // --

        p = Pattern.compile("^(?:[0-9]{1,2}:)?(?:[0-9]{1,2}:)?[0-9]{1,2}(?:[.][0-9]+)?$");

        m = p.matcher(value);

        if (m.find()) {
            return "time";
        }

        return "resource";
    }
}

class HttpRequest {
    static public String get(String urlString) {
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String response = "";

                try {
                    String urlString = params[0];

                    URL url = new URL(urlString);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setDoOutput(false); // true 일 경우 request body 를 처리할 수 있는 웹 페이지로만 전송 가능
                    conn.setDoInput(true); // true 일 경우만 response body 를 읽을 수 있음
                    conn.setUseCaches(false);
                    conn.setDefaultUseCaches(false);

                    InputStream is = conn.getInputStream();

                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }

                    response = builder.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return response;
            }
        };

        // --

        String response = "";

        try {
            response = asyncTask.execute(urlString).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    // --

    static public String get(URL url) {
        AsyncTask<URL, Void, String> asyncTask = new AsyncTask<URL, Void, String>() {

            @Override
            protected String doInBackground(URL... params) {
                String response = "";

                try {
                    URL url =params[0];

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setDoOutput(false); // true 일 경우 request body 를 처리할 수 있는 웹 페이지로만 전송 가능
                    conn.setDoInput(true); // true 일 경우만 response body 를 읽을 수 있음
                    conn.setUseCaches(false);
                    conn.setDefaultUseCaches(false);

                    InputStream is = conn.getInputStream();

                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }

                    response = builder.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return response;
            }
        };

        // --

        String response = "";

        try {
            response = asyncTask.execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    // --

    static public String post(String urlString, final String data) {
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String response = "";

                try {
                    String urlString = params[0];

                    URL url = new URL(urlString);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setDefaultUseCaches(false);
                    conn.setRequestProperty("Content-Type", "application/json");

                    // --

                    String data = params[1];

                    OutputStream os = conn.getOutputStream();
                    os.write(data.getBytes());
                    os.flush();
                    os.close();

                    // --

                    InputStream is = conn.getInputStream();

                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }

                    response = builder.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return response;
            }
        };

        // --

        String response = "";

        try {
            response = asyncTask.execute(urlString, data).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}