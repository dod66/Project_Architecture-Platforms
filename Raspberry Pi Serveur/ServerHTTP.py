#!/usr/bin/python

from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import os
 
#Create custom HTTPRequestHandler class
class TheServer(BaseHTTPRequestHandler):
  
  #handle GET command
  def do_GET(self):
   
    #send code 200 response
    self.send_response(200)

    #send header first
    self.send_header('Content-type','text-html')
    self.end_headers()
    #send websocket address
    if (self.client_address[0]) == "192.1681.215":
        self.wfile.write('{"IP":"192.1681.10","port":5000}\n')
    else :
        self.wfile.write('{"IP":"172.30.0.190","port":5000}\n')
    return
  
def run(address):
  print('http server is starting on :'+address)
  server_address = (address, 8090)
  httpd = HTTPServer(server_address, TheServer)
  print('http server is running...')
  httpd.serve_forever()
  
if __name__ == '__main__':
  run("0.0.0.0")
