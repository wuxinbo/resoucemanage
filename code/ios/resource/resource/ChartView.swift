//
//  ChartView.swift
//  resource
//
//  Created by wuxinbo on 2023/3/1.
//

import Foundation
import Charts
import SwiftUI
struct lineChartView: View{
    @EnvironmentObject var photo:Photo;
    var url:String="";
    
    init(url: String,title:String){
        self.url = url;
    }
    var body: some View{
        VStack{
            Chart{
                ForEach(photo.chartData.data) { item in
                    BarMark(
                    x: .value("镜头", item.category),
                    y: .value("数量", item.data),
                    width: 60
                    )
                }
            }
            Text(photo.chartData.title)
            
        }.task {
            photo.getData(url: url);

        }
        
    }
    
    
    
    
    
}
