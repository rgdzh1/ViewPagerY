package com.yey.viewpagery;

public class ResType<T> {


    public enum Type {
        IAMG,
        LAYOUT,
        URL
    }

    private T mRes;
    private Type mType;

    public ResType(T res, Type mType) {
        this.mRes = res;
        this.mType = mType;
    }

    public T getRes() {
        return mRes;
    }

    public Type getmType() {
        return mType;
    }
}
