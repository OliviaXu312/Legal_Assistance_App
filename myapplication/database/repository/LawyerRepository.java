package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.LawyerModel;

import org.bson.types.ObjectId;

import java.util.List;

public interface LawyerRepository {
    //添加文档
    void create(LawyerModel user);

    //更新文档
    void update(LawyerModel user);

    //检索所有文档
    List<LawyerModel> findAll();

    //以id检索文档
    LawyerModel findById(ObjectId code);

    //以关键字检索
    List<LawyerModel> findByCondition(String keyWord);
}
