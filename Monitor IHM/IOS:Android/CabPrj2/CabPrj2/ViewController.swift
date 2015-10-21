//
//  ViewController.swift
//  CabPrj2
//
//  Created by Remi Gandou on 19/10/2015.
//  Copyright © 2015 Gnd Industry. All rights reserved.
//
//  In this source we used Starscream Library : https://github.com/daltoniam/Starscream
//

import UIKit
import Starscream
import SwiftyJSON

class ViewController: UIViewController{
    
    let myConnection = Connection()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        super.viewDidLoad()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func connection(sender: UIButton) { //Button 'Connection' Action
        print("#Function Launched") //print for debug
        myConnection.connectionWS() //Launch function connectionWS
    }
}