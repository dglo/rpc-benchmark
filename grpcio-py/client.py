#!/usr/bin/env python

import grpc
import daqclient_pb2_grpc as pb2_grpc
import daqclient_pb2 as pb2


class DAQClient(object):
    """
    Client for gRPC functionality
    """

    def __init__(self, host, port):
        # instantiate a channel
        self.channel = grpc.insecure_channel('{}:{}'.format(host, port))

        # bind the client and the server
        self.stub = pb2_grpc.DAQClientStub(self.channel)

    def commit_subrun(self, subrun, time_string):
        request = pb2.CommitSubrunRequest(subrun=subrun,
                                          timeString=time_string)
        return self.stub.commitSubrun(request)

    def configure(self, filename):
        request = pb2.ConfigureRequest(filename=filename)
        return self.stub.configure(request)

    def connect(self, connections):
        connlist = []
        for conn in connections:
            nconn = pb2.ConnectRequest.Connection(type=conn[0],
                                                  compName=conn[1],
                                                  compNum=conn[2],
                                                  host=conn[3],
                                                  port=conn[4])
            connlist.append(nconn)
        request = pb2.ConnectRequest(connections=connlist)
        return self.stub.connect(request)

    def startRun(self, runnum):
        request = pb2.StartRunRequest(runNum=runnum)
        return self.stub.startRun(request)

    def stopRun(self):
        request = pb2.EmptyRequest()
        return self.stub.configure(request)


def create_client(host, port):
    return DAQClient(host, port)


def single_daq_run(client, verbose):
    result = client.configure("run_config.xml")
    if verbose:
        print("configure: %s" % result.result)

    connection = ("i", "component", 0, "localhost", 12345)
    result = client.connect((connection, ))
    if verbose:
        print("connect: %s" % result.result)

    result = client.startRun(123456)
    if verbose:
        print("start_run: %s" % result.result)

    result = client.stopRun()
    if verbose:
        print("stop_run: %s" % result.result)


def shutdown_client(client):
    pass


def main():
    host = 'localhost'
    port = 50051

    client = create_client(host, port)
    single_daq_run(client, true)
    shutdown_client(client)


if __name__ == '__main__':
    main()
