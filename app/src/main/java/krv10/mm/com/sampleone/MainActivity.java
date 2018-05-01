package krv10.mm.com.sampleone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;

    OkHttpClient okHttpClient;
    Request request;
    FormBody formBody;
    Gson gson;

    List<String> list;
    List<String> valuelist;
    List<String> valuelist2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);


        okHttpClient = new OkHttpClient();
        gson = new Gson();

        formBody = new FormBody.Builder()
                .add("date", "01/2018")
                .add("DeptID", "1")
                .add("financialyear", "1 April 2017 - 31 March 2018")
                .build();

        request = new Request.Builder()
                .post(formBody)
                .url("http://fsms.f-tec.net.in/app/ws_reports.asmx/CertifiedReport")
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    String res = response.body().string();
                    JSONArray jsonArray = new JSONArray(res);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Iterator<String> Keys = jsonObject.keys();

                        list = new ArrayList<>();
                        valuelist = new ArrayList<>();
                        valuelist2 = new ArrayList<>();

                        while (Keys.hasNext()) {

                            String key = Keys.next();

                            String value = jsonObject.getString(key);

                            list.add(key);

                            valuelist.add(value);


                            if (i == 0) {
                                valuelist.add(value);
                            } else {
                                valuelist2.add(value);
                            }

                        }

                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (list.isEmpty()) {
                                Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                            } else {

                                rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                rv.setAdapter(new Adapter());
                            }

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    class Adapter extends RecyclerView.Adapter<Adapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.inflater, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {

            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class VH extends RecyclerView.ViewHolder {

            TextView tv1, tv2, tv3;

            public VH(View itemView) {
                super(itemView);

                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);
                tv3 = itemView.findViewById(R.id.tv3);
            }

            public void bind(final int position) {
                tv1.setText(list.get(position));
                tv2.setText(valuelist.get(position));
                tv3.setText(valuelist2.get(position));
            }
        }
    }

}
