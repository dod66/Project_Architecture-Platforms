import signal, sys, ssl
import json
from SimpleWebSocketServer import *


# message to the CAB
secondtry = "{\"rootObject\":{\"cabInfo\":{\"odometer\":\"16166\",\"destination\":\"None\",\"loc_now\":\"None\",\"loc_prior\":\"oki\"}}}"


# the MAP 
essai = "{\"areas\":[{\"name\":\"Quartier Nord\",\"map\":{\"weight\":{\"w\":\"1\",\"h\":\"1\"},\"vertices\":[{\"name\":\"m\",\"x\": \"0.5\",\"y\":\"0.5\"},{\"name\":\"b\",\"x\": \"0.5\",\"y\":\"1\"}],\"streets\":[{\"name\":\"mb\",\"path\":[\"m\",\"b\"],\"oneway\":\"false\"}],\"bridges\":[{\"from\":\"b\",\"to\":{\"area\":\"Quartier Sud\",\"vertex\":\"h\"},\"weight\":\"2\"}]}},{\"name\":\"Quartier Sud\",\"map\":{\"weight\":{\"w\":\"1\",\"h\":\"1\"}\"vertices\":[{\"name\":\"a\",\"x\":\"1\",\"y\":\"1\"},{\"name\":\"n\",\"x\":\"0\",\"y\":\"1\"},{\"name\": \"h\",\"x\": \"0.5\",\"y\":\"0\"}],\"streets\":[{\"name\": \"ah\",\"path\":[\"a\",\"h\"],\"oneway\": \"false\"},{\"name\": \"nh\",\"path\": [\"n\",\"h\"],\"oneway\":\"false\"}],\"bridges\":[{\"from\":\"h\",\"to\":{\"area\":\"Quartier Nord\",\"vertex\":\"b\"},\"weight\":\"2\"}]}}]}"


# the graph from the MAP
g = {'m' : {'b' : 1},
     'b' : {'m' : 1, 'h' : 1},
     'h' : {'b' : 1, 'a' : 1, 'n' : 1},
     'a' : {'h' : 1},
     'n' : {'h' : 1}}


carte = json.dumps(essai)
taxi = json.loads(theCab)

# Galileo address
Galileo = None
# array of Monitor address
Monitor = []
# array for the file
file = []

start = 'm'

#for the odometer
number = 0

wait = 0

class SimpleEcho(WebSocket):

    def affiche_peres(self,pere,depart,extremite,trajet):

        if extremite == depart:
            return [depart] + trajet
        else:
            return (affiche_peres(pere, depart, pere[extremite], [extremite] + trajet))

    def plus_court(self, graphe,etape,fin,visites,dist,pere,depart):
        
        # si on arrive a la fin, on affiche la distance et les peres
        if etape == fin:
           return affiche_peres(pere,depart,fin,[])
        # si premiere visite, c est que l etape actuelle est le depart : on met dist[etape] a 0
        if  len(visites) == 0 : dist[etape]=0
        # on commence a tester les voisins non visites
        for voisin in graphe[etape]:
            if voisin not in visites:
                # la distance est soit la distance calculee precedemment soit l'infini
                dist_voisin = dist.get(voisin,float('inf'))
                # on calcule la nouvelle distance calculee en passant par l etape
                candidat_dist = dist[etape] + graphe[etape][voisin]
                # on effectue les changements si cela donne un chemin plus court
                if candidat_dist < dist_voisin:
                    dist[voisin] = candidat_dist
                    pere[voisin] = etape
        # on a regarde tous les voisins : le noeud entier est visite
        visites.append(etape)
        # on cherche le sommet *non visite* le plus proche du depart
        non_visites = dict((s, dist.get(s,float('inf'))) for s in graphe if s not in visites)
        noeud_plus_proche = min(non_visites, key = non_visites.get)
        # on applique recursivement en prenant comme nouvelle etape le sommet le plus proche 
        return plus_court(graphe,noeud_plus_proche,fin,visites,dist,pere,depart)
     
    def dij_rec(self, graphe,debut,fin):
        return plus_court(graphe,debut,fin,[],{},{},debut)


    #add an element to the file
    def Enfile(self):
        print('i fill the file')
        file.append(self.data)
        
     # for defile the file
    def Defile(self):
        print('i defile the file')
        file.pop(0)

     # to create CabInfo for Galileo
    def createCab(self, number, loc, destination, wait):
        print ('cab creation')
        a = {}
        a["name"] = "m"
        a["x"] = 0.5
        a["y"] = 0.5

        infos = {}
        rootObject = {}
        cabInfo = {}
        cabInfo["odometer"] = number
        cabInfo["destination"] = destination
        cabInfo["loc_now"] = a
        cabInfo["loc_prior"] = wait
        
        rootObject["cabInfo"] = cabInfo

        infos["rootObject"] = rootObject
        return infos


    def handleMessage(self):
        # echo message back to client

        print ('je recois des infos')
        print (self.data)

        toMonitor = {}
        cabRequest = {}
        loc = {}
        location = {}
        
        

        #print (json.loads(self.data))

        # if NO from Galileo
        if self.data == unicode('{"takePassenger":"No"}'):
            print('Galileo says No')
            self.Defile()
            if file[0] != None:
