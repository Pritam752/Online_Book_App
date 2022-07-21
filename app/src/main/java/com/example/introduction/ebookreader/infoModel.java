package com.example.introduction.ebookreader;

public class infoModel {
    private String Name;
    private int image;
    private  String SearchHistory;
    private String micSearchHistory;
    private String uri;

    public String getName() {
        return Name;
    }
    public int getImage() {
        return image;
    }
    public String getSearchHistory() {return SearchHistory;}
    public String getMicSearchHistory() {return micSearchHistory;}

    public void setName(String name) {
        Name = name;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public void setSearchHistory(String searchHistory) {SearchHistory = searchHistory;}
    public void setMicSearchHistory(String micSearchHistory) {this.micSearchHistory = micSearchHistory;}

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
//    public infoModel(String searchHistory, String micSearchHistory) {
//        SearchHistory = searchHistory;
//        this.micSearchHistory = micSearchHistory;
//    }

    public infoModel(String searchHistory) {
        SearchHistory = searchHistory;
    }

    public infoModel(String name, String uri) {
        Name = name;
        this.uri = uri;
    }

    public infoModel(String name, int image) {
        Name = name;
        this.image = image;
    }

    public infoModel(String name, int image, String uri) {
        Name = name;
        this.image = image;
        this.uri = uri;
    }
}
