import socket
import sys
import re
import os

USAGE = '''
Usage: python get.py [action] [files...]
Available actions:
    pull - get file to local repository
    install - get file to local repository and push onto device
'''

DEFAULTCONFIG = [
    "DEFAULT_HOST = 'localhost'",
    "DEFAULT_PORT = 5000",
    "BUFFERSIZE = 2**20*16"]

try:
    with open("config.txt","r") as fuck:
        for i in fuck:
            if re.match("[A-Za-Z_]+ ?= ?[^\n]+",i):
                exec(i)
except:
    print "Populating config file with default repository..."
    with open("config.txt","w") as fuck:
        for i in DEFAULTCONFIG:
            fuck.write(i+"\n")
            exec(i)
try:
    ADDRESS = (DEFAULT_HOST,DEFAULT_PORT)
except:
    print "Config file malformed - delete it."
    quit()

def get_file_list(filelist):
    for file in filelist:
        if file:
            s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
            s.connect(ADDRESS)
            request = "GET /res/"+file+" HTTP/1.1\r\n\r\n"
            print request[:-4]
            s.send(request)
            data = ''
            r = 'filler'
            while r!='':
                r = s.recv(BUFFERSIZE)
                data += r
            if "200 OK" not in data:
                print "Exception: Request on file '" + file + "' not accepted."
                continue
            data = data.split('\r\n\r\n')
            data = data[1]
            data = data.strip()
            data = data.decode('base64')
            if "/" in file:
                path = file.split("/")
                path = "/".join(path[:-1])
                if not os.path.exists(path):
                    os.makedirs(path)
            with open(file,"w") as fuck:
                fuck.write(data)
            data = data.split("\n")
            if data[0][:14] == "AUTYZM-PACKAGE":
                data = data[1:]
                data = filter(lambda x: True if x!='' else False ,data)
                if "/" in file:
                    path = file.split("/")
                    path = "/".join(path[:-1])
                    data = map(lambda x: path + "/" + x,data)
                get_file_list(data)

if len(sys.argv) < 3:
    print USAGE
if (sys.argv[1].lower() in ['install','pull']):
    get_file_list(sys.argv[2:])
