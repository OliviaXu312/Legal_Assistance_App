package com.example.joan.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joan.myapplication.DIYComponent.NoScrollGridView;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.fragment.NinePicturesAdapter;
import com.example.joan.myapplication.image.ImagePagerActivity;
import com.example.joan.myapplication.MyApplication;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class CaseConsultActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next;//, adConfirm, adCancel;
    private ImageView back;
    private EditText text;
    private AlertDialog finished;
    private int length = -1, state, submitState;
    private boolean isSubmitted;
    private SharedPreferences sp;
    private String id, content;
    private int result;
    private SimpleDateFormat timeFormat;
    private SharedPreferences.Editor editor;
    private AlertDialog.Builder alert;
    private AlertDialog dialogTwo;
    private Date time;

    //photo picker
    private static final int REQUEST_CODE = 100;
    private NoScrollGridView itemLayout;
    private ArrayList<String> photos = new ArrayList<>();
    private List<String> photossss;
    private NinePicturesAdapter ninePicturesAdapter;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    //upload
    private JSONArray encodedString = new JSONArray();
    private Bitmap bitmap;
    //進度條元件
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_consult);

        initItems();
        CaseConsultActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initItems(){

        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);
        editor = sp.edit();

//        System.out.println(sp.getBoolean("login", false));
//        if (!sp.getBoolean("login", false)) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }

        timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        next = findViewById(R.id.case_consult_next);
        back = findViewById(R.id.case_consult_back);
        text = findViewById(R.id.case_consult_text);

        submitState = -1;
        time = new Date();
        isSubmitted = false;

        x.Ext.init(getApplication());

        loadSave();
        pickPicture();
        setListener();
    }

    private void loadSave() {

        if (!sp.getString("tempCaseConsultSave", "nothing").equals("nothing"))

            text.setText(sp.getString("tempCaseConsultSave", "nothing"));

    }

    private void setListener(){

        back.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.case_consult_next:
                encodeImagetoString();
                break;
            case R.id.case_consult_back:
                goBack();
                break;
        }
    }

    private void setNext() {
        if (isEnoughLength()) {
            submit();
        }else{
            setNotEnough();
        }
    }

    private int setNotEnough() {
        finished = new AlertDialog.Builder(this)
                .setTitle("字數不夠喔~")//设置对话框的标题
                .setMessage("最少字數需要" + length + "才可以提交！")//设置对话框的内容
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finished.cancel();
                    }
                })
                .create();
        finished.show();
        Button confirm = finished.getButton(DialogInterface.BUTTON_POSITIVE);;
        confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
        return -2;
    }

    private int confirmBack(){
        finished = new AlertDialog.Builder(this)
                .setTitle("確定要退出嗎")//设置对话框的标题
                .setMessage("距離獲得幫助已經不遠了！現在退出您已經輸入的信息可能會丟失喔！")//设置对话框的内容
                .setPositiveButton("確定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        overridePendingTransition(R.anim.left, R.anim.left_exit);
                    }
                })
                .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finished.cancel();
                    }
                })
                .create();
        return 1;
    }

    private boolean saveContent(){

        boolean success = false;

        content = text.getText().toString();
        Set<String> set1 = sp.getStringSet("caseConsultIdList", null);
        if (set1 == null) set1 = new TreeSet<>();
        System.out.println(id);
        set1.add(id);
        editor.putStringSet("caseConsultIdList", set1);
        editor.putString(id, content);
        editor.commit();

        if (sp.getString(id, null).equals(content)) success = true;

        return success;

    }

    private boolean tempSave(){

        boolean success = false;

        content = text.getText().toString();
        editor.putString("tempCaseConsultSave", content);
        editor.commit();

        if (sp.getString("tempCaseConsultSave", null).equals(content)) success = true;

        return success;

    }

    private void submit() {//提交 //0 成功 -1未知失败 -2储存失败 -3上傳失敗

        id = timeFormat.format(time) + sp.getString("user_name", "unknown" + time);

        submitState = saveContent()? -1: -2;
        System.out.println("BeforeStore");
        final int[] type1 = {submitState};
        while (type1[0] == -2){
            alert.setMessage("儲存失敗！是否再嘗試一次？");
            alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogTwo.cancel();
                    type1[0] = saveContent()? -1: -2;
                }
            });
            alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogTwo.cancel();
                }
            });
            dialogTwo = alert.create();
            dialogTwo.show();
            Button confirm = dialogTwo.getButton(DialogInterface.BUTTON_POSITIVE);
            Button cancel = dialogTwo.getButton(DialogInterface.BUTTON_NEGATIVE);
            confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
            cancel.setTextColor(getResources().getColor(R.color.selector_item_color));
        }
        submitState = -1;
        System.out.println("BeforeSubmit");
        final int[] type2 = {-3};
        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/getCaseResult.action");
            params.addQueryStringParameter("id", id);
            params.addQueryStringParameter("content", content);
            params.addQueryStringParameter("picturelst", encodedString.toString());
            params.addQueryStringParameter("owner", sp.getString("_id",null));
            params.setMaxRetryCount(0);
            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                    type2[0] = jsonObject.get("type").getAsInt();

                    if (type2[0] == 1) {

                        //當完成的時候，把進度條消失
                        progressBar.setProgress(100);
                        progressBar.dismiss();
                        success();
                        type2[0] = 0;

                    } else {
                        progressBar.dismiss();
                        failed();

                    }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    progressBar.dismiss();
                    failed();
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

        submitState = type2[0];

        editor.putString("tempCaseConsultSave", "nothing");

        isSubmitted = true;
        return;
    }

    private void goBack() {

        if (isEmpty()){
            finish();
            overridePendingTransition(R.anim.left, R.anim.left_exit);
        }else{
            id = timeFormat.format(time) + sp.getString("user_name", "unknown" + time);
            tempSave();
            state = confirmBack();
        }
    }

    private boolean isEnoughLength(){
        return text.getText().length() > length ? true : false;
    }

    private boolean isEmpty(){
        return text.getText().length() == 0 ? true : false;
    }

    private void failed() {
        finished = new AlertDialog.Builder(this)
                .setTitle("您的諮詢信息")//设置对话框的标题
                .setMessage("送出失敗了誒~")//设置对话框的内容
                .setPositiveButton("重試", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submit();
                    }
                })
                .setNegativeButton("再看看~", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finished.cancel();
                    }
                })
                .create();
        finished.show();
        Button back, cancel;
        back = finished.getButton(DialogInterface.BUTTON_POSITIVE);
        cancel = finished.getButton(DialogInterface.BUTTON_NEGATIVE);
        back.setTextColor(getResources().getColor(R.color.selector_item_color));
        cancel.setTextColor(getResources().getColor(R.color.selector_item_color));
    }

    private void success() {
        finished = new AlertDialog.Builder(this)
                .setTitle("您的諮詢信息")//设置对话框的标题
                .setMessage("已經成功送出咯~")//设置对话框的内容
                .setPositiveButton("查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CaseConsultActivity.this, CaseConsultResultActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        System.out.println(id);
                        overridePendingTransition(R.anim.right, R.anim.left);
                        finished.cancel();
                        finish();
                    }
                })
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finished.cancel();
                        finish();
                    }
                })
                .create();
        finished.show();
        Button no, next;
        next = finished.getButton(DialogInterface.BUTTON_POSITIVE);
        no = finished.getButton(DialogInterface.BUTTON_NEGATIVE);
        next.setTextColor(getResources().getColor(R.color.selector_item_color));
        no.setTextColor(getResources().getColor(R.color.selector_item_color));
    }

    /**
     * 每一张图片放大查看
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }


    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(CaseConsultActivity.this);
        intent.setPhotoCount(9);
        intent.setShowCamera(true);
        startActivityForResult(intent, REQUEST_CODE);

        //ImageLoaderUtils.display(context,imageView,path);
    }

    private static final String TAG = "MainActivity";

    /**
     * 接受返回的图片数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);

                for (int i = 0; i < photos.size(); i++) {
                    Log.i(TAG, "----------onActivityResult: "+ photos.get(i));
                }
                if(ninePicturesAdapter!=null) {
                    Log.i(TAG, "----------photossss: ========");
                    ninePicturesAdapter.addAll(photos);
                    photossss = ninePicturesAdapter.getData();
                    Log.i(TAG, "----------photossss: ========"+photossss.size());

                }
            }
        }
    }

    //圖片選擇適配器
    private void pickPicture(){
        itemLayout = (NoScrollGridView) findViewById(R.id.recycler_view);

       /* tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int photoCount = ninePicturesAdapter.getPhotoCount();
                Log.i(TAG, "---onClick:--- "+photoCount);

                if (photoCount>9){
                    Toast.makeText(MainActivity.this, "智能选择9张图片，才能提交", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < photoCount; i++) {
                    Log.i(TAG, "onClick: "+photos.get(i));
                }
                Log.i(TAG, "---提交:--- "+photoCount);

            }
        });*/


        ninePicturesAdapter = new NinePicturesAdapter(this, 9, new NinePicturesAdapter.OnClickAddListener() {
            @Override
            public void onClickAdd(int positin) {
//                finished.dismiss();
//                dialogTwo.dismiss();
                if (checkPermissionREAD_EXTERNAL_STORAGE(getApplicationContext())) {
                    choosePhoto();
                }
            }
        }, new NinePicturesAdapter.OnItemClickAddListener() {
            @Override
            public void onItemClick(int positin) {
                if (checkPermissionREAD_EXTERNAL_STORAGE(getApplicationContext())) {
                    Log.i(TAG, "-------------onItemClick: "+positin);

                    String[] array = new String[ninePicturesAdapter.getPhotoCount()];
                    // List转换成数组
                    for (int i = 0; i < photossss.size()-1; i++) {
                        array[i] = photossss.get(i);
                    }
                    //数组转换为集合
                    //List<String> stringsss = Arrays.asList(array);

                    Log.i(TAG, "----array:--- "+array.length);
                    imageBrower(positin,array);
                }

            }
        });
        itemLayout.setAdapter(ninePicturesAdapter);
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    this,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(getApplicationContext(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    //处理图片，压缩，转码
    @SuppressLint("StaticFieldLeak")
    public void encodeImagetoString() {

        new AsyncTask<Void , Integer , String>() {
            protected void onPreExecute() {
                //執行前 設定可以在這邊設定
                super.onPreExecute();

                progressBar = new ProgressDialog(CaseConsultActivity.this);
                if(photos.size() > 0){
                    progressBar.setMessage("圖片上傳中請稍候...");
                }else {
                    progressBar.setMessage("上傳中請稍候...");
                }
                progressBar.setCancelable(false);
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.show();
                //初始化進度條並設定樣式及顯示的資訊。
            }

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                int progress = 0;

                for(int i = 0; i < photos.size(); i++){
                    //decode to bitmap
//                    Bitmap bitmap = BitmapFactory.decodeFile(photos.get(i));
//                    Log.d(TAG, "bitmap width: " + bitmap.getWidth() + " height: " + bitmap.getHeight());
//                    //convert to byte array
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                    byte[] bytes = baos.toByteArray();
//
//                    //base64 encode
//                    byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
//                    String encodeString = new String(encode);

                    bitmap = BitmapFactory.decodeFile(photossss.get(i),options);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // 压缩图片
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    System.out.println(stream.size());
                    byte[] byte_arr = stream.toByteArray();
                    // Base64图片转码为String
                    String encode = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                    encodedString.add(encode);
                    publishProgress(progress+=(int)(100/photossss.size()));
                }
                return "";
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                //執行中 可以在這邊告知使用者進度
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
                //取得更新的進度
            }

            @Override
            protected void onPostExecute(String msg) {
                super.onPostExecute(msg);

                // 上传图片
                setNext();
            }
        }.execute(null, null, null);
    }

}
