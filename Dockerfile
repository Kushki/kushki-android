FROM java:8
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN git config --global user.email "davidm@kushkipagos.com"
RUN git config --global user.name "moransk8"
RUN mkdir -p "$HOME/.ssh"
RUN echo -e $PRIVATE_SSH_KEY >> $HOME/.ssh/id_rsa
RUN chmod -R 700 $HOME/.ssh
RUN ssh-keyscan -t rsa github.com > $HOME/.ssh/known_hosts
RUN echo "StrictHostKeyChecking no " > $HOME/.ssh/config
RUN ssh -T git@github.com
CMD ["echo", "1"]
