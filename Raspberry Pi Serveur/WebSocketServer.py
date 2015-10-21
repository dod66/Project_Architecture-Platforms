import signal, sys, ssl
import json
from SimpleWebSocketServer import *


# message to the CAB actualy not use in the programm
secondtry = "{\"rootObject\":{\"cabInfo\":{\"odometer\":\"16166\",\"destination\":\"None\",\"loc_now\":\"None\",\"loc_prior\":\"oki\"}}}"


# the MAP in JSON
essai = "{\"areas\":[{\"name\":\"Quartier Nord\",\"map\":{\"weight\":{\"w\":\"1\",\"h\":\"1\"},\"vertices\":[{\"name\":\"m\",\"x\": \"0.5\",\"y\":\"0.5\"},{\"name\":\"b\",\"x\": \"0.5\",\"y\":\"1\"}],\"streets\":[{\"name\":\"mb\",\"path\":[\"m\",\"b\"],\"oneway\":\"false\"}],\"bridges\":[{\"from\":\"b\",\"to\":{\"area\":\"Quartier Sud\",\"vertex\":\"h\"},\"weight\":\"2\"}]}},{\"name\":\"Quartier Sud\",\"map\":{\"weight\":{\"w\":\"1\",\"h\":\"1\"}\"vertices\":[{\"name\":\"a\",\"x\":\"1\",\"y\":\"1\"},{\"name\":\"n\",\"x\":\"0\",\"y\":\"1\"},{\"name\": \"h\",\"x\": \"0.5\",\"y\":\"0\"}],\"streets\":[{\"name\": \"ah\",\"path\":[\"a\",\"h\"],\"oneway\": \"false\"},{\"name\": \"nh\",\"path\": [\"n\",\"h\"],\"oneway\":\"false\"}],\"bridges\":[{\"from\":\"h\",\"to\":{\"area\":\"Quartier Nord\",\"vertex\":\"b\"},\"weight\":\"2\"}]}}]}"


# the graph from the MAP
g = {'m' : {'b' : 1},
     'b' : {'m' : 1, 'h' : 1},
     'h' : {'b' : 1, 'a' : 1, 'n' : 1},
     'a' : {'h' : 1},
     'n' : {'h' : 1}}


# Galileo address
Galileo = None
# array of Monitor address
Monitor = []
# array for the file
file = []
# the first top where is the Cab
start = 'm'
#for the odometer
number = 0
# for the number of destination "waiting" 
wait = 0

