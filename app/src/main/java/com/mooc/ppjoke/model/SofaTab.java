package com.mooc.ppjoke.model;

import java.util.List;

public class SofaTab {

    /**
     * activeSize : 16
     * normalSize : 14
     * activeColor : #ED7282
     * normalColor : #666666
     * select : 0
     * tabGravity : 0
     * tabs : [{"title":"图片","index":0,"tag":"pics","enable":true},{"title":"视频","index":1,"tag":"video","enable":true},{"title":"文本","index":1,"tag":"text","enable":true}]
     */

    public int activeSize;
    public int normalSize;
    public String activeColor;
    public String normalColor;
    public int select;
    public int tabGravity;
    public List<Tabs> tabs;

    public static class Tabs {
        /**
         * title : 图片
         * index : 0
         * tag : pics
         * enable : true
         */

        public String title;
        public int index;
        public String tag;
        public boolean enable;
    }
}
