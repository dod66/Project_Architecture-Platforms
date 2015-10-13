//: Playground - noun: a place where people can play

import UIKit

var str = "Hello, playground"

let url = NSURL(string:"http://localhost/project")
let cachePolicy = NSURLRequestCachePolicy.ReloadIgnoringLocalCacheData
var request = NSMutableURLRequest(URL: url!, cachePolicy: cachePolicy, timeoutInterval: 2.0)
request.HTTPMethod = "POST"

// set Content-Type in HTTP header
let boundaryConstant = "----------V2ymHFg03esomerandomstuffhbqgZCaKO6jy";
let contentType = "multipart/form-data; boundary=" + boundaryConstant
NSURLProtocol.setProperty(contentType, forKey: "Content-Type", inRequest: request)

// set data
var dataString = "your data here" + boundaryConstant
let requestBodyData = (dataString as NSString).dataUsingEncoding(NSUTF8StringEncoding)
request.HTTPBody = requestBodyData

// set content length
NSURLProtocol.setProperty(requestBodyData!.length, forKey: "Content-Length", inRequest: request)

var response: NSURLResponse? = nil
var error: NSError? = nil
let reply = NSURLConnection.sendSynchronousRequest(request, returningResponse:&response, error:&error)

let results = NSString(data:reply, encoding:NSUTF8StringEncoding)