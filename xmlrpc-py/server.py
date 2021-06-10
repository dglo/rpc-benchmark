#!/usr/bin/env python

from __future__ import print_function

import logging
import os

try:
    from SimpleXMLRPCServer import SimpleXMLRPCServer
    from DocXMLRPCServer import DocXMLRPCServer
except ModuleNotFoundError as exc:
    from xmlrpc.server import DocXMLRPCServer, SimpleXMLRPCServer


if False:
    baseClass = SimpleXMLRPCServer
else:
    baseClass = DocXMLRPCServer

class RPCServer(baseClass):
    "Generic class for serving methods to remote objects"
    # also inherited: register_function
    def __init__(self, portnum, servername="localhost",
                 documentation="XML-RPC Server", logRequests=False, timeout=1):
        self.servername = servername
        self.portnum = portnum

        self.__running = False
        self.__timeout = timeout

        baseClass.__init__(self, ('', portnum), logRequests=logRequests)

        # note that this has to be AFTER the init above as it can be
        # set to false in the __init__
        self.allow_reuse_address = True
        self.set_server_title("Server Methods")
        self.set_server_name("XML-RPC server at %s:%s" % (servername, portnum))
        self.set_server_documentation(documentation)

    def _dispatch(self, method, params):
        logging.warn("RPC: %s%s" % (method, params))
        if method not in self.funcs:
            raise Exception("method \"%s\" is not supported" % (method, ))

        return self.funcs[method](*params)


class DAQClient(object):
    def __init__(self, name=None, debug=False):
        if name is None:
            raise SystemExit("Please specify a DAQ client name")

        self.__name = name
        self.__debug = debug

    def __str__(self):
        return self.__name

    def __commit_subrun(self, subnum, latest_time):
        if self.__debug:
            print("CommitSubrun[%s] num %d time %s" %
                  (self, subnum, latest_time))

        return "CommitSubrun"

    def __configure(self, filename):
        if self.__debug:
            print("Configure[%s] %s" % (self, filename, ))

        return "Configure"

    def __connect(self, connections):
        if self.__debug:
            print("Connect[%s] *%d" % (self, len(connections), ))

        return "Connect"

    def __start_run(self, runnum):
        if self.__debug:
            print("StartRun[%s] num %d" % (self, runnum))

        return "StartRun"

    def __stop_run(self):
        if self.__debug:
            print("StopRun[%s]" % (self, ))

        return "StopRun"

    def register_functions(self, server):
        server.register_function(self.__commit_subrun, "xmlrpc.commitSubrun")
        server.register_function(self.__configure, "xmlrpc.configure")
        server.register_function(self.__connect, "xmlrpc.connect")
        """
        server.register_function(self.__get_events, 'xmlrpc.getEvents')
        server.register_function(self.__get_run_data, 'xmlrpc.getRunData')
        server.register_function(self.__get_run_number, 'xmlrpc.getRunNumber')
        server.register_function(self.__get_state, 'xmlrpc.getState')
        server.register_function(self.__get_version_info,
                                 'xmlrpc.getVersionInfo')
        server.register_function(self.__list_conn_states,
                                 'xmlrpc.listConnectorStates')
        server.register_function(self.__log_to, 'xmlrpc.logTo')
        server.register_function(self.__prepare_subrun, 'xmlrpc.prepareSubrun')
        server.register_function(self.__start_subrun, 'xmlrpc.startSubrun')
        server.register_function(self.__switch_to_new_run,
                                 'xmlrpc.switchToNewRun')
        server.register_function(self.__reset, 'xmlrpc.reset')
        server.register_function(self.__reset_logging, 'xmlrpc.resetLogging')
        server.register_function(self.__set_first_good_time,
                                 'xmlrpc.setFirstGoodTime')
        server.register_function(self.__set_last_good_time,
                                 'xmlrpc.setLastGoodTime')
        """
        server.register_function(self.__start_run, 'xmlrpc.startRun')
        server.register_function(self.__stop_run, 'xmlrpc.stopRun')


def main():
    # Set up logging
    logging.basicConfig(level=logging.ERROR)

    # create local RPC server
    port = 50051
    server = RPCServer(port, logRequests=False)

    # create client
    client = DAQClient("GenericClient", debug=False)

    # hook client up to local RPC server
    client.register_functions(server)

    try:
        print('Use Control-C to exit')
        server.serve_forever()
    except KeyboardInterrupt:
        print('Exiting')


if __name__ == "__main__":
    main()
