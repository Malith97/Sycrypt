package com.synnlabz.sycryptr.other;

public class TypeItem {
    private String mItemName;
    private int mImage;

    public TypeItem(String itemName, int flagImage) {
        mItemName = itemName;
        mImage = flagImage;
    }

    public String getTypeName() {
        return mItemName;
    }

    public int getFlagImage() {
        return mImage;
    }
}
