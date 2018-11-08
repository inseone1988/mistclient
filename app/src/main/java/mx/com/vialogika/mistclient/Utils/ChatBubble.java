package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import mx.com.vialogika.mistclient.Comment;
import mx.com.vialogika.mistclient.R;

public class ChatBubble extends LinearLayout {

    private String user,messageText,timestampText;
    private int messageType = Messages.MESSAGE_INBOUND;


    private LayoutParams textViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    private TextView title,message,timestamp;
    private Context ctx;


    public ChatBubble(Context context,int type){
        super(context);
        this.ctx = context;
        this.messageType = type;
        setLayoutParams();
        initTextViews();
    }

    public ChatBubble(Context context, AttributeSet attributeSet,int defStyleAttr){
        super(context,attributeSet,defStyleAttr);
        setLayoutParams();
        initTextViews();
    }

    private void initTextViews(){
        bubbleChatTitle();
        bubbleChatMessage();
        bubbleChatTimestamp();
        this.addView(title);
        this.addView(message);
        this.addView(timestamp);
    }

    private void setLayoutParams(){
        LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(5,5,5,5);
        this.setLayoutParams(llParams);
        this.setOrientation(VERTICAL);
        this.setPadding(35,10,35,10);
        if(messageType == Messages.MESSAGE_INBOUND){
            this.setBackground(getContext().getDrawable(R.drawable.shape_bg_incoming_bubble));
        }else{
            this.setBackground(getContext().getDrawable(R.drawable.shape_bg_outgoing_bubble));
            this.setGravity(Gravity.RIGHT);
        }
    }

    private void bubbleChatTitle(){
        title = new TextView(ctx);
        title.setText(R.string.example_username);
        title.setTypeface(null,Typeface.BOLD_ITALIC);

        if(messageType == Messages.MESSAGE_INBOUND){
            title.setPadding(25,0,0,0);
            title.setGravity(Gravity.START);
        }else{
            title.setPadding(0,0,25,0);
            title.setGravity(Gravity.END);
        }
    }

    private void bubbleChatMessage(){
        LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,5,10,5);
        message = new TextView(ctx);
        message.setLayoutParams(params);
        if (messageType == Messages.MESSAGE_INBOUND){
            message.setGravity(Gravity.START);
        }else {
            message.setGravity(Gravity.END);
        }
        message.setText(R.string.example_message);
    }

    private void bubbleChatTimestamp(){
        LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,0,15,0);
        timestamp = new TextView(ctx);
        timestamp.setLayoutParams(params);
        timestamp.setText(R.string.example_timestamp);
        timestamp.setTextSize(10);
        timestamp.setTextColor(getResources().getColor(R.color.colorAccent));
        if (messageType == Messages.MESSAGE_INBOUND){
            timestamp.setGravity(Gravity.START);
        }else {
            timestamp.setGravity(Gravity.END);
        }
    }

    public void setMessageTexts(String mUser,String mMessage,String mTimestamp){
        this.title.setText(mUser);
        this.message.setText(mMessage);
        this.timestamp.setText(mTimestamp);

    }

    public void setMessageTexts(Comment comment){
        this.title.setText(comment.getUserName());
        this.message.setText(comment.getCommentContent());
        this.timestamp.setText(comment.getCommentTimestamp());
    }
}
