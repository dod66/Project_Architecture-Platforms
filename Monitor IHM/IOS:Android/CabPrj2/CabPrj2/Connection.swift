//
//  Connection.swift
//  CabPrj2
//
//  Created by Remi Gandou on 21/10/2015.
//  Copyright © 2015 Gnd Industry. All rights reserved.
//

import Foundation
import UIKit
import Starscream
import SwiftyJSON

class Connection: WebSocketDelegate{
    
    var message : String = ""

    func connectionWS(){
    let socket = WebSocket(url: NSURL(string: "ws://172.30.0.190:5000")!) //Enter WS adress
    socket.delegate = self //event
    socket.connect() //socket connection
    socket.writeString("test") //send message to the server
}

func websocketDidConnect(socket: WebSocket) {
    print("#Socket State : ", socket.isConnected) //Check socket state
    print("#Socket Connected") //Print debug
}

func websocketDidDisconnect(socket: WebSocket, error: NSError?) {
    socket.disconnect() //Disconnect socket
    print("#Socket Disconnected \(error?.localizedDescription)")
}

func websocketDidReceiveMessage(socket: WebSocket, text: String) {
    print("#Message Received \(text)")
    self.message=text
    
    
    if let test = self.message.dataUsingEncoding(NSUTF8StringEncoding)
    {
        let json = JSON(data: test)
        //print(json)
        let taille = json["map"]["vertices"].count
        print(taille)
    }
}

func websocketDidReceiveData(socket: WebSocket, data: NSData) {
    print("#Data Received: \(data.length)")
}
}