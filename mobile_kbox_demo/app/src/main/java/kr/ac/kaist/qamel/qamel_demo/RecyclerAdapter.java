package kr.ac.kaist.qamel.qamel_demo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    ArrayList<RecyclerItem> items;

    public RecyclerAdapter(ArrayList<RecyclerItem> items){
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.itemView.setText(items.get(position).getContent());

        if (items.get(position).isHighlighted()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffff00"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<RecyclerItem> getItems() { return items; }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView itemView;

        public ItemViewHolder(View itemLayout) {
            super(itemLayout);

            itemView = (TextView) itemLayout.findViewById(R.id.itemView);
        }
    }
}
