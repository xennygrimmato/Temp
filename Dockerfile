FROM python:2.7
VOLUME /tmp
COPY requirements.txt /
RUN apt-get -y update && pip install awscli && pip install -r requirements.txt
ADD . /
CMD python eval/manage.py runserver 0.0.0.0:8080
