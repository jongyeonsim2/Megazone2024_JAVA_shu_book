package com.iteratrlearning.shu_book.chapter_03;


// 다양한 형식의 보고서를 작성을 위한 value object. 

// 현재는 수많은 데이터가 클스간에 전달 및 이동이 되고 있음.
// 향후에 애플리케이션이 더 확장되면, 논리적으로 계층(Layer)을 나누어야 함.
// 
// 계층(Layer) => SW 아키텍처와 연관됨.
// 이정도에서 데이터 이동은 계층(Layer) 간 이동이 발생. => DTO(Data Transfer Object)

public class SummaryStatistics {
    private final double sum;
    private final double max;
    private final double min;
    private final double average;

    public SummaryStatistics(final double sum, final double max, final double min, final double average) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.average = average;
    }

    public double getSum() {
        return sum;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getAverage() {
        return average;
    }
}
