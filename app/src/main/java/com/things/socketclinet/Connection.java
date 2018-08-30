package com.things.socketclinet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

import static android.content.ContentValues.TAG;
import static android.os.Environment.DIRECTORY_DCIM;

/**
 * Created by zzy on 2017/12/7 0007.
 */

public class Connection {

//    public static Connection connection;
//    public static Connection getInstance(){
//        if (connection == null){
//            connection = new Connection();
//        }
//        return connection;
//    }

    public static  boolean CONNECTION_STATE=false;
    private Socket socket = null;
    public InputStream is = null;
    private OutputStream outputStream;

    private mListener listener;

    public Connection(mListener listener) {
        this.listener = listener;
    }

    public  void getConnect(final String IP, final int PORT, final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new Socket(IP, PORT);
                        if (socket.isConnected()) {
                            CONNECTION_STATE = true;
                            Log.i(TAG,"CONNECTION  SOCKET"+"  "+true);
                            while (true){
                                getMessage();
                            }
                        } else {
                            CONNECTION_STATE = false;
                            Log.i(TAG,"CONNECTION  SOCKET"+"  "+false);
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getMessage() throws IOException {
    //本方法本身已经在由上一方法开启的子线程中运行
        Log.i(TAG, "get message……");
        DataInputStream dataInput = new DataInputStream(socket.getInputStream());
        String file = MainActivity.path;
//        String file = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)+"/123.jpg";
//        File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)+"/123.jpg");
        int size = dataInput.readInt();
        byte[] data = new byte[size];
        int len = 0;
        while (len < size) {
            len += dataInput.read(data, len, size - len);
        }
//        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        FileOutputStream outPut= new FileOutputStream(file);
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outPut);
        Log.i("123","接收完毕");
//        listener.setImageView(bmp);
//        showImage(file);
        MainActivity.handler.sendEmptyMessage(1);


/*
字符串数据转换为int数组形式
 */
//        String[] arrss=new String[40];
//        String ss= result.toString();
//        arrss=ss.split(" ");
//        intarr=new int[arrss.length];
//        for (int i=0;i<arrss.length;i++){
//            intarr[i]=Integer.parseInt(arrss[i]);
//            Log.i(TAG,intarr[i]+"   Int类型结果");
//        }
//        Intent intent=new Intent("HelloNewMessage");
//        context.sendBroadcast(intent);
//        Log.i(TAG,"广播后");

    }

    public void showImage(String path){
        if (fileIsExists(path)){
//            Bitmap bitmap = BitmapFactory.decodeFile(path);
//            listener.setImageView();

        }
    }


    public  void sendMessage(final String sendData){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream=socket.getOutputStream();
                    //数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                    outputStream.write(sendData.getBytes());
                    outputStream.flush();
//                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void disconnect(){
        try {
            socket.close();
            if(socket.isConnected()){
                Log.i(TAG,"断开失败！");
            }else {
                CONNECTION_STATE=false;
                Log.i(TAG,"断开成功！");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