##                data = unicode(file[0])
##                self.data = data
##                self.sendMessage(self.data)
                
                infos = self.createCab(number, start, destination, wait) 
                trameGalileo = json.dumps(infos)
                data = unicode(trameGalileo)
                self.data = data
                
                print('Envoi a Galileo une nouvelle demande')
            else:
                print('La file est vide')
                
##            # test envoi trame JSON
##            data = unicode(secondtry)
##            self.data = data
##            self.sendMessage(Galileo.data)
##            print('le message a bien ete envoye')

            

        # if YES from Galileo    
        elif self.data == unicode('{"takePassenger":"Yes"}'):
            print(' Galileo says Yes')
            number = 0
            #you have to kown the cab position and where he goes

            var = file[0]
            a = var.data
            b = a["cabRequest"]
            c = b[0]
            d = c["location"]
            e = d[0]
            
            destination = e["location"]
        

            ####DO RESOLVE GRAPH####
            
            #first the list of vertex
            path = (g, start,destination)

            
            #send each vertex to the Monitor
            for i in len(path):

                if path[i] == 'm':
                    print('go Quartir Nord, to the vertex m')
                    cabRequest["area"] = "Quartier-Nord"
                    loc["area"] = "Quartier-Nord"
 
                    
                    #say it's on the Quartir Nord, to the vertex m
                elif path[i] == 'b':
                    #say it's on the Quartier Nord, to the vertex b
                    print('go Quartir Nord, to the vertex b')
                    cabRequest["area"] = "Quartier-Nord"
                elif path[i] == 'h':
                    # say it's on the Quartier Sud, to the vertax h
                    print('go Quartir Sud, to the vertex h')
                    cabRequest["area"] = "Quartier-Sud"
                elif path[i] == 'a':
                    #say it's on the Quartier Sud, to the vertex a
                    print('go Quartir Sud, to the vertex a')
                    cabRequest["area"] = "Quartier-Sud"
                elif path[i] == 'n':
                    #say it's on the Quartier Sud, to the vertex n
                    print('go Quartir Sud, to the vertex n')
                    cabRequest["area"] = "Quartier-Sud"
                else :
                    #it's a mistake
                    print('fail')

                # create JSON
                
                loc["locationType"] = "vertex"
                location["from"] = start
                location["to"] = path[i]
                location["progression"] = 1

                loc["location"] = location
                cabRequest["location"] = loc
                toMonitor["cabRequest"] = cabRequest

                toSend = json.dumps(toMonitor)
                data = unicode(toSend)
                print('data :', toSend)
            
               # send Message to all Monitor
                for i in Monitor:
                    Monitor[i].data = data
                    Monitor[i].sendMessage(Monitor[i].data)
                    
                    
                #send Message to the Galileo
                number = number + 1
                infos = self.createCab(number, start, destination, wait) 
                trameGalileo = json.dumps(infos)
                data = unicode(trameGalileo)
                self.data = data
                
                Galileo.sendMessage(self.data)      # technicaly it is Galileo the "self"

            ###### END OF THE FOR ######        
            # when the message is send, defill the fill
            self.Defile()
            wait = wait -1    
                
            
            if file[0] != None:
                data = unicode(file[0])
                self.data = data
                Galileo.sendMessage(self.data) # technicaly it is Galileo the "self"
                print('Envoi a Galileo une nouvelle demande')
            else:
                print('La file est vide')

            # the destination become the start
            start = destination


        # if is a Monitor
        else:    
            print('Message from the Monitor')

            self.Enfile()
            wait = wait +1
            infos = self.createCab(0, start, None, wait) 
            trameGalileo = json.dumps(infos)
            data = unicode(trameGalileo)
            

    def handleConnected(self):
        print (self.address, 'connected')
        affiche = self.address
        print (affiche[0])
        #print (affiche)
        

        if affiche[0] == ('192.168.1.215'):
            print('Galileo is connected')

            #cabInf = self.createCab()
            Galileo = self

            
        else :
            
            print('send Message to Monitor')
            #Monitor.append(self)
            #print(number)

            #print('First quartier')
            #envoi = json.loads(carte)
            #envoi = json.dumps(essai)

            # SEND THE MAP TO MONITOR#
            data = unicode(essai)
            self.data = data
            self.sendMessage(self.data)

    def handleClose(self):
        print (self.address, 'closed')


server = SimpleWebSocketServer('0.0.0.0', 5000, SimpleEcho)
server.serveforever()
