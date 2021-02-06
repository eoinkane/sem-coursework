# Use the latest MySQL image
FROM mysql:latest
# Set the working directory
#WORKDIR /tmp
# COPY the source file to the working directory of the container
COPY world.sql /docker-entrypoint-initdb.d/
# Set the root password
ENV MYSQL_ROOT_PASSWORD example
