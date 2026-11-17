package com.hexp.quicknote;

public class Note {
    public static final String CATEGORY_SCHOOL = "School";
    public static final String CATEGORY_PERSONAL = "Personal";
    public static final String CATEGORY_IDEAS = "Ideas";

    private String title;
    private String body;
    private String category;
    private long updatedAt;

    public Note(String title, String body, String category, long updatedAt) {
        this.title = sanitize(title);
        this.body = sanitize(body);
        this.category = sanitize(category);
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = sanitize(title);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = sanitize(body);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = sanitize(category);
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    private static String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
}
