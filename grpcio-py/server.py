#!/usr/bin/env python

from concurrent import futures
import grpc
from grpc_reflection.v1alpha import reflection
import daqclient_pb2_grpc as pb2_grpc
import daqclient_pb2 as pb2


class DAQClientService(pb2_grpc.DAQClientServicer):

    def __init__(self, *args, **kwargs):
        self.__debug = False

    def configure(self, request, context):
        if self.__debug:
            print("Configure[%s] %s" % (self, request.filename, ))

        return pb2.ClientReply(result="Configure")

    def connect(self, request, context):
        if self.__debug:
            print("Connect[%s] *%d" % (self, len(request.connections), ))

        return pb2.ClientReply(result="Connect")

    def startRun(self, request, context):
        if self.__debug:
            print("StartRun[%s] num %d" % (self, request.runnum))

        return pb2.ClientReply(result="StartRun")

    def stopRun(self, request, context):
        if self.__debug:
            print("StopRun[%s]" % (self, ))

        return pb2.ClientReply(result="StopRun")


def serve():
    port = 50051

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pb2_grpc.add_DAQClientServicer_to_server(DAQClientService(), server)
    SERVICE_NAMES = [val.full_name
                     for val in pb2.DESCRIPTOR.services_by_name.values()]
    SERVICE_NAMES.append(reflection.SERVICE_NAME)
    reflection.enable_server_reflection(SERVICE_NAMES, server)
    server.add_insecure_port('[::]:%s' % port)
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
