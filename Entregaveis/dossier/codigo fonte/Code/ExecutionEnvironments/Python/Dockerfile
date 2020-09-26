FROM python:3
WORKDIR /src
COPY requirements.txt /src
RUN pip install --no-cache-dir -r requirements.txt
COPY startup.sh /src
RUN chmod +x startup.sh 
COPY /src /src
ENTRYPOINT [ "./startup.sh" ]
