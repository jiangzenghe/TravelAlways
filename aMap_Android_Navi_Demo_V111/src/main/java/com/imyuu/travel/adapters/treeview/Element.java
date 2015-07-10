package com.imyuu.travel.adapters.treeview;
public class Element {
    /** 文字内容 */
    private String contentText;
    /** 在tree中的层级 */
    private int level;
    /** 元素的id */
    private int id;
    /** 父元素的id */
    private int parendId;
    /** 是否有子元素 */
    private boolean hasChildren;
    /** item是否展开 */
    private boolean isExpanded;

    /** 表示该节点没有父元素，也就是level为0的节点 */
    public static final int NO_PARENT = -1;
    /** 表示该元素位于最顶层的层级 */
    public static final int TOP_LEVEL = 0;

    public Element(String contentText, int level, int id, int parendId,
                   boolean hasChildren, boolean isExpanded) {
        super();
        this.contentText = contentText;
        this.level = level;
        this.id = id;
        this.parendId = parendId;
        this.hasChildren = hasChildren;
        this.isExpanded = isExpanded;
        canNav =  false;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParendId() {
        return parendId;
    }

    public void setParendId(int parendId) {
        this.parendId = parendId;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    private String  scenicName;
    private String scenicId;
    private String  mapSize;

    public boolean getCanNav() {
        return canNav;
    }

    public void setCanNav(boolean canNav) {
        this.canNav = canNav;
    }

    private boolean canNav;
    public boolean isHasDownload() {
        return hasDownload;
    }

    public void setHasDownload(boolean hasDownload) {
        this.hasDownload = hasDownload;
    }

    private boolean hasDownload;
    public static int getNoParent() {
        return NO_PARENT;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getMapSize() {
        return mapSize;
    }

    public void setMapSize(String mapSize) {
        this.mapSize = mapSize;
    }
}
