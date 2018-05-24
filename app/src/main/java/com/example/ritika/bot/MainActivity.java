package com.example.ritika.bot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ConversationService conversationService=new ConversationService("2017-05-26" ,getString(R.string.username),getString(R.string.password));
        final TextView convo=(TextView)findViewById(R.id.convo);
        final EditText user_input=(EditText) findViewById(R.id.user_input);
        user_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    final String text=user_input.getText().toString();
                    convo.append(
                            Html.fromHtml("<p><b>You:</b>"+text+"</p>")
                    );
                    user_input.setText("");
                    MessageRequest messageRequest=new MessageRequest.Builder().inputText(text).build();
                    conversationService.message(getString(R.string.workspace),messageRequest).enqueue(new ServiceCallback<MessageResponse>() {
                        @Override
                        public void onResponse(MessageResponse response) {
                            final String output_text=response.getText().get(0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    convo.append(
                                            Html.fromHtml("<p><b>BOT:</b>"+output_text+"</p>")
                                    );
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });

                }
                return false;
            }
        });

    }
}
