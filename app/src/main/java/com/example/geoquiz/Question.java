package com.example.geoquiz;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mAnswered;
    private boolean isCheat;

    public Question(int textResId,boolean answerTrue,boolean answered){
        mTextResId=textResId;
        mAnswerTrue=answerTrue;
        mAnswered=answered;

    }

    public int getTextResId(){
        return mTextResId;
    }

    public void setTextResId(int textResId){
        mTextResId=textResId;
    }

    public boolean isAnswerTrue(){
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue){
        mAnswerTrue=answerTrue;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setIsAnswerd(boolean anSwered) {
        mAnswered=anSwered;
    }

    public boolean isCheat(){
        return isCheat;
    }

    public void setCheat(boolean cheat){
        isCheat=cheat;
    }

}
