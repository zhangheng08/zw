package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ouchn.lib.adapter.AnswerCardAdapter;
import com.ouchn.lib.adapter.QuestionPagerAdapter;
import com.ouchn.lib.adapter.holder.QuestionViewItemHolder;
import com.ouchn.lib.entity.Question;
import com.ouchn.lib.entity.QuestionOption;
import com.ouchn.lib.entity.QuestionType;
import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.AsyncExecutor;
import com.ouchn.lib.handler.BaseTask;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Block;
import com.zhiwang123.mobile.phone.bean.ExamPaper;
import com.zhiwang123.mobile.phone.bean.ExamPublic;
import com.zhiwang123.mobile.phone.bean.QuestionZW;
import com.zhiwang123.mobile.phone.bean.ResultItem;
import com.zhiwang123.mobile.phone.bean.parser.ExamParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Created by ty on 2016/11/2.
 */

public class ExamActivity extends BaseActivity implements AsyncExecuteCallback {

    private static final String TAG = ExamActivity.class.getName();

    private TextView mTitleTimer;
    private View mExamGoback;

    private ViewPager mPager;
    private QuestionPagerAdapter mExamAdapter;
    private AnswerCardAdapter mAnswerCardAdpater;

    private View mAnswerCardBtn;
    private View mHandOutBtn;
    private View mCheckAnswerBtn;
    private GridView mGridView;
    private View mAnswerCardLayer;
    private TextView mCurrentNumTv;
    private TextView mTotalNumTv;
    private View mCloseAnswerCardBtn;
    private TextView mAnswerCardTitleTv;

    private ExamPublic mExamPublic;
    private String mExamPaperId;
    private Context mContext;

