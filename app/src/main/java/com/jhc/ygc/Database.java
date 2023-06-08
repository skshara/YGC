package com.jhc.ygc;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<SearchItems> myObjects;

    public Database() {
        myObjects = new ArrayList<>();
        // Add objects to the database
        //Grade 6 Video
        myObjects.add(new SearchItems("Grade 6 Science", "file:///android_asset/interactive-video.html?grade=6&subject=science",'v'));
        myObjects.add(new SearchItems("Grade 6 Maths", "file:///android_asset/interactive-video.html?grade=6&subject=maths",'v'));
        myObjects.add(new SearchItems("Grade 6 Tamil", "file:///android_asset/interactive-video.html?grade=6&subject=tamil",'v'));
        myObjects.add(new SearchItems("Grade 6 English", "file:///android_asset/interactive-video.html?grade=6&subject=english",'v'));
        myObjects.add(new SearchItems("Grade 6 History", "file:///android_asset/interactive-video.html?grade=6&subject=history",'v'));
        myObjects.add(new SearchItems("Grade 6 ICT", "file:///android_asset/interactive-video.html?grade=6&subject=ict",'v'));
        //Grade 7 Video
        myObjects.add(new SearchItems("Grade 7 Science", "file:///android_asset/interactive-video.html?grade=7&subject=science",'v'));
        myObjects.add(new SearchItems("Grade 7 Maths", "file:///android_asset/interactive-video.html?grade=7&subject=maths",'v'));
        myObjects.add(new SearchItems("Grade 7 Tamil", "file:///android_asset/interactive-video.html?grade=7&subject=tamil",'v'));
        myObjects.add(new SearchItems("Grade 7 English", "file:///android_asset/interactive-video.html?grade=7&subject=english",'v'));
        myObjects.add(new SearchItems("Grade 7 History", "file:///android_asset/interactive-video.html?grade=7&subject=history",'v'));
        myObjects.add(new SearchItems("Grade 7 ICT", "file:///android_asset/interactive-video.html?grade=7&subject=ict",'v'));
        //Grade 8 Video
        myObjects.add(new SearchItems("Grade 8 Science", "file:///android_asset/interactive-video.html?grade=8&subject=science",'v'));
        myObjects.add(new SearchItems("Grade 8 Maths", "file:///android_asset/interactive-video.html?grade=8&subject=maths",'v'));
        myObjects.add(new SearchItems("Grade 8 Tamil", "file:///android_asset/interactive-video.html?grade=8&subject=tamil",'v'));
        myObjects.add(new SearchItems("Grade 8 English", "file:///android_asset/interactive-video.html?grade=8&subject=english",'v'));
        myObjects.add(new SearchItems("Grade 8 History", "file:///android_asset/interactive-video.html?grade=8&subject=history",'v'));
        myObjects.add(new SearchItems("Grade 8 ICT", "file:///android_asset/interactive-video.html?grade=8&subject=ict",'v'));
        //Grade 9 Video
        myObjects.add(new SearchItems("Grade 9 Science", "file:///android_asset/interactive-video.html?grade=9&subject=science",'v'));
        myObjects.add(new SearchItems("Grade 9 Maths", "file:///android_asset/interactive-video.html?grade=9&subject=maths",'v'));
        myObjects.add(new SearchItems("Grade 9 Tamil", "file:///android_asset/interactive-video.html?grade=9&subject=tamil",'v'));
        myObjects.add(new SearchItems("Grade 9 English", "file:///android_asset/interactive-video.html?grade=9&subject=english",'v'));
        myObjects.add(new SearchItems("Grade 9 History", "file:///android_asset/interactive-video.html?grade=9&subject=history",'v'));
        myObjects.add(new SearchItems("Grade 9 ICT", "file:///android_asset/interactive-video.html?grade=9&subject=ict",'v'));
        //Grade 10 Video
        myObjects.add(new SearchItems("Grade 10 Science", "file:///android_asset/interactive-video.html?grade=10&subject=science",'v'));
        myObjects.add(new SearchItems("Grade 10 Maths", "file:///android_asset/interactive-video.html?grade=10&subject=maths",'v'));
        myObjects.add(new SearchItems("Grade 10 Tamil", "file:///android_asset/interactive-video.html?grade=10&subject=tamil",'v'));
        myObjects.add(new SearchItems("Grade 10 English", "file:///android_asset/interactive-video.html?grade=10&subject=english",'v'));
        myObjects.add(new SearchItems("Grade 10 History", "file:///android_asset/interactive-video.html?grade=10&subject=history",'v'));
        myObjects.add(new SearchItems("Grade 10 ICT", "file:///android_asset/interactive-video.html?grade=10&subject=ict",'v'));
        //Grade 11 Video
        myObjects.add(new SearchItems("Grade 11 Science", "file:///android_asset/interactive-video.html?grade=11&subject=science",'v'));
        myObjects.add(new SearchItems("Grade 11 Maths", "file:///android_asset/interactive-video.html?grade=11&subject=maths",'v'));
        myObjects.add(new SearchItems("Grade 11 Tamil", "file:///android_asset/interactive-video.html?grade=11&subject=tamil",'v'));
        myObjects.add(new SearchItems("Grade 11 English", "file:///android_asset/interactive-video.html?grade=11&subject=english",'v'));
        myObjects.add(new SearchItems("Grade 11 History", "file:///android_asset/interactive-video.html?grade=11&subject=history",'v'));
        myObjects.add(new SearchItems("Grade 11 ICT", "file:///android_asset/interactive-video.html?grade=11&subject=ict",'v'));
    }

    public List<SearchItems> searchObjects(String query) {
        List<SearchItems> results = new ArrayList<>();

        for (SearchItems object : myObjects) {
            if (object.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(object);
            }
        }

        return results;
    }
}
