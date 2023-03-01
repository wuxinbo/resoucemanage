//
//  chartEntity.swift
//  resource
//
//  Created by wuxinbo on 2023/3/1.
//

import Foundation
//柱状图
struct lineChartData : Identifiable{
    var id = UUID();
    var num: Int32;
    var type: String;
    
}
struct lineChart{
    var title:String;
    var data: [lineChartData]
    init(title: String, data: [lineChartData]) {
        self.title = title
        self.data = data
    }
}
