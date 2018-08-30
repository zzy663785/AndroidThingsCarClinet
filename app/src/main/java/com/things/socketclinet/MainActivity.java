package com.things.socketclinet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DCIM;

public class MainActivity extends AppCompatActivity {

    public static String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)+"/123.jpg";
    public static Handler handler;
    EditText SendText;
    Button Con_Button;
    Button Send_Button;
    Button Takephoto;
    EditText IpText;
    EditText PortText;
    ImageButton ForwardButt;
    ImageButton BackButt;
    ImageButton LeftButt;
    ImageButton RightButt;

    ImageView imageView;
    boolean GoFoward = false;
    boolean GoBack = false;
    boolean TurnLeft = false;
    boolean TurnRight = false;
    boolean stop = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SendText = findViewById(R.id.send_Text);
        Con_Button = findViewById(R.id.connect_But);
        Send_Button = findViewById(R.id.send_But);
        IpText = findViewById(R.id.IPText);
        PortText = findViewById(R.id.PORTText);

        Takephoto = findViewById(R.id.takepicture);
        imageView = findViewById(R.id.ImageView);

        ForwardButt = findViewById(R.id.Forward);
        BackButt = findViewById(R.id.Back);
        LeftButt = findViewById(R.id.Left);
        RightButt = findViewById(R.id.Right);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==1){
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imageView.setImageBitmap(bitmap);
                }
            }
        };

        final Connection connection = new Connection(new mListener() {
            @Override
            public void ChangeDirection() {

            }

            @Override
            public void setImageView() {
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                imageView.setImageBitmap(bitmap);
            }
        });

        Takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.sendMessage("takepicture"+"\n");
            }
        });

        ForwardButt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        connection.sendMessage("stop"+"\n");
                        connection.sendMessage("forward"+"\n");
                        //按下了按钮
                        break;
                    case MotionEvent.ACTION_UP:
                        connection.sendMessage("stop"+"\n");
                        //松开了按钮
                        break;
                        default:
                            break;
                }
                return false;//如果还有下一个ForwadButt监听事件，则下一个还能继续监听到 反之true则不能
            }
        });

        BackButt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        connection.sendMessage("stop"+"\n");
                        connection.sendMessage("back"+"\n");
                        Log.i("123","按下了后退按键");
                        //按下了按钮
                        break;
                    case MotionEvent.ACTION_UP:
                        connection.sendMessage("stop"+"\n");
                        Log.i("123","松开了后退按键");
                        //松开了按钮
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        LeftButt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        connection.sendMessage("stop"+"\n");
                        connection.sendMessage("left"+"\n");
                        //按下了按钮
                        break;
                    case MotionEvent.ACTION_UP:
                        connection.sendMessage("stop"+"\n");
                        connection.sendMessage("stop"+"\n");
                        //松开了按钮
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        RightButt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        connection.sendMessage("stop"+"\n");
                        connection.sendMessage("right"+"\n");
                        //按下了按钮
                        break;
                    case MotionEvent.ACTION_UP:
                        connection.sendMessage("stop"+"\n");
//                        Connection.getInstance().sendMessage("back"+"\n");
                        //松开了按钮
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        Con_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String IP=IpText.getText().toString();
                Log.i("123",IP);
                int Port=Integer.valueOf(PortText.getText().toString());
                Log.i("123",Port+" ");
                connection.getConnect(IP,Port,MainActivity.this);
            }
        });

        Send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String text= SendText.getText().toString();
//                Log.i("123",text);
//                connection.sendMessage(text+"\n");
            }
        });
    checkPermission();
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("123", "checkPermission: 已经授权！");
        }
    }


}
