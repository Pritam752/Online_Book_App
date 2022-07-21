package com.example.introduction.ebookreader;

public class BookModel {

    // details for our book
    private String title;
    private String subtitle;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private  String language;
    private String thumbnail;
    private String previewLink;
    private String buyLink;
    private String category;
    private  String id;

    public String getId() { return id;}


    public String getCategory() {
        return category;
    }

    public BookModel(String category) {
        this.category = category;
    }

    //get methods
    public String getTitle() { return title; }

    public String getSubtitle() { return subtitle; }

    public String getPublisher() { return publisher; }

    public String getPublishedDate() { return publishedDate; }

    public String getDescription() { return description; }

    public int getPageCount() { return pageCount; }

    public String getThumbnail() { return thumbnail; }

    public String getLanguage() { return language; }

    public String getPreviewLink() { return previewLink; }

    public String getBuyLink() { return buyLink; }

    //set methods
    public void setId(String id) {this.id = id;}

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) { this.title = title; }

    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public void setPublisher(String publisher) { this.publisher = publisher; }

    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }

    public void setDescription(String description) { this.description = description; }

    public void setPageCount(int pageCount) { this.pageCount = pageCount; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public void setLanguage(String language) { this.language = language; }

    public void setPreviewLink(String previewLink) { this.previewLink = previewLink; }

    public void setBuyLink(String buyLink) { this.buyLink = buyLink; }



    //constructor for 11 parameters
    public BookModel(String id,String title, String subtitle, String publisher, String publishedDate,
                     String description, int pageCount, String thumbnail, String language, String previewLink, String buyLink)
    {
        this.id=id;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.thumbnail = thumbnail;
        this.language=language;
        this.previewLink = previewLink;
        this.buyLink = buyLink;
    }

    //constructor for 4 parameters
    public BookModel(String title,String publisher, String thumbnail, String previewLink) {
        this.title = title;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.previewLink=previewLink;
    }
}
