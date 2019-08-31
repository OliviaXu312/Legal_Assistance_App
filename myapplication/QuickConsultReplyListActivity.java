package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.QuickConsultModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.bson.types.ObjectId;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class QuickConsultReplyListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<QuickConsultModel.Reply> data;
    private String quick_id, parent_id, author_name;
    private int parent_index;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private LinearLayout ll;
    private Button onlyText;
    private ImageView back;
    private SharedPreferences sp;
    private List<View> comments;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_consult_reply_list);

        initItems();

    }

    private void initItems() {

        Intent intent = getIntent();

        quick_id = intent.getStringExtra("quick_id");
        parent_id = intent.getStringExtra("reply_id");
        parent_index = intent.getIntExtra("reply_index", -1);
        author_name = intent.getStringExtra("author_name");

        ll = findViewById(R.id.quick_consult_reply_list_list);
        onlyText = findViewById(R.id.quick_consult_reply_list_timetext);
//        onlyImage = findViewById(R.id.quick_consult_reply_list_timepic);
        title = findViewById(R.id.quick_consult__reply_list_title);
        back = findViewById(R.id.quick_consult_reply_list_back);

        data = new ArrayList<>();
        alert = new AlertDialog.Builder(QuickConsultReplyListActivity.this);
        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);
        comments = new ArrayList<>();

        onlyText.setOnClickListener(this);
        back.setOnClickListener(this);

        getData();

    }

    private void setData() {

        title.setText(author_name + "的評論回復");

        if (data.size() == 0){

            onlyText.setVisibility(View.INVISIBLE);
//            onlyImage.setVisibility(View.INVISIBLE);
            TextView tv = new TextView(this);
            tv.setText("這條評論下面沒有回復誒，它真的很寂寞啊。");
            tv.setPadding(16,16,16,16);
            tv.setTextSize(80);
            tv.setGravity(Gravity.CENTER);
            ll.addView(tv);

        }

    }

    public void getData() {

        x.Ext.init(getApplication());
        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/getQuickConsultReplyList.action");
            params.addQueryStringParameter("quick_id", quick_id);
            params.addQueryStringParameter("reply_id", parent_id);
            params.addQueryStringParameter("reply_index", String.valueOf(parent_index));
            System.out.println(params.toString());
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                    JsonArray comments = jsonObject.getAsJsonArray("lawyer_reply");
                    for (JsonElement comment: comments){
                        QuickConsultModel what = new QuickConsultModel();
                        QuickConsultModel.Reply reply = what.new Reply();
                        JsonObject temp = comment.getAsJsonObject();
                        reply.setIndex(temp.get("index").getAsInt());
//                            ObjectId tempid = new ObjectId(temp.get("author").getAsString());
                        reply.setAuthor(temp.get("author").getAsString());
//                            System.out.println(reply.getAuthor());
                        reply.setAuthor_name(temp.get("author_name").getAsString());
                        reply.setDate((temp.get("create_time").toString().replace("T", " ")));
                        reply.setContent(temp.get("content").getAsString());
                        reply.setIs(temp.get("is_lawyer_author").getAsBoolean());
                        JsonArray ror = temp.getAsJsonArray("replies");
                        List<String> rsor = new ArrayList<>();
//                            int count = 0;
                        for (JsonElement r: ror){
                            String rr = r.getAsString();
                            rsor.add(rr);
                        }
                        reply.setReplies(rsor);
                        reply.setParent(new ObjectId(temp.get("parent").getAsString()));
                        reply.setId(new ObjectId(temp.get("reply_id").getAsString()));
                        reply.setLike(temp.get("like").getAsInt());
                        reply.setParent_index(temp.get("parent_index").getAsInt());
                        data.add(reply);
                    }

                    initCommentList();
                    setData();
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (Exception e) {

        }
    }

    private void initCommentList() {

        ll.removeAllViews();
        if (onlyText.getText().equals(getResources().getString(R.string.quick_consult_reply_list_upright))){
            for (QuickConsultModel.Reply reply: data) initOneComment(reply);
        }else if(onlyText.getText().equals(getResources().getString(R.string.quick_consult_reply_list_downright))){
            for (int i = data.size() - 1; i >= 0; i --) initOneComment(data.get(i));
        }

    }

    private void initOneComment(final QuickConsultModel.Reply reply){

            final View view = View.inflate(this, R.layout.sample_quick_consult_single_comment, null);
            Button image, like, comment, delete;
            TextView content, time, replies, favorites, name;

            comment = view.findViewById(R.id.quick_consult_result_single_reply);

            content = view.findViewById(R.id.quick_consult_result_single_content);
            time = view.findViewById(R.id.quick_consult_result_single_time);
            replies = view.findViewById(R.id.quick_consult_result_single_replies);
            favorites = view.findViewById(R.id.quick_consult_result_single_favorites);
            name = view.findViewById(R.id.quick_consult_result_single_user_name);
            delete = view.findViewById(R.id.quick_consult_result_single_delete);
            content.setText(reply.getContent());
            time.setText(reply.getDate().replace("\"", ""));
            replies.setText(reply.getReplies().size() + " 條回復");
            favorites.setText(String.valueOf(reply.getLike()));
            name.setText(reply.getAuthor_name());

            replies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(QuickConsultReplyListActivity.this, QuickConsultReplyListActivity.class);
                    intent.putExtra("quick_id", quick_id);
                    intent.putExtra("reply_id", reply.getId());
                    intent.putExtra("reply_index", reply.getIndex());
                    startActivity(intent);

                }
            });

            if (reply.getAuthor().toString().equals(sp.getString("_id", "notValid"))){
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View viewN) {
                        alert.setMessage("確定要刪除這條評論嗎？");
                        alert.setPositiveButton("是的！", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ll.removeView(view);
                                deleteComment(reply.getId().toString(), reply.getIndex(), reply.getParent_index(), reply.getParent().toString());
                            }
                        });
                        alert.setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.cancel();
                            }
                        });
                        dialog = alert.create();
                        dialog.show();
                    }
                });
            }else{
                delete.invalidate();
                delete.setTextColor(0xFFFFFF);
            }

