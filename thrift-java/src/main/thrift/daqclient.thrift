namespace java org.glowacki.thrift

struct Connection {
    1: required string type;
    2: required string compName;
    3: required i32 compNum;
    4: required string host;
    5: required i32 port;
}

service DAQClient {
     string commitSubrun(1:i32 subrun, 2:string timeString);
     string configure(1:string filename);
     string connect(1:list<Connection> connections);
     string startRun(1:i32 runnum);
     string stopRun();
}
