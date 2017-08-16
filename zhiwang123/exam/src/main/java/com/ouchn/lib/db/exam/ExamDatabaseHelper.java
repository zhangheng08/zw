package com.ouchn.lib.db.exam;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ouchn.lib.entity.Question;
import com.ouchn.lib.entity.QuestionAnswer;
import com.ouchn.lib.entity.QuestionDepotPage;

public class ExamDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "ExamDatabaseHelper";
	private static final int 	VERSION = 1;
	private static final String NAME = "exam.db";
	
	private static final String TABLE_DEOPT_JSON_STR = "depot_json_str";
	private static final String CREATE_DEPOT_JSON_STR =  "CREATE TABLE depot_json_str( _id integer primary key autoincrement, url text unique, json text);";
	private static final String DEPOT_JSON_STR_COLUMN_ID = "_id";
	private static final String DEPOT_JSON_STR_COLUMN_URL = "url";
	private static final String DEPOT_JSON_STR_COLUMN_JSON = "json";
	
	
	private static final String TABLE_QUESTION_JSON_STR = "question_json_str";
	private static final String CREATE_QUESTION_JSON_STR = "CREATE TABLE question_json_str (_id integer primary key autoincrement, depotId text unique, json text);";
	private static final String QUESTION_JAON_STR_COLUMN_ID = "_id";
	private static final String QUESTION_JAON_STR_COLUMN_DEPOTID = "depotId";
	private static final String QUESTION_JAON_STR_COLUMN_JSON = "json";
	
	private static final String TABLE_DEPOT_PAGE_INFO = "depot_page_info";
	private static final String CREATE_DEPOT_INFO = "" + 
	"CREATE TABLE depot_page_info " + 
	"(" +
	"_id                    integer primary key autoincrement," +
	"depot_id               text unique," +
	"score                  integer," +
	"totle_num              integer," +
	"answered_num           integer," +
	"worng_num              integer," +
	"depot_state            integer," +
	"cost_time              text" +
	");";
	private static final String DEPOT_INFO_COLUMN_ID = "_id";
	private static final String DEPOT_INFO_COLUMN_DEPOT_ID = "depot_id";
	private static final String DEPOT_INFO_COLUMN_SCORE = "score";
	private static final String DEPOT_INFO_COLUMN_TOTLE_NUM = "totle_num";
	private static final String DEPOT_INFO_COLUMN_ANSWERED_NUM = "answered_num";
	private static final String DEPOT_INFO_COLUMN_WORNG_NUM = "worng_num";
	private static final String DEPOT_INFO_COLUMN_DEPOT_STATE = "depot_state";
	private static final String DEPOT_INFO_COLUMN_COST_TIME = "cost_time";
	
	private static final String TABLE_ANSWERED_QUESTION = "answered_question";
	private static final String CREATE_ANSWERED_QUESTION = "" +
	"CREATE TABLE answered_question" + 
	"(" + 
	"_id                  integer primary key autoincrement," +
	"id                   text unique," + 
	"content              text," +
	"question_type        integer," +
	"question_score       integer," +
	"question_type_str    text," +
	"question_score_str   text," +
	"question_serial_num  integer" +
	");";
	private static final String ANSWERED_QUESTION_COLUMN_ID_PR = "_id";
	private static final String ANSWERED_QUESTION_COLUMN_ID = "id";
	private static final String ANSWERED_QUESTION_COLUMN_CONTENT = "content";
	private static final String ANSWERED_QUESTION_COLUMN_QUESTION_TYPE = "question_type";
	private static final String ANSWERED_QUESTION_COLUMN_QUESTION_SCORE = "question_score";
	private static final String ANSWERED_QUESTION_COLUMN_QUESTION_TYPE_STR = "question_type_str";
	private static final String ANSWERED_QUESTION_COLUMN_QUESTION_SCORE_STR = "question_score_str";
	private static final String ANSWERED_QUESTION_COLUMN_QUESTION_SERIAL_NUM = "question_serial_num";
	
	private static final String TABLE_QUESTION_OPTIONS = "question_options";
	private static final String CREATE_QUESTION_OPTIONS = "" +
	"CREATE TABLE question_options" +
	"(" +
	"_id                          integer primary key autoincrement," +
	"id                           text unique," +
	"if_right_answer              integer default 0," +
	"order_num                    integer," +
	"question_id                  text," +
	"foreign key(question_id) references answered_question(id)" +
	");";
	private static final String QUESTION_OPTIONS_COLUMN_ID_PR = "_id";
	private static final String QUESTION_OPTIONS_COLUMN_ID = "id";
	private static final String QUESTION_OPTIONS_COLUMN_IF_RIGHT_ANSWER = "if_right_answer";
	private static final String QUESTION_OPTIONS_COLUMN_ORDER_NUM = "order_num";
	private static final String QUESTION_OPTIONS_COLUMN_QUESTION_ID = "question_id";
	
	private static final String TABLE_ANSWERS_INFO = "answers_info";
	private static final String CREATE_ANSWERS_INFO = "" + 
	"CREATE TABLE answers_info" +
	"(" +
	"_id                       integer primary key autoincrement," +
	"question_depot_id         text," +
	"question_id               text," +
	"quesiton_content          text," +
	"question_answer           text," +
	"question_type			   integer," +
	"is_answer_right           integer default 0" +
