//
//  ViewController.swift
//  CabPrj2
//
//  Created by Remi Gandou on 19/10/2015.
//  Copyright © 2015 Gnd Industry. All rights reserved.
//

import UIKit
import Starscream

class ViewController: UIViewController, WebSocketDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func connection(sender: UIButton) {
        connectionWS()
        print("method launched")
    }
    
    func connectionWS(){
        let socket = WebSocket(url: NSURL(string: "ws://172.30.0.190:5000")!)
        socket.delegate = self
        socket.connect()
        socket.writeString("Hi Server!")
    }

    func websocketDidConnect(socket: WebSocket) {
        print("websocket is connected")
    }
    
    func websocketDidDisconnect(socket: WebSocket, error: NSError?) {
        print("websocket is disconnected: \(error?.localizedDescription)")
    }
    
    func websocketDidReceiveMessage(socket: WebSocket, text: String) {
        print("got some text: \(text)")
    }
    
    func websocketDidReceiveData(socket: WebSocket, data: NSData) {
        print("got some data: \(data.length)")
    }
    
}

