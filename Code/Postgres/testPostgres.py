import os 
import psycopg2
#from PIL import Image

# configs
schema = 'ise_learning'
dir_path = os.path.dirname(os.path.realpath(__file__))+'\\'

def convertToBinaryData(filename):
    # Convert digital data to binary format
    with open(filename, 'rb') as file:
        binaryData = file.read()
    return binaryData

def write_file(data, filename):
    # Convert binary data to proper format and write it
    with open(filename, 'wb') as file:
        file.write(data)

def insertUser(username,password,name,email):
    try:
        conn = psycopg2.connect(database='postgres', user='postgres', password='postgres', host='127.0.0.1')
        print('Postgres connection is open')
        cur = conn.cursor()
        cur.execute('INSERT INTO ise_learning.USERS(username,password,name,email) VALUES(%s,%s,%s,%s)', (username,password,name,email,))
        print('{} created'.format(username,))
    except Exception as err:
        print('insertUser: {}'.format(err))
    finally:
        cur.close()
        conn.commit()
        conn.close()
        print('Postgres connection is closed')
        print()

def deleteUser(username):
    try:
        conn = psycopg2.connect(database='postgres', user='postgres', password='postgres', host='127.0.0.1')
        print('Postgres connection is open')
        cur = conn.cursor()
        cur.execute('DELETE FROM ise_learning.USERS U WHERE U.username=%s',(username,))
        print('%s deleted', (username))
    except Exception as err:
        print('deleteUser: {}'.format(err))
    finally:
        cur.close()
        conn.commit()
        conn.close()
        print('Postgres connection is closed')
        print()

def updateUserImage(username, filename):
    try:
        conn = psycopg2.connect(database='postgres', user='postgres', password='postgres', host='127.0.0.1')
        print('Postgres connection is open')
        cur = conn.cursor()
        imageBinaryData = convertToBinaryData(dir_path + filename)
        cur.execute('UPDATE ise_learning.USERS SET image = %s WHERE username=%s', (imageBinaryData,'user1',))
        print('{} image updated'.format(username,))
    except Exception as err:
        print('updateUserImage: {}'.format(err))
    finally:
        cur.close()
        conn.commit()
        conn.close()
        print('Postgres connection is closed')
        print()

def printImage(username):
    try:
        conn = psycopg2.connect(database='postgres', user='postgres', password='postgres', host='127.0.0.1')
        print('Postgres connection is open')
        cur = conn.cursor()
        cur.execute('SELECT * FROM ise_learning.USERS WHERE username=%s', (username,))
        record = cur.fetchall()
        for row in record:
            print('userID: ', row[0], 'username: ', row[1], 'password: ', row[2], 'name: ', row[3], 'email: ', row[4])
            image = row[5]
            write_file(image, dir_path + 'out.jpg')
            print('Storing image out.jpg')
    except Exception as err:
        print('printImage: {}'.format(err))
    finally:
        cur.close()
        conn.commit()
        conn.close()
        print('Postgres connection is closed')
        print()

#deleteUser('pythonUser')
#insertUser('pythonUser','pythonUser','aquintelao','pythonUser@isel.pt')
#updateUserImage('pythonUser','boba_fett.jpg')
#printImage('pythonUser')
