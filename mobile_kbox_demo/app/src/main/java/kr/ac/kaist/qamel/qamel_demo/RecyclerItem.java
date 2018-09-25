package kr.ac.kaist.qamel.qamel_demo;

public class RecyclerItem {
    private String content;

    private boolean highlighted;

    public RecyclerItem(String content, boolean highlighted) {
        this.content = content;
        this.highlighted = highlighted;
    }

    public String getContent() {
        return content;
    }

    public boolean isHighlighted() { return highlighted; }
}
