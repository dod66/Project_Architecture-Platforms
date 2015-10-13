//
//  client.swift
//  CabProject
//
//  Created by Remi Gandou on 12/10/2015.
//  Copyright © 2015 Gnd Industry. All rights reserved.
//

import Foundation

let url = NSURL(fileURLWithPath: "192.168.1.76")
let request = NSURLRequest(URL:url)
var response: NSURLResponse? = nil
var error: NSError? = nil

if(response == null){
    print("Error");
}