#!/usr/bin/env python

# This client demonstrates Thrift's connection and "oneway" asynchronous jobs
# Client connects to server host:port and calls 2 methods
# showCurrentTimestamp : which returns current time stamp from server
# asynchronousJob() : which calls a "oneway" method
#

host = "localhost"
port = 50051

import sys

# your gen-py dir
sys.path.append('gen-py')

# HelloWorld files
from daqclient import *
from daqclient.ttypes import *

# Thrift files
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol


class WrappedClient(object):
    def __init__(self, host, port):
        # initialize Thrift stuff
        socket = TSocket.TSocket(host , port)
        self.transport = TTransport.TBufferedTransport(socket)

        # create client and connect to server
        protocol = TBinaryProtocol.TBinaryProtocol(self.transport)
        self.client = DAQClient.Client(protocol)

        self.transport.open()

    def configure(self, filename):
        return self.client.configure(filename)

    def connect(self, connections):
        return self.client.connect(connections)

    def shutdown(self):
        self.transport.close()

    def startRun(self, runnum):
        return self.client.startRun(runnum)

    def stopRun(self):
        return self.client.stopRun()


def create_client(host, port):
    return WrappedClient(host, port)


def single_daq_run(client, verbose):
    result = client.configure("run_config.xml")
    if verbose:
        print("configure: %s" % result)

    connection = Connection("i", "component", 0, "localhost", 12345)
    result = client.connect([connection, ])
    if verbose:
        print("connect: %s" % result)

    result = client.startRun(123456)
    if verbose:
        print("start_run: %s" % result)

    result = client.stopRun()
    if verbose:
        print("stop_run: %s" % result)


def shutdown_client(client):
    client.shutdown()


def main():
    host = 'localhost'
    port = 50051

    client = create_client(host, port)
    single_daq_run(client, True)
    shutdown_client(client)


if __name__ == "__main__":
    main()
