import sys
import os
import ssl
import time
import json
import MySQLdb
from datetime import datetime  
import socket
import random
import threading

class GetSvaData():
    isError = False
    nowTime = datetime.now()
    appname = ""
    brokeip = ""
    brokerport = ""
    queueid = ""
    companyid = ""


    def __init__(self,appName,brokeIP,brokerPort,queueID,companyID):
        self.appname = appName
        self.brokeip = brokeIP
        self.brokerport = brokerPort
        self.queueid = queueID
        self.companyid = companyID

    def CheckSvaError(self):
        if(self.isError == True):
            return True
        messageTime = self.nowTime
        nowTime = datetime.now()
        if((nowTime - messageTime).seconds > 60 * 5):
           return True 
        return False 
     
    def Run(self):
        
        try:
            while True:
                multi1x = 38
                multi1y = 32
                multi2x = 22
                multi2y = 44
                startTime = datetime(2016,6,15)
                endtime = datetime.now()
                chaDays = (endtime-startTime).days
                print chaDays
                differenceTime = chaDays*86400000
                print differenceTime        
                timestamp = int(time.time())* 1000
                print timestamp
                selectTime = timestamp-differenceTime
                print selectTime
                message = "{\"locationstreamanonymous\":[{\"IdType\":\"IP\",\"Timestamp\":"+str(timestamp)+",\"datatype\":\"coordinates\",\"location\":{\"x\":1133.0,\"y\":492.0,\"z\":2333},\"userid\":[\"c20a820a69\"]}"
                #print message
  #              for i in range(1):
 #                   x = random.randint(122, 1321)
  #                  y = random.randint(223, 1233)
 #                   message = message + ',{"IdType":"IP","Timestamp":'+str(timestamp)+',"datatype":"coordinates","location":{"x":'+str(x)+',"y":'+str(y)+',"z":2},"userid":["yyya33sssasd2'+str(i)+'"]}'
 #               message = message + "]}"
               #message = ',{"geofencing":[{"IdType": "IP", "userid": ["bea80202"], "mapid": 2, "zoneid": 0, "zone_event": "exit", "Timestamp":1461054031000}]}'
                #{"locationstream":[{"IdType":"IP","Timestamp":1427560872000,"datatype":"coordinates","location":{"x":1133.0,"y":492.0,"z":1},"userid":["c0a80a69"]}]}
                try:             
                    conn=MySQLdb.connect(host='127.0.0.1',user='root',passwd='123456',port=3307)
                    cursor = conn.cursor()   
                    conn.select_db('sva')
                    #select * from location20150822 where z = 3 and timestamp=(select max(timestamp) from location20150822 where timestamp < %s)
                    cursor.execute ("select * from location20160615 where z = 40001 and timestamp=(select max(timestamp) from location20160615 where timestamp < %s and z = 40001)",[selectTime]) 
                    results=cursor.fetchall()
                    for r in results:
                        IdType1 = r[0]
                        Timestamp1 = r[1]
                        datatype1 = r[2]
                        x1 = r[3] * multi1x /10
                        y1 = r[4] * multi1y / 10
                        z1 = 7
                        userid1 = r[6]
                        message = message + ',{"IdType":"'+str(IdType1)+'","Timestamp":'+str(timestamp)+',"datatype":"coordinates","location":{"x":'+str(x1)+',"y":'+str(y1)+',"z":'+str(z1)+'},"userid":["'+str(userid1)+'"]}'
                    
                    cursor.execute ("select * from location20160615 where z = 40003 and timestamp=(select max(timestamp) from location20160615 where timestamp < %s and z = 40003)",[selectTime]) 
                    results=cursor.fetchall()
                    for r in results:
                        IdType1 = r[0]
                        Timestamp1 = r[1]
                        datatype1 = r[2]
                        x1 = r[3] * multi2x /10
                        y1 = r[4] * multi2y / 10
                        z1 = 8
                        userid1 = r[6]
                        message = message + ',{"IdType":"'+str(IdType1)+'","Timestamp":'+str(timestamp)+',"datatype":"coordinates","location":{"x":'+str(x1)+',"y":'+str(y1)+',"z":'+str(z1)+'},"userid":["'+str(userid1)+'"]}'
                    
                    
                    message = message + "]}"
                    #print message
                    #print repr(message)                    
                    jsonData = json.loads(message, encoding="utf-8")                  
                    if jsonData.keys()[0] == 'locationstreamanonymous':
                        jsonList = jsonData["locationstreamanonymous"]
                        dataStr = ""
                        for index in range(len(jsonList)):                            
                            IdType = jsonList[index]["IdType"]
                            Timestamp = jsonList[index]["Timestamp"]
                            datatype = jsonList[index]["datatype"]
                            x = jsonList[index]["location"]["x"]
                            y = jsonList[index]["location"]["y"]
                            z = jsonList[index]["location"]["z"]
                            if z > 0:
                                z = z + int(self.companyid)*10000
                            else:
                                z = abs(z) + 5000 + int(self.companyid)*10000
                            if len(jsonList[index]["userid"]) < 1:
                                continue
                            userid = jsonList[index]["userid"][0]                      
                            sqlparam = [IdType,Timestamp,datatype,x,y,z,userid]
                            if dataStr == "":
                                ltime=time.localtime(Timestamp/1000)
                                dataStr=time.strftime("%Y%m%d", ltime)
                          #  print sqlparam
                            time_begin = Timestamp       
                            loc_count= 1       
                            during = 0      
                            time_local = time.time() * 1000
                            cursor.execute ("select loc_count, time_begin,timestamp,userid  from location"+dataStr+" where userid=%s and z = %s",[userid,z])  
                            row = cursor.fetchone ()
                            if row != None:
                                loc_count = loc_count + int(row[0])
                                during = Timestamp - int(row[1]);
                                sqlparam = [IdType,Timestamp,time_local,loc_count,during,datatype,x,y,z,userid]
                            #    print sqlparam
                                cursor.execute("update location"+dataStr+" set IdType=%s, Timestamp = %s,time_local=%s,loc_count=%s, during=%s,datatype=%s,x=%s, y =%s where z = %s and userid = %s ",sqlparam)
                            else:
                                sqlparam = [IdType,Timestamp,time_begin,time_local,loc_count,during,datatype,x,y,z,userid] 
                                cursor.execute("insert into location"+dataStr+" (IdType,Timestamp,time_begin,time_local,loc_count,during,datatype,x,y,z,userid) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",sqlparam)
                            #cursor.execute("insert into location"+dataStr+" (IdType,Timestamp,datatype,x,y,z,userid) values (%s,%s,%s,%s,%s,%s,%s)",sqlparam) 
                    conn.commit() 
                    self.nowTime = datetime.now()
                    cursor.close()  
                    conn.close()   
                    time.sleep(2)
                except Exception as e:  
                    print e

        except Exception as m: 
            print m
            self.isError = True
             #sys.exit(-1)


if __name__ == "__main__":
    #appName = "app0"
    #brokeIP = "182.138.104.35"
    #brokerPort = "4703"
    #queueID = "app0.7ce75a30c6184ef08b20994bdcb53dcb.66fc8841"
    #companyID = "861300010010300005"
    #appName = sys.argv[1] #app0
    #brokeIP = sys.argv[2] #182.138.104.35
    #brokerPort = sys.argv[3] #4703
    #queueID = sys.argv[4] #app0.7ce75a30c6184ef08b20994bdcb53dcb.66fc8841
    #companyID = sys.argv[5] #861300010010300005

    appName = "app0"
    brokeIP = "182.138.104.35"
    brokerPort = 4703
    queueID = "app0.7ce75a30c6184ef08b20994bdcb53dcb.66fc8841"
    companyID = 1
    getSvaData = GetSvaData(appName,brokeIP,brokerPort,queueID,companyID)
    try:
        thread1 = threading.Thread(target=getSvaData.Run)
        thread1.setDaemon(True) 
        thread1.start()
    except Exception as e:
        sys.exit(-1)

    while True:
        try:
            time.sleep(200)
            if(getSvaData.CheckSvaError()):
                sys.exit(-1) 
        except Exception as e:  
            sys.exit(-1)   

 