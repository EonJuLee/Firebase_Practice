package com.example.dbapp02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private String confirmed="";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    EditText editID,editPW,enterID,enterPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        editID=(EditText)findViewById(R.id.editID);
        editPW=(EditText)findViewById(R.id.editPW);
        enterID=(EditText)findViewById(R.id.enterID);
        enterPW=(EditText)findViewById(R.id.enterPW);

    }

    public void chkUser(){
        final String id=editID.getText().toString().trim();
        if(id.equals("")){
            showMessage("아이디를 입력하세요");
        }
        else{
            myRef.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        showMessage(id+"는 중복된 아이디입니다");
                        editID.setText("");
                    }
                    else{
                        confirmed=id;
                        showMessage("사용가능한 아이디입니다");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showMessage("인터넷 연결을 확인하세요");
                }
            });
        }
    }

    public void addUser(){
        final String id=editID.getText().toString().trim();
        final String pw=editPW.getText().toString().trim();
        if(id.equals("")){
            showMessage("아이디를 입력하세요");
        }
        else if(id.equals(confirmed)==false){
            showMessage("아이디 중복 검사가 필요합니다");
        }
        else if(pw.equals("")){
            showMessage("비밀번호를 입력하세요");
        }
        else{
            User user=new User(id,pw);
            myRef.child("Users").child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showMessage(id+"의 계정을 생성했습니다");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showMessage("인터넷 연결 상태를 확인하세요");
                }
            });
            editID.setText("");
            editPW.setText("");
        }
    }

    public void loginUser(){
        final String id=enterID.getText().toString().trim();
        final String pw=enterPW.getText().toString().trim();
        if(id.equals("")){
            showMessage("아이디를 입력하세요");
        }
        else if(pw.equals("")){
            showMessage("비밀번호를 입력하세요");
        }
        else{
            myRef.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        User user=dataSnapshot.getValue(User.class);
                        if(user.getPW().equals(pw)){
                            showMessage("로그인되었습니다");
                        }
                        else{
                            showMessage("비밀번호 오류입니다");
                        }
                    }
                    else{
                        showMessage("없는 아이디입니다");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showMessage("인터넷 연결 상태를 확인하세요");
                }
            });
        }
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnchk:
                chkUser();
                break;
            case R.id.btnadd:
                addUser();
                break;
            case R.id.btnlogin:
                loginUser();
                break;
        }
    }

    public void showMessage(String contents){
        Toast.makeText(MainActivity.this,contents,Toast.LENGTH_SHORT).show();
    }
}
