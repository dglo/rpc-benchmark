Install the library with:
```
pip install grpcio grpcio-tools grpcio-reflection
```

Compile the '*.proto' file with:
```
python -m grpc_tools.protoc \
    --proto_path=. \
    --python_out=. \
    --grpc_python_out=. \
    --descriptor_set_out=helloworld.protoset \
    ./helloworld.proto
```
