package org.badgerworks.hymn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shakir on 2017-03-19.
 */

public class ListAdapter extends ArrayAdapter<ListItem> {

    public ListAdapter(Context context, ArrayList<ListItem> videos) {
        super(context, 0, videos);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListItem video = (ListItem) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.textView);
        Log.d("getView",video.getTitle());
        title.setText(video.getTitle());
        return convertView;
    }




}
