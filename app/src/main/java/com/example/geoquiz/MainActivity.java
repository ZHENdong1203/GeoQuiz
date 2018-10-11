package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class MainActivity extends Activity {

    private static final String TAG="MainActivity";
    private static final String KEY_INDEX="index";
    private static final String KEY_ISCHEATER = "ischeater";
    private static final int REQUEST_CODE_CHEAT=0;


    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    public static int cheatNumber = 3;

    private Question[] mQuestionBnak=new Question[]{
            new Question(R.string.question_australia,true,false),
            new Question(R.string.question_oceans,true,false),
            new Question(R.string.question_mideast,false,false),
            new Question(R.string.question_africa,false,false),
            new Question(R.string.question_americas,true,false),
            new Question(R.string.question_asia,true,false)
    };

    private int mCurrentIndex=0;
    private boolean mIsCheater;
    private int FullGrade = mQuestionBnak.length;
    private int RightGrade = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(KEY_ISCHEATER, false);
        }


        mQuestionTextView=(TextView)findViewById(R.id.question_text_view);
        //updateQuestion();
        //点击题目也可换题
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBnak.length;
                updateQuestion();
            }
        });
        //点击True按钮
        mTrueButton=(Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                Inspect();
            }
        });
        //点击False按钮
        mFalseButton=(Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                Inspect();
            }
        });
        //点击下一题按钮
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBnak.length;
                mIsCheater=false;
                updateQuestion();
            }
        });
        //点击上一题按钮
        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBnak.length - 1) % mQuestionBnak.length;
                updateQuestion();
            }
        });
        //点击作弊按钮
        mCheatButton=(Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue=mQuestionBnak[mCurrentIndex].isAnswerTrue();
                Intent intent=CheatActivity.newIntent(MainActivity.this,answerIsTrue);
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();

    }

    //function updateQuestion
    private void updateQuestion() {
        //Log.d(TAG, "Updating question text for question #" + mCurrentIndex, new Exception());
        int question = mQuestionBnak[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if(mQuestionBnak[mCurrentIndex].isAnswered()){
            mTrueButton.setVisibility(View.GONE);
            mFalseButton.setVisibility(View.GONE);
        }else{
            mTrueButton.setVisibility(View.VISIBLE);
            mFalseButton.setVisibility(View.VISIBLE);
        }
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue =mQuestionBnak[mCurrentIndex].isAnswerTrue();
        int messageResId =0;
        if(mIsCheater||mQuestionBnak[mCurrentIndex].isCheat()){
            messageResId=R.string.judgement_toast;
            mQuestionBnak[mCurrentIndex].setCheat(true);
        }else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                RightGrade++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        mQuestionBnak[mCurrentIndex].setIsAnswerd(true);
        mTrueButton.setVisibility(View.GONE);
        mFalseButton.setVisibility(View.GONE);
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    private void printGrade() {
        DecimalFormat df = new DecimalFormat("#.00");
        mQuestionTextView.setText(String.valueOf(df.format(((double)RightGrade / FullGrade) * 100)));
        mNextButton.setVisibility(View.GONE);
        mPrevButton.setVisibility(View.GONE);
    }

    private void Inspect() {
        int index = 0;
        for (Question q : mQuestionBnak) {
            if (q.isAnswered()) {
                index++;
            }
        }
        if (index == mQuestionBnak.length) {
            printGrade();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() called");
    }

    @Override
    protected void onResume() {
        /**
         * 在MainActivity从新变为栈顶活动并加载到当前视图时检测cheat是否为0，
         * 若为0，则隐藏mCheatButton，若不为0，则输出剩余次数
         */
        super.onResume();
        Log.d(TAG,"onResume() called");
        if (cheatNumber == 0){
            mCheatButton.setVisibility(View.GONE);
        } else {
            Toast.makeText(MainActivity.this,
                    "作弊次数还有" + String.valueOf(cheatNumber) + "次",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putBoolean(KEY_ISCHEATER, mIsCheater);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_CODE_CHEAT){
            if(data==null){
                return;
            }
            mIsCheater=CheatActivity.wasAnswerShown(data);
        }
    }

}
