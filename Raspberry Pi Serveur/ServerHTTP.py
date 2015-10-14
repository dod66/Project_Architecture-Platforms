import BaseHTTPServer
import CGIHHTPServer

PORT = 5000
server_address = ('0.0.0.0", PORT)

server = BaseHTTPServer.HTTPServer
handler = CGIHHTPServer.CGIHHTPRequestHandler
handler.cgi_directories = ["/"]
print "Serveur actif sur le port : ", PORT


def do_GET(self):
    self.send_header('Content-type', 'text-html')
    self.end-header()
    self.wfile.write('ws://172.30.0.190:6000')
    return

def run():
    httpd = server(server_address, PORT)
    httpd.serve_forever()

if __name__ == '__main__' :
    run()
