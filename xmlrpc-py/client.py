#!/usr/bin/env python

from __future__ import print_function

import traceback

try:
    from xmlrpclib import Fault, ServerProxy
except ModuleNotFoundError as exc:
    from xmlrpc.client import Fault, ServerProxy


def create_client(host, port):
    return ServerProxy('http://%s:%s'% (host, port))


def single_daq_run(client, verbose):
    try:
        result = client.xmlrpc.configure('run_config.xml')
        if verbose:
            print("configure: %s" % (result, ))
    except Fault:
        print("Method \"configure\" failed")
        traceback.print_exc()

    try:
        connections = (("i", "component", 0, "localhost", 12345), )
        result = client.xmlrpc.connect(connections)
        if verbose:
            print("connect: %s" % (result, ))
    except Fault:
        print("Method \"connect\" failed")
        traceback.print_exc()

    try:
        result = client.xmlrpc.startRun(123456)
        if verbose:
            print("start_run: %s" % (result, ))
    except Fault:
        print("Method \"startRun\" failed")
        traceback.print_exc()

    try:
        result = client.xmlrpc.stopRun()
        if verbose:
            print("stop_run: %s" % (result, ))
    except Fault:
        print("Method \"xmlrpc.stopRun\" failed")
        traceback.print_exc()


def shutdown_client(client):
    pass


def main():
    host = 'localhost'
    port = 50051

    client = create_client(host, port)
    single_daq_run(client, True)
    shutdown_client(client)


if __name__ == "__main__":
    main()
