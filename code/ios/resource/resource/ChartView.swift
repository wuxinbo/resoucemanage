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
    var chart:lineChart;
    init(chart: lineChart) {
        self.chart = chart
    }
    var body: some View{
        VStack{
            Chart{
                ForEach(chart.data) { data in
                    BarMark(
                    x: .value("焦段", data.type),
                    y: .value("数量", data.num),
                    width: 60
                    )
                }
            }
            Text(chart.title)
            
        }
        
    }
    
    
    
    
    
}
