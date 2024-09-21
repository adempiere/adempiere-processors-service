#!/bin/bash

if [ "$( psql -U $POSTGRES_USER -tAc "SELECT 1 FROM pg_roles WHERE rolname='adempiere'" )" != '1' ]
then
    createuser -U postgres adempiere -dlrs
    psql -U postgres -tAc "alter user adempiere password 'adempiere';"
    createdb -U adempiere adempiere
    pg_restore -U adempiere -d adempiere < /tmp/adempiere/seed.backup -v
fi

AFTER_RUN_DIR="/tmp/adempiere/after_run"
if [ -d "$AFTER_RUN_DIR" ]; then
    find "$AFTER_RUN_DIR" -maxdepth 1 -type f -name '*.sql' -print0 | while IFS= read -r -d '' file; do
        echo "importing $file"
        psql -U adempiere < "$file"
    done
fi