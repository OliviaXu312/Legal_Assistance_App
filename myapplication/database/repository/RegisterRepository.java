package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.RegisterModel;

import org.bson.types.ObjectId;

import java.util.List;

public interface RegisterRepository {
    //添加文档
    void create(RegisterModel user);

    //更新文档
    void update(RegisterModel user);

    //检索所有文档
    List<RegisterModel> findAll();

    //以id检索文档
    RegisterModel findById(ObjectId code);

    //以关键字检索
    List<RegisterModel> findByCondition(String keyWord);

}
