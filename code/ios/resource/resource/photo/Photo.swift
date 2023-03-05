//
//  Photo.swift
//  resource
//
//  Created by wuxinbo on 2023/3/5.
//

import Foundation
/**
 *照片信息统计
 */
struct PhotoStatis :Decodable {
    var data: [Int32]
    var category:[String]
    
}
class Photo : ObservableObject {
    @Published var chartData = lineChart(title:"",data:[])
    
    func getData(url:String )  {
        let request = URLRequest(url:URL(string:url)!);
        let session = URLSession.shared;
        print("request Url is ",request.url?.absoluteString ?? "");
        session.dataTask(with: request, completionHandler: {(data,res,error) in
            do {
                let photo = try  JSONDecoder().decode(PhotoStatis.self, from:data!)
                print("photo is ", photo)
                if !photo.data.isEmpty {
                    var charDatas:[lineChartData] = [];
                    for (index,item) in photo.data.enumerated() {
                        charDatas.append(lineChartData(data:item, category: photo.category[index]))
                        
                    }
                    self.chartData.data = charDatas;

                }
            }catch{
                            
            }
         
            if let res = (res as? HTTPURLResponse){
                print(res.statusCode)
            }
        }).resume()
    }



    
}
