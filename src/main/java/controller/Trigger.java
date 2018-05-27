package controller;

public class Trigger {
    public enum TriggerType{
        FORWARD, REDIRECT
    }

    private String pagePath;
    private TriggerType root;

    public Trigger(String pagePath, TriggerType root){
        this.pagePath = pagePath;
        this.root = root;
    }

    public String getPagePath() {
        return pagePath;
    }

    public TriggerType getRoot() {
        return root;
    }
}