//	"foreign key(question_id) references answered_question(id)" + 
	");";
	private static final String ANSWERS_INFO_ID_PR = "_id";
	private static final String ANSWERS_INFO_COLUMN_QUESTION_DEPOT_ID = "question_depot_id";
	private static final String ANSWERS_INFO_COLUMN_QUESTION_ID = "question_id";
	private static final String ANSWERS_INFO_COLUMN_QUESITON_CONTENT = "quesiton_content";
	private static final String ANSWERS_INFO_COLUMN_QUESTION_ANSWER = "question_answer";
	private static final String ANSWERS_INFO_COLUMN_QUESTION_TYPE = "question_type";
	private static final String ANSWERS_INFO_COLUMN_IS_ANSWER_RIGHT = "is_answer_right";
	
	private static final String TABLE_ANSWER_NOTE = "answer_note";
	private static final String CREATE_ANSWER_NOTE = "" +
	"CREATE TABLE answer_note" +
	"(" +
	"_id                     integer primary key autoincrement," +
	"depot_id                text unique," +
	"note_content            text" +
	");";
	private static final String ANSWER_NOTE_COLUMN_ID_PR = "_id";
	private static final String ANSWER_NOTE_COLUMN_DEPOT_ID = "depot_id";
	private static final String ANSWER_NOTE_COLUMN_CONTENT = "note_content";
	
	
	private static final String QUERY_DEPOT_JSON_STR_ALL = "select * from depot_json_str";
	private static final String QUERY_DEPOT_JSON_STR_SINGLE = "select * from depot_json_str where url = ?";
	private static final String QUERY_QUESTION_JSON_STR = "select * from question_json_str where depotId = ?";
	private static final String QUERY_ANSWERS_INFO = "select * from answers_info where question_depot_id = ?";
	private static final String QUERY_DEPOT_PAGE_INFO = "select * from depot_page_info where depot_id = ?";
	private static final String QUERY_ANSWER_NOTE = "select * from answer_note where depot_id = ?";
	
	public static final String SEPARATOR = ";";
	
	
	private Context mContext;
	
	public ExamDatabaseHelper(Context context) {
		super(context, NAME, null, VERSION);
		mContext = context;
	}
	
	public ExamDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public SQLiteDatabase createDatabase() {
		SQLiteDatabase db = getWritableDatabase();
		if(db != null) {
			db.close();
		}
		return db;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL(CREATE_DEPOT_JSON_STR);
			db.execSQL(CREATE_QUESTION_JSON_STR);
			db.execSQL(CREATE_DEPOT_INFO);
			db.execSQL(CREATE_ANSWER_NOTE);
			//db.execSQL(CREATE_ANSWERED_QUESTION);
			//db.execSQL(CREATE_QUESTION_OPTIONS);
			db.execSQL(CREATE_ANSWERS_INFO);
			db.setTransactionSuccessful();
			Log.v(TAG, "SQL: db create success!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO 
	}
	
	public void saveDeoptJsonInfo(String url, JSONArray jsonArr) {
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.beginTransaction();
			Cursor c = db.rawQuery(QUERY_DEPOT_JSON_STR_SINGLE, new String[] {url});
			ContentValues cv = new ContentValues();
			if(c == null || c.getCount() == 0) {
				cv.put(DEPOT_JSON_STR_COLUMN_URL, url);
				cv.put(DEPOT_JSON_STR_COLUMN_JSON, jsonArr.toString());
				db.insert(TABLE_DEOPT_JSON_STR, "", cv);
			} else {
				cv.put(DEPOT_JSON_STR_COLUMN_JSON, jsonArr.toString());
				db.update(TABLE_DEOPT_JSON_STR, cv, "url = ?", new String[] {url});
			}
			
			if(c != null) c.close();
			db.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, ":< ---------------------------------save deopt json info error!");
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public JSONArray getDepotJsonInfo(String url) {
		SQLiteDatabase db = getWritableDatabase();
		JSONArray jsonArr = null;
		Cursor c = null;
		try {
			c = db.rawQuery(QUERY_DEPOT_JSON_STR_SINGLE, new String[] {url});
			if(c != null && c.getCount() != 0) {
				c.moveToFirst();
				String json = c.getString(c.getColumnIndex(DEPOT_JSON_STR_COLUMN_JSON));
				jsonArr = new JSONArray(json);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(c != null) c.close();
			db.close();
		}
		return jsonArr;
	}
	
	public void saveQuestionJsonInfo(String depotId, JSONArray jsonArr) {
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.beginTransaction();
			Cursor c = db.rawQuery(QUERY_QUESTION_JSON_STR, new String[] {depotId});
			ContentValues cv = new ContentValues();
			if(c == null || c.getCount() == 0) {
				cv.put(QUESTION_JAON_STR_COLUMN_DEPOTID, depotId);
				cv.put(QUESTION_JAON_STR_COLUMN_JSON, jsonArr.toString());
				db.insert(TABLE_QUESTION_JSON_STR, "", cv);
			} else {
				cv.put(QUESTION_JAON_STR_COLUMN_JSON, jsonArr.toString());
				db.update(TABLE_QUESTION_JSON_STR, cv, "depotId = ?", new String[] {depotId});
			}
			
			if(c != null) c.close();
			db.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, ":< ---------------------------------save question json info error!");
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public JSONArray getQuestionJsonInfo(String depotId) {
		SQLiteDatabase db = getWritableDatabase();
		JSONArray jsonArr = null;
		Cursor c = null;
		try {
			c = db.rawQuery(QUERY_QUESTION_JSON_STR, new String[] {depotId});
			if(c != null && c.getCount() != 0) {
				c.moveToFirst();
				String json = c.getString(c.getColumnIndex(QUESTION_JAON_STR_COLUMN_JSON));
				jsonArr = new JSONArray(json);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(c != null) c.close();
			db.close();
		}
		return jsonArr;
	}
	
	public void saveAnswersInfo(Question question) {
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.beginTransaction();
			
			db.delete(TABLE_ANSWERS_INFO, "question_depot_id = ?", new String[] {question.getDeoptId()});
			
			ContentValues cv = new ContentValues();
			cv.put(ANSWERS_INFO_COLUMN_QUESTION_DEPOT_ID, question.getDeoptId());
			cv.put(ANSWERS_INFO_COLUMN_QUESTION_ID, question.getId());
			cv.put(ANSWERS_INFO_COLUMN_QUESITON_CONTENT, question.getContent());
			
			StringBuffer strb = new StringBuffer("");
			for(String str : question.mAnswerState.getAnswer(false)) {
				strb.append(str);
				strb.append(SEPARATOR);
			}
			cv.put(ANSWERS_INFO_COLUMN_QUESTION_ANSWER, strb.toString());
			cv.put(ANSWERS_INFO_COLUMN_QUESTION_TYPE, question.getQuestionType());
			cv.put(ANSWERS_INFO_COLUMN_IS_ANSWER_RIGHT, question.mAnswerState.isAnswerRight() ? 1 : 0);
			db.insert(TABLE_ANSWERS_INFO, "", cv);
			db.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public ArrayList<QuestionAnswer> getAnswersInfo(String depotId) {
		SQLiteDatabase db = getWritableDatabase();
		ArrayList<QuestionAnswer> list = new ArrayList<QuestionAnswer>();
		Cursor c = null;
		try {
			c = db.rawQuery(QUERY_ANSWERS_INFO, new String[] {depotId});
			if(c != null && c.getCount() != 0) {
				c.moveToFirst();
				do {
					QuestionAnswer answer = new QuestionAnswer();
					answer.setDepotId(c.getString(c.getColumnIndex(ANSWERS_INFO_COLUMN_QUESTION_DEPOT_ID)));
					answer.setQuestionId(c.getString(c.getColumnIndex(ANSWERS_INFO_COLUMN_QUESTION_ID)));
					answer.setQuestionAnswer(c.getString(c.getColumnIndex(ANSWERS_INFO_COLUMN_QUESTION_ANSWER)));
					answer.setQuestionContent(c.getString(c.getColumnIndex(ANSWERS_INFO_COLUMN_QUESITON_CONTENT)));
					answer.setQuestionType(c.getInt(c.getColumnIndex(ANSWERS_INFO_COLUMN_QUESTION_TYPE)));
					answer.setAnswerRight(c.getInt(c.getColumnIndex(ANSWERS_INFO_COLUMN_IS_ANSWER_RIGHT)) == 0 ? false : true);
					list.add(answer);
				} while(c.moveToNext());
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(c != null) c.close();
			db.close();
		}
		return list;
	}
	
	public QuestionDepotPage getDepotInfo(String depotId) {
		SQLiteDatabase db = getWritableDatabase();
		QuestionDepotPage qdp = null;
		Cursor c = null;
		try {
			
			c = db.rawQuery(QUERY_DEPOT_PAGE_INFO, new String[]{depotId});
			
			if(c != null && c.getCount() != 0) {
				c.moveToFirst();
				String dId = c.getString(c.getColumnIndex(DEPOT_INFO_COLUMN_DEPOT_ID));
				int score = c.getInt(c.getColumnIndex(DEPOT_INFO_COLUMN_SCORE));
				int totleNum = c.getInt(c.getColumnIndex(DEPOT_INFO_COLUMN_TOTLE_NUM));
				int answeredNum = c.getInt(c.getColumnIndex(DEPOT_INFO_COLUMN_ANSWERED_NUM));
				int wrongNum = c.getInt(c.getColumnIndex(DEPOT_INFO_COLUMN_WORNG_NUM));
				int depotState = c.getInt(c.getColumnIndex(DEPOT_INFO_COLUMN_DEPOT_STATE));
				String costTime = c.getString(c.getColumnIndex(DEPOT_INFO_COLUMN_COST_TIME));
				
				qdp = new QuestionDepotPage();
				qdp.setDepotId(dId);
				qdp.setScore(score);
				qdp.setTotleNum(totleNum);
				qdp.setAnsweredNum(answeredNum);
				qdp.setWrongNum(wrongNum);
				qdp.setDepotState(depotState);
				qdp.setCostTime(costTime);
			} 
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(c != null) c.close(); 
			db.close();
		}
		return qdp;
	}
	
	public void saveDepotPageInfo(QuestionDepotPage qdp) {
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.beginTransaction();
			
			db.delete(TABLE_DEPOT_PAGE_INFO, "depot_id = ?", new String[] {qdp.getDepotId()});
			
			ContentValues cv = new ContentValues();
			cv.put(DEPOT_INFO_COLUMN_DEPOT_ID, qdp.getDepotId());
			cv.put(DEPOT_INFO_COLUMN_SCORE, qdp.getScore());
			cv.put(DEPOT_INFO_COLUMN_TOTLE_NUM, qdp.getTotleNum());
			cv.put(DEPOT_INFO_COLUMN_ANSWERED_NUM, qdp.getAnsweredNum());
			cv.put(DEPOT_INFO_COLUMN_WORNG_NUM, qdp.getWrongNum());
			cv.put(DEPOT_INFO_COLUMN_DEPOT_STATE, qdp.getDepotState());
			cv.put(DEPOT_INFO_COLUMN_COST_TIME, qdp.getCostTime());
			db.insert(TABLE_DEPOT_PAGE_INFO, "", cv);
			
			db.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public String getAnswerNote(String depotId) {
		SQLiteDatabase db = getWritableDatabase();
		String note = "";
		Cursor c = null;
		
		try {
			c = db.rawQuery(QUERY_ANSWER_NOTE, new String[] {depotId});
			if(c != null && c.getCount() != 0) {
				c.moveToFirst();
				note = c.getString(c.getColumnIndex(ANSWER_NOTE_COLUMN_CONTENT));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(c != null) c.close();
			db.close();
		}
		
		return note;
	}
	
	public void saveAnswerNote(String depotId, String note) {
		SQLiteDatabase db = getWritableDatabase();
		
		try {
			db.beginTransaction();
			
			db.delete(TABLE_ANSWER_NOTE, "depot_id = ?", new String[] {depotId});

			ContentValues cv = new ContentValues();
			cv.put(ANSWER_NOTE_COLUMN_DEPOT_ID, depotId);
			cv.put(ANSWER_NOTE_COLUMN_CONTENT, note);
			db.insert(TABLE_ANSWER_NOTE, "", cv);
			
			db.setTransactionSuccessful();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	

}
