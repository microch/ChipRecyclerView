package org.fengye.chiprecyclerview;


/**
 * author : caohao
 * date : 2021-03-07 11:15
 * description :
 */
public abstract class AbstractChipBean implements IChipBean {

    private boolean isEnable = true;

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public abstract int getId();

    public abstract String getTitle();


}
