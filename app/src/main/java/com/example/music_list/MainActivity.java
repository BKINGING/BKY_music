package com.example.music_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String EXTRA_MESSAGE = "it.uibe.edu.cn.myapplication.MESSAGE";
    EditText edt1;
    EditText edt2;
    Button btn;
    Toast mtoast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt1=(EditText)findViewById(R.id.edit1);
        edt2=(EditText)findViewById(R.id.edit2);
        btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if (edt1.getText().toString().equals("")||edt2.getText().toString().equals("")) {
            showToast("用户名或密码不能为空！");
        }else if(edt1.getText().toString().equals("BKY123456")&&edt2.getText().toString().equals("BKY123456")){
            showToast("登录成功！！！");
            Intent intent=new Intent(this,MainInterface.class);
            EditText editText=(EditText)findViewById(R.id.edit1);
            String message=editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE,message);
            startActivity(intent);
        }else{
            showToast("用户名或密码错误！");
        }
    }
    private void showToast(String msg){
        mtoast=Toast.makeText(this,msg,Toast.LENGTH_SHORT);
        mtoast.show();
    }

}
