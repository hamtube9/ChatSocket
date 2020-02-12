package com.haiph.chatsocket;

import android.content.Context;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    Context context;
    List<Chat> list;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ChatAdapter(Context context, List<Chat> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getItemViewType(int position) {
        String name =list.get(position).name;
        if(name.equals("Lmao")){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
            return new ChatHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
            return new ChatHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
    holder.tvUser.setText(list.get(position).getName());
    holder.tvChat.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView tvUser,tvChat;



        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            tvChat=itemView.findViewById(R.id.tvComment);
            tvUser=itemView.findViewById(R.id.tvUser);

        }
    }
}
