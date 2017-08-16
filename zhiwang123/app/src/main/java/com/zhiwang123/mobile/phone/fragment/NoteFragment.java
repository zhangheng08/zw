package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiwang123.mobile.R;

/**
 * @author wz
 */
public class NoteFragment extends BaseFragment {

	public static final String TAG = "RegisterFragment";

	public static final String KEY_COURSE_ID_KEY = "key_course_id";

	private View mRootView;

	private EditText mNoteInput;
	private View mNoteSaveBtn;
	private TextView mNoteSaveTxt;

	private String video_id;

	private SharedPreferences mSp;

	private static NoteFragment self = new NoteFragment();

	public NoteFragment() {
		super();
		name = TAG;
	}

	public static NoteFragment getInstance() {
		return self;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();

		mSp = getActivity().getSharedPreferences("course_note", 0);

		if(bundle != null) {

			video_id = bundle.getString(KEY_COURSE_ID_KEY);

			if(TextUtils.isEmpty(video_id)) {

				String noteContent = mSp.getString(video_id, "");
				mNoteInput.setText(noteContent);

			}

		}

	}

	@Override
	public void onAttach(Context context) {

		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.note_fragment_layout, null);
		initViews();
		return mRootView;
	}

	@Override
	protected void initViews() {

		mNoteInput = (EditText) mRootView.findViewById(R.id.video_frag_note_input);
		mNoteSaveBtn = mRootView.findViewById(R.id.video_frag_btn_save);
		mNoteSaveTxt = (TextView) mRootView.findViewById(R.id.video_frag_save_txt);

//		mNoteInput.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mNoteSaveTxt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

		mNoteSaveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(TextUtils.isEmpty(video_id)) {

					if(TextUtils.isEmpty(video_id)) {

						mSp.edit().putString(video_id, mNoteInput.getText().toString()).commit();
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();

					}

				}

			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

}
