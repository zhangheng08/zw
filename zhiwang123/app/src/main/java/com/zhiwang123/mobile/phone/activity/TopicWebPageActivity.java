package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.wxapi.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class TopicWebPageActivity extends BaseActivity {

    private static final String TAG = "TopicWebPageActivity";

    public static final String KEY_URL = "url_key";
    public static final String KEY_NAME = "title_name";
    public static final String KEY_CMS_ID = "cms_id";

    private Context mContext;

    private WebView mWebView;

    private View mGoback;
    private TextView mTitle_txv;
    private TextView mSubBtn_txv;
    private ImageView mShareBtn;

    private View shareLayer;
    private View shareBlank;
    private View shareActionWxcs;
    private View shareActionWxtl;
    private View shareActionQQ;
    private View shareActionSina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_page);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_custom_layout);
        initLayout();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            String url = bundle.getString(KEY_URL);
            String name = bundle.getString(KEY_NAME);
            String cmsId = bundle.getString(KEY_CMS_ID);

            mTitle_txv.setText(name);

            if(!TextUtils.isEmpty(url)) {

                loadUrl(url);

            } else if(!TextUtils.isEmpty(cmsId)) {

                mShareBtn.setVisibility(View.VISIBLE);
                loadHtmlInfo(cmsId);

            }


        }

    }

    protected void initLayout() {

        View view = mActionBar.getCustomView();

        mTitle_txv = (TextView) view.findViewById(R.id.title);
        mSubBtn_txv = (TextView) view.findViewById(R.id.sub_btn);
        mShareBtn = (ImageView) view.findViewById(R.id.sub_btn_share);
        mSubBtn_txv.setVisibility(View.GONE);

        mGoback = view.findViewById(R.id.go_back_btn);
        mWebView = (WebView) findViewById(R.id.web_view);

        shareLayer = findViewById(R.id.share_layer);
        shareBlank = findViewById(R.id.share_blank);
        shareActionWxcs = findViewById(R.id.share_action_wxcs);
        shareActionWxtl = findViewById(R.id.share_action_wxtl);
        shareActionQQ = findViewById(R.id.share_action_qq);
        shareActionSina = findViewById(R.id.share_action_sina);

        mShareBtn.setOnClickListener(mShareClick);
        shareBlank.setOnClickListener(mShareClick);
        shareActionWxcs.setOnClickListener(mShareClick);
        shareActionWxtl.setOnClickListener(mShareClick);
        shareActionQQ.setOnClickListener(mShareClick);
        shareActionSina.setOnClickListener(mShareClick);

        mGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void loadHtmlInfo(String cmsId) {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCmsDeitail(cmsId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            JSONObject job = response.getJSONObject("DataObject");

                            mTitle_txv.setText(job.getString("Title"));
                            String content = job.getString("Content");
                            fillHtmlInfos(content);

                        } catch(JSONException ex) {

                            ex.printStackTrace();

                        }



                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, error.getMessage() + "");

                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    public void fillHtmlInfos(String htmlStr) {

        mWebView.loadDataWithBaseURL("about:blank", htmlStr, "text/html", "utf-8", null);

    }

    public void loadUrl(String url) {

        mWebView.loadUrl(url);

        WebSettings webSettings = mWebView.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);

    }

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.sub_btn_share:

                    shareLayer.setVisibility(shareLayer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    break;
                case R.id.share_action_wxcs:

                    sharewx(SendMessageToWX.Req.WXSceneSession);

                    break;
                case R.id.share_action_wxtl:

                    sharewx(SendMessageToWX.Req.WXSceneTimeline);

                    break;
                case R.id.share_action_qq:

                    shareqq();

                    break;
                case R.id.share_action_sina:

                    sharesina();

                    break;
                case R.id.share_blank:

                    shareLayer.setVisibility(View.GONE);

                    break;
            }

        }
    };


    //-----share-------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode,resultCode,data, null);
        }
    }

    public void shareqq()
    {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "智网新闻");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.zhiwang123.com/");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mTitle_txv.getText().toString());
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                Tencent.createInstance("101361700", mContext).shareToQQ(TopicWebPageActivity.this, params, qqShareListener);
            }
        });
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {

        }
        @Override
        public void onComplete(Object response) {

        }


        @Override
        public void onError(UiError e) {

        }
    };

    public void sharewx(int scene) {

        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = "http://www.zhiwang123.com/";
        WXMediaMessage mediaMessage = new WXMediaMessage(wxWebpageObject);
        mediaMessage.title = "智网新闻";
        mediaMessage.description = mTitle_txv.getText().toString();
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.app_share_icon);
        mediaMessage.thumbData = Util.bmpToByteArray(bm, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webPage");
        req.message = mediaMessage;
        req.scene = scene;

        WXAPIFactory.createWXAPI(this, "wx5dbdd6555a391027").sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void sharesina() {

        UMWeb web = new UMWeb("http://www.zhiwang123.com/");
        web.setTitle("智网新闻");//标题
        web.setThumb(new UMImage(mContext, R.drawable.app_share_icon));  //缩略图
        web.setDescription("my description");

        new ShareAction(TopicWebPageActivity.this)
                .withMedia(web)
                .setPlatform(SHARE_MEDIA.SINA)
                .share();

    }


}
