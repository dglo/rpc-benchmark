#!/usr/bin/env python

from __future__ import print_function

import sys

from datetime import datetime, timedelta
from client import create_client, single_daq_run, shutdown_client


def format_delta(delta_time):
    return "%6.3fs" % (float(delta_time.seconds) +
                       (float(delta_time.microseconds) / 1E6))


def main():
    host = 'localhost'
    port = 50051
    reps = 1000

    if len(sys.argv) > 1:
        reps = int(sys.args[1])

    init_start = datetime.now()

    client = create_client(host, port)

    init_time = datetime.now() - init_start

    print("Run %d reps" % reps)

    run_start = datetime.now()

    for idx in range(reps):
        single_daq_run(client, False)

    run_time = datetime.now() - run_start

    stop_start = datetime.now()

    shutdown_client(client)

    stop_time = datetime.now() - stop_start

    print("Init: %s      Run: %s      Stop: %s      Total: %s" %
          (format_delta(init_time), format_delta(run_time),
           format_delta(stop_time),
           format_delta(init_time + run_time + stop_time)))


if __name__ == "__main__":
    main()
