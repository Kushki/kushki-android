FROM java:8
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN git config --global user.email "davidm@kushkipagos.com"
RUN git config --global user.name "moransk8"
RUN mkdir -p "/root/.ssh"
ADD id_rsa /root/.ssh/id_rsa
RUN chmod -R 600 /root/.ssh
RUN ssh-keyscan -t rsa github.com >> /root/.ssh/known_hosts
RUN echo "Host github.com\n\tStrictHostKeyChecking no\n" >> /root/.ssh/config
RUN ssh -v git@github.com
RUN ssh -T git@github.com
CMD ["echo", "1"]
