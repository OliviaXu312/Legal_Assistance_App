package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.JudgementModel;

import org.bson.types.ObjectId;

import java.util.List;

public interface JudgementRepository {
    //检索所有文档
    List<JudgementModel> findAll();

    //以id检索文档
    JudgementModel findById(ObjectId code);

    //以关键字检索
    List<JudgementModel> findByCondition(String keyWord);

}
