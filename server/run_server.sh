#!/bin/bash
rm storage.txt
python base.py
sleep 2s
./run_server.sh
