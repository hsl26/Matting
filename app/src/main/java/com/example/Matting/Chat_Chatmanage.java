package com.example.Matting;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Chat_Chatmanage {
    static void addNewChatRoom(String chatRoomId, User user) {
        //채팅방 유저목록에 추가
        DatabaseReference chatdb;
        chatdb = FirebaseDatabase.getInstance().getReference().child("chatroomlist").child(chatRoomId).child("users");
        Set<String> userSet = new HashSet<>();
        userSet.add(user.getUserId());
        chatdb.setValue(new ArrayList<>(userSet));

        //유저의 채팅참여 목록 추가
        DatabaseReference db;
        db = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUserId());

        // 기존 chats 목록을 가져와서 새로운 chatRoomId 추가
        db.child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Set으로 채팅방 ID 관리 (중복 방지)
                Set<String> chatSet = new HashSet<>();
                if (snapshot.exists()) {
                    for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                        String existingChatRoomId = chatSnapshot.getValue(String.class);
                        if (existingChatRoomId != null) {
                            chatSet.add(existingChatRoomId);
                        }
                    }
                }
                // 새로운 채팅방 ID 추가
                chatSet.add(chatRoomId);

                // Firebase에 저장
                db.child("chats").setValue(new ArrayList<>(chatSet)); // Firebase는 배열 형식으로 저장
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러 처리
            }
        });
    }

}