class SimpleEcho(WebSocket):

     # function to print the best path
    def affiche_peres(self,pere,depart,extremite,trajet):
        if extremite == depart:
            return [depart] + trajet
        else:
            return (affiche_peres(pere, depart, pere[extremite], [extremite] + trajet))

     # function to find the best path in the graphe
    def plus_court(self, graphe,etape,fin,visites,dist,pere,depart):
        # f we arrive at the end ,  displays fathers
        if etape == fin:
           return affiche_peres(pere,depart,fin,[])
        # if first visite, it is this step is the first : we put dist[etape] to 0
        if  len(visites) == 0 : dist[etape]=0
        # we begin to test the non visits neighbors
        for voisin in graphe[etape]:
            if voisin not in visites:
                # distance is the calculated distance or the infinity
                dist_voisin = dist.get(voisin,float('inf'))
                # calculate the new distance through the step
                candidat_dist = dist[etape] + graphe[etape][voisin]
                # changing if the path is shorter
                if candidat_dist < dist_voisin:
                    dist[voisin] = candidat_dist
                    pere[voisin] = etape
        # we look at all the neighbors: the entire node is visit
        visites.append(etape)
        # finding the top "not visiting" nearest departure
        non_visites = dict((s, dist.get(s,float('inf'))) for s in graphe if s not in visites)
        noeud_plus_proche = min(non_visites, key = non_visites.get)
        # recursively applying new stage taking the nearest top 
        return plus_court(graphe,noeud_plus_proche,fin,visites,dist,pere,depart)
     
     # the "main" of Dijsktra algorithm
    def dij_rec(self, graphe,debut,fin):
        return plus_court(graphe,debut,fin,[],{},{},debut)


    #add an element to the file
    def Enfile(self):
        print('i fill the file')
        file.append(self.data)
        
     # for pop the first element in the file
    def Defile(self):
        print('i defile the file')
        file.pop(0)

     # to create JSON CabInfo for Galileo
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
        # Message receive by a client, this fonction is the head of my programm
        print ('je recois des infos')
        print (self.data)

        # if NO from Galileo
        if self.data == unicode('{"takePassenger":"No"}'):
            print('Galileo says No')
            # i pop the first element in my file
            self.Defile()
            # i look if my file have another request and i send it to Galileo
            if file[0] != None:
                infos = self.createCab(number, start, destination, wait) 
                trameGalileo = json.dumps(infos)
                data = unicode(trameGalileo)
                self.data = data
                print('Envoi a Galileo une nouvelle demande de destination')
            else:
                print('La file est vide')

        # if YES from Galileo    
        elif self.data == unicode('{"takePassenger":"Yes"}'):
            print(' Galileo says Yes')
            number = 0  # number is for the odometer, for each destination odometer is reset.
            
            # i find the destination of the Cab when i parse the JSON send by a Monitor
            var = file[0]
            a = var.data
            b = a["cabRequest"]
            c = b[0]
            d = c["location"]
            e = d[0]
            destination = e["location"]  # this is the destination
      
            ####DO RESOLVE GRAPH####
            path = (g, start,destination)  # path is a array of point 

             ### CREATE THE JSON ###
            # for the creation of the JSON
            toMonitor = {}
            cabRequest = {}
            loc = {}
            location = {}
             
             # for each point, i create a json who was sending to the monitors and another sending to the Galileo
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
                    loc["area"] = "Quartier-Nord"
                elif path[i] == 'h':
                    # say it's on the Quartier Sud, to the vertax h
                    print('go Quartir Sud, to the vertex h')
                    cabRequest["area"] = "Quartier-Sud"
                    loc["area"] = "Quartier-Sud"
                elif path[i] == 'a':
                    #say it's on the Quartier Sud, to the vertex a
                    print('go Quartir Sud, to the vertex a')
                    cabRequest["area"] = "Quartier-Sud"
                    loc["area"] = "Quartier-Sud"
                elif path[i] == 'n':
                    #say it's on the Quartier Sud, to the vertex n
                    print('go Quartir Sud, to the vertex n')
                    cabRequest["area"] = "Quartier-Sud"
                    loc["area"] = "Quartier-Sud"
                else :
                    #it's a mistake
                    print('fail')

                ### CREATE THE JSON FOR THE MONITOR ###
                
                
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
                    
                ### CREATE THE JSON MESSAGE FOR THE GALILEO ###
                number = number + 1
                infos = self.createCab(number, start, destination, wait) 
                trameGalileo = json.dumps(infos)
                data = unicode(trameGalileo)
                self.data = data
                
                Galileo.sendMessage(self.data)      # technicaly it is Galileo the "self"

            ###### END OF THE FOR ######        
            # when the message is send, defill the fill
            self.Defile()
            # decrease the number of people waiting
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
            if trameGalileo != None:
                wait = wait +1
                infos = self.createCab(0, start, None, wait) 
                trameGalileo = json.dumps(infos)
                data = unicode(trameGalileo)
            
    def handleConnected(self):
        print (self.address, 'connected')
        affiche = self.address
          # i compare the IP address to know who start the connexion 
        if affiche[0] == ('192.168.1.215'):
            print('Galileo is connected')
            Galileo = self
        else :
            
            print('Monitor connected')
            # i put Monitor info in a array
            Monitor.append(self)
            # SEND THE MAP TO MONITOR#
            data = unicode(essai)
            self.data = data
            self.sendMessage(self.data)
            
     #close the connexion
    def handleClose(self):
        print (self.address, 'closed')

### THE MAIN ###
server = SimpleWebSocketServer('0.0.0.0', 5000, SimpleEcho)
server.serveforever()
