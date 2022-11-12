package com.example.npm_visualiser;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText) EditText et;
    @BindView(R.id.tv_pck_name) TextView pck_name;
    @BindView(R.id.tv_pck_text) TextView pck_text;
    @OnClick (R.id.button_search) void search ()
    {
        URL url = generateURL(et.getText().toString());
        new NPIQueryTask().execute(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
    public URL generateURL(String pName)
    {
        Uri uri = Uri.parse("https://registry.npmjs.org/"+pName);
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }
    public String getResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {
            InputStream in = urlConnection.getInputStream();
            Scanner scn = new Scanner(in);
            scn.useDelimiter("\\A");
            boolean hasInput = scn.hasNext();
            if (hasInput)
            {
                return scn.next();
            }
            else
            {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    class NPIQueryTask extends AsyncTask<URL, Void, String>
    {
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponse(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String dependency = "";
            String name = null;
            String version = null;
            try {
                if (response!=null) {
                    JSONObject jsonObject = new JSONObject(response);
                    name = jsonObject.getString("name");
                    Iterator<String> ver = jsonObject.getJSONObject("versions").keys();
                    while (true) {
                        version = ver.next();
                        if (!ver.hasNext()) {
                            break;
                        }
                    }
                    JSONObject jsonObject1 = jsonObject.getJSONObject("versions").getJSONObject(version).getJSONObject("dependencies");
                    JSONArray jsonArray = jsonObject1.names();
                    Iterator<String> itstr = jsonObject1.keys();
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String vsn = jsonObject1.getString(jsonArray.get(i).toString());
                            if (vsn.charAt(0) == '^' || vsn.charAt(0) == '~')
                            {
                                vsn = jsonObject1.getString(jsonArray.get(i).toString()).substring(1);
                            }
                            dependency += (itstr.next() + "@" + vsn + "\n");
                        }
                    }

                    pck_name.setVisibility(View.VISIBLE);
                }
                else
                {
                    pck_name.setVisibility(View.GONE);
                    dependency = "Пакет не найден :<";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pck_text.setText(dependency);
            pck_name.setText("Имя пакета:\n"+name+"\nВерсия: \n"+version+"\n\nЗависимости:");
        }
    }
}