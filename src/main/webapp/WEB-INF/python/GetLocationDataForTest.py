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
                timestamp = int(time.time())* 1000
                print timestamp
                userId = "C0A80A66"
                message1 = "{\"networkinfo\":[{\"userid\":\""+str(userId)+"\",\"infotype\":\"ransignal\",\"lampsiteinfo\":{\"enbid\":\"509146\",\"prrusignal\":[{\"gpp\":\"0_2_1\",\"rsrp\":\"-1100\"},{\"gpp\":\"0_2_2\",\"rsrp\":\"-1000\"},{\"gpp\":\"0_2_3\",\"rsrp\":\"-1300\"},{\"gpp\":\"0_2_4\",\"rsrp\":\"-1100\"},{\"gpp\":\"0_2_5\",\"rsrp\":\"-900\"},{\"gpp\":\"0_2_6\",\"rsrp\":\"-1300\"}]},\"timestamp\":"+str(timestamp)+"}]}"
                message2 = "{\"networkinfo\":[{\"userid\":\""+str(userId)+"\",\"infotype\":\"ransignal\",\"lampsiteinfo\":{\"enbid\":\"509146\",\"prrusignal\":[{\"gpp\":\"0_2_2\",\"rsrp\":\"-1400\"},{\"gpp\":\"0_2_3\",\"rsrp\":\"-1100\"},{\"gpp\":\"0_2_5\",\"rsrp\":\"-1000\"},{\"gpp\":\"0_2_6\",\"rsrp\":\"-1350\"},{\"gpp\":\"0_2_7\",\"rsrp\":\"-1500\"},{\"gpp\":\"0_2_8\",\"rsrp\":\"-1100\"}]},\"timestamp\":"+str(timestamp)+"}]}"
                message3 = "{\"networkinfo\":[{\"userid\":\""+str(userId)+"\",\"infotype\":\"ransignal\",\"lampsiteinfo\":{\"enbid\":\"509146\",\"prrusignal\":[{\"gpp\":\"0_2_2\",\"rsrp\":\"-1100\"},{\"gpp\":\"0_2_3\",\"rsrp\":\"-900\"},{\"gpp\":\"0_2_4\",\"rsrp\":\"-1600\"},{\"gpp\":\"0_2_5\",\"rsrp\":\"-800\"},{\"gpp\":\"0_2_6\",\"rsrp\":\"-1360\"},{\"gpp\":\"0_2_7\",\"rsrp\":\"-1100\"}]},\"timestamp\":"+str(timestamp)+"}]}"
                message4 = "{\"networkinfo\":[{\"userid\":\""+str(userId)+"\",\"infotype\":\"ransignal\",\"lampsiteinfo\":{\"enbid\":\"509146\",\"prrusignal\":[{\"gpp\":\"0_2_2\",\"rsrp\":\"-1400\"},{\"gpp\":\"0_2_3\",\"rsrp\":\"-1500\"},{\"gpp\":\"0_2_4\",\"rsrp\":\"-1500\"},{\"gpp\":\"0_2_5\",\"rsrp\":\"-1400\"},{\"gpp\":\"0_2_6\",\"rsrp\":\"-1400\"}]},\"timestamp\":"+str(timestamp)+"}]}"
                message5 = "{\"networkinfo\":[{\"userid\":\""+str(userId)+"\",\"infotype\":\"ransignal\",\"lampsiteinfo\":{\"enbid\":\"509146\",\"prrusignal\":[{\"gpp\":\"0_2_1\",\"rsrp\":\"-900\"},{\"gpp\":\"0_2_2\",\"rsrp\":\"-1100\"},{\"gpp\":\"0_2_3\",\"rsrp\":\"-1300\"},{\"gpp\":\"0_2_4\",\"rsrp\":\"-1100\"},{\"gpp\":\"0_2_5\",\"rsrp\":\"-1400\"},{\"gpp\":\"0_2_6\",\"rsrp\":\"-1360\"}]},\"timestamp\":"+str(timestamp)+"}]}"
                message6 = "{\"networkinfo\":[{\"userid\":\""+str(userId)+"\",\"infotype\":\"ransignal\",\"lampsiteinfo\":{\"enbid\":\"509146\",\"prrusignal\":[{\"gpp\":\"0_2_1\",\"rsrp\":\"-900\"},{\"gpp\":\"0_2_3\",\"rsrp\":\"-1100\"},{\"gpp\":\"0_2_4\",\"rsrp\":\"-800\"},{\"gpp\":\"0_2_5\",\"rsrp\":\"-1200\"},{\"gpp\":\"0_2_6\",\"rsrp\":\"-1400\"},{\"gpp\":\"0_2_7\",\"rsrp\":\"-1100\"}]},\"timestamp\":"+str(timestamp)+"}]}"
                messageList = [message1,message2,message3,message4,message5,message6]
                count = random.randint(0, 5)
                message = messageList[count]
                #print message
                
                #message = '{"geofencing":[{"IdType": "IP", "userid": ["bea80202"], "mapid": 2, "zoneid": 0, "zone_event": "exit", "Timestamp":1461054031000}]}'
                #{"locationstream":[{"IdType":"IP","Timestamp":1427560872000,"datatype":"coordinates","location":{"x":1133.0,"y":492.0,"z":1},"userid":["0a26d23d"]}]}
                try:             
                    print message
                    jsonData = json.loads(message)
                    conn=MySQLdb.connect(host='127.0.0.1',user='root',passwd='123456',port=3306)
                    cursor = conn.cursor()   
                    conn.select_db('sva')
                    if jsonData.keys()[0] == 'locationstream':
                        jsonList = jsonData["locationstream"]
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
                            print sqlparam
                            time_begin = Timestamp       
                            loc_count= 1       
                            during = 0      
                            time_local = time.time() * 1000
                            cursor.execute ("select loc_count, time_begin,timestamp,userid  from locationPhone where userid=%s",[userid])  
                            row = cursor.fetchone ()
                            if row != None:
                                sqlparam = [IdType,Timestamp,time_local,datatype,x,y,z,userid] 
                                cursor.execute("update locationphone set IdType=%s, Timestamp = %s,time_local= %s,datatype= %s,x=%s, y =%s, z = %s where userid = %s",sqlparam)
                            else:
                                sqlparam = [IdType,Timestamp,time_begin,time_local,loc_count,during,datatype,x,y,z,userid] 
                                cursor.execute("replace into locationPhone (IdType,Timestamp,time_begin,time_local,loc_count,during,datatype,x,y,z,userid) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",sqlparam)
                            #cursor.execute("replace into locationPhone (IdType,Timestamp,datatype,x,y,z,userid) values (%s,%s,%s,%s,%s,%s,%s)",sqlparam)
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
                            print sqlparam
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
                                print sqlparam
                                cursor.execute("update location"+dataStr+" set IdType=%s, Timestamp = %s,time_local=%s,loc_count=%s, during=%s,datatype=%s,x=%s, y =%s where z = %s and userid = %s ",sqlparam)
                            else:
                                sqlparam = [IdType,Timestamp,time_begin,time_local,loc_count,during,datatype,x,y,z,userid] 
                                cursor.execute("insert into location"+dataStr+" (IdType,Timestamp,time_begin,time_local,loc_count,during,datatype,x,y,z,userid) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",sqlparam)
                            #cursor.execute("insert into location"+dataStr+" (IdType,Timestamp,datatype,x,y,z,userid) values (%s,%s,%s,%s,%s,%s,%s)",sqlparam)
                    if jsonData.keys()[0] == 'networkinfo':
                        print "in"
                        time_local = time.time() * 1000
                        userid = jsonData["networkinfo"][0]["userid"]
                        enbid = jsonData["networkinfo"][0]["lampsiteinfo"]["enbid"]
                        jsonList = jsonData["networkinfo"][0]["lampsiteinfo"]["prrusignal"]
                        for index in range(len(jsonList)):                            
                            gpp = jsonList[index]["gpp"]
                            rsrp = jsonList[index]["rsrp"]                   
                            sqlparam = [userid,enbid,gpp,rsrp,time_local]   
                            print sqlparam
                            cursor.execute("insert into prrusignal (userId,enbid,gpp,rsrp,timestamp) values (%s,%s,%s,%s,%s)",sqlparam)
                    if jsonData.keys()[0] == 'geofencing':
                        jsonList = jsonData["geofencing"]
                        for index in range(len(jsonList)):                            
                            IdType = jsonList[index]["IdType"]
                            if len(jsonList[index]["userid"]) < 1:
                                continue
                            userid = jsonList[index]["userid"][0] 
                            mapid = jsonList[index]["mapid"]    
                            zoneid = jsonList[index]["zoneid"] 
                            zone_event = jsonList[index]["zone_event"]
                            Timestamp = jsonList[index]["Timestamp"]
                            time_local = time.time() * 1000
                            sqlparam = [IdType,userid,mapid,zoneid,zone_event,Timestamp,time_local]                       
                            print sqlparam
                            cursor.execute("insert into geofencing (IdType,userid,mapid,zoneid,enter,Timestamp,time_local) values (%s,%s,%s,%s,%s,%s,%s)",sqlparam)
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
            time.sleep(20)
            if(getSvaData.CheckSvaError()):
                sys.exit(-1) 
        except Exception as e:  
            sys.exit(-1)   

 