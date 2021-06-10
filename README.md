## Overview
Client does 1000 reps of a pretend run (4 RPC calls)
Run benchmark 6 times, throw out first two results


## Benchmarking
Start server: `cd directory && ./run_server`
For each client: `cd client_dir && ./run_benchmark`


## Results

### gRPC

| server  client  init   run  stop<br>
| ------  ------  ----  ----  ----<br>
| java    java    0.59  7.60  0.01<br>
| java    python  0.01  2.40  0.00<br>
| python  java    0.29  4.14  0.01<br>
| python  python  0.01  1.58  0.00<br>

### thrift
| server  client  init   run  stop<br>
| ------  ------  ----  ----  ----<br>
| java    java    0.05  0.21  0.00<br>
| java    python  0.00  0.22  0.00<br>
| python  java    0.04  0.38  0.00<br>
| python  python  0.02  0.36  0.00<br>

### xml-rpc
| server  client  init   run  stop<br>
| ------  ------  ----  ----  ----<br>
| java    java    0.04  3.29  0.00<br>
| java    python  0.00  2.44  0.00<br>
| python  java    0.04  4.29  0.00<br>
| python  python  0.00  3.50  0.00<br>
