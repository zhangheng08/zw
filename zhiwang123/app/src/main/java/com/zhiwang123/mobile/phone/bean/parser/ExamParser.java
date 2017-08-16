package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.Block;
import com.zhiwang123.mobile.phone.bean.ExamEntity;
import com.zhiwang123.mobile.phone.bean.ExamPaper;
import com.zhiwang123.mobile.phone.bean.QuestionZW;
import com.zhiwang123.mobile.phone.bean.ResultItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/9.
 */

public class ExamParser<T extends ExamEntity> {

    private static final String TAG = ExamParser.class.getName();

    private T t;

    public ExamParser(T t) {

        this.t = t;

    }

    public T parse(JSONObject jsonObject) {

        try {
            if(jsonObject.has(T.STATE)) t.state = jsonObject.getBoolean(T.STATE);
            if(jsonObject.has(T.MESSAGE)) t.message = jsonObject.getString(T.MESSAGE);
            if(jsonObject.has(T.DATA)) {
                JSONArray jsonArray = jsonObject.getJSONArray(T.DATA);
                if(jsonArray != null && jsonArray.length() != 0) {
                    t.data = new String[jsonArray.length()];
                    for(int i = 0; i < jsonArray.length(); i ++) {
                        t.data[i] = jsonArray.getString(i);
                    }
                }
            }
            if(jsonObject.has(T.DATAOBJECT)) {
                JSONObject subJo = jsonObject.getJSONObject(T.DATAOBJECT);
                ExamPaper paper = parseExamPaper(subJo);
                t.dataobject = new ArrayList<>();
                t.dataobject.add(paper);
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return t;
    }

    public ExamPaper parseExamPaper(JSONObject json) throws JSONException {

        ExamPaper p = new ExamPaper();

        if(json.has(ExamPaper.ID)) p.id = json.getString(ExamPaper.ID);
        if(json.has(ExamPaper.TITLE)) p.title = json.getString(ExamPaper.TITLE);
        if(json.has(ExamPaper.DESCRIPTION)) p.description = json.getString(ExamPaper.DESCRIPTION);
        if(json.has(ExamPaper.TIMELONG)) p.timeLong = json.getInt(ExamPaper.TIMELONG);
        if(json.has(ExamPaper.TOTALSCORE)) p.totalScore = json.getInt(ExamPaper.TOTALSCORE);
        if(json.has(ExamPaper.QUESTIONNUM)) p.questionNum = json.getInt(ExamPaper.QUESTIONNUM);
        if(json.has(ExamPaper.PASSSCORE)) p.passScore = json.getInt(ExamPaper.PASSSCORE);
        if(json.has(ExamPaper.USERSCORE)) p.userScore = json.getInt(ExamPaper.USERSCORE);
        if(json.has(ExamPaper.BLOCKS)) {
            JSONArray jsonArray = json.getJSONArray(ExamPaper.BLOCKS);
            if(jsonArray != null && jsonArray.length() != 0) {
                p.blocks = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i ++) {
                    JSONObject subJo = jsonArray.getJSONObject(i);
                    Block b = parseBlock(subJo);
                    p.blocks.add(b);
                }
            }
        }

        return p;
    }

    public Block parseBlock(JSONObject json) throws JSONException {

        Block b = new Block();

        if(json.has(Block.TITLE)) b.title = json.getString(Block.TITLE);
        if(json.has(Block.ORDER)) b.order = json.getInt(Block.ORDER);
        if(json.has(Block.QUESTIONS)) {
            JSONArray jsonArray = json.getJSONArray(Block.QUESTIONS);
            if(jsonArray != null && jsonArray.length() != 0) {
                b.questions = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i ++) {
                    JSONObject subJo = jsonArray.getJSONObject(i);
                    QuestionZW q = parseQuestion(subJo);
                    b.questions.add(q);
                }
            }
        }

        return b;
    }

    public QuestionZW parseQuestion(JSONObject json) throws JSONException {

        QuestionZW q = new QuestionZW();

        if(json.has(QuestionZW.ID)) q.id = json.getString(QuestionZW.ID);
        if(json.has(QuestionZW.TITLE)) q.title = json.getString(QuestionZW.TITLE);
        if(json.has(QuestionZW.QUESTIONTYPE)) q.questionType = json.getInt(QuestionZW.QUESTIONTYPE);
        if(json.has(QuestionZW.SCORE)) q.score = json.getInt(QuestionZW.SCORE);
        if(json.has(QuestionZW.ANSWER)) q.answer = json.getString(QuestionZW.ANSWER);
        if(json.has(QuestionZW.ORDER)) q.order = json.getInt(QuestionZW.ORDER);

        if(json.has(QuestionZW.RESULTITEMS)) {
            JSONArray jsonArray = json.getJSONArray(QuestionZW.RESULTITEMS);
            if(jsonArray != null && jsonArray.length() != 0) {
                q.resultItems = new ArrayList<ResultItem>();
                for(int i = 0; i < jsonArray.length(); i ++) {
                    JSONObject subJo = jsonArray.getJSONObject(i);
                    ResultItem r = parseResultItem(subJo);
                    q.resultItems.add(r);
                }
            }
        }
        if(json.has(QuestionZW.RIGHTANSWER)) {

            JSONArray jsonArray = json.getJSONArray(QuestionZW.RIGHTANSWER);

            q.rightAnswers = new ArrayList<>();

            if(jsonArray != null && jsonArray.length() !=0 ) {

                for(int i = 0; i < jsonArray.length(); i ++) {

                    JSONObject jsonObject =  jsonArray.getJSONObject(i);

                    QuestionZW.ItemAnswer itemAnswer = q.new ItemAnswer();

                    if(jsonObject.has(QuestionZW.CONTENT)) itemAnswer.content = jsonObject.getString(QuestionZW.CONTENT);

                    q.rightAnswers.add(itemAnswer);

                }

            }

        }
        if(json.has(QuestionZW.USERANSWER)) {

            JSONArray jsonArray = json.getJSONArray(QuestionZW.USERANSWER);

            q.userAnswers = new ArrayList<>();

            if(jsonArray != null && jsonArray.length() !=0 ) {

                for(int i = 0; i < jsonArray.length(); i ++) {

                    JSONObject jsonObject =  jsonArray.getJSONObject(i);

                    QuestionZW.ItemAnswer itemAnswer = q.new ItemAnswer();

                    if(jsonObject.has(QuestionZW.CONTENT)) itemAnswer.content = jsonObject.getString(QuestionZW.CONTENT);

                    q.userAnswers.add(itemAnswer);

                }

            }

        }

        return q;

    }

    public ResultItem parseResultItem(JSONObject json) throws JSONException {

        ResultItem r = new ResultItem();

        if(json.has(ResultItem.NUMBER)) r.number = json.getString(ResultItem.NUMBER);
        if(json.has(ResultItem.TITLE)) r.title = json.getString(ResultItem.TITLE);
        if(json.has(ResultItem.ORDER)) r.order = json.getInt(ResultItem.ORDER);

        return r;
    }

}