    private ArrayList<View> mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exam_pager_layout);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_exam);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            mExamPaperId = bundle.getString(StaticConfigs.KEY_EXAM_PAPER);
        }

        initLayout();

        if(StaticConfigs.mLoginResult != null) {

            switch(StaticConfigs.mLoginResult.loginMode) {

                case StaticConfigs.LOGIN_MODE_PB:

                    loadExamPaper();

                    break;
                case StaticConfigs.LOGIN_MODE_E:

                    loadExamPaperE();

                    break;
            }

        }

    }

    @Override
    protected void initLayout() {

        View actv = mActionBar.getCustomView().findViewById(R.id.course_list_action_layout_root);
        mTitleTimer = (TextView) actv.findViewById(R.id.exam_title_timer);
        mExamGoback = actv.findViewById(R.id.exam_go_back);

        mPager = (ViewPager) findViewById(R.id.exam_question_paper);
        mAnswerCardBtn = findViewById(R.id.exam_answer_card_btn);
        mHandOutBtn = findViewById(R.id.exam_hand_out_btn);
        mCheckAnswerBtn = findViewById(R.id.exam_check_answer_btn);
        mAnswerCardLayer = findViewById(R.id.exam_answer_card_layer);
        mGridView = (GridView) findViewById(R.id.card_option_grid);
        mTotalNumTv = (TextView) findViewById(R.id.exam_total_page);
        mCurrentNumTv = (TextView) findViewById(R.id.exam_current_page);
        mCloseAnswerCardBtn = findViewById(R.id.card_go_back);
        mAnswerCardTitleTv = (TextView) findViewById(R.id.card_depot_name);

        mExamGoback.setOnClickListener(onClickListener);
        mAnswerCardBtn.setOnClickListener(onClickListener);
        mCloseAnswerCardBtn.setOnClickListener(onClickListener);
    }

    private void loadExamPaper() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlExamPublic(StaticConfigs.mLoginResult.accessToken, mExamPaperId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        processData(response);

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage() + "");
                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private void loadExamPaperE() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlExamE(StaticConfigs.mLoginResult.accessToken, mExamPaperId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        processData(response);

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage() + "");
                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private void processData(JSONObject response) {

        mExamPublic = new ExamParser<ExamPublic>(new ExamPublic()).parse(response);
        ExamPaper examPaper = mExamPublic.dataobject == null || mExamPublic.dataobject.size() == 0 ? null : mExamPublic.dataobject.get(0);
        mAnswerCardTitleTv.setText(examPaper.title);
        if(examPaper == null) return;
        BaseTask task = new BaseTask(0x1, examPaper, ExamActivity.this);
        AsyncExecutor.getInstance().execMuliteTask(task);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {

        if(mAnswerCardLayer.getVisibility() == View.VISIBLE) {
            showOrHiddenCard();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void pre(Object... objs) {

    }

    @Override
    public BaseTask.TaskResult async(int what, BaseTask baseTask) {

        BaseTask.TaskResult result = null;

        switch (what) {

            case 0x1:

                ExamPaper examPaper =  (ExamPaper) baseTask.param;

                ArrayList<Question> questions = new ArrayList<>();

                for(Block b : examPaper.blocks) {

                    int type = QuestionType.getQuestionTypeInt("（" + b.title+ "）", mContext);

                    for(QuestionZW qzw : b.questions) {

                        Question question = new Question();
                        question.setDepotId(examPaper.id);
                        question.setQuestionType(type);
                        question.setContent(qzw.title);
                        question.setId(qzw.id);
                        question.setqScore(qzw.score);
                        question.setqTypeString(b.title);
                        question.setqScoreToString(qzw.score + "");
                        question.setqSerialNumber(qzw.order);

                        if(type != QuestionType.JUDGMENT) {

                            QuestionOption[] questionOptions = new QuestionOption[qzw.resultItems.size()];
                            int i = 0;
                            for(ResultItem itm :  qzw.resultItems) {

                                QuestionOption questionOption = new QuestionOption();
                                questionOption.setQuestionId(qzw.id);
                                questionOption.setOrderNum(itm.order);
                                questionOption.setContent(itm.title);
                                questionOption.setOrderString(itm.number);
                                questionOptions[i ++] = questionOption;

                            }
                            question.setOptions(questionOptions);

                        } else {

                            QuestionOption questionOption = new QuestionOption();
                            questionOption.setQuestionId(qzw.id);
                            questionOption.setOrderNum(0);
                            questionOption.setContent(getString(R.string.exam_right));
                            questionOption.setOrderString("A");
                            QuestionOption questionOption2 = new QuestionOption();
                            questionOption2.setQuestionId(qzw.id);
                            questionOption2.setOrderNum(1);
                            questionOption2.setContent(getString(R.string.exam_wrong));
                            questionOption2.setOrderString("B");
                            QuestionOption[] questionOptions = new QuestionOption[] {questionOption, questionOption2};
                            question.setOptions(questionOptions);

                        }
                        questions.add(question);
                    }

                }

                initReuseViews(false);

                result = baseTask.new TaskResult();
                result.setResult(questions);

                return result;

            case 0x2:

                HashMap<String, String> postParam = new HashMap<String, String>();
                Question question = (Question) baseTask.param;

                postParam.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
                postParam.put("TestPaperId", question.getDeoptId());
                postParam.put("QuestionId", question.getId());

                String answer = "";

                if(question.getQuestionType() == QuestionType.SINGLE || question.getQuestionType() == QuestionType.MULTIPLE || question.getQuestionType() == QuestionType.JUDGMENT) {

                    HashSet<String> answers = question.mAnswerState.indexAnswers;
                    Iterator<String> i = answers.iterator();

                    if(question.getQuestionType() == QuestionType.SINGLE) {

                        while(i.hasNext()) {

                            answer = i.next();

                        }

                    } else if(question.getQuestionType() == QuestionType.MULTIPLE) {

                        StringBuffer tempStrb = new StringBuffer("");

                        while(i.hasNext()) {

                            if(tempStrb.length() != 0) tempStrb.append(",");
                            tempStrb.append(i.next());

                        }

                        answer = tempStrb.toString();

                    } else if(question.getQuestionType() == QuestionType.JUDGMENT) {

                        while(i.hasNext()) {

                            String value = i.next();

                            if("A".equalsIgnoreCase(value)) {
                                answer = getString(R.string.exam_right);
                            } else if("B".equalsIgnoreCase(value)) {
                                answer = getString(R.string.exam_wrong);
                            }

                        }
                    }

                    postParam.put("Answer", answer);

                } else {
                    // TODO: 2016/12/2
                    postParam.put("Answer", "");
                }

                result = baseTask.new TaskResult();
                result.setResult(postParam);

                return result;
        }

        return null;
    }

    @Override
    public void post(Message msg) {

        BaseTask baseTask = (BaseTask) msg.obj;

        switch(msg.what) {
            case 0x1:
                ArrayList<Question> questions =  (ArrayList<Question>) baseTask.result.getResult();

                mTotalNumTv.setText(questions.size() + "");

                mExamAdapter = new QuestionPagerAdapter(ExamActivity.this, questions, mViews);
                mPager.setAdapter(mExamAdapter);
                mPager.setPageMargin(20);
                mPager.addOnPageChangeListener(onPageChangeListener);
                mExamAdapter.notifyDataSetChanged();

                mAnswerCardAdpater = new AnswerCardAdapter(this, questions);
                mGridView.setAdapter(mAnswerCardAdpater);
                mAnswerCardAdpater.notifyDataSetChanged();
                mGridView.setOnItemClickListener(mOnGridItemClick);

                break;
            case 0x2:

                HashMap<String, String> kvs = (HashMap<String, String>) baseTask.result.getResult();

                toCommitAnswers(kvs);

                break;
        }

    }

    private void initReuseViews(boolean forWrongQuestion) {
        ArrayList<View> vList = new ArrayList<View>();
        for(int i = 0; i < QuestionPagerAdapter.MAX_REUSE_NUM; i ++) {
            View view = LayoutInflater.from(mContext).inflate(com.ouchn.lib.R.layout.question_item, null);
            QuestionViewItemHolder holder =
                    forWrongQuestion?
                            new QuestionViewItemHolder(view.findViewById(com.ouchn.lib.R.id.question_item_root), true) :
                            new QuestionViewItemHolder(view.findViewById(com.ouchn.lib.R.id.question_item_root));
            holder.setOnQuestionAnsweredListener(questionAnsweredListener);
            view.setTag(holder);
            vList.add(view);
        }
        mViews = vList;

    }

    private void showOrHiddenCard() {
        if(mAnswerCardLayer.getVisibility() == View.GONE) {
            mAnswerCardAdpater.notifyDataSetChanged();
            Animation inAnim = AnimationUtils.loadAnimation(ExamActivity.this, com.ouchn.lib.R.anim.translate_in);
            mAnswerCardLayer.startAnimation(inAnim);
            mAnswerCardLayer.setVisibility(View.VISIBLE);
        } else {
            Animation inAnim = AnimationUtils.loadAnimation(ExamActivity.this, com.ouchn.lib.R.anim.translate_out);
            mAnswerCardLayer.startAnimation(inAnim);
            mAnswerCardLayer.setVisibility(View.GONE);
        }
    }

    private AdapterView.OnItemClickListener mOnGridItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showOrHiddenCard();
            mPager.setCurrentItem(position, false);
        }

    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.exam_answer_card_btn:
                case R.id.card_go_back:
                    showOrHiddenCard();
                    break;
                case R.id.exam_go_back:
                    onBackPressed();
                    break;
            }
        }
    };

    public QuestionViewItemHolder.QuestionAnsweredListener questionAnsweredListener = new QuestionViewItemHolder.QuestionAnsweredListener() {
        @Override
        public void onQuestionAnswered(Question question) {

            BaseTask task = new BaseTask(0x2, question, ExamActivity.this);
            AsyncExecutor.getInstance().execMuliteTask(task);

        }
    };

    public void toCommitAnswers (final HashMap<String, String> kvs) {

        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCommitAnswers(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "question " + kvs.get("QuestionId") + " commit answer: " + kvs.get("Answer") + " , result : " + response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

        mVolleyRequestQueue.add(jreq);

    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mCurrentNumTv.setText((position + 1) + "");

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };


}
