package com.haiph.chatsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh");
        } catch (URISyntaxException e) {}
    }
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference();

    private RecyclerView rcView;
    private LinearLayoutManager manager;
    private List<Chat> list;
    private ChatAdapter adapter;
    private EditText edtChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
       mSocket.on("new message", onNewMessage);

        mSocket.connect();
        mSocket.emit("add user", "Hai bi");
        mSocket.on("login",onNewMessage);

    edtChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i==4){
        attemptSend();
        }
        return true;
    }
});


    }

    private void initView() {
        rcView=findViewById(R.id.rcMain);
        edtChat=findViewById(R.id.edtChat);
        manager=new LinearLayoutManager(getApplicationContext());
        list = new ArrayList<>();
        adapter=new ChatAdapter(getApplicationContext(),list);
        rcView.setAdapter(adapter);
        rcView.setLayoutManager(manager);

    }


    private void attemptSend() {
        String message = edtChat.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        edtChat.setText("");
        Chat chat=new Chat();
        chat.name = "Lmao";
        chat.message=message;
        list.add(chat);
        adapter.notifyDataSetChanged();
        rcView.smoothScrollToPosition(list.size()-1);
        mSocket.emit("new message", message);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;

                    try {
                        username = data.getString("username");
                        message = data.getString("message");

                        Chat chat=new Chat();
                        chat.setMessage(message);
                        chat.setName(username);
                        list.add(chat);
                        adapter.notifyDataSetChanged();
                        rcView.smoothScrollToPosition(list.size() -1);
                    } catch (JSONException e) {
                        return;
                    }


                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
