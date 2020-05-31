package com.stenleone.innakozak.recycler;

public class CardScript {
    private String mText1;
    private String mText2;

    public CardScript( String text1, String text2) {
        mText1 = text1;
        mText2 = text2;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
}