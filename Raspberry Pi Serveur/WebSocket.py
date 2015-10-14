import signal, sys, ssl
from SimpleSocketServer import WebSocket, SimpleWebSocketServer, SimpleSSLWebSocketServer
from optparse import OptionParser

class SimpleEcho(WebSocket):
    def handleMessage(self)
    # echo message back to client
    print self.data
    if self.data == unicode('test'):
        data = unicode('envoi JSON')
        self.data = data
        self.sendMessage(self.data)

    print self.data

    def handleConnected(self):
        print self.address, 'connected'

    def handleClose(self):
        print self.address, 'closed'

server = SimpleWebSocketServer('0.0.0.0', 6000, SimpleEcho)
print 'le serveur WS est en marche'
server.serveforever()
