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
    
    #print(predict_x)
    return predict_x

def predict_vic(predict_x):
    reload = lgb.Booster(model_file='lightgbm_model.txt')
    
    y_pred = reload.predict(predict_x)
    y_pred = [list(x).index(max(x)) for x in y_pred]
     
    print(y_pred)
    return y_pred # 0 勝，1 平， 2 敗訴


def knn_five_judgement(predict_x):
    df_dataframe = pd.read_csv("matrix1500_new.csv", encoding = 'utf8').reset_index().dropna()
    
    reload_knn = joblib.load('knn_model.model')
    neighborpoint = reload_knn.kneighbors(predict_x, 5, False)
    neighborlst = []
    for idx in neighborpoint[0]:
        neighborlst.append(df_dataframe.loc[idx,'filename'])
    
    print(neighborlst)    
    return neighborlst

'''    
conn = MongoClient('mongodb://dajiayiqibiye:wxby@wxby-shard-00-00-7ea9c.mongodb.net:27017,wxby-shard-00-01-7ea9c.mongodb.net:27017,wxby-shard-00-02-7ea9c.mongodb.net:27017/test?ssl=true&replicaSet=WXBY-shard-0&authSource=admin&retryWrites=true')#改成本機ip
db = conn.wxby
case_set = db.case_consult
judgement_set = db.judgement

a = '5c1c5cdc6ac0791484571fc7'
#a = sys.argv[1]
condition = {'id': a}
#a = case_set.find_one(condition)
print(a)
#print(type(sys.argv[1]))
'''
#string = case_set.find_one(condition).get("content")
string = '事實及理由上訴人經合法通知,未於言詞辯論期日到場,核無民事訴訟法第386條所列各款情形,爰依被上訴人聲請,由其一造辯論而為判決二、被上訴人起訴主張:上訴人於民國101年11月1日,向被上訴人承租新北市○區∞o路00o0號3樓房屋(下稱系爭房屋),租期1年即自101年11月1日起至102年10月31日止,租金每月新台幣(下同)1萬2,000元,於每月1日前給付。嗣租期屆滿,上訴人拒絕遷讓返還系爭房屋,並累計積欠租金3萬6,θθ0元未付,迭經被上訴人催討,上訴人均置之不理。又本件租期既已屆滿,上訴人即屬無權占有系爭房屋,另應自102年11月1日起至返還房屋之日止,按月賠償被上訴人相當於未收租金額計算之損害金。爰依租賃及不當得利之法律關係,求為判決∶上訴人應將系爭房屋全部遷讓返還被上訴人,並應給付被上訴人3萬6,000元,暨應自102年11月1日起至返還房屋之日止,按月給付被上訴人1萬2,000元(原審判決上訴人敗訴,並依職權為准、免假執行宣告,上訴人不服提起上訴)。並答辯聲明:上訴駁回。三、上訴人則以:被上訴人未給與上訴人搬遷費用,上訴人無法遷讓返還系爭房屋等語,資為抗辯。並上訴聲明:(一)原判決廢棄;(二)被上訴人在第一審之訴駁回。四、被上訴人主張上訴人於101年11月1日,向被上訴人承租系爭房屋,租期1年至102年10月31日止,租金每月1萬2,000元,嗣租期屆滿,上訴人拒絕遷讓返還系爭房屋,並累計積欠租金3萬6,000元未付,且無權占有系爭房屋,致被上訴人受有每月相當租金額之不當得利損害金,被上訴人自得依租賃及不當得利之法律關係,請求上訴人將系爭房屋全部遷讓返還被上訴人,並給付租金3萬6,000元,暨自102年11月1日起至返還房屋之日止,按月給付不當得利損害金1萬2,000元等情,有租賃契約書在卷可稽(原審司板簡調字卷第6、7頁)且為上訴人所不爭執,自堪信為真實,並屬於法有據。上訴人雖抗辯被上訴人未給與上訴人搬遷費用,上訴人無法遷讓返還系爭房屋云云,惟所辯尚屬如何遷讓房屋之執行問題,自不能據以對抗被上訴人。五、從而,被上訴人依租賃及不當得利之法律關係,求為判決上訴人應將系爭房屋全部遷讓返還被上訴人,並應給付被上訴人3萬6,000元,暨應自102年11月1日起至返還房屋之日止按月給付被上訴人1萬2,000元,自屬應予准許。原審為上訴人敗訴之判決,核無違誤,上訴論旨指摘原判決不當,求予廢棄改判,為無理由六、兩造其餘之攻擊或防禦方法及未經援用之證據,經本院斟酌後,認為均不足以影響本判決之結果,自無逐一詳予論駁之必要,併此敘明。七、據上論結,本件上訴為無理由,依民事訴訟法第436條之1第3項、第463條、第385條第1項前段、第449條第1項、第78條判決如主文'

predict_x = made_matrix(string)
y_pred = predict_vic(predict_x)
neighborlst = knn_five_judgement(predict_x)

#print(predict_x)
print(neighborlst)
print(y_pred)


'''
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
'''