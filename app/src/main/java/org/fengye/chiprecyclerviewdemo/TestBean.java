package org.fengye.chiprecyclerviewdemo;

import org.fengye.chiprecyclerview.AbstractChipBean;

public class TestBean extends AbstractChipBean {

    private int id;
    private String title;

    public TestBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public TestBean(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getId() {
        return id;
    }
}
