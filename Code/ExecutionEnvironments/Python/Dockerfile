FROM python:3
WORKDIR /src
COPY requirements.txt /src
RUN pip install --no-cache-dir -r requirements.txt
COPY /src /src
CMD [ "python", "./app.py" ]
