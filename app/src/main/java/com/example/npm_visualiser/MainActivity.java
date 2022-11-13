package com.example.npm_visualiser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.npm_visualiser.utils.CustomRvAdapter;
import com.example.npm_visualiser.utils.ParseObject;
import com.example.npm_visualiser.utils.TabFragmentAdapter;
import com.example.npm_visualiser.utils.TabObject;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private int indicatorWidth;
    public int screenHeight, depFound;
    public TabObject tabObject;
    public String searchRequest;
    TabFragmentAdapter adapter2;

    @BindView(R.id.tvprogress) TextView tvprogress;
    @BindView(R.id.vp) ViewPager mViewPager;
    @BindView(R.id.nopck) RelativeLayout nopck;
    @BindView(R.id.pb) RelativeLayout pb;
    @BindView(R.id.editText) EditText et;
    @BindView(R.id.rl_root) RelativeLayout root;
    @BindView(R.id.tv_pck_name) TextView pck_name;
    @BindView(R.id.indicator) View mIndicator;
    @BindView(R.id.tab) TabLayout mTabs;
    @BindView(R.id.scrollView) NestedScrollView sview;
    @BindView(R.id.rl_top) RelativeLayout rl;
    @BindView(R.id.rl_back) RelativeLayout back;
    @BindView(R.id.button_search) Button btn;
    @BindView(R.id.tv_nodep) TextView nodep;
    @BindView(R.id.ll_dep) LinearLayout ll_dep;
    @BindView(R.id.rl_bottom) RelativeLayout rl_bottom;

    public void search ()
    {
        tabObject = null;
        searchRequest = et.getText().toString();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        et.clearFocus();
        URL url = generateURL(searchRequest);
        btn.setAlpha((float)0.50);
        btn.setOnClickListener(null);
        back.setAlpha((float)0.50);
        back.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        new NPIQueryTask(this).execute(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        measureScreen();
        setupTab();
        setupTextChanger();

    }

    private void measureScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
    }

    private void setupTextChanger() {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if (et.getText().length()==0 || et.getText().toString().equals(searchRequest))
                    {
                        btn.setAlpha((float)0.50);
                        btn.setOnClickListener(null);
                    }
                    else
                    {
                        btn.setAlpha(1);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                search();
                            }
                        });
                    }
            }
        });
    }

    public void setupTab() {
        adapter2 = new TabFragmentAdapter(getSupportFragmentManager());
        adapter2.addFragment(FragmentOne.newInstance(), "Таблица");
        adapter2.addFragment(FragmentTwo.newInstance(), "Дерево");
        mViewPager.setAdapter(adapter2);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();
                float translationOffset =  (positionOffset+i) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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
    class NPIQueryTask extends AsyncTask<URL, String, String>
    {
        private WeakReference<MainActivity> weakReference;
        NPIQueryTask(MainActivity activity){
            weakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            MainActivity activity = weakReference.get();
            if (activity == null || activity.isFinishing()){
                return;
            }
            activity.tvprogress.setText(values[0]);
        }

        @Override
        protected String doInBackground(URL... urls) {
            publishProgress("Поиск пакета...");
            String response = null;
            try {
                response = getResponse(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            depFound = 0;
            publishProgress("Поиск зависимостей...");
            tabObject = runRecResponse(response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response!=null) {
                String dependency = "";
                String name = tabObject.name;
                String version = tabObject.version;
                nopck.setVisibility(View.INVISIBLE);
                sview.setVisibility(View.VISIBLE);
                if (tabObject.getList().size()==0)
                {
                    ll_dep.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.GONE);
                    nodep.setVisibility(View.VISIBLE);
                }
                else
                {
                    nodep.setVisibility(View.GONE);
                    ll_dep.setVisibility(View.VISIBLE);
                    FragmentOne fr = (FragmentOne)adapter2.getItem(0);
                    setupScrollView(fr);
                    CustomRvAdapter adapter = new CustomRvAdapter(tabObject);
                    fr.setRvAdapter(adapter);
                    int height = fr.getFragHeight();
                    adjustVPheight(findVPHeight(height));
                    mViewPager.setVisibility(View.VISIBLE);
                }
                pck_name.setText("Имя пакета:\n"+name+"\nВерсия: \n"+version+"\n\nЗависимости:");
            }
            else
            {
                sview.setVisibility(View.INVISIBLE);
                nopck.setVisibility(View.VISIBLE);
            }
            pb.setVisibility(View.GONE);
            back.setAlpha(1);
            back.setVisibility(View.VISIBLE);
        }
        private TabObject runRecResponse(String response)
        {
            if (response!=null)
            {
                ParseObject obj = parseResponse(response);
                String name = (obj.getName());
                String version = (obj.getVersion());
                ArrayList<String> st = obj.getStrings();
                if (st!=null)
                {
                    ArrayList<TabObject> objectArrayList = new ArrayList<>();
                    for (int i = 0; i<obj.getStrings().size(); i++)
                    {
                        depFound++;
                        publishProgress("Поиск зависимостей...\n Найдено: "+depFound);
                        URL url = generateURL(st.get(i));
                        try {
                            response = getResponse(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        objectArrayList.add(runRecResponse(response));
                    }
                    return new TabObject(name,version,objectArrayList);
                }
                return new TabObject(name,version,null);
            }
            return null;
        }

    }

    private ParseObject parseResponse(String response) {
        String name = "";
        ArrayList<String> list = new ArrayList<>();
        String version = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
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
                    if (vsn.length()!=0) {
                        if (vsn.charAt(0) == '^' || vsn.charAt(0) == '~') {
                            vsn = jsonObject1.getString(jsonArray.get(i).toString()).substring(1);
                        }
                        version = vsn;
                        list.add(itstr.next());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ParseObject(name, version, list);
    }

    private void setupScrollView(FragmentOne fr) {

    }

    private int findVPHeight(int h)
    {
        int height = screenHeight - ll_dep.getHeight() - rl_bottom.getHeight();
        return height;
    }
    private void adjustVPheight(int h) {
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = h;
        mViewPager.requestLayout();
    }
}
