FROM postgres:13

ADD https://github.com/adempiere/adempiere-processors-service/releases/download/1.0.2/adempiere_postgresql_seed.tar.gz /tmp/adempiere_postgresql_seed.tar.gz

COPY --chown=1  initdb.sh /docker-entrypoint-initdb.d
RUN chmod +x /docker-entrypoint-initdb.d/initdb.sh && \
	tar -xvf /tmp/adempiere_postgresql_seed.tar.gz && \
	rm /tmp/adempiere_postgresql_seed.tar.gz
