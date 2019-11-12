FROM anapsix/alpine-java:8_server-jre_unlimited

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

WORKDIR /member-server

EXPOSE 9321

ADD ./target/member-service-1.0-SNAPSHOT.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -Xms512m -Xmx512m -jar member-service-1.0-SNAPSHOT.jar