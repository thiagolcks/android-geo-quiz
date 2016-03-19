package xyz.thiago.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private int mCurrentIndex = 0;
    private ArrayList<Integer> mCheatedQuestions;

    private static final String TAG = "QuizActivity";
    private static final String QUESTION_KEY_INDEX = "question_index";
    private static final String IS_CHEATER_INDEX = "is_cheater";
    private static final String CHEATED_QUESTIONS_INDEX = "cheated_questions";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Question[] mQuestionBank = {
            new Question(R.string.q1, true),
            new Question(R.string.q2, false),
            new Question(R.string.q3, false),
            new Question(R.string.q4, true),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate was called");

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(QUESTION_KEY_INDEX, 0);
            mCheatedQuestions = savedInstanceState.getIntegerArrayList(CHEATED_QUESTIONS_INDEX);
        } else {
            mCheatedQuestions = new ArrayList<Integer>();
        }

        View.OnClickListener nextQuestion = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                setCurrentQuestion();
            }
        };

        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(nextQuestion);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextQuestion);

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                if (mCurrentIndex < 0) mCurrentIndex = mQuestionBank.length - 1;
                setCurrentQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        setCurrentQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called");

        outState.putInt(QUESTION_KEY_INDEX, mCurrentIndex);
        outState.putIntegerArrayList(CHEATED_QUESTIONS_INDEX, mCheatedQuestions);
    }

    private void checkAnswer(boolean answer) {
        int result;
        if (this.mCheatedQuestions.indexOf(mCurrentIndex) > -1) {
            result = R.string.judgement_toast;
        } else {
            result = (mQuestionBank[mCurrentIndex].isAnswerTrue() == answer) ? R.string.correct_toast : R.string.incorrect_toast;
        }
        Toast.makeText(QuizActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    private void setCurrentQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHEAT && resultCode == RESULT_OK && data != null) {
            if (CheatActivity.wasAnswerShown(data)) {
                mCheatedQuestions.add(mCurrentIndex);
            }
        }
    }
}
