package com.jhc.ygc;

public class SearchItems {
    private String name;
    private String link;
    private Character type; // AudioBook = a,Video = v, quiz=q,ebook=e (ONLY a,v,q,e)

    public SearchItems(String name, String link,Character type) {
        this.name = name;
        this.link = link;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public Character getType() {
        return type;
    }
}

