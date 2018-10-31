package edu.cnm.deepdive.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivty extends AppCompatActivity {

  private static final String EXTRA_ANSWER_IS_TRUE = "answer_is_true";
  private static final String EXTRA_ANSWER_SHOWN = "answer_shown";
  private static final String FORMAT_STRING = "API Level: %d%nNumber of times cheated: %d";


  private boolean mAnswerIsTrue;
  private TextView mAnswerTextView;
  private Button mShowAnswerButton;
  private TextView showApi;
  private int cheatsNum;

  public static Intent newIntent(Context packageContext, boolean answerIstrue) {
    Intent intent = new Intent(packageContext, CheatActivty.class);
    intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIstrue);
    return intent;
  }

  public static boolean wasAnswerShown(Intent result) {
    return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cheat);

    mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
    mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

    Bundle extras = getIntent().getExtras();
    cheatsNum = extras.getInt("NUM_CHEATS", 0);

    showApi = findViewById(R.id.show_api);
    showApi
        .setText(String.format(FORMAT_STRING, android.os.Build.VERSION.SDK_INT, cheatsNum));

    mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
    mShowAnswerButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mAnswerIsTrue) {
          mAnswerTextView.setText(R.string.true_button);
        } else {
          mAnswerTextView.setText(R.string.false_button);
        }
        setAnswerShownResult(true);

        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          int cx = mShowAnswerButton.getWidth() / 2;
          int cy = mShowAnswerButton.getHeight() / 2;
          float radius = mShowAnswerButton.getWidth();
          Animator anim = ViewAnimationUtils
              .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
          anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              super.onAnimationEnd(animation);
              mShowAnswerButton.setVisibility(View.INVISIBLE);
            }
          });
          anim.start();
        } else {
          mShowAnswerButton.setVisibility(View.INVISIBLE);
        }
      }
    });
  }

  private void setAnswerShownResult(boolean isAnswerShown) {
    Intent data = new Intent();
    data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
    setResult(RESULT_OK, data);
  }
}
