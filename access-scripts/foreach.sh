#!/bin/bash

if [ "$#" -lt 2 ]; then
    echo "Usage: $0 script1.sh [script2.sh] ... [scriptN.sh] command"
    exit
fi

while [ "$#" -gt 1 ]; do
    # This ugly syntax is to append an element to array; ${#access_scripts[@]} is the array length
    access_scripts[${#access_scripts[@]}]="$1"
    shift
done

command_to_execute="$1"

export RUN_COMMAND_ONLY="yes"

# "${access_scripts[@]}" syntax allows to handle properly script paths with spaces
for access_script in "${access_scripts[@]}"; do
    echo >&2
    echo "------------ ${access_script} ------------" >&2
    "${access_script}" "${command_to_execute}"
done

