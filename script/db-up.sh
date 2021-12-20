#!/bin/bash

# Use this script to run docker image with Postegres DB and "rps_game_db" volume

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"
docker-compose -f ./local-db.yml up -d
