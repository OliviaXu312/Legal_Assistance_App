package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.QuickConsultModel;
import com.example.joan.myapplication.database.repository.QuickResponseRepositoryImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class QuickConsultResultActivity extends AppCompatActivity implements View.OnClickListener {

//    private List<Fragment> list;
//    private ViewPager pager;
//    private TabLayout tabLayout;
    private LinearLayout ll;
    private QuickConsultModel data;
    private String id;
    private TextView content, name, time, view;
    private CheckBox only;
    private List<View> comments;
    //    private List<Integer> likeList;
    private Button onlyText, onlyImage, reply, like;
    private ImageView back;
    private SharedPreferences sp;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_consult_result);

//        initBase();
        initItems();
        getData();

    }

//    private void initBase() {
//    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus){
//            getData();
//        }else{
//        }
//
//    }

    private void initItems() {

        comments = new ArrayList<>();

        ll = findViewById(R.id.quick_consult_result_comment_list);
        only = findViewById(R.id.quick_consult_result_only);
        onlyText = findViewById(R.id.quick_consult_result_time_text);
        onlyImage = findViewById(R.id.quick_consult_result_time_image);
        reply = findViewById(R.id.quick_consult_result_reply);
        like = findViewById(R.id.quick_consult_result_like);
        back = findViewById(R.id.quick_consult_result_back);

        alert = new AlertDialog.Builder(QuickConsultResultActivity.this);

        onlyText.setOnClickListener(this);
        onlyImage.setOnClickListener(this);
        reply.setOnClickListener(this);
        like.setOnClickListener(this);
        back.setOnClickListener(this);

        data = new QuickConsultModel();

    }

    private void getData() {
        x.Ext.init(getApplication());
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
//        id = "5b6eaad08d35692c10ea06d1";
        final int[] type = new int[1];

        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/getQuickConsultResult.action");
            params.addQueryStringParameter("id", id);
            params.setMaxRetryCount(0);
            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                    System.out.println(jsonObject);
                    type[0] = jsonObject.get("state").getAsInt();

                    if (type[0] == 1) {JsonObject jo = jsonObject.getAsJsonObject("data");

                        data = new QuickResponseRepositoryImpl().convert(jo);

                        initMain();
                        initCommentList();
                        copyToBack();

                    } else {
                        onError(new Throwable(), false);
                    }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {

                    alert.setMessage("好像出了點問題啊？");
                    alert.setPositiveButton("再試一次！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                        }
                    });
                    alert.setNegativeButton("返回算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    dialog = alert.create();
                    dialog.show();

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

    private void initMain() {

        content = findViewById(R.id.quick_consult_result_context);
        name = findViewById(R.id.quick_consult_result_name);
        view = findViewById(R.id.quick_consult_result_view);
        time = findViewById(R.id.quick_consult_result_time);

        content.setText(data.getContent());
        name.setText(data.getAuthor_name());
        time.setText(data.getDate().replace("\"", ""));
        view.setText(String.valueOf(data.getView_count()) + getResources().getString(R.string.quick_consult_result_viewtime));

        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);

        only.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                initCommentList();
            }
        });

    }

    private void initCommentList() {

        ll.removeAllViews();
        if (onlyText.getText().equals(getResources().getString(R.string.quick_consult_result_upright))){
            for (QuickConsultModel.Reply reply: data.getLawyer_reply()) initOneComment(reply);
        }else if(onlyText.getText().equals(getResources().getString(R.string.quick_consult_result_downright))){
            for (int i = data.getLawyer_reply().size() - 1; i >= 0; i --) initOneComment(data.getLawyer_reply().get(i));
        }

    }

    private void initOneComment(final QuickConsultModel.Reply reply){

        if (reply.isIs() || (!only.isChecked())) {
            final View view = View.inflate(this, R.layout.sample_quick_consult_single_comment, null);
            Button image, like, comment, delete;
            TextView content, time, replies, favorites, name;

//            image = view.findViewById(R.id.quick_consult_result_single_user_image);
//            like = view.findViewById(R.id.quick_consult_result_single_like);
            comment = view.findViewById(R.id.quick_consult_result_single_reply);
            content = view.findViewById(R.id.quick_consult_result_single_content);
            time = view.findViewById(R.id.quick_consult_result_single_time);
            replies = view.findViewById(R.id.quick_consult_result_single_replies);
//            favorites = view.findViewById(R.id.quick_consult_result_single_favorites);
            name = view.findViewById(R.id.quick_consult_result_single_user_name);
            delete = view.findViewById(R.id.quick_consult_result_single_delete);
            content.setText(reply.getContent());
            time.setText(reply.getDate().replace("\"", ""));
            replies.setText(reply.getReplies().size() + " 條回復");
//            favorites.setText(String.valueOf(reply.getLike()));
            System.out.println(reply.getAuthor_name());
            name.setText(reply.getAuthor_name());

            replies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(QuickConsultResultActivity.this, QuickConsultReplyListActivity.class);
                    intent.putExtra("quick_id", data.getId().toString());
                    intent.putExtra("reply_id", reply.getId().toString());
                    intent.putExtra("reply_index", reply.getIndex());
                    intent.putExtra("author_name", reply.getAuthor_name());
                    startActivity(intent);

                }
            });

//            image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//            System.out.println(reply.getAuthor());
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
                delete.setVisibility(View.INVISIBLE);
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
                    finish();
                    Intent intent = new Intent(QuickConsultResultActivity.this, QuickConsultResultReplyActivity.class);
                    intent.putExtra("index", reply.getIndex());
                    intent.putExtra("name", reply.getAuthor_name());
                    intent.putExtra("parent_id", reply.getId().toString());
                    intent.putExtra("quick_id", id);
                    startActivity(intent);
                    finish();
                }
            });

            ll.addView(view);
            comments.add(view);
        }

    }

    private void deleteComment(final String reply_id, final int reply_index, final int parent_index, final String parent_id) {
        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/deleteQuickConsultComment.action");
            params.addQueryStringParameter("quick_id", id);
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

        switch (view.getId()){

            case R.id.quick_consult_result_time_image:
            case R.id.quick_consult_result_time_text:
                if (onlyText.getText().equals(getResources().getString(R.string.quick_consult_result_upright))){
                    onlyText.setText(getResources().getString(R.string.quick_consult_result_downright));
                    onlyImage.setBackground(getResources().getDrawable(R.drawable.time_up_l));
                }else if(onlyText.getText().equals(getResources().getString(R.string.quick_consult_result_downright))){
                    onlyText.setText(getResources().getString(R.string.quick_consult_result_upright));
                    onlyImage.setBackground(getResources().getDrawable(R.drawable.time_down_l));
                }
                initCommentList();
                break;
            case R.id.quick_consult_result_reply:
                Intent intent = new Intent(QuickConsultResultActivity.this, QuickConsultResultReplyActivity.class);
                intent.putExtra("index", -1);
                intent.putExtra("name", data.getAuthor_name());
                intent.putExtra("parent_id", data.getId().toString());
                intent.putExtra("quick_id", id);
                startActivity(intent);
                finish();
                break;
            case R.id.quick_consult_result_back:
                finish();
                break;
        }

    }

    private void copyToBack() {

        // for ()

    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus){
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//        } else {
//        }
//    }

}
