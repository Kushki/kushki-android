FROM java:8
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN git config --global user.email "davidm@kushkipagos.com"
RUN git config --global user.name "moransk8"
