package com.jhc.ygc;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<SearchItems> results;

    public MyAdapter(List<SearchItems> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SearchItems item = results.get(position);
        holder.nameTextView.setText(item.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform action when the item is clicked
                // You can access the selected item using the position parameter
                SearchItems selectedItem = results.get(position);
                String selectedName = selectedItem.getName();

                // Call your desired method or perform the necessary action with the selected item
                performAction(selectedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView5);
        }
    }

    private void performAction(SearchItems item) {
        // Implement your desired action here
        // This method will be called when an item is clicked
        String link = item.getLink();
        Character type = item.getType();
        switch (type) {
            case 'v':
                // Video
                return;
            case 'a':
                // Audio
                return;
            case 'q':
                // quiz
                return;
            case 'e':
                // ebook
                return;
            default:
                //Error
        }
    }
}


