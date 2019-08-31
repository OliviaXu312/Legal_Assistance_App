package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.CourtModel;

import org.bson.types.ObjectId;

import java.util.List;

public interface CourtRepository {
    //检索所有文档
    List<CourtModel> findAll();

    //以id检索文档
    CourtModel findById(ObjectId code);

    //以关键字检索
    List<CourtModel> findByCondition(String keyWord);


}
