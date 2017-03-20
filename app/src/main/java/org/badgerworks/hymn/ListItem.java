package org.badgerworks.hymn;

/**
 * Created by Shakir on 2017-03-19.
 */

public class ListItem {
    private String title;
    private String url;

    public ListItem(String title, String url){
        this.title = title;
        this.url = url;
    }

    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return url;
    }


}
