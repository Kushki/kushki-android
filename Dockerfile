FROM java:8
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN git config --global user.email "davidm@kushkipagos.com"
RUN git config --global user.name "moransk8"
RUN mkdir -p "$HOME/.ssh"
RUN echo "AAAAB3NzaC1yc2EAAAADAQABAAACAQDzZbzqZu067oIDKbynCIqdxmd0v1Kdw/ZPBeHiNw4ZidPhWNFfHgqZDAUQiGHJ4HpFOK/otMceV/62A7laW5pQlr15n6DIBRHRtWgd/Wvwqg673X/em61WJn1qhRB0Uc4D+OFGjBTt844/6RWggipHLeDjT9OyF+7rA9WNrTKFmi+b72gmz/rZjZvEotl4A1K5PC4g0MEubCx2VlgndGiZ+dqfo6OVWhmsrKLUpGjsUQTOlVHwZNAngr6n0xo9hS1WRnHr6ye6NujR+avAigyzQnIgGyHp2V9cpV78hDfbeQT2fXnRMsMQ9PyrUovJv/d9iMryoGZG5jfCoOGzvBY/gLmI79iEQTPNC/60VdE/ALMXnavNAA9ZGN79DuVdTbSoGJtLii7bXkDYm6rYrYotbMdd91jjB1MlYDC7Vd7Tjg1g5OlvRZbvbRFYHkZuqJZTUs5ANRCN4HY9k7038RKKtJ8+kj4o4mSp+XW0tFrVSDYl5I8RzxeAveVy/kKbeNpCZAtxPoAEa6JHzhWH3yaZxHHDxNqeSvQsDBN+Vwz0nRDQFkdmdenRCNohpKECEyLggkumAZ0gG7E1Eal/AvNdAmzZdJGjQBZ2cBVDCxWEcd10NaZrT3sVqMX/REojGCaDa6In1vnttwrQ0cS/zQw8N+w/fgkmNb517GyX3sxKIw== davidm@kushkipagos.com" >> $HOME/.ssh/id_rsa
RUN chmod -R 700 $HOME/.ssh
RUN ssh-keyscan -t rsa github.com > $HOME/.ssh/known_hosts
RUN echo "StrictHostKeyChecking no " > $HOME/.ssh/config
RUN ssh -T git@github.com
CMD ["echo", PRIVATE_SSH_KEY]