//            like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//
//                }
//            });

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(QuickConsultReplyListActivity.this, QuickConsultResultReplyActivity.class);
                    intent.putExtra("index", reply.getIndex());
                    intent.putExtra("name", reply.getAuthor_name());
                    intent.putExtra("parent_id", reply.getId().toString());
                    intent.putExtra("quick_id", quick_id);
                    startActivity(intent);
                }
            });
            ll.addView(view);
            comments.add(view);

    }

    private void deleteComment(final String reply_id, final int reply_index, final int parent_index, final String parent_id) {
        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/deleteQuickConsultComment.action");
            params.addQueryStringParameter("quick_id", quick_id);
            params.addQueryStringParameter("reply_index", String.valueOf(reply_index));
            params.addQueryStringParameter("reply_id", reply_id);
            params.addQueryStringParameter("parent_index", String.valueOf(parent_index));
            params.addQueryStringParameter("parent_id", parent_id);
            params.setMaxRetryCount(0);
            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                    if (jsonObject.get("state").getAsInt() != 1) {
                        alert.setMessage("好像出了點問題啊？");
                        alert.setPositiveButton("再刪一次！", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteComment(reply_id, reply_index, parent_index, parent_id);
                            }
                        });
                        alert.setNegativeButton("算了放過它吧", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.cancel();
                            }
                        });
                        dialog = alert.create();
                        dialog.show();
                    }else {
                        getData();
                    }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {

                    System.out.println("OnError");

                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.quick_consult_reply_list_timetext:
                if (onlyText.getText().equals(getResources().getString(R.string.quick_consult_reply_list_upright))) {
                    onlyText.setText(getResources().getString(R.string.quick_consult_reply_list_downright));
//                    onlyImage.setBackground(getResources().getDrawable(R.drawable.time_up_l));
                } else if (onlyText.getText().equals(getResources().getString(R.string.quick_consult_reply_list_downright))) {
                    onlyText.setText(getResources().getString(R.string.quick_consult_reply_list_upright));
//                    onlyImage.setBackground(getResources().getDrawable(R.drawable.time_down_l));
                }
                initCommentList();
                break;
            case R.id.quick_consult_reply_list_back:
                finish();
                break;
        }
    }
}
