#!/bin/bash

# Use this script to stop Postegres DB container and delete volume rps_game_db

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"
docker-compose -f ./local-db.yml down -v 
