package com.example.introduction.ebookreader;

public class PdfInfo {
    private  String PdfName;
    private  String Uri;

    public String getPdfName() {
        return PdfName;
    }

    public void setPdfName(String pdfName) {
        PdfName = pdfName;
    }

    public String getUri() {
        return Uri;
    }

    public PdfInfo(String pdfName, String uri) {
        PdfName = pdfName;
        Uri = uri;
    }

    public PdfInfo(String pdfName) {
        PdfName = pdfName;
    }

    public void setUri(String uri) {
        Uri = uri;
    }
}
