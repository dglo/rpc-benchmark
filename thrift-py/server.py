#!/usr/bin/env python

# This server demonstrates Thrift's connection and "oneway" asynchronous jobs
# showCurrentTimestamp : which returns current time stamp from server
# asynchronousJob() : prints something, waits 10 secs and print another string
#

port = 50051

import sys
# your gen-py dir
sys.path.append('gen-py')

import time

# Generated files
from daqclient import *
from daqclient.ttypes import *

# Thrift files
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer


# Server implementation
class DAQClientHandler(object):
    def __init__(self, name=None, debug=False):
        if name is None:
            raise SystemExit("Please specify a DAQ client name")

        self.__name = name
        self.__debug = debug

    def __str__(self):
        return self.__name

    def commitSubrun(self, subnum, latest_time):
        if self.__debug:
            print("CommitSubrun[%s] num %d time %s" %
                  (self, subnum, latest_time))

        return "CommitSubrun"

    def configure(self, filename):
        if self.__debug:
            print("Configure[%s] %s" % (self, filename, ))

        return "Configure"

    def connect(self, connections):
        if self.__debug:
            print("Connect[%s] *%d" % (self, len(connections), ))

        return "Connect"

    def startRun(self, runnum):
        if self.__debug:
            print("StartRun[%s] num %d" % (self, runnum))

        return "StartRun"

    def stopRun(self):
        if self.__debug:
            print("StopRun[%s]" % (self, ))

        return "StopRun"


# set handler to our implementation
handler = DAQClientHandler("GenericComponent")

processor = DAQClient.Processor(handler)
transport = TSocket.TServerSocket("0.0.0.0", port)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

# set server
server = TServer.TThreadedServer(processor, transport, tfactory, pfactory)

print('Starting server')
server.serve()
