import jieba
import logging
import sys
import codecs
import traceback
import pandas as pd
import numpy as np
import re
from collections import Counter
from pymongo import MongoClient 
jieba.load_userdict('台灣法律用語.txt')


if sys.version_info[0] > 2:
    is_py3 = True
else:
    reload(sys)
    sys.setdefaultencoding("utf-8")
    is_py3 = False

def delCNf(line):
    regex = re.compile('[^\u4e00-\u9fa5a-zA-Z\s]')
    return regex.sub('', line)


def str_replace(str_source, char, *words):
    str_temp = str_source
    for word in words:
        str_temp = str_temp.replace(word, char)
    return str_temp


def seg_words(sentence):
    stopwords = {}.fromkeys([ line.rstrip() for line in open('stopword.txt', encoding = 'utf8') ])
    segs = jieba.cut(sentence, cut_all=False)  # 默认是精确模式
    return " ".join(segs)     # 分词，然后将结果列表形式转换为字符串


def segmentation(file):
    files = re.split(",", file)
    lst = []
    for line in files:
        line = delCNf(line)
        line = str_replace(line, "", "\t", "\n", " ", "\u3000", "○")
        seg_list = jieba.cut(line, cut_all = False)
        words = " ".join(seg_list)
        words = "".join(words.split('\n')) # 去除回车符
        words = delCNf(words)
        word = re.split(r" ", words)
        lst.extend(word)
    return lst


from sklearn.linear_model import *
from sklearn.neighbors import KNeighborsRegressor
from sklearn import *
from random import randint
from sklearn.externals import joblib
import math
import pickle
import lightgbm as lgb 
import os

def made_matrix(string):
    matrix = pd.read_csv("matrix_column.csv", encoding = 'utf8')
    matrix.loc[0] = randint(0,0)

    #for col in matrix.columns:
    #    count = 0
    #    for word in segmentation(string):
     #       if str(word) == str(col):
    #           count += 1
     #   matrix.loc[0, col] = count

    df = pd.Series(segmentation(string)).value_counts()

    for col in matrix.columns:
        for i in range(df.size):
            if df.index[i] == str(col):
                matrix.loc[0,col] = df[i]

    predict_x = matrix.dropna().astype('int')
    
    return predict_x

def predict_vic(predict_x):
    reload = lgb.Booster(model_file='lightgbm_model.txt')
    
    y_pred = reload.predict(predict_x)
    y_pred = [list(x).index(max(x)) for x in y_pred]
    
    return y_pred # 0 勝，1 平， 2 敗訴


def knn_five_judgement(predict_x):
    df_dataframe = pd.read_csv("matrix1500_new.csv", encoding = 'utf8').reset_index().dropna()
    
    reload_knn = joblib.load('knn_model.model')
    neighborpoint = reload_knn.kneighbors(predict_x, 5, False)
    neighborlst = []
    for idx in neighborpoint[0]:
        neighborlst.append(df_dataframe.loc[idx,'filename'])
        
    return neighborlst

    
conn = MongoClient('mongodb://dajiayiqibiye:wxby@wxby-shard-00-00-7ea9c.mongodb.net:27017,wxby-shard-00-01-7ea9c.mongodb.net:27017,wxby-shard-00-02-7ea9c.mongodb.net:27017/test?ssl=true&replicaSet=WXBY-shard-0&authSource=admin&retryWrites=true')#改成本機ip
db = conn.wxby
case_set = db.case_consult
judgement_set = db.judgement

#a = '201812172120390970557865'
a = sys.argv[1]
condition = {'id': a}
#a = case_set.find_one(condition)
print(a)
#print(type(sys.argv[1]))

string = case_set.find_one(condition).get("content")

old = case_set.find_one(condition)
case_set.update_one(old,{"$set": {"state": 1}})

predict_x = made_matrix(string)
y_pred = predict_vic(predict_x)
neighborlst = knn_five_judgement(predict_x)

#print(neighborlst)
#for i in neighborlst:
 #   print(i)
print(y_pred)

neighborlstu = []
for i in neighborlst:
    j_id = judgement_set.find_one({"j_id": {"$regex": i.split("_")[1].split(".")[0]}}).get("_id")
    neighborlstu.append(j_id)


old_one = {"id": case_set.find_one(condition).get("id")}
new_one = {"$set": { "result": y_pred[0], "neighborlst": neighborlstu, "state":2}}
case_set.update_one(old_one, new_one)
conn.close()
print("finish")

#old_one = {"id": case_set.find_one(condition).get("id")}
#new_one = {"$set": { "neighborlst": neighborlst}}
#case_set.update_one(old_one, new_one)